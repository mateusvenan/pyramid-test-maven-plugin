package com.mvenancio;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Mojo(name = "run", defaultPhase = LifecyclePhase.NONE)
public class PyramidTestExtractor extends AbstractMojo {

    private static final String PYRAMID_TEST = "@pyramid-test";
    private static final String FEATURE = "@feature ";
    private static final String SCENARIO = "@scenario ";
    private static final String TYPE = "@type ";
    private static final List<String> AVAILABLE_TYPES = Arrays.asList("UNIT", "COMPONENT", "END-TO-END");
    private final File location;

    public PyramidTestExtractor() {
        location = FileUtils.getFile(".");
    }

    public PyramidTestExtractor(File location) {
        this.location = location;
    }

    public void execute() throws MojoExecutionException {
        try {
            getLog().info("EXECUTING ANALYSIS");
            List<PyramidScenario> pyramidScenarios = execute(location);
            export(pyramidScenarios);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void export(List<PyramidScenario> pyramidScenarios) throws IOException {
        StringBuilder report = new StringBuilder();
        report.append("FEATURE,SCENARIO,TYPE\n");
        for (PyramidScenario scenario : pyramidScenarios) {
            report.append(scenario.getCsvLine());
        }
        FileUtils.writeStringToFile(new File("pyramid-report.csv"), report.toString(), "UTF-8");
    }


    List<PyramidScenario> execute(File file) throws IOException {
        final List<PyramidScenario> pyramidScenarios = new ArrayList<>();
        if (file.isDirectory()) {
            for (File currentFile : Arrays.asList(file.listFiles())) {
                pyramidScenarios.addAll(execute(currentFile));
            }
        } else if (file.getAbsolutePath().contains("/src/test")) {
            final List<String> javadocs = extractJavadocs(file);
            pyramidScenarios.addAll(extractPyramid(javadocs));
        }
        return pyramidScenarios;
    }

    List<PyramidScenario> extractPyramid(List<String> javadocs) {
        List<PyramidScenario> scenarios = new ArrayList<>();
        for (String javadoc : javadocs) {
            if (isPyramidTestDocumentation(javadoc)) {
                String feature = null;
                String scenario = null;
                String type = null;
                for (String line : Arrays.asList(javadoc.split("\n"))) {
                    feature = Optional.ofNullable(extract(line, FEATURE)).orElse(feature);
                    scenario = Optional.ofNullable(extract(line, SCENARIO)).orElse(scenario);
                    type = Optional.ofNullable(extract(line, TYPE)).orElse(type);

                }
                scenarios.add(new PyramidScenario(feature, scenario, type));
            }
        }
        return scenarios;
    }

    private String extract(String line, String part) {
        if (is(line, part)) {
            return line.substring(line.indexOf(part) + part.length()).trim();
        }
        return null;
    }

    private boolean isPyramidTestDocumentation(String javadoc) {
        return javadoc.indexOf(PYRAMID_TEST) > 0 && is(javadoc, FEATURE)
                && is(javadoc, SCENARIO) && is(javadoc, TYPE);
    }

    private boolean is(String javadoc, String feature) {
        return javadoc.indexOf(feature) > 0;
    }

    private List<String> extractJavadocs(File file) throws IOException {
        final List<String> lines = FileUtils.readLines(file, "UTF-8");

        List<String> javadocs = new ArrayList<>();
        StringBuilder builder = null;
        boolean startedJavadoc = false;
        for (String line : lines) {
            if (!startedJavadoc) {
                startedJavadoc = line.contains("/**");
                builder = new StringBuilder();
            } else {
                if (line.contains("*/")) {
                    startedJavadoc = false;
                    javadocs.add(builder.toString());
                } else {
                    builder.append(line + "\n");
                }
            }
        }
        return javadocs;
    }

    class PyramidScenario {
        private final String feature;
        private final String scenario;
        private final String type;

        public PyramidScenario(String feature, String scenario, String type) {
            this.feature = feature;
            this.scenario = scenario;
            this.type = type;
        }

        public String getFeature() {
            return feature;
        }

        public String getScenario() {
            return scenario;
        }

        public String getType() {
            return type;
        }

        public String getCsvLine() {
            return feature + ',' + scenario + ',' + type + '\n';
        }

        @Override
        public String toString() {
            return "Pyramid{" +
                    "feature='" + feature + '\'' +
                    ", scenario='" + scenario + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }
}