package edu.ktu.atg.main;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class ClassLoaderProvider {
    public ClassLoader getLoader(OptionsRequest request) {
        final List<URL> urls = new ArrayList<>();
        File resourcesDir = new File(request.getInstrumentedClassesDir());
        resourcesDir.mkdirs();
        for (String x : request.getClassesDir()) {
            try {
                File f = new File(x);
                if (!f.exists()) {
                    continue;
                }
              //  FileUtils.copyDirectory(f, resourcesDir);
                FileUtils.copyDirectory(f, resourcesDir, pathname -> !pathname.getName().endsWith(".class"));
                
            } catch (Throwable e) {
                e.printStackTrace();
            }
            
        }

        try {
            urls.add(resourcesDir.toURI().toURL());
            for (String s : request.getLibs()) {
                urls.add(new File(s).toURI().toURL());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return new URLClassLoader(urls.toArray(new URL[0]), this.getClass().getClassLoader());
    }
}
