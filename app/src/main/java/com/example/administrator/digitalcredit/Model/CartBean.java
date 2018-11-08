package com.example.administrator.digitalcredit.Model;

public class CartBean{
    private int fk_product_id;
    private int qty;

    public CartBean(int fk_product_id, int qty) {
        this.fk_product_id = fk_product_id;
        this.qty = qty;
    }

    public int getFk_product_id() {
        return fk_product_id;
    }

    public void setFk_product_id(int fk_product_id) {
        this.fk_product_id = fk_product_id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "CartBean{" +
                "fk_product_id=" + fk_product_id +
                ", qty=" + qty +
                '}';
    }
}
