package com.davivienda.sv.app.dto;

import java.util.List;

public  class BatchOptimizedRequest {
    private List<Integer> transactionIds;
    private List<Integer> definitionIds;

    public BatchOptimizedRequest(List<Integer> transactionIds, List<Integer> definitionIds) {
		super();
		this.transactionIds = transactionIds;
		this.definitionIds = definitionIds;
	}
	// Getters y Setters
    public List<Integer> getTransactionIds() { return transactionIds; }
    public void setTransactionIds(List<Integer> transactionIds) { this.transactionIds = transactionIds; }

    public List<Integer> getDefinitionIds() { return definitionIds; }
    public void setDefinitionIds(List<Integer> definitionIds) { this.definitionIds = definitionIds; }
}
