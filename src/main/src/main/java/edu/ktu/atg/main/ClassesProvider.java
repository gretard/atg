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

        List<String> classes = new LinkedList<>();
        List<String> selectedClasses = Arrays.asList(request.getClasses());
        List<String> excludedClasses = Arrays.asList(request.getExcludedClasses());
        Arrays.asList(request.getClassesDir()).forEach(d -> {
            scanner.scanFrom(Paths.get(d));
        });
        scanner.getClasses().stream()
                .filter(c -> !(c.isAbstract() || c.isInterface() || AccessFlag.isPrivate(c.getAccessFlags()))
                        && (!excludedClasses.contains(c.getName()) || selectedClasses.contains(c.getName())))
                .forEach(c -> {
                    classes.add(c.getName());
                });
        if (request.getClasses().length > 0) {
            List<String> t = Arrays.asList(request.getClasses());
            classes.removeIf(p -> !t.contains(p));
        }
        return classes;
    }
}
