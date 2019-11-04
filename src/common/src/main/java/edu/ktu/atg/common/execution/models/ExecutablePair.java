package edu.ktu.atg.common.execution.models;

import edu.ktu.atg.common.executables.IExecutable;

public final class ExecutablePair {

    public boolean isOk = true;

    private final IExecutable root;

    public IExecutable getRoot() {
        return root;
    }

    public IExecutable getItem() {
        return item;
    }

    private final IExecutable item;

    private final IExecutable returnValue;

    public IExecutable getReturnValue() {
        return returnValue;
    }

    public ExecutablePair(IExecutable root, IExecutable item, IExecutable returnValue) {
        this.root = root;
        this.item = item;
        this.returnValue = returnValue;
    }

    public String toShort() {
        StringBuilder sb = new StringBuilder();
        sb.append("ROOT: ");
        if (root != null) {
            sb.append(root.getId());
        }
        sb.append(" item: ");
        if (item != null) {
            sb.append(item.getId());
        }
        sb.append(" rt: ");
        if (returnValue != null) {
            sb.append(returnValue.getId());
        }
        return sb.toString();
    }

    public static ExecutablePair ok(IExecutable root, IExecutable item, IExecutable returnValue) {
        return new ExecutablePair(root, item, returnValue);
    }

    public static ExecutablePair ko(IExecutable root, IExecutable item) {
        ExecutablePair pair = new ExecutablePair(root, item, null);
        pair.isOk = false;
        return pair;
    }

    public static ExecutablePair ko(IExecutable root, IExecutable item, IExecutable returnValue) {
        ExecutablePair pair = new ExecutablePair(root, item, returnValue);
        pair.isOk = false;
        return pair;
    }
}
