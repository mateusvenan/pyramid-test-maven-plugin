package com.mvenancio;


import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PyramidTestExtractorTest {

    @Test
    public void extractPyramid() {
        String javadoc = "     * /**\n" +
                " * @pyramid-test\n" +
                " * @feature Feature example\n" +
                " * @scenario Some scenario described for feature that complements\n" +
                " * @type UNIT\n" +
                " */\n";

        final List<PyramidTestExtractor.PyramidScenario> pyramidScenarios = new PyramidTestExtractor().extractPyramid(Arrays.asList(javadoc));

        final PyramidTestExtractor.PyramidScenario scenario = pyramidScenarios.get(0);
        Assert.assertEquals("Feature example", scenario.getFeature());
        Assert.assertEquals("Some scenario described for feature that complements", scenario.getScenario());
        Assert.assertEquals("UNIT", scenario.getType());
    }
}
