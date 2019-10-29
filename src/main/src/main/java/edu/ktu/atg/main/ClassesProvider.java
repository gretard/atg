package edu.ktu.atg.main;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.github.baev.ClasspathScanner;

import edu.ktu.atg.common.models.OptionsRequest;
import javassist.bytecode.AccessFlag;

public class ClassesProvider {
    public List<String> getClasses(OptionsRequest request) {
        final ClasspathScanner scanner = new ClasspathScanner();
        final ClassesFilter filter = new ClassesFilter(request);

        List<String> classes = new LinkedList<>();
        Arrays.asList(request.getClassesDir()).forEach(d -> {
            scanner.scanFrom(Paths.get(d));
        });
        scanner.getClasses().stream()
                .filter(c -> filter.shouldAdd(c.getName())
                        && !(c.isAbstract() || c.isInterface() || AccessFlag.isPrivate(c.getAccessFlags())))
                .forEach(c -> {
                    classes.add(c.getName());
                });

        return classes;
    }
}
