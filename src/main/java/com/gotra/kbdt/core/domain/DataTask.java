package com.gotra.kbdt.core.domain;

/**
 * @author gotra
 */

public class DataTask {
    private OperationType operation;
    private int clientId;
    private String data;

    public DataTask(OperationType operation, int clientId, String data) {
        this.operation = operation;
        this.clientId = clientId;
        this.data = data;
    }

    public OperationType getOperation() {
        return operation;
    }

    public int getClientId() {
        return clientId;
    }

    public String getData() {
        return data;
    }
}
