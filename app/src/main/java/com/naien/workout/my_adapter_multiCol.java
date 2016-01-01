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
        return myList.length;
    }

    public Object getItem(int position){
        return myList[position][1];
    }

    public Object getthis(int position){
        return myList[position][0];
    }


    @Override
    public long getItemId(int position) {

        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater theInflater = activity.getLayoutInflater();

        if(convertView == null){
            convertView=theInflater.inflate(R.layout.multi_row_layout, null);

            txtFirst=(TextView) convertView.findViewById(R.id.textViewWO_multi);
            txtSecond=(TextView) convertView.findViewById(R.id.textViewDate_multi);
        }

        txtFirst.setText(myList[position][0]);
        txtSecond.setText(myList[position][1]);

        return convertView;
    }
}
