package com.example.tik.puyo_simu3.field_data;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tik.puyo_simu3.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by TIK on 2016/02/10.
 */
public class FieldAdapter extends BaseAdapter {
    private ArrayList<FieldStatus_brief> items;
    private LayoutInflater inflater;
    Context context;

    public FieldAdapter(Context context){
        this.context=context;
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void setFieldList(ArrayList<FieldStatus_brief> FieldList){
        items=FieldList;
    }
    @Override
    public int getCount(){
        return items.size();
    }
    @Override
    public Object getItem(int position){
        return items.get(position);
    }
    @Override
    public long getItemId(int position){
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView=inflater.inflate(R.layout.simufield_row, parent, false);

        ((TextView)convertView.findViewById(R.id.simufield_name)).setText(items.get(position).getFieldName());
        ((TextView)convertView.findViewById(R.id.simufield_text)).setText(items.get(position).getText());
        switch(items.get(position).getMode()){
            case SIMU:
                ((RelativeLayout)convertView.findViewById(R.id.simufield_row_layout)).setBackgroundColor(Color.rgb(255,235,235));
                break;
            case TOKO:
                ((RelativeLayout)convertView.findViewById(R.id.simufield_row_layout)).setBackgroundColor(Color.rgb(235,235,255));
                break;
            case NAZO:
                ((RelativeLayout)convertView.findViewById(R.id.simufield_row_layout)).setBackgroundColor(Color.rgb(235,255,235));
                break;
        }
        return convertView;
    }
    public void remove(int index){
        items.remove(index);
    }
    public void rename(int index, String name){
        FieldStatus_brief temp;
        temp=items.get(index);
        temp.setFieldName(name);
        items.set(index, temp);
    }
}
