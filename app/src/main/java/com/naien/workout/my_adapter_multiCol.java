package com.naien.workout;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

class my_adapter_multiCol extends BaseAdapter{

    private int myResourcexml;
    private int mytextViewID;
    private Activity activity;
    private String[][] myList;

    TextView txtFirst;
    TextView txtSecond;

    public my_adapter_multiCol(Activity activity, String[][] values) {
        super();
        this.activity = activity;
        this.myList = values;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return myList.length;
    }

    public Object getItem(int position){
        return myList[position][1];
    }

    /*public String getItem(int position, int col) { //col 0 or 1
        // TODO Auto-generated method stub
        return myList[position][col];
    }*/

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater theInflater = activity.getLayoutInflater();

        if(convertView == null){
            convertView=theInflater.inflate(R.layout.multi_row_layout, null);

            txtFirst=(TextView) convertView.findViewById(R.id.textViewWO);
            txtSecond=(TextView) convertView.findViewById(R.id.textViewDate);
        }

        txtFirst.setText(myList[position][0]);
        txtSecond.setText(myList[position][1]);


        return convertView;
    }
};
