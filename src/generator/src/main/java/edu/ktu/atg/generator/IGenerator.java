package edu.ktu.atg.generator;

import java.util.List;

import edu.ktu.atg.common.execution.GenerationData;
import edu.ktu.atg.common.models.ClasszInfo;
import edu.ktu.atg.common.models.OptionsRequest;

public interface IGenerator {
    public List<GenerationData> generate(ClasszInfo info, OptionsRequest context) throws Throwable;
}
