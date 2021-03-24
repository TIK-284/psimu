package com.example.tik.puyo_simu3.simu.puyo_data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tik.puyo_simu3.R;
import com.example.tik.puyo_simu3.View.UseBitmap;

import java.util.ArrayList;

/**
 * Created by TIK on 2016/02/11.
 */
public class PuyoAdapter extends BaseAdapter {
    private ArrayList<PuyoKind> items=null;
    private LayoutInflater inflater=null;
    private Context context;

    public PuyoAdapter(Context context){
        this.context=context;
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public View getView(int position, View convertView, ViewGroup parent){
        convertView=inflater.inflate(R.layout.puyoselect_row,parent,false);
        ((ImageView)convertView.findViewById(R.id.puyoselect_IMG)).setImageBitmap(UseBitmap.getIMG4Edit(position));

//        ImageView iv=(ImageView)convertView.findViewById(R.id.puyoselect_IMG);
//        ViewGroup.LayoutParams p=iv.getLayoutParams();
//        p.height=p.width;
//        iv.setLayoutParams(p);
        return convertView;
    }
    public void setItems(ArrayList<PuyoKind> temp){
        items=temp;
    }
}
