package edu.ktu.atg.common.goals;

public enum DistanceCheckType {
    HITMIN(Double.MAX_VALUE, true, true), HITMAX(Double.MIN_VALUE, true, false),
    UNHITMIN(Double.MAX_VALUE, false, true), UNHITMAX(Double.MIN_VALUE, false, false);
    double initialValue;
    boolean needsHit;
    private boolean lowerIsBetter;

    DistanceCheckType(double initialValue, boolean needsHit, boolean lowerIsBetter) {
        this.initialValue = initialValue;
        this.needsHit = needsHit;
        this.lowerIsBetter = lowerIsBetter;
    }

    public boolean matches(double currentDistance, double newDistance) {
        if (lowerIsBetter &&  newDistance < currentDistance) {
            return true;
        }
        if (!lowerIsBetter && newDistance > currentDistance ) {
            return true;
        }
        return false;
    }

    public double returnBetter(double currentDistance, double newDistance) {
        if (lowerIsBetter &&  newDistance < currentDistance) {
            return newDistance;
        }
        if (!lowerIsBetter && newDistance > currentDistance ) {
            return newDistance;
        }
        return currentDistance;
    }

}