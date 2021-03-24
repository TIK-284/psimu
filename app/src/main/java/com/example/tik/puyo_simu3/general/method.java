package com.example.tik.puyo_simu3.general;

/**
 * Created by TIK on 2016/02/15.
 */
public class method {
    static public int getRensaBonus(int version, int rensa){
        //通以降は0、初代は1
        if(version==0) {
            switch (rensa) {
                case 1:
                    return 0;
                case 2:
                    return 8;
                case 3:
                    return 16;
                case 4:
                    return 32;
                case 5:
                    return 64;
                case 6:
                    return 96;
                case 7:
                    return 128;
                case 8:
                    return 160;
                case 9:
                    return 192;
                case 10:
                    return 224;
                case 11:
                    return 256;
                case 12:
                    return 288;
                case 13:
                    return 320;
                case 14:
                    return 352;
                case 15:
                    return 384;
                case 16:
                    return 416;
                case 17:
                    return 448;
                case 18:
                    return 480;
                case 19:
                    return 512;
            }
        } else if(version==1){
            switch (rensa){
                case 1:
                    return 0;
                case 2:
                    return 8;
                case 3:
                    return 16;
                case 4:
                    return 32;
                case 5:
                    return 64;
                case 6:
                    return 128;
                case 7:
                    return 256;
                case 8:
                    return 512;
            }
            if(rensa>8)return 999;
        }
        return 0;
    }
    static public int getRenketsuBonus(int renketsu){
        switch (renketsu){
            case 4:return 0;
            case 5:return 2;
            case 6:return 3;
            case 7:return 4;
            case 8:return 5;
            case 9:return 6;
            case 10:return 7;
        }
        if(renketsu>10)return 10;
        return 0;
    }
    static public int getColorBonus(int Color){
        switch (Color){
            case 1:return 0;
            case 2:return 3;
            case 3:return 6;
            case 4:return 12;
            case 5:return 24;
        }
        return 0;
    }
    static public int getPTS(int version, int counter, int rensa, int color, int renketsu){
        //最後だけは連結ボーナスを計算されたものを
        int temp=method.getRensaBonus(version, rensa)+
                method.getColorBonus(color)+renketsu;
        if(counter!=0 && temp==0)temp=1;
        if(temp>999)temp=999;
        return counter*10*(temp);
    }
}
