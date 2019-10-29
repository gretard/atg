package edu.ktu.atg.example;

public enum SampleWithEnum {
    ONE("1"), TWO("2");

    private final String v;

    private SampleWithEnum(String v) {
        this.v = v;
    }

    public String toSampleString() {
        return v;
    }
}
