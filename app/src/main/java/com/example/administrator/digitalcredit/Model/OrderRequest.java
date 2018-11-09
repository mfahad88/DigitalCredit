package com.example.administrator.digitalcredit.Model;

import java.util.List;

public class OrderRequest {

	private float totalAmt;
	private int userId;
	private int totalItem;
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
}