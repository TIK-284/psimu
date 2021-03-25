package com.example.tik.puyo_simu.IO;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

/**
 * Created by Takumi on 15/12/11.
 */
public class Spinner_deal implements Spinner.OnItemSelectedListener{
    static public int tumo_type=0;
    public void onItemSelected(AdapterView<?> parent, View view,
                               int position, long id) {
        tumo_type=position;
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }
    public int getTumo_type(){
        return tumo_type;
    }
}