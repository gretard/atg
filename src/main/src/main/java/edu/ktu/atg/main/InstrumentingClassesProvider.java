package edu.ktu.atg.main;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import edu.ktu.atg.common.models.ClasszInfo;
import edu.ktu.atg.common.models.OptionsRequest;
import edu.ktu.atg.instrumentor.SootInstrumenter;
import edu.ktu.atg.instrumentor.SootInstrumenter.SootInstrumenterRequest;
import edu.ktu.atg.instrumentor.SootInstrumenter.SootInstrumenterResponse;

public class InstrumentingClassesProvider {
    
    private final SootInstrumenter instrumenter = new SootInstrumenter();
    private final ClassesProvider classesProvider = new ClassesProvider();

    public Map<String, ClasszInfo> get(final OptionsRequest request) {
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
