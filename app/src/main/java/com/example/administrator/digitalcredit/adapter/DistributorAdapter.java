package com.example.administrator.digitalcredit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.administrator.digitalcredit.Model.DistributorResponse;
import com.example.administrator.digitalcredit.R;

import java.util.List;

public class DistributorAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private List<DistributorResponse> list;
    public DistributorAdapter(Context context, int resource, List<DistributorResponse> list) {
        super(context, resource, list);
        this.context=context;
        this.resource=resource;
        this.list=list;
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getUserId();
    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent) {
        return init(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return init(position,convertView,parent);
    }

    public View init(int position, View convertView, ViewGroup parent){
        View row=convertView;
        if(convertView==null){
            row=LayoutInflater.from(context).inflate(resource,parent,false);
        }
        DistributorResponse distributorResponse=list.get(position);
        TextView textView=row.findViewById(R.id.textViewDistributor);
        textView.setText(distributorResponse.getUserName());
        return row;
    }
}
