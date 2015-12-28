package com.naien.workout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class my_adapter_sets_arraylist_static extends ArrayAdapter<String> {


    public my_adapter_sets_arraylist_static(Context context, ArrayList<String> values) {
        super(context, R.layout.sets_row_layout_static,values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(R.layout.sets_row_layout_static, parent, false);

        String tvShow = getItem(position);

        TextView theTextView = (TextView) theView.findViewById(R.id.textViewSets_static);

        theTextView.setText(tvShow);


        return theView;
    }
}
