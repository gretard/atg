package edu.ktu.atg.main;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.ktu.atg.common.execution.PopulationData;
import edu.ktu.atg.common.models.ClasszInfo;
import edu.ktu.atg.generator.GaTestsGenerator;
import edu.ktu.atg.outputter.OuputGenerator;

public class MainTestsGenerator {
    private final InstrumentingClassesProvider classesProvider = new InstrumentingClassesProvider();
    private final ClassLoaderProvider classLoaderProvider = new ClassLoaderProvider();
    private final OuputGenerator outputGenerator = new OuputGenerator();

    public void generate(OptionsRequest request) throws Throwable {
        Map<String, ClasszInfo> classes = classesProvider.get(request);
        ClassLoader loader = classLoaderProvider.getLoader(request);
        Thread.currentThread().setContextClassLoader(loader);
        Class<?> generator = loader.loadClass(GaTestsGenerator.class.getName());

        @SuppressWarnings("deprecation")
        Object generatorObj = generator.newInstance();
        Method m = generator.getMethod("generate", ClasszInfo.class, Object.class);
        for (Entry<String, ClasszInfo> data : classes.entrySet()) {
            @SuppressWarnings("unchecked")
            List<PopulationData> results = (List<PopulationData>) m.invoke(generatorObj, data.getValue(), request);
            outputGenerator.generate(results, request.getResultsDir());
        }

    }
}
