package edu.ktu.atg.main;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class TempDirInitializer {
    public void initialize(OptionsRequest request) throws IOException {
        File resourcesDir = new File(request.getInstrumentedClassesDir()).getCanonicalFile().getAbsoluteFile();
        FileUtils.deleteDirectory(resourcesDir);
        resourcesDir.mkdirs();
        for (String x : request.getClassesDir()) {
            try {
                File f = new File(x).getCanonicalFile().getAbsoluteFile();
                if (!f.exists()) {
                    continue;
                }
                System.out.println("Copying.. "+f+" "+resourcesDir);
                FileUtils.copyDirectory(f, resourcesDir);
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }
    }
}
