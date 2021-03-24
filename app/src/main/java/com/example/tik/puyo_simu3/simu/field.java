package com.example.tik.puyo_simu3.simu;

import com.example.tik.puyo_simu3.general.method;
import com.example.tik.puyo_simu3.simu.puyofu.ozyamafu;
import com.example.tik.puyo_simu3.simu.puyofu.puyofu;
import com.example.tik.puyo_simu3.simu.puyofu.tsumo;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by TIK on 2016/02/10.
 */
public class field implements Serializable, Cloneable{
    private puyo default_field[][]=new puyo[6][13];
    private puyo field_puyo[][]=new puyo[6][13];

    private tsumo tsumoList[]=new tsumo[2048];
    private puyofu puyofuList[]=new puyofu[2048];

    private ArrayList<ozyamafu> ozyamaList;
    private int tesuu=0;
    private int nazo_condition[]=new int[4];//今は保留


    public field() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 13; j++) {
                default_field[i][j] = new puyo();
            }
        }
        for (int i = 0; i < 6; i++){
            for (int j = 0; j < 13; j++) {
                field_puyo[i][j] = new puyo();
            }
        }
        for(int i=0;i<2048;i++)
            tsumoList[i]=new tsumo();
        for(int i=0;i<2048;i++)
            puyofuList[i]=new puyofu();
    }
    public puyo[][] getField_puyo(){
        return field_puyo;
    }
    public boolean setColor(int x,int y,puyo.Color color){
        //存在しないをセットはUNDEFINED 変化したらtrue
        if(color==puyo.Color.UNDEFINED){
            if(field_puyo[x][y].getExist_flag()==false)
                return false;
            field_puyo[x][y].setExist_flag(false);
        } else {
            if(field_puyo[x][y].getExist_flag()==true && field_puyo[x][y].getColor()==color)
                return false;
            field_puyo[x][y].setExist_flag(true);
            field_puyo[x][y].setColor(color);
        }
        return true;
    }
    public puyo.Color getPuyoColor(int x, int y){
        //UNDEFINEDは存在しない時
        if(field_puyo[x][y].getExist_flag()==false){
            return puyo.Color.UNDEFINED;
        } else {
            return field_puyo[x][y].getColor();
        }
    }
    public int[] deleteConnection(){
        //消した個数・連結ボーナス・色数を配列にして返す
        int[] result=new int[3];
        int[][][] connection=new int[2][6][13];

        for(int i=0;i<2;i++){
            for(int j=0;j<6;j++){
                for(int k=0;k<13;k++){
                    connection[i][j][k]=0;
                }
            }
        }

        connection=findconnection();

        //繋がっている個数を測定
        int[] connect_sum=new int[80];
        for(int i=0;i<80;i++)
            connect_sum[i]=0;
        for(int i=1;i<80;i++){
            int counter=0;
            for(int j=0;j<6;j++){
                for(int k=0;k<13;k++){
                    if(connection[1][j][k]==i)counter++;
                }
            }
            for(int j=0;j<6;j++){
                for(int k=0;k<13;k++){
                    if(connection[1][j][k]==i)connection[0][j][k]=counter;
                }
            }
            connect_sum[i]++;
        }
        result[1]=0;
        for(int i=1;i<80;i++){
            if(connect_sum[i]>=4)
                result[1]+= method.getRenketsuBonus(connect_sum[i]);
        }

        result[2]=calcConnctColor(connection);

        //消滅エフェクトに置き換え
        int counter=0;
        for(int i=0;i<6;i++){
            for(int j=0;j<13;j++){
                if(connection[0][i][j]>=4){
                    field_puyo[i][j].setColor(puyo.Color.DELETED);
                    indirectDelete(i,j);
                    counter++;
                }
            }
        }
        result[0]=counter;
        return result;
    }
    public boolean dogravity(){
        //落下するぷよが有ったらtrue
        boolean flag=false;
        for(int i=0;i<15;i++){
            for(int ix=0;ix<6;ix++){
                for(int iy=1;iy<13;iy++){
                    if(!field_puyo[ix][iy].getExist_flag() && field_puyo[ix][iy-1].getExist_flag()
                            && field_puyo[ix][iy-1].getColor()!=puyo.Color.KABE){
                        field_puyo[ix][iy].setColor(field_puyo[ix][iy-1].getColor());
                        field_puyo[ix][iy].setExist_flag(true);
                        field_puyo[ix][iy-1].setExist_flag(false);
                        flag=true;
                    }
                }
            }
        }
        return flag;
    }
    public void deleteComplete(){
        for(int i=0;i<6;i++){
            for(int j=0;j<13;j++){
                if(field_puyo[i][j].getColor()== puyo.Color.DELETED){
                    field_puyo[i][j].setExist_flag(false);
                }
            }
        }
        dogravity();
    }


    private int[][][] findconnection(){
        //どのように繋がっているかを検出
        int[][][] connection=new int[2][6][13];
        for(int i=0;i<2;i++)
            for(int j=0;j<6;j++)
                for(int k=0;k<13;k++)
                    connection[i][j][k]=0;
        int temp=1;//連結の数（ID）的な
        for(int i=0;i<6;i++) {
            for (int j = 1; j < 13; j++) {
                if (field_puyo[i][j].getExist_flag() && connection[1][i][j] == 0) {
                    connection = findConnection_loop(i, j, connection, temp);
                    temp++;
                }
            }
        }
        return connection;
    }
    private int[][][] findConnection_loop(int x, int y, int[][][] connection, int id){
        if(connection[1][x][y]!=0)return connection;
        connection[1][x][y]=id;

        if(y>1) {
            if (sameColor(x,y,x,y-1))
                connection= findConnection_loop(x, y - 1,connection,id);
        }
        if(x<5) {
            if (sameColor(x,y,x+1,y))
                connection= findConnection_loop(x + 1, y,connection,id);
        }
        if(y<12) {
            if (sameColor(x,y,x,y+1))
                connection= findConnection_loop(x, y + 1,connection,id);
        }
        if(x>0) {
            if (sameColor(x,y,x-1,y))
                connection= findConnection_loop(x - 1, y,connection,id);
        }
        return connection;
    }
    private boolean sameColor(int x1,int y1, int x2, int y2){
        if(!field_puyo[x1][y1].getExist_flag() || !field_puyo[x2][y2].getExist_flag()){
            return false;
        }
        if(!field_puyo[x1][y1].getColor().Normal() || !field_puyo[x2][y2].getColor().Normal()){
            return false;
        }
        if(field_puyo[x1][y1].getColor()==field_puyo[x2][y2].getColor()){
            return true;
        }
        return false;
    }
    private int calcConnctColor(int[][][] connection){
        puyo.Color[] usedColor=new puyo.Color[80];
        int pointer=0;
        for(int i=0;i<6;i++){
            for(int j=0;j<13;j++){
                if(connection[0][i][j]>=4){
                    usedColor[pointer++]=field_puyo[i][j].getColor();
                }
            }
        }

        int counter=0;
        for(int i=0;i<pointer;i++){
            if(usedColor[i]== puyo.Color.RED){
                counter++;
                break;
            }
        }
        for(int i=0;i<pointer;i++){
            if(usedColor[i]== puyo.Color.BLUE){
                counter++;
                break;
            }
        }
        for(int i=0;i<pointer;i++){
            if(usedColor[i]== puyo.Color.GREEN){
                counter++;
                break;
            }
        }
        for(int i=0;i<pointer;i++){
            if(usedColor[i]== puyo.Color.YELLOW){
                counter++;
                break;
            }
        }
        for(int i=0;i<pointer;i++){
            if(usedColor[i]== puyo.Color.PURPLE){
                counter++;
                break;
            }
        }
        return counter;
    }
    private void indirectDelete(int x, int y){
        //x,yが消えた時の周辺処理（主にお邪魔）
        if(y>1) {
            if(field_puyo[x][y-1].getExist_flag()){
                field_puyo[x][y-1].setColor(field_puyo[x][y-1].getColor().indirectDelete());
            }
        }
        if(x<5) {
            if(field_puyo[x+1][y].getExist_flag()){
                field_puyo[x+1][y].setColor(field_puyo[x+1][y].getColor().indirectDelete());
            }
        }
        if(y<12) {
            if(field_puyo[x][y+1].getExist_flag()){
                field_puyo[x][y+1].setColor(field_puyo[x][y + 1].getColor().indirectDelete());
            }
        }
        if(x>0) {
            if(field_puyo[x-1][y].getExist_flag()){
                field_puyo[x-1][y].setColor(field_puyo[x-1][y].getColor().indirectDelete());
            }
        }
    }
    @Override
    public field clone(){
        field b=null;
        try{
            b=(field)super.clone();
            for(int i=0;i<6;i++){
                for(int j=0;j<13;j++){
                    b.field_puyo[i][j]=this.field_puyo[i][j].clone();
                }
            }
        } catch(Exception e){

        }
        return b;
    }
}