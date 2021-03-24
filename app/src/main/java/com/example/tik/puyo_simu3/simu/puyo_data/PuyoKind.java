package com.example.tik.puyo_simu3.simu.puyo_data;

/**
 * Created by TIK on 2016/02/11.
 */
public class PuyoKind {
    private long id;
    public PuyoKind(long id){
        this.id=id;
    }
    public long getId(){
        return id;
    }
    public void setId(long id){
        this.id=id;
    }
}
