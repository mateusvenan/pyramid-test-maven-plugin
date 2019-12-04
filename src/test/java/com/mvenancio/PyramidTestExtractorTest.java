package com.mvenancio;


import org.junit.Test;

import java.util.Arrays;

public class PyramidTestExtractorTest {

    @Test
    public void extractPyramid() {
        String javadoc = "/**\n" +
                "     * PYRAMID-TEST\n" +
                "     * FEATURE:  Feature X\n" +
                "     * SCENARIO: Teste de Unitário do cenário X\n" +
                "     * TYPE: UNIT\n" +
                "     */";

        new PyramidTestExtractor().extractPyramid(Arrays.asList(javadoc));

    }
}
