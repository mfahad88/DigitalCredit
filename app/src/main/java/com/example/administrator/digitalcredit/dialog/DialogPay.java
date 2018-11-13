package com.example.administrator.digitalcredit.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.digitalcredit.R;
import com.example.administrator.digitalcredit.Utils.Helper;
import com.example.administrator.digitalcredit.activity.HomeActivity;
import com.example.administrator.digitalcredit.client.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogPay extends DialogFragment {
    private View viewRoot;
    private TextView tvId;
    private Bundle bundle;
    private int OrderId;
    private Helper helper;
    private Button btn_ok;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.dialog_pay, container, false);
        tvId=viewRoot.findViewById(R.id.textViewOrderId);
        btn_ok=viewRoot.findViewById(R.id.buttonOK);
        helper=Helper.getInstance();
        if(getArguments()!=null) {
            bundle = getArguments();
            OrderId=bundle.getInt("OrderId");
            ApiClient.getInstance().updateOrder(OrderId,'C').enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if(response.isSuccessful() || response.code()==200){
                        tvId.setText(String.valueOf(OrderId));
                    }else{
                        helper.showMesage(viewRoot.getRootView(),"Something went wrong...");
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    helper.showMesage(viewRoot.getRootView(),t.getMessage());
                }
            });
        }

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                startActivity(new Intent(viewRoot.getContext(),HomeActivity.class));
            }
        });
        return viewRoot;
    }
}
