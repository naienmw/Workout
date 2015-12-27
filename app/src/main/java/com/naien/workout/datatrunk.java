package com.naien.workout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

class datatrunk {

    ArrayList<String> blub;
    Integer Offset;

    public ArrayList<String> getList(){
        return blub;
    }

    public Integer getOffset(){
        return Offset;
    }

    public void setList(ArrayList<String> list){
        blub = list;
    }

    public void setOffset(Integer numb){
        Offset = numb;
    }



}