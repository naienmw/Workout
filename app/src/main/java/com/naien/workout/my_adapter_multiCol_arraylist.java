package com.naien.workout;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class my_adapter_multiCol_arraylist extends ArrayAdapter{

    private Activity activity;
    private ArrayList<ArrayList<String>> myList;
    TextView txtFirst;
    TextView txtSecond;

    //ViewHolder holder;

    private static LayoutInflater theInflater = null;




   /* public static class ViewHolder{

    }*/

        public my_adapter_multiCol_arraylist(Activity activity, ArrayList<ArrayList<String>> values) {
        super(activity,R.layout.multi_row_layout,values);
        this.activity = activity;
        this.myList = values;
            theInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    public Object getItem(int position){
        return myList.get(position).get(1);
    }

    public Object getthis(int position){
        return myList.get(position).get(0);
    }


    @Override
    public long getItemId(int position) {

        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {




        if(convertView == null){
            convertView=theInflater.inflate(R.layout.multi_row_layout,parent,false);
            //holder = new ViewHolder();

            //LayoutInflater theInflater = activity.getLayoutInflater();

            //convertView.setTag(holder);


        }
        txtFirst = (TextView) convertView.findViewById(R.id.textViewWO_multi);
        txtSecond = (TextView) convertView.findViewById(R.id.textViewDate_multi);

        txtFirst.setText(myList.get(position).get(0));
        txtSecond.setText(myList.get(position).get(1));

        return convertView;
    }
}
