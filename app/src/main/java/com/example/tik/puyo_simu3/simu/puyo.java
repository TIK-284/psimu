package com.example.tik.puyo_simu3.simu;

import android.bluetooth.BluetoothA2dp;

import java.io.Serializable;

/**
 * Created by TIK on 2016/02/10.
 */
public class puyo implements Serializable, Cloneable{
    protected boolean exist_flag=false;
    protected Color color=Color.UNDEFINED;

    public enum Color{
        RED{
            @Override
            public boolean Normal(){return true;}
        },
        GREEN{
            @Override
            public boolean Normal(){return true;}
        },
        BLUE{
            @Override
            public boolean Normal(){return true;}
        },
        YELLOW{
            @Override
            public boolean Normal(){return true;}
        },
        PURPLE{
            @Override
            public boolean Normal(){return true;}
        },
        OZYAMA{
            @Override
            public Color indirectDelete(){return DELETED;}
        },
        KABE,
        IRON,
        FIRM{
            @Override
            public Color indirectDelete(){return OZYAMA;}
        },
        UNDEFINED,
        DELETED;

        public boolean Normal(){
            return false;
        }
        public Color indirectDelete(){
            return this;
        }
        public Color ret(){
            return this;
        }
    }

    public void setColor(Color color){
        this.color=color;
    }
    public void setExist_flag(boolean flag){
        exist_flag=flag;
    }
    public Color getColor(){
        return color;
    }
    public boolean getExist_flag(){
        return exist_flag;
    }
    @Override
    public puyo clone(){
        puyo b=null;
        try{
            b=(puyo)super.clone();
            b.color=this.color.ret();
        } catch(Exception e){

        }
        return b;
    }
}
