package com.example.tik.puyo_simu3.field_data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by TIK on 2016/02/11.
 */
public class FieldStatus_brief implements Serializable {
    private String fieldName;
    private String text;
    private long id;
    private FieldState state=FieldState.NORMAL;
    private Date lastModified;//最終更新日時
    private Mode mode;

    public enum FieldState{
        NORMAL,
        OTHER
    }
    public enum Mode{
        SIMU,
        TOKO,
        NAZO
    }

    public FieldStatus_brief(){
        if(lastModified==null)lastModified=new Date();
    }

    public long getId(){
        return id;
    }
    public void setId(long id){
        this.id=id;
    }
    public String getFieldName(){
        return fieldName;
    }
    public void setFieldName(String fieldName){
        this.fieldName=fieldName;
    }
    public String getText(){
        return text;
    }
    public void setText(String text){
        this.text=text;
    }
    public void setState(FieldState state){
        this.state=state;
    }
    public FieldState getState(){
        return state;
    }

    public void setMode(Mode mode){
        this.mode=mode;
    }
    public void setMode(int mode){
        switch(mode){
            case 0:
                this.mode=Mode.SIMU;
                break;
            case 1:
                this.mode=Mode.TOKO;
                break;
            case 2:
                this.mode=Mode.NAZO;
                break;
        }
    }
    public Mode getMode(){
        return mode;
    }
    public void setStateOther(){
        state=FieldState.OTHER;
    }
    public boolean StateNormal(){
        if(state==FieldState.NORMAL){
            return true;
        } else {
            return false;
        }
    }
}
