package com.example.tik.puyo_simu3.View;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.tik.puyo_simu3.R;
import com.example.tik.puyo_simu3.simu.puyo;

/**
 * Created by TIK on 2016/02/11.
 */
public class UseBitmap {
    private static boolean finish_loadgraphic=false;
    public static Bitmap IMG_red;
    public static Bitmap IMG_blue;
    public static Bitmap IMG_yellow;
    public static Bitmap IMG_green;
    public static Bitmap IMG_purple;
    public static Bitmap IMG_ozyama;
    public static Bitmap IMG_kabe;
    public static Bitmap IMG_iron;
    public static Bitmap IMG_firm;
    public static Bitmap IMG_e;

    public static void includeGraphics(Resources res){
        if(!finish_loadgraphic){
            IMG_red = BitmapFactory.decodeResource(res, R.drawable.r);
            IMG_blue = BitmapFactory.decodeResource(res, R.drawable.b);
            IMG_yellow = BitmapFactory.decodeResource(res, R.drawable.y);
            IMG_green = BitmapFactory.decodeResource(res, R.drawable.g);
            IMG_purple = BitmapFactory.decodeResource(res, R.drawable.p);
            IMG_ozyama = BitmapFactory.decodeResource(res, R.drawable.o);
            IMG_kabe = BitmapFactory.decodeResource(res, R.drawable.w);
            IMG_iron = BitmapFactory.decodeResource(res, R.drawable.i);
            IMG_firm = BitmapFactory.decodeResource(res, R.drawable.k);
            IMG_e=BitmapFactory.decodeResource(res, R.drawable.e);
            finish_loadgraphic=true;
        }
    }
    public static Bitmap getIMG(puyo.Color color){
        switch (color){
            case RED:
                return IMG_red;
            case GREEN:
                return IMG_green;
            case BLUE:
                return IMG_blue;
            case YELLOW:
                return IMG_yellow;
            case PURPLE:
                return IMG_purple;
            case OZYAMA:
                return IMG_ozyama;
            case KABE:
                return IMG_kabe;
            case IRON:
                return IMG_iron;
            case FIRM:
                return IMG_firm;
            default:
                return IMG_e;
        }
    }
    public static Bitmap getIMG(int color){
        switch (color){
            case 0:
                return IMG_red;
            case 1:
                return IMG_green;
            case 2:
                return IMG_blue;
            case 3:
                return IMG_yellow;
            case 4:
                return IMG_purple;
            case 5:
                return IMG_ozyama;
            case 6:
                return IMG_kabe;
            case 7:
                return IMG_iron;
            case 8:
                return IMG_firm;
            default:
                return IMG_e;
        }
    }
    public static Bitmap getIMG4Edit(int color){
        switch (color){
            case 0:
                return IMG_e;
            case 1:
                return IMG_red;
            case 2:
                return IMG_green;
            case 3:
                return IMG_blue;
            case 4:
                return IMG_yellow;
            case 5:
                return IMG_purple;
            case 6:
                return IMG_ozyama;
            case 7:
                return IMG_kabe;
            case 8:
                return IMG_iron;
            case 9:
                return IMG_firm;
            default:
                return IMG_e;
        }
    }
    public static puyo.Color IntEToColor(int color){
        switch(color){
            case 1:
                return puyo.Color.RED;
            case 2:
                return puyo.Color.GREEN;
            case 3:
                return puyo.Color.BLUE;
            case 4:
                return puyo.Color.YELLOW;
            case 5:
                return puyo.Color.PURPLE;
            case 6:
                return puyo.Color.OZYAMA;
            case 7:
                return puyo.Color.KABE;
            case 8:
                return puyo.Color.IRON;
            case 9:
                return puyo.Color.FIRM;
            default:
                return puyo.Color.UNDEFINED;
        }
    }

    public static int getIMGSize(){
        return IMG_red.getWidth();
    }
}
