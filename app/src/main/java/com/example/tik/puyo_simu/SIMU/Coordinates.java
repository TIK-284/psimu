package com.example.tik.puyo_simu.SIMU;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.example.tik.puyo_simu.Setting;
import com.example.tik.puyo_simu.general.method;

/**
 * Created by TIK on 2015/12/19.
 */
public class Coordinates {
    public static int field_offsetX=0;
    public static int field_offsetY=0;
    public static int panel_offsetX=0;
    public static int panel_offsetY=0;
    public static int puyo_size =30;

    public static int disp_x;
    public static int disp_y;
    public static int view_x;
    public static int view_y;
    public static int contentViewTop;

    public static void getDisplaySize(Context context, Resources res){
        Display disp = ((WindowManager) context.
                getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        disp_x=size.x;
        disp_y=size.y;
        puyo_size =disp_y/20;
        setcoordinates(res);
        method.debug_makelog("disp_size x=" + disp_x + "y=" + disp_y);
    }
    public static void setViewSize(Resources res, int x, int y){
        view_x=x;
        view_y=y;
        contentViewTop=disp_y-view_y;
        decide();
        method.debug_makelog("ViewSize x=" + x + "y=" + y + "top=" + contentViewTop + "size=" + puyo_size);
        setcoordinates(res);
    }
    public static void decide(){
        if (Setting.expansion) {
            puyo_size = view_y / 17;
            field_offsetY = puyo_size * 2;
            panel_offsetY = puyo_size * 2;
        } else {
            puyo_size = view_y / 19;
            field_offsetY = puyo_size * 4;
            panel_offsetY = puyo_size * 3;
        }

        if(!Setting.twoP_place) {
            field_offsetX = (int) (puyo_size * -0.5);
            panel_offsetX = puyo_size * 8;
        } else {
            field_offsetX = (int) (puyo_size * 4.5);
            panel_offsetX = (int) (puyo_size * 1);
        }
    }
    public static void setcoordinates(Resources res){
        puyo.loadGraphic(res);
        puyo.resize(puyo_size);
    }
    public static int getfieldX(float x){
        return (int)(field_offsetX+ puyo_size *x);
    }
    public static int getfieldY(float y){
        return (int)(field_offsetY+ puyo_size *y);
    }
    public static int getpanelX(double x){
        return (int)(panel_offsetX+ puyo_size *x);
    }
    public static int getpanelY(double y){
        return (int)(panel_offsetY+ puyo_size *y);
    }
    public static void setViewTop(int t){
        contentViewTop=t;
    }

}