package com.example.tik.puyo_simu.general;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;

import com.example.tik.puyo_simu.SIMU.puyo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by TIK on 2015/12/12.
 */
public class method {
    public final static boolean debug_state=false;
    public static ArrayList<String> status=new ArrayList<String>();//デバッグ用
    public static int log_num=0;

    static public int dirToX(int dir){
        if(dir==1)return 1;
        if(dir==3)return -1;
        return 0;
    }
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


    static public Intent tweet(String URL) {
        String strTweet = "";
        String strMessage = "";
        String strHashTag = "#AndroidPuyoSimu";

        try {
            strTweet = "http://twitter.com/intent/tweet?text="
                    + URLEncoder.encode(strMessage, "UTF-8")
                    + "+"
                    + URLEncoder.encode(strHashTag, "UTF-8")
                    + "&url="
                    + URLEncoder.encode(URL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strTweet));
        return intent;
    }

    static public String calculateIPSURL(puyo field[]){
        String URL="http://ips.karou.jp/simu/ps.html?";
        String tempStr="";
        int temp1, temp2;
        if(field.length<78)return "0";
        for(int i=0;i<39;i++){
            temp1=0;temp2=0;
            if(field[i*2].getExist())
                temp1=colorToIPS(field[i*2].getColor());
            if(field[i*2+1].getExist())
                temp2=colorToIPS(field[i*2+1].getColor());
            tempStr=tempStr+getIPSChar(temp1*8+temp2);
        }
        while(tempStr.isEmpty()!=true && tempStr.charAt(0)=='0')tempStr=tempStr.substring(1);
        URL=URL+tempStr;
        return URL;
    }
    static public char nextToIPS(int col1, int col2){
        int temp1=colorToIPS(col1);
        int temp2=colorToIPS(col2);
        switch(temp2){
            case 1:
                return (char)('0'+(temp1-1)*2);
            case 2:
                return (char)('c'+(temp1-1)*2);
            case 3:
                return (char)('o'+(temp1-1)*2);
            case 4:
                return (char)('A'+(temp1-1)*2);
            case 5:
                return (char)('M'+(temp1-1)*2);
            default:
                return '0';
        }
    }
    static public int colorToIPS(int color){
        switch(color){
            case -2:return 6;
            case 0:return 1;
            case 1:return 3;
            case 2:return 4;
            case 3:return 2;
            case 4:return 5;
        }
        return 0;
    }
    static public char getIPSChar(int code){
        if(code<10)return (char)('0'+code);
        if(code<36)return (char)('a'+code-10);
        if(code<62)return (char)('A'+code-36);
        return '0';
    }
    static public int random_int(int max){
        //max:4 -> return:0-3
        if(max==0)return 0;
        int temp;
        temp= (int) (Math.random() * max);
        if(temp==max)temp--;
        return temp;
    }
    private static int pow2(int in){
        if(in==0)return 1;
        int temp=1;
        for(int i=0;i<in;i++){
            temp*=2;
        }
        return temp;
    }
    public static boolean allFalse(boolean[] in){
        for(int i=0;i<in.length;i++){
            if(in[i]==true)return false;
        }
        return true;
    }
    public static String BoolArrayToString(boolean[] in){
        String temp="";
        for(int i=0;i<in.length;i++){
            if(in[i])temp+="1";
            else temp+="0";
        }
        return temp;
    }

    public static void debug_makelog(String func) {
        if(debug_state==false)return;
        log_num++;
        status.add(0, log_num + func);
    }

    public static void debug_output(Canvas canvas, Paint paint){
        paint.setTextSize(40);
        for(int i=0;i<Math.min(30, status.size());i++)
            canvas.drawText(status.get(i),0,400+i*40,paint);
    }
}
