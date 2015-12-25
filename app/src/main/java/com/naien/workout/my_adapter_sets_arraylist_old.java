package com.naien.workout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class my_adapter_sets_arraylist_old extends ArrayAdapter<String> {

    Integer id;

    public my_adapter_sets_arraylist_old(Context context, ArrayList<String> values) {
        super(context, R.layout.sets_row_layout_old,values);
        this.id = id;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(R.layout.sets_row_layout_old, parent, false);

        String tvShow = getItem(position);

        TextView theTextView = (TextView) theView.findViewById(R.id.textViewSets_old);

        theTextView.setText(tvShow);


        return theView;
    }
};
