package com.example.administrator.digitalcredit.Model;

import java.util.List;

public class OrderRequest {

	private float totalAmt;
	private int userId;
	private int totalItem;
	private char order_status;
	private List<CartBean> orderDetail;
	
	
	public OrderRequest() {
		// TODO Auto-generated constructor stub
	}

	public float getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(float totalAmt) {
		this.totalAmt = totalAmt;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getTotalItem() {
		return totalItem;
	}

	public void setTotalItem(int totalItem) {
		this.totalItem = totalItem;
	}

	public List<CartBean> getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(List<CartBean> orderDetail) {
		this.orderDetail = orderDetail;
	}

	public char getOrder_status() {
		return order_status;
	}

	public void setOrder_status(char order_status) {
		this.order_status = order_status;
	}
}