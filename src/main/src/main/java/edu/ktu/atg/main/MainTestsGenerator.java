package edu.ktu.atg.main;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.ktu.atg.common.execution.GenerationData;
import edu.ktu.atg.common.models.ClasszInfo;
import edu.ktu.atg.generator.GaTestsGenerator;
import edu.ktu.atg.outputter.OuputGenerator;

public class MainTestsGenerator {
    private final InstrumentingClassesProvider classesProvider = new InstrumentingClassesProvider();
    private final ClassLoaderProvider classLoaderProvider = new ClassLoaderProvider();
    private final OuputGenerator outputGenerator = new OuputGenerator();
    private final TempDirInitializer initializer = new TempDirInitializer();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void generate(OptionsRequest request) throws Throwable {
        initializer.initialize(request);
        Map<String, ClasszInfo> classes = classesProvider.get(request);
        ClassLoader loader = classLoaderProvider.getLoader(request);
        Thread.currentThread().setContextClassLoader(loader);
        Class<?> generator = loader.loadClass(GaTestsGenerator.class.getName());

        @SuppressWarnings("deprecation")
        Object generatorObj = generator.newInstance();
        Method m = generator.getMethod("generate", ClasszInfo.class, Object.class);
        for (Entry<String, ClasszInfo> data : classes.entrySet()) {
            @SuppressWarnings("unchecked")
            List<GenerationData> results = (List<GenerationData>) m.invoke(generatorObj, data.getValue(), request);
            if (request.isDebug()) {
                System.out.println("SAVING");
                FileUtils.write(new File(request.getTracesDir(), data.getKey() + ".json"), gson.toJson(results),
                        Charset.defaultCharset());
            }
            outputGenerator.generate(results, request.getResultsDir());
        }

    }
}
