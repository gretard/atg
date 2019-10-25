package edu.ktu.atg.main;

import java.io.File;

import com.sampullara.cli.Argument;

public class OptionsRequest {

    public final String getBaseDir() {
        return baseDir;
    }

    public final void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public final void setLibs(String[] libs) {
        this.libs = libs;
    }

    public final void setUseTimestampedReports(boolean useTimestampedReports) {
        this.useTimestampedReports = useTimestampedReports;
    }

    public final void setClasses(String[] classes) {
        this.classes = classes;
    }

    public final void setClassesDir(String[] classesDir) {
        this.classesDir = classesDir;
    }

    public final void setTestClasses(String[] testClasses) {
        this.testClasses = testClasses;
    }

    public final void setExcludedClasses(String[] excludedClasses) {
        this.excludedClasses = excludedClasses;
    }

    public final void setResultsDir(String resultsDir) {
        this.resultsDir = resultsDir;
    }

    public final void setTimeoutGlobal(Integer timeoutGlobal) {
        this.timeoutGlobal = timeoutGlobal;
    }

    private static final long timeStamp = System.nanoTime();

    @Argument(alias = "libs", description = "Path to libs, comman separated", delimiter = ",")
    private String[] libs = new String[] { "./libs/", "./lib", "./dependency" };

    @Argument(description = "Flag whether use timeStamped output")
    private boolean useTimestampedReports = true;

    @Argument(alias = "c", description = "Specific classes under check, comma separated", delimiter = ",")
    private String[] classes = new String[] {};

    @Argument(alias = "cp", description = "Path to compiled classes dir, comma separated", delimiter = ",")
    private String[] classesDir = new String[] { "./classes/", "./test-classes/" };

    @Argument(alias = "t", description = "Test classes under check, comma separated", delimiter = ",")
    private String[] testClasses = new String[] {};

    @Argument(alias = "x", description = "Excluded classes under check, comma separated", delimiter = ",")
    private String[] excludedClasses = new String[] {};

    @Argument(description = "Path to generated tests")
    private String resultsDir = "./results/";

    @Argument(description = "Path to base dir")
    private String baseDir = "./temp";

    @Argument(description = "Global tests execution timeout in seconds for a single class")
    private Integer timeoutGlobal = 60;

    @Argument(description = "Flag whether to store intermiadiate results")
    private boolean debug = false;

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public static final long getTimestamp() {
        return timeStamp;
    }

    public final String[] getLibs() {
        return libs;
    }

    public final boolean isUseTimestampedReports() {
        return useTimestampedReports;
    }

    public final String[] getClasses() {
        return classes;
    }

    public final String[] getClassesDir() {
        return classesDir;
    }

    public final String[] getTestClasses() {
        return testClasses;
    }

    public final String[] getExcludedClasses() {
        return excludedClasses;
    }

    public final String getResultsDir() {
        if (useTimestampedReports) {
            return String.join(File.separator, this.resultsDir, timeStamp + "");
        }
        return String.join(File.separator, this.resultsDir);
    }

    public String getInstrumentedClassesDir() {
        if (useTimestampedReports) {
            return String.join(File.separator, this.baseDir, timeStamp + "", "classes");
        }
        return String.join(File.separator, this.baseDir, "classes");
    }

    public String getDataDir() {
        if (useTimestampedReports) {
            return String.join(File.separator, this.baseDir, timeStamp + "", "data", "infos");
        }
        return String.join(File.separator, this.baseDir, "data", "infos");
    }

    public String getTracesDir() {
        if (useTimestampedReports) {
            return String.join(File.separator, this.baseDir, timeStamp + "", "data", "traces");
        }
        return String.join(File.separator, this.baseDir, "data", "traces");
    }

    public final Integer getTimeoutGlobal() {
        return timeoutGlobal;
    }
}
