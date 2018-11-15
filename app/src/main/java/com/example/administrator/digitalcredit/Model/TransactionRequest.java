package com.example.administrator.digitalcredit.Model;

public class TransactionRequest {
private Transaction traction;
private int order_id;
private int distributer_id;
 public TransactionRequest() {
	// TODO Auto-generated constructor stub
}
 
public Transaction getTraction() {
	return traction;
}

public void setTraction(Transaction traction) {
	this.traction = traction;
}

	public int getOrder_id() {
		return order_id;
	}

	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

	public int getDistributer_id() {
		return distributer_id;
	}

	public void setDistributer_id(int distributer_id) {
		this.distributer_id = distributer_id;
	}

	@Override
	public String toString() {
		return "TransactionRequest{" +
				"traction=" + traction +
				", order_id=" + order_id +
				", distributer_id=" + distributer_id +
				'}';
	}
}