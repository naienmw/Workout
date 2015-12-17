package com.naien.workout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class my_adapter_sets extends ArrayAdapter<String> {

    private int myResourcexml;
    private int mytextViewID;
    private Context myContext;
    private String[] myList;

    public my_adapter_sets(Context context, String[] values) {
        super(context, R.layout.sets_row_layout,values);
    }
    /*public my_adapter(Context context, @LayoutRes int resource, @IdRes int textViewResourceId,
                      String[] list) {
        super(context,resource,textViewResourceId,list);
        myResourcexml = resource;
        mytextViewID = textViewResourceId;
        myContext = context;
        myList = list;
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater theInflater = LayoutInflater.from(getContext());

        //View theView = theInflater.inflate(R.layout.row_layout_2, parent, false);
        View theView = theInflater.inflate(R.layout.sets_row_layout, parent, false);

        String tvShow = getItem(position);

        TextView theTextView = (TextView) theView.findViewById(R.id.textViewSets);

        theTextView.setText(tvShow);


        return theView;
    }
};
