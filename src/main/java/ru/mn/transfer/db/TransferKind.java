package ru.mn.transfer.db;

public enum TransferKind {
    IN(1),
    OUT(-1);

    private final int value;

    TransferKind(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
