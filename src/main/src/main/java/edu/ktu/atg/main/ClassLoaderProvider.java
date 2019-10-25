package edu.ktu.atg.main;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class ClassLoaderProvider {
    public ClassLoader getLoader(OptionsRequest request) {
        final List<URL> urls = new ArrayList<>();
        try {
            File resourcesDir = new File(request.getInstrumentedClassesDir()).getCanonicalFile().getAbsoluteFile();
            urls.add(resourcesDir.toURI().toURL());
            for (String s : request.getLibs()) {
                File f = new File(s);
                if (!f.exists()) {
                    continue;
                }
                urls.add(f.toURI().toURL());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return new URLClassLoader(urls.toArray(new URL[0]), this.getClass().getClassLoader());
    }
}
