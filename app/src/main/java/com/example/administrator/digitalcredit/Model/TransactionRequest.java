package com.example.administrator.digitalcredit.Model;

public class TransactionRequest {
private Transaction traction;

 public TransactionRequest() {
	// TODO Auto-generated constructor stub
}
 
public Transaction getTraction() {
	return traction;
}

public void setTraction(Transaction traction) {
	this.traction = traction;
}

@Override
public String toString() {
	return "TransactionRequest [traction=" + traction + "]";
}


}