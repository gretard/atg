package edu.ktu.atg.main;

import java.util.Arrays;
import java.util.List;

import edu.ktu.atg.common.models.OptionsRequest;

public class ClassesFilter {
    private final List<String> classes;
    private final List<String> excluded;

    public ClassesFilter(OptionsRequest request) {
        this.classes = Arrays.asList(request.getClasses());
        this.excluded = Arrays.asList(request.getExcludedClasses());
    }

    public boolean shouldAdd(String className) {
        if (!this.classes.isEmpty()) {
            return this.classes.contains(className);
        }
        if (!this.excluded.isEmpty()) {
            return !this.classes.contains(className);
        }
        return true;

    }
}
