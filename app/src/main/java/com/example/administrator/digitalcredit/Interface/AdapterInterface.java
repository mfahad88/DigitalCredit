package com.example.administrator.digitalcredit.Interface;

import com.example.administrator.digitalcredit.Model.CartBean;

public interface AdapterInterface {

    void totalItems(String operator);
    void totalAmount(Float amount,String operator);
    void order(CartBean cartBean);
}
