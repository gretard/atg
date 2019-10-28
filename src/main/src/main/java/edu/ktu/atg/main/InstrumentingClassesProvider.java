package edu.ktu.atg.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import edu.ktu.atg.common.models.ClasszInfo;
import edu.ktu.atg.common.models.OptionsRequest;
import edu.ktu.atg.instrumentor.SootInstrumenter;
import edu.ktu.atg.instrumentor.SootInstrumenter.SootInstrumenterRequest;
import edu.ktu.atg.instrumentor.SootInstrumenter.SootInstrumenterResponse;

public class InstrumentingClassesProvider {

    private final SootInstrumenter instrumenter = new SootInstrumenter();
    private final ClassesProvider classesProvider = new ClassesProvider();
    private final Gson gson = new Gson();

    public Map<String, ClasszInfo> get(final OptionsRequest request) throws Exception {
        File resourcesDir = request.getDataDirFile();
        if (resourcesDir.exists()) {
            System.out.println("Reading from: " + resourcesDir);
            Map<String, ClasszInfo> infos = new HashMap<String, ClasszInfo>();
            for (File file : resourcesDir.listFiles()) {
                ClasszInfo ci = gson.fromJson(new FileReader(file), ClasszInfo.class);
                infos.put(ci.getName(), ci);
            }
            if (!infos.isEmpty()) {
                return infos;
            }
        }

        final List<String> classes = classesProvider.getClasses(request);

        final SootInstrumenterRequest sootInstrumenterRequest = new SootInstrumenterRequest();
        sootInstrumenterRequest.instrumentedClassesDir = request.getInstrumentedClassesDir();
        sootInstrumenterRequest.dataDir = request.getDataDir();
        sootInstrumenterRequest.getClasses().addAll(classes);
        sootInstrumenterRequest.getSupportingPaths().addAll(Arrays.asList(request.getClassesDir()));
        sootInstrumenterRequest.getSupportingPaths().addAll(Arrays.asList(request.getLibs()));

        final SootInstrumenterResponse response = instrumenter.instrument(sootInstrumenterRequest);
        return response.getClasses();

    }
}
