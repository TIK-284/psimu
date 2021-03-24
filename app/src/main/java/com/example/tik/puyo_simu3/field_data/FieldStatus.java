package com.example.tik.puyo_simu3.field_data;

import com.example.tik.puyo_simu3.simu.field;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by TIK on 2016/02/10.
 */
public class FieldStatus extends FieldStatus_brief{
    private field field_data;

    public FieldStatus(){
        super();
        field_data=new field();
    }
    public field getField(){
        return field_data;
    }
    public void setField(field data){
        this.field_data=data;
    }
}