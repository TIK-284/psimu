package com.example.tik.puyo_simu.SIMU;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.opengl.Matrix;

import com.example.tik.puyo_simu.R;
import com.example.tik.puyo_simu.Setting;

import java.util.Set;

/**
 * Created by TIK on 2015/12/07.
 */
public class puyo{
    //キーぷよの位置がx,y（ぷよ単位の座標）
    protected int puyo_x, puyo_y;
    protected boolean exist_flag;

    Resources res;
    //0:赤 1:青 2:黄 3:緑
    protected int puyo_color;
    private static boolean finish_loadgraphic=false;
    protected static Bitmap IMG_puyo_red;
    protected static Bitmap IMG_puyo_red_before;
    protected static Bitmap IMG_puyo_blue;
    protected static Bitmap IMG_puyo_blue_before;
    protected static Bitmap IMG_puyo_yellow;
    protected static Bitmap IMG_puyo_yellow_before;
    protected static Bitmap IMG_puyo_green;
    protected static Bitmap IMG_puyo_green_before;
    protected static Bitmap IMG_puyo_delete;
    protected static Bitmap IMG_puyo_delete_before;
    protected static Bitmap IMG_puyo_o;
    protected static Bitmap IMG_puyo_o_before;
    protected static Bitmap IMG_puyo_o1;
    protected static Bitmap IMG_puyo_o1_before;
    protected static Bitmap IMG_puyo_o2;
    protected static Bitmap IMG_puyo_o2_before;
    protected static Bitmap IMG_puyo_o3;
    protected static Bitmap IMG_puyo_o3_before;
    protected static Bitmap IMG_puyo_o4;
    protected static Bitmap IMG_puyo_o4_before;
    protected static Bitmap IMG_puyo_o5;
    protected static Bitmap IMG_puyo_o5_before;
    protected static Bitmap IMG_puyo_o6;
    protected static Bitmap IMG_puyo_o6_before;



    public puyo(Resources r){
        exist_flag=false;
        this.res=r;
    }

    public void setPuyo(int x, int y, int color){
        if(!exist_flag){
            puyo_x =x;
            puyo_y =y;
            puyo_color=color;
            exist_flag=true;
        }
    }

    public void draw(Canvas canvas, Paint paint){
        if(exist_flag)
            drawpuyo(0,puyo_color, puyo_x, puyo_y,canvas,paint);
    }
    protected void drawpuyo(int flag,int color,float x,float y, Canvas canvas, Paint paint){
        //flag:0だとfield, 1だとpanel基準にxy設定
        int x_temp;
        int y_temp;
        if(flag==0){
            x_temp=Coordinates.getfieldX(x);
            y_temp=Coordinates.getfieldY(y);
        } else {
            x_temp=Coordinates.getpanelX(x);
            y_temp=Coordinates.getpanelY(y);
        }
        switch (color) {
            case 0:
                canvas.drawBitmap(IMG_puyo_red,x_temp, y_temp,paint);
                break;
            case 1:
                canvas.drawBitmap(IMG_puyo_blue,x_temp, y_temp,paint);
                break;
            case 2:
                canvas.drawBitmap(IMG_puyo_yellow,x_temp, y_temp,paint);
                break;
            case 3:
                canvas.drawBitmap(IMG_puyo_green,x_temp, y_temp,paint);
                break;
            default:
                drawOther(x_temp, y_temp, color, canvas, paint);
                break;
        }
    }
    public void drawyokoku(int flag,int syurui,float x,float y, Canvas canvas, Paint paint){
        //flag:0だとfield, 1だとpanel基準にxy設定
        int x_temp;
        int y_temp;
        if(flag==0){
            x_temp=Coordinates.getfieldX(x);
            y_temp=Coordinates.getfieldY(y);
        } else {
            x_temp=Coordinates.getpanelX(x);
            y_temp=Coordinates.getpanelY(y);
        }
        switch (syurui) {
            case 0:
                canvas.drawBitmap(IMG_puyo_o1,x_temp, y_temp,paint);
                break;
            case 1:
                canvas.drawBitmap(IMG_puyo_o2,x_temp, y_temp,paint);
                break;
            case 2:
                canvas.drawBitmap(IMG_puyo_o3,x_temp, y_temp,paint);
                break;
            case 3:
                canvas.drawBitmap(IMG_puyo_o4,x_temp, y_temp,paint);
                break;
            case 4:
                canvas.drawBitmap(IMG_puyo_o5,x_temp, y_temp,paint);
                break;
            case 5:
                canvas.drawBitmap(IMG_puyo_o6, x_temp, y_temp, paint);
                break;
        }
    }
    private void drawOther(float x, float y, int color, Canvas canvas, Paint paint){
        switch (color){
            case -1://削除後
                canvas.drawBitmap(IMG_puyo_delete,x, y,paint);
                break;
            case -2://おじゃまぷよ
                canvas.drawBitmap(IMG_puyo_o,x, y,paint);
                break;
        }
    }

    public static void loadGraphic(Resources res){
        if(!finish_loadgraphic){
            IMG_puyo_red_before= BitmapFactory.decodeResource(res, R.drawable.puyo_red);
            IMG_puyo_blue_before= BitmapFactory.decodeResource(res, R.drawable.puyo_blue);
            IMG_puyo_yellow_before= BitmapFactory.decodeResource(res, R.drawable.puyo_yellow);
            IMG_puyo_green_before= BitmapFactory.decodeResource(res, R.drawable.puyo_green);
            IMG_puyo_delete_before= BitmapFactory.decodeResource(res, R.drawable.puyo_delete);
            IMG_puyo_o_before= BitmapFactory.decodeResource(res, R.drawable.o);
            IMG_puyo_o1_before= BitmapFactory.decodeResource(res, R.drawable.o1);
            IMG_puyo_o2_before= BitmapFactory.decodeResource(res, R.drawable.o2);
            IMG_puyo_o3_before= BitmapFactory.decodeResource(res, R.drawable.o3);
            IMG_puyo_o4_before= BitmapFactory.decodeResource(res, R.drawable.o4);
            IMG_puyo_o5_before= BitmapFactory.decodeResource(res, R.drawable.o5);
            IMG_puyo_o6_before= BitmapFactory.decodeResource(res, R.drawable.o6);
            finish_loadgraphic=true;
        }
    }
    public static void resize(int size){
        IMG_puyo_red = Bitmap.createScaledBitmap(IMG_puyo_red_before, size, size, false);
        IMG_puyo_blue = Bitmap.createScaledBitmap(IMG_puyo_blue_before, size, size, false);
        IMG_puyo_yellow = Bitmap.createScaledBitmap(IMG_puyo_yellow_before, size,size, false);
        IMG_puyo_green = Bitmap.createScaledBitmap(IMG_puyo_green_before, size,size, false);
        IMG_puyo_delete = Bitmap.createScaledBitmap(IMG_puyo_delete_before, size,size, false);
        IMG_puyo_o = Bitmap.createScaledBitmap(IMG_puyo_o_before, size,size, false);
        size*=0.8;
        IMG_puyo_o1 = Bitmap.createScaledBitmap(IMG_puyo_o1_before, size,size, false);
        IMG_puyo_o2 = Bitmap.createScaledBitmap(IMG_puyo_o2_before, size,size, false);
        IMG_puyo_o3 = Bitmap.createScaledBitmap(IMG_puyo_o3_before, size,size, false);
        IMG_puyo_o4 = Bitmap.createScaledBitmap(IMG_puyo_o4_before, size,size, false);
        IMG_puyo_o5 = Bitmap.createScaledBitmap(IMG_puyo_o5_before, size,size, false);
        IMG_puyo_o6 = Bitmap.createScaledBitmap(IMG_puyo_o6_before, size,size, false);

    }
    public void nextdraw(int mode, int number, int col1, int col2, Canvas canvas, Paint paint){
        if(!Setting.twoP_place) {
            if (mode == 5) {
                drawpuyo(1, col1, (float) (-1 + number), 3, canvas, paint);
                drawpuyo(1, col2, (float) (-1 + number), 4, canvas, paint);
            } else if (mode > 2) {
                drawpuyo(1, col1, (float) (-0.5 + number), 3, canvas, paint);
                drawpuyo(1, col2, (float) (-0.5 + number), 4, canvas, paint);
            } else if (mode == 2) {
                drawpuyo(1, col1, (float) (0 + number * 1.5), 3, canvas, paint);
                drawpuyo(1, col2, (float) (0 + number * 1.5), 4, canvas, paint);
            } else if (mode == 1) {
                drawpuyo(1, col1, (float) (0 + number), 3, canvas, paint);
                drawpuyo(1, col2, (float) (0 + number), 4, canvas, paint);
            }
        } else {
            if (mode == 5) {
                drawpuyo(1, col1, (float) (3 - number), 3, canvas, paint);
                drawpuyo(1, col2, (float) (3 - number), 4, canvas, paint);
            } else if (mode > 2) {
                drawpuyo(1, col1, (float) (2.5 - number), 3, canvas, paint);
                drawpuyo(1, col2, (float) (2.5 - number), 4, canvas, paint);
            } else if (mode == 2) {
                drawpuyo(1, col1, (float) (2 - number * 1.5), 3, canvas, paint);
                drawpuyo(1, col2, (float) (2 - number * 1.5), 4, canvas, paint);
            } else if (mode == 1) {
                drawpuyo(1, col1, (float) (2 - number), 3, canvas, paint);
                drawpuyo(1, col2, (float) (2 - number), 4, canvas, paint);
            }
        }
    }

    public int getPuyo_x(){
        return puyo_x;
    }
    public float getPuyo_y(){
        return puyo_y;
    }
    public int getColor(){
        return puyo_color;
    }
    public boolean getExist(){
        return exist_flag;
    }
    public void delete(){
        puyo_color=-1;
    }
    public void exist_set(boolean ex){
        exist_flag=ex;
    }
    public void setcolor(int col){
        puyo_color=col;
    }
    public puyo clone(){
        puyo cloned=new puyo(res);
        if(exist_flag==true)
            cloned.setPuyo(puyo_x, puyo_y, puyo_color);
        return cloned;
    }
}
