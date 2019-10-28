package edu.ktu.atg.main;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import edu.ktu.atg.common.models.OptionsRequest;

public class TempDirInitializer {
    public void initialize(OptionsRequest request) throws IOException {
        File resourcesDir = request.getInstrumentedClassesDirFile();
        if (resourcesDir.exists() && resourcesDir.listFiles().length > 0) {
            return;
        }
        FileUtils.deleteDirectory(resourcesDir);
        resourcesDir.mkdirs();
        for (String x : request.getClassesDir()) {
            try {
                File f = new File(x).getCanonicalFile().getAbsoluteFile();
                if (!f.exists()) {
                    continue;
                }
                FileUtils.copyDirectory(f, resourcesDir);
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }
    }
}
