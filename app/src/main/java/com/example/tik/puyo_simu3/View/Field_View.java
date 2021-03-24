package com.example.tik.puyo_simu3.View;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.example.tik.puyo_simu3.R;
import com.example.tik.puyo_simu3.simu.field;
import com.example.tik.puyo_simu3.simu.puyo;

/**
 * Created by TIK on 2016/02/11.
 */
public class Field_View extends View {
    private Resources res=this.getContext().getResources();
    private int puyo_size;
    private boolean validTouch=true;

    private field field_;
    private puyo.Color selected_color= puyo.Color.RED;

    public Field_View(Context context){
        super(context);
        field_=new field();
        UseBitmap.includeGraphics(res);
    }

    @Override
    public void onDraw(Canvas canvas){
        puyo_size=getHeight()/13;

        Paint paint=new Paint();
        for(int i=0;i<6;i++){
            for(int j=0;j<13;j++){
                if(field_.getPuyoColor(i,j)!=puyo.Color.UNDEFINED){
                    drawPuyo(i,j,field_.getPuyoColor(i,j),canvas,paint);
                }
            }
        }

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.argb(90, 0, 0, 0));
        canvas.drawRect(0, 0, puyo_size * 6, puyo_size, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, puyo_size * 6, puyo_size * 13, paint);
    }
    private void drawPuyo(int x, int y, puyo.Color color,Canvas canvas,Paint paint){
        Rect src=new Rect(0,0,UseBitmap.getIMGSize(),UseBitmap.getIMGSize());
        Rect dst=new Rect(x*puyo_size,y*puyo_size,(x+1)*puyo_size,(y+1)*puyo_size);
        canvas.drawBitmap(UseBitmap.getIMG(color), src, dst, paint);
    }
    public void setField(field field_){
        this.field_=field_;
    }
    @Override
    public boolean onTouchEvent(MotionEvent e){
        if(!validTouch)return false;
        int x,y;
        x=(int)e.getX()/puyo_size;
        y=(int)e.getY()/puyo_size;
        if(x<0 || y<0 || x>5 || y>12)return false;
        field_.setColor(x,y,selected_color);
        invalidate();
        return false;
    }
    public void setSelected_color(puyo.Color color){this.selected_color=color;}
    public void ON_ValidTouch(){
        validTouch=true;
    }
    public void OFF_ValidTouch(){
        validTouch=false;
    }
}
