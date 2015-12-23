package com.naien.workout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class my_adapter extends ArrayAdapter<String> {


    public my_adapter(Context context, String[] values) {
        super(context, R.layout.excercise_row_layout,values);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(R.layout.excercise_row_layout, parent, false);

        String tvShow = getItem(position);

        TextView theTextView = (TextView) theView.findViewById(R.id.textViewEx);

        theTextView.setText(tvShow);


        return theView;
    }
};
