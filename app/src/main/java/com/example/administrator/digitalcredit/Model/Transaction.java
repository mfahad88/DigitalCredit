package com.example.administrator.digitalcredit.Model;

public class Transaction {

	private int trx_id;
	private int debit_user_id;
	private int credit_user_id;
	private float amt;
	private String created_at;
	private float feesAmt;
	
	public float getFeesAmt() {
		return feesAmt;
	}

	public void setFeesAmt(float feesAmt) {
		this.feesAmt = feesAmt;
	}

	public Transaction() {
		// TODO Auto-generated constructor stub
	}

	public int getTrx_id() {
		return trx_id;
	}

	public void setTrx_id(int trx_id) {
		this.trx_id = trx_id;
	}

	public int getDebit_user_id() {
		return debit_user_id;
	}

	public void setDebit_user_id(int debit_user_id) {
		this.debit_user_id = debit_user_id;
	}

	public int getCredit_user_id() {
		return credit_user_id;
	}

	public void setCredit_user_id(int credit_user_id) {
		this.credit_user_id = credit_user_id;
	}

	public float getAmt() {
		return amt;
	}

	public void setAmt(float amt) {
		this.amt = amt;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public Transaction(int trx_id, int debit_user_id, int credit_user_id, float amt, String created_at) {
		super();
		this.trx_id = trx_id;
		this.debit_user_id = debit_user_id;
		this.credit_user_id = credit_user_id;
		this.amt = amt;
		this.created_at = created_at;
	}

	@Override
	public String toString() {
		return "Transaction [trx_id=" + trx_id + ", debit_user_id=" + debit_user_id + ", credit_user_id="
				+ credit_user_id + ", amt=" + amt + ", created_at=" + created_at + "]";
	}
	
	
}