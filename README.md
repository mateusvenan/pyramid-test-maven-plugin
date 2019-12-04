# Pyramid test maven plugin

Javadoc example
``` markdown
/**
 * PYRAMID-TEST
 * FEATURE: Feature example
 * SCENARIO: Some scenario described for feature that complements
 * TYPE: UNIT|COMPONENT|END-TO-END
 */
```
Adding the dependency
``` markdown
<plugin>
  <groupId>br.com.mvenancio</groupId>
  <artifactId>pyramid-test-maven-plugin</artifactId>
  <version>1.0-SNAPSHOT</version>
</plugin>
```

Running
``` markdown
mvn br.com.mvenancio:pyramid-test-maven-plugin:1.0-SNAPSHOT:run -N
```

Than will be generated the pyramid-report.csv
``` markdown
FEATURE,SCENARIO,TYPE
Feature example,Scenarion one as UNIT,UNIT
Feature example,Scenarion one as COMPONENT,COMPONENT
```
