package edu.ktu.atg.generator.ga.stopping;

import edu.ktu.atg.generator.ga.SolutionsContext;

public class AnySolutionExistsForCheckingStoppingFuntion implements IStoppingFuntion {

    private SolutionsContext context;

    public AnySolutionExistsForCheckingStoppingFuntion(SolutionsContext context) {
        this.context = context;
    }

    @Override
    public boolean shouldStop() {
        return context.solutionsToCheck.isEmpty();

    }

}
