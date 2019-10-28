package edu.ktu.atg.common.monitors;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import edu.ktu.atg.common.execution.SolutionExecutionData;

public enum BranchesMonitor implements IBaseMonitor {
    INSTANCE;

    private List<BranchInfo> branchesCalled = new LinkedList<BranchInfo>();

    private List<BranchHit> branchesHit = new LinkedList<BranchHit>();

    public void clear() {
        this.branchesCalled = new LinkedList<>();
        this.branchesHit = new LinkedList<>();
    }

    public List<BranchInfo> getBranchesCalled() {
        return branchesCalled;
    }

    public List<BranchHit> getBranchesHit() {
        return branchesHit;
    }

    public synchronized void fill(SolutionExecutionData results) {
        BranchInfo[] branchesInfo = this.branchesCalled.toArray(new BranchInfo[0]);
        BranchHit[] branchesHit = this.branchesHit.toArray(new BranchHit[0]);
        for (int i = 0; i < branchesInfo.length; i++) {
            results.getBranchesCalled().add(branchesInfo[i]);
        }
        for (int i = 0; i < branchesHit.length; i++) {
            results.getBranchesHit().add(branchesHit[i]);
        }

    }

    public synchronized void hit(String methodUniqueName, int no) {
        final BranchHit info = new BranchHit();
        info.name = methodUniqueName;
        info.no = no;
        this.branchesHit.add(info);
    }

    public synchronized void hitWithValue(String methodUniqueName, int no, Object left, Object right) {
        final BranchInfo info = new BranchInfo();
        info.name = methodUniqueName;
        info.no = no;
        info.distance = calculateDistance(left, right);
        info.left = left;
        info.right = right;
        this.branchesCalled.add(info);
    }

    public static class BranchInfo {
        public Object right;
        public Object left;

        @Override
        public String toString() {
            return "BranchInfo [methodUniqueName=" + name + ", no=" + no + ", distance=" + distance + "]";
        }

        public BranchInfo() {

        }

        public BranchInfo(String name, int no, double distance) {
            this.name = name;
            this.no = no;
            this.distance = distance;
        }

        public String name;
        public int no;

        public String getName() {
            return name;
        }

        public int getNo() {
            return no;
        }

        public double getDistance() {
            if (distance == Double.POSITIVE_INFINITY) {
                return Double.MAX_VALUE;
            }
            return distance;
        }

        public double distance = Double.NaN;
    }

    public static class BranchHit {
        public String getName() {
            return name;
        }

        public int getNo() {
            return no;
        }

        public String name;
        public int no;

        public BranchHit() {

        }

        public BranchHit(String name, int no) {
            this.name = name;
            this.no = no;
        }
    }

    public static double calculateDistance(final Object o1, final Object o2) {

        if (o1 == o2) {
            return 0;
        }

        if (o1 instanceof Number && o2 instanceof Number) {
            try {
                final double v1 = Double.parseDouble(o1.toString());
                final double v2 = Double.parseDouble(o2.toString());
                return Math.abs((v2 - v1) * 1.0);
            } catch (Throwable e) {
                return Double.MAX_VALUE;

            }
        }

        if (o1 instanceof Boolean && o2 instanceof Boolean) {
            Boolean b1 = (Boolean) o1;
            Boolean b2 = (Boolean) o2;
            return Math.abs(b1.compareTo(b2));
        }

        if (o1 instanceof CharSequence && o2 instanceof CharSequence) {
            CharSequence s1 = (CharSequence) o1;
            CharSequence s2 = (CharSequence) o2;
            return calculateFromChars(s1, s2);
        }
        return Double.MAX_VALUE;

    }

    private static double calculateFromChars(final CharSequence s1, final CharSequence s2) {
        final int max = Math.max(s1.length(), s2.length());
        final int[] ar1 = Arrays.copyOf(s1.chars().toArray(), max);
        final int[] ar2 = Arrays.copyOf(s2.chars().toArray(), max);

        double distance = 0;
        for (int i = 0; i < max; i++) {
            distance += Math.pow(Math.abs(ar1[i] - ar2[i]), 2);
        }
        return Math.sqrt(distance);
    }

}
