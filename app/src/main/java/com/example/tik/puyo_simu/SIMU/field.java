package com.example.tik.puyo_simu.SIMU;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.tik.puyo_simu.Setting;
import com.example.tik.puyo_simu.TOKO.toko_simu;
import com.example.tik.puyo_simu.general.method;

import java.util.Set;

/**
 * Created by TIK on 2015/12/07.
 */
public class field{
    private int[] default_field=new int[78];
    private puyo field_puyo[]=new puyo[78];
    private int connect[]=new int[78];//連結している数
    private boolean fin_explorer[]=new boolean[78];
    private int renketsu_flag[]=new int[78];
    Resources res;

    private int Tokuten1;
    private int Tokuten2;

    public field(Resources r){
        res=r;
        for(int i=0;i<78;i++)
            field_puyo[i]=new puyo(r);
    }
    public void setDefaultField_Clear(){
        for(int i=0;i<78;i++)field_puyo[i].exist_set(false);
        if(Setting.SecondMode){
            for(int i=0;i<Setting.SecondKosu;i++)SecondDrop();
        }
        saveToDefault();
    }
    private void SecondDrop(){
        int x=decideX();
        int color;
        while(true){
            if(method.random_int(100)<Setting.SecondConnect){
                color=searchContact(x);
            } else {
                color=method.random_int(4);
            }
            addpuyo(x,color);
            if(!findConnection())break;
            deleteX(x);
            if(Setting.SecondConnect==100){
                color=method.random_int(4);
                addpuyo(x,color);
                if(!findConnection())break;
                deleteX(x);
            }
        }
    }
    private int searchContact(int x){
        int y=getStep(x)+1;
        if(y>13)return 0;
        int[] color=new int[4];
        int pointer=0;
        if(y>2) {
            if (field_puyo[getNumber(x,y-1)].getExist()){
                color[pointer]=field_puyo[getNumber(x,y-1)].getColor();
                pointer++;
            }
        }
        if(x<6) {
            if (field_puyo[getNumber(x+1, y)].getExist()){
                color[pointer]=field_puyo[getNumber(x+1,y)].getColor();
                pointer++;
            }
        }
        if(y<13) {
            if (field_puyo[getNumber(x,y+1)].getExist()){
                color[pointer]=field_puyo[getNumber(x,y+1)].getColor();
                pointer++;
            }
        }
        if(x>1) {
            if (field_puyo[getNumber(x-1,y)].getExist()){
                color[pointer]=field_puyo[getNumber(x-1,y)].getColor();
                pointer++;
            }
        }
        if(pointer==0)return method.random_int(4);
        return color[method.random_int(pointer)];
    }
    private int decideX(){
        int x;
        if(method.random_int(100)<Setting.SecondFlat){
            int min;
            min=getStep(1);
            min=Math.min(min,getStep(2));
            min=Math.min(min,getStep(3));
            min=Math.min(min,getStep(4));
            min=Math.min(min,getStep(5));
            min=Math.min(min,getStep(6));
            while(true){
                x=method.random_int(6)+1;
                if(getStep(x)==min)break;
            }
        } else {
            if(method.random_int(6)==0){
                return 2;
            }
            if(method.random_int(5)==0){
                return 5;
            }
            int temp=method.random_int(2+Math.abs(Setting.SecondEdge));
            if(Setting.SecondEdge>0) {
                if (method.random_int(2) == 0) {
                    if (temp == 0) {
                        x=3;
                    } else {
                        x=1;
                    }
                } else {
                    if (temp == 0) {
                        x=4;
                    } else {
                        x=6;
                    }
                }
            } else {
                if (method.random_int(2) == 0) {
                    if (temp == 0) {
                        x=1;
                    } else {
                        x=3;
                    }
                } else {
                    if (temp == 0) {
                        x=6;
                    } else {
                        x=4;
                    }
                }
            }
        }
        return x;
    }
    public void saveToDefault(){
        for(int i=0;i<78;i++){
            if(field_puyo[i].exist_flag==false){
                default_field[i]=-1;
            } else {
                default_field[i]=field_puyo[i].getColor();
            }
        }
    }
    public int addpuyo(int x, int color){
        for(int i=0;i<13;i++){
            if(!field_puyo[71+x-i*6].exist_flag) {
                field_puyo[71+x-i*6].setPuyo(x, 13-i, color);
                return i+1;
            }
        }
        return 0;
    }
    public void deleteX(int x){
        for(int i=0;i<13;i++){
            if(!field_puyo[71+x-i*6].exist_flag) {
                if(i==0)return;
                i--;
                field_puyo[71+x-i*6].exist_set(false);
                return;
            }
        }
    }

    public int getStep(int x){
        for(int i=0;i<13;i++){
            if(!field_puyo[71+x-i*6].exist_flag) {
                return i;
            }
        }
        return 13;
    }
    public boolean findConnection(){
        int x,y;
        int k=0;
        boolean flag=false;//消える対象が有るかどうか
        for(int i=6;i<78;i++)connect[i]=0;
        for(int i=6;i<78;i++){
            if(field_puyo[i].getExist()){
                for (int j=6;j<78;j++)fin_explorer[j]=false;
                x=i%6+1;
                y=(i/6)+1;
                connect[i]=findConnection_loop(x, y);
                if(connect[i]>=4)flag = true;
            }
        }
        return flag;
    }
    private int findConnection_loop(int x, int y){
        //上下左右を探索、未探索で色が同じ、存在してるなら呼び出す
        int sum=1;
        fin_explorer[getNumber(x,y)]=true;
        if(y>2) {
            if (sameColor(x, y, x, y - 1))
                sum += findConnection_loop(x, y - 1);
        }
        if(x<6) {
            if (sameColor(x, y, x + 1, y))
                sum += findConnection_loop(x + 1, y);
        }
        if(y<13) {
            if (sameColor(x, y, x, y + 1))
                sum += findConnection_loop(x, y + 1);
        }
        if(x>1) {
            if (sameColor(x, y, x - 1, y))
                sum += findConnection_loop(x - 1, y);
        }
        return sum;
    }
    private boolean sameColor(int ax, int ay, int bx, int by){
        int a=getNumber(ax,ay);
        int b=getNumber(bx,by);
        if(field_puyo[a].getExist()==false || field_puyo[b].getExist()==false)
            return false;
        if(field_puyo[a].getColor()<0 || field_puyo[b].getColor()<0)
            return false;
        if(fin_explorer[b])
            return false;
        if(field_puyo[a].getColor()!=field_puyo[b].getColor())
            return false;
        return true;
    }
    private int find_renketsu(){
        for (int i=6;i<78;i++)renketsu_flag[i]=1;
        int x, y;
        int sum=0;
        for(int i=6;i<78;i++){
            x=i%6+1;
            y=(i/6)+1;
            findConnection_renketsu(x,y,true);
        }
        for(int i=6;i<78;i++){
            if(renketsu_flag[i]==2&&connect[i]>=4){
                sum+=method.getRenketsuBonus(connect[i]);
            }
        }
        return sum;
    }
    private void findConnection_renketsu(int x, int y, boolean first){
        //上下左右を探索、未探索で色が同じ、存在してるなら呼び出す
        if(renketsu_flag[getNumber(x,y)]!=1)return;
        if(first)renketsu_flag[getNumber(x,y)]=2;
        else renketsu_flag[getNumber(x,y)]=0;
        if(y>2)//上
            if(sameColor_renketsu(x, y, x, y - 1)) {
                method.debug_makelog("uprenketsu x=" + x + " y=" + y);
                findConnection_renketsu(x, y - 1, false);
            }
        if(x<6)//右
            if(sameColor_renketsu(x, y, x + 1, y)) {
                method.debug_makelog("rightrenketsu x=" + x + " y=" + y);
                findConnection_renketsu(x + 1, y, false);
            }
        if(y<13)//下
            if(sameColor_renketsu(x, y, x, y + 1)) {
                method.debug_makelog("downrenketsu x=" + x + " y=" + y);
                findConnection_renketsu(x, y + 1, false);
            }
        if(x>1)//左
            if(sameColor_renketsu(x, y, x - 1, y)) {
                method.debug_makelog("leftrenketsu x=" + x + " y=" + y);
                findConnection_renketsu(x - 1, y, false);
            }
        return;
    }
    private boolean sameColor_renketsu(int ax, int ay, int bx, int by){
        int a=getNumber(ax,ay);
        int b=getNumber(bx,by);
        if(field_puyo[a].getExist()==false || field_puyo[b].getExist()==false)
            return false;
        if(field_puyo[a].getColor()<0 || field_puyo[b].getColor()<0)
            return false;
        if (field_puyo[a].getColor() != field_puyo[b].getColor())
            return false;
        return true;
    }
    private void searchOzyama(int number){
        int x,y;
        x=number%6+1;
        y=(number/6)+1;
        if(y>2) {
            if (field_puyo[getNumber(x,y-1)].getColor()==-2)
                field_puyo[getNumber(x,y-1)].setcolor(-1);
        }
        if(x<6) {
            if (field_puyo[getNumber(x+1,y)].getColor()==-2)
                field_puyo[getNumber(x+1,y)].setcolor(-1);
        }
        if(y<13) {
            if (field_puyo[getNumber(x,y+1)].getColor()==-2)
                field_puyo[getNumber(x,y+1)].setcolor(-1);
        }
        if(x>1) {
            if (field_puyo[getNumber(x-1,y)].getColor()==-2)
                field_puyo[getNumber(x-1,y)].setcolor(-1);
        }
    }
    public int deletepuyo(int rensa_count){
        int counter=0;
        int colcount=0;
        int renketsu_sum=0;
        boolean ColFlag[]=new boolean[toko_simu.MAX_COLOR];
        renketsu_sum=find_renketsu();
        for(int i=0;i<78;i++){
            if(connect[i]>=4) {
                counter++;
                ColFlag[field_puyo[i].getColor()]=true;
                field_puyo[i].delete();
                searchOzyama(i);
            }
        }
        for(int i=0;i< toko_simu.MAX_COLOR;i++){
            if(ColFlag[i]==true)colcount++;
        }
        Tokuten1=counter;
        Tokuten2=method.getPTS(Setting.score_version, counter,rensa_count,colcount,renketsu_sum)/Tokuten1;
        return Tokuten1*Tokuten2;
    }
    public void deletecomplete(){
        for(int i=0;i<78;i++){
            if(field_puyo[i].getColor()==-1){
                field_puyo[i].exist_set(false);
            }
        }
    }
    public void dogravity(){
        for(int i=0;i<15;i++){
            for(int iy=13;iy>1;iy--){
                for(int ix=1;ix<=6;ix++){
                    if (!field_puyo[getNumber(ix,iy)].getExist()
                            && field_puyo[getNumber(ix,iy-1)].getExist()){
                        field_puyo[getNumber(ix,iy)].setcolor(field_puyo[getNumber(ix,iy-1)].getColor());
                        field_puyo[getNumber(ix,iy)].exist_set(true);
                        field_puyo[getNumber(ix,iy-1)].exist_set(false);
                    }
                }
            }
        }
    }
    public void clearField(){
        for(int i=0;i<78;i++){
            if(default_field[i]==-1)
                field_puyo[i].exist_set(false);
            else
                field_puyo[i].exist_set(true);
                field_puyo[i].setcolor(default_field[i]);
        }
    }
    public boolean getExpose(int number){
        int x,y;
        x=number%6+1;
        y=(number/6)+1;
        if(y>1) {
            if (!field_puyo[getNumber(x,y-1)].exist_flag)
                return true;
        }
        if(x<6) {
            if (!field_puyo[getNumber(x+1,y)].exist_flag)
                return true;
        }
        if(y<13) {
            if (!field_puyo[getNumber(x,y+1)].exist_flag)
                return true;
        }
        if(x>1) {
            if (!field_puyo[getNumber(x-1,y)].exist_flag)
                return true;
        }
        return false;
    }
    public int[] rensa_skip(){
        int i=0;
        int sum=0;
        while(true) {
            if (!findConnection()) break;
            i++;
            sum+=deletepuyo(i);
            deletecomplete();
            dogravity();
        }
        int[] ret=new int[2];
        ret[0]=i;
        ret[1]=sum;
        return ret;
    }
    public void draw(int Tesuu, int rensa, int PTS, Canvas canvas, Paint paint){
        for(int i=0;i<78;i++)
            field_puyo[i].draw(canvas, paint);
        drawFound(canvas, paint);
        paint.setTextSize(Coordinates.puyo_size);
        if(rensa!=0)canvas.drawText(rensa + "連鎖",
                Coordinates.getpanelX(0), Coordinates.getpanelY(6.5), paint);
        canvas.drawText(Tesuu + "手目", Coordinates.getpanelX(0), Coordinates.getpanelY(2), paint);
    }
    private void drawFound(Canvas canvas, Paint paint){
        canvas.drawLine(Coordinates.getfieldX(1),Coordinates.getfieldY(1),
                Coordinates.getfieldX(7),Coordinates.getfieldY(1),paint);
        canvas.drawLine(Coordinates.getfieldX(1), Coordinates.getfieldY(1),
                Coordinates.getfieldX(1), Coordinates.getfieldY(14), paint);
        canvas.drawLine(Coordinates.getfieldX(7), Coordinates.getfieldY(1),
                Coordinates.getfieldX(7), Coordinates.getfieldY(14), paint);
        canvas.drawLine(Coordinates.getfieldX(1), Coordinates.getfieldY(14),
                Coordinates.getfieldX(7), Coordinates.getfieldY(14), paint);
    }
    public boolean getCanLand(int x, int dir){
        if(field_puyo[getNumber(x,1)].getExist())return false;
        if(field_puyo[getNumber(x+method.dirToX(dir),1)].getExist())return false;
        return true;
    }
    public String getURL(){
        return method.calculateIPSURL(field_puyo);
    }
    public int getNumber(int x, int y){
        return y*6+x-7;
    }
    public String getScore(){
        return Tokuten1+"x"+Tokuten2;
    }
    public field clone(){
        field temp=new field(res);
        for(int i=0;i<78;i++)temp.field_puyo[i]=this.field_puyo[i].clone();
        return temp;
    }
}
