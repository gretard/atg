package edu.ktu.atg.instrumentor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.ktu.atg.common.models.ClasszInfo;
import edu.ktu.atg.instrumentor.transformers.DefaultTransformersProvider;
import edu.ktu.atg.instrumentor.transformers.SootBodiesTransformer;
import edu.ktu.atg.instrumentor.transformers.TransformersProvider;
import soot.Pack;
import soot.PackManager;
import soot.PhaseOptions;
import soot.Transform;
import soot.options.Options;

public class SootInstrumenter {

    private final TransformersProvider transformersProvider;

    public SootInstrumenter(TransformersProvider transformersProvider) {
        this.transformersProvider = transformersProvider;
    }

    public SootInstrumenter() {
        this(new DefaultTransformersProvider());
    }

    public static class SootInstrumenterRequest {

        public String outputFormat = "c";
        public String instrumentedClassesDir = "./out/classes";
        public String dataDir = "./out/data";

        public String getOutputFormat() {
            return outputFormat;
        }

        private final List<String> classes = new ArrayList<>();
        private final List<String> supportingPaths = new ArrayList<>();

        public List<String> getSupportingPaths() {
            return supportingPaths;
        }

        public File getTempDir() {
            return new File(instrumentedClassesDir);
        }

        public File getDataDir() {
            return new File(dataDir);
        }

        public List<String> getClasses() {
            return classes;
        }

    }

    public static class SootInstrumenterResponse {

        private final Map<String, ClasszInfo> classes = new HashMap<>();

        public Map<String, ClasszInfo> getClasses() {
            return classes;
        }
    }

    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping()
            .serializeSpecialFloatingPointValues().create();

    public SootInstrumenterResponse instrument(SootInstrumenterRequest request) {
        File tempDir = request.getTempDir();
        File dataDir = request.getDataDir();

        final List<String> selectedClasses = request.getClasses();

        soot.G.reset();
        final StringBuffer sb = new StringBuffer();
        sb.append(System.getProperty("java.class.path") + File.pathSeparator);

        for (final String x : request.getSupportingPaths()) {

            String t = x;
            if (t.endsWith("/*")) {
                t = t.replace("/*", "");
            }
            File f = new File(t);
            if (f.exists()) {
                sb.append(File.pathSeparator);
                sb.append(t);
            }
        }

        Options.v().set_keep_line_number(true);
        Options.v().set_verbose(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_output_dir(tempDir.getAbsolutePath());
        Options.v().set_ignore_resolution_errors(true);
        final List<String> options = new ArrayList<String>();
        options.add("-cp");
        options.add(sb.toString());
        options.add("--java-version");
        options.add("8");
        options.add("-f");

        options.add(request.getOutputFormat());
        options.add("-p");
        options.add("jb");
        options.add("use-original-names:true");

        options.addAll(selectedClasses);
        final Pack pack = PackManager.v().getPack("jtp");

        SootBodiesTransformer transformer = new SootBodiesTransformer(transformersProvider);

        pack.add(new Transform("jtp.atg", transformer));
        PhaseOptions.v().setPhaseOption("jtp.atg", "on");

        final String[] arguments = options.toArray(new String[0]);
        logger.info("Starting soot transformation with arguments: " + Arrays.toString(arguments));
        soot.Main.main(arguments);

        final Map<String, ClasszInfo> ciClasses = transformer.getInfos();

        for (final Entry<String, ClasszInfo> entry : ciClasses.entrySet()) {
            final String key = entry.getKey();
            if (!selectedClasses.contains(key)) {
                continue;
            }
            try {
                FileUtils.write(new File(dataDir, String.join("", key, ".json")), gson.toJson(entry.getValue()),
                        Charset.defaultCharset());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        final SootInstrumenterResponse response = new SootInstrumenterResponse();
        response.classes.putAll(ciClasses);
        return response;
    }
}
