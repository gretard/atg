package edu.ktu.atg.generator.ga.stopping;

public class TimeBasedStoppingFuntion implements IStoppingFuntion {

    private final long started = System.currentTimeMillis();
    private long timeout;

    public TimeBasedStoppingFuntion(int timeout) {
        this.timeout = timeout * 1000;
    }

    @Override
    public boolean shouldStop() {
        return (System.currentTimeMillis() - started) > this.timeout;

    }

}
