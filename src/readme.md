# Examples

## edu.ktu.atg.main.ExampleITCase
This class contains example of instrumenting edu.ktu.atg.example.Calculator class in the **example** project. To run that, at first compile **example** project by using command:

```mvn compile -f example/pom.xml```

Then you can run **edu.ktu.atg.main.ExampleITCase** test case which tracks which statements and branches have been executed.

Building:

```
mvn clean install
```

```
mvn -Dcheckstyle.maxAllowedViolations=99999999 clean install com.github.spotbugs:spotbugs-maven-plugin:3.1.12.2:spotbugs pmd:pmd checkstyle:check sonar:sonar
```


