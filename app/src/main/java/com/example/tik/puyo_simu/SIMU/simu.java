package com.example.tik.puyo_simu.SIMU;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;

import java.util.*;

/**
 * Created by TIK on 2015/12/26.
 */
public class simu{

    protected field pyfield;
    public int TimeCounter=0;

    public Handler handler=new Handler();
    public java.util.Timer mTimer=null;
    public Handler handler2=new Handler();
    public java.util.Timer CountPress=null;


    protected Resources res=this.getContext().getResources();


    public simu(){
//        super(context);
//        setBackgroundColor(Color.WHITE);
//

//        Coordinates.getDisplaySize(context, res);
        pyfield=new field(res);

    }
    public void addTimerCounter(){TimeCounter++;}
    public void invalidate(){}
}
