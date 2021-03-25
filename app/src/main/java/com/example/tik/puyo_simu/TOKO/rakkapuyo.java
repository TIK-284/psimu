package com.example.tik.puyo_simu.TOKO;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.tik.puyo_simu.SIMU.puyo;
import com.example.tik.puyo_simu.Setting;

/**
 * Created by Takumi on 15/12/07.
 */
public class rakkapuyo extends puyo {
    //over_xは超過して移動した分
    //puyodirectionで0,1,2,3がそれぞれ上右下左(軸ぷよにどの方向に付いているか)
    //自由落下をonにしたならば、落下中で少し浮いている分をパラメータに
    private int default_x, default_y,default_direction;
    private int over_x;
    private int puyo_direction;
    private int second_puyo_color;

    //現スクロール中に左右にどれだけ移動したか、変わる時に操作を入れる
    private int prev_scroll_sigma=0;
    private int scroll_sigma=0;

    public rakkapuyo(int def_x,int def_y,int def_direction,Resources res){
        super(res);
        default_x=def_x;
        default_y=def_y;
        default_direction=def_direction;
    }
    public void run(){
        //自由落下をonにした時用
        //落下処理、接地判定について
    }
    public void setScroll_Sigma(int x) {
        prev_scroll_sigma=scroll_sigma;
        scroll_sigma=x;
        if(prev_scroll_sigma!=scroll_sigma){
            if(prev_scroll_sigma<scroll_sigma)
                moveright();
            else
                moveleft();
        }
    }
    public void scroll_clear(){
        prev_scroll_sigma=0;
        scroll_sigma=0;
    }
    @Override
    public void draw(Canvas canvas, Paint paint){
        int temp= Setting.expansion?0:2;
        if(exist_flag){
            drawpuyo(0,puyo_color, puyo_x, puyo_y-temp,canvas,paint);
            switch (puyo_direction) {
                case 0:
                    drawpuyo(0,second_puyo_color, puyo_x, puyo_y -1-temp, canvas, paint);
                    break;
                case 1:
                    drawpuyo(0,second_puyo_color, puyo_x +1, puyo_y-temp, canvas, paint);
                    break;
                case 2:
                    drawpuyo(0,second_puyo_color, puyo_x, puyo_y +1-temp, canvas, paint);
                    break;
                case 3:
                    drawpuyo(0,second_puyo_color, puyo_x -1, puyo_y-temp, canvas, paint);
                    break;
            }
        }
    }

    //具体的な操作系
    public void moveleft(){
        if(over_x<0)over_x+=1;
        else if(!invalid_place(puyo_x +1,puyo_direction)) puyo_x +=1;
        else over_x+=1;
    }
    public void moveright(){
        if(over_x>0)over_x-=1;
        else if(!invalid_place(puyo_x -1,puyo_direction)) puyo_x -=1;
        else over_x-=1;
    }
    public void turnleft(){
        //画面外にはみ出さない処理
        if(puyo_direction==0)puyo_direction=3;
        else puyo_direction--;
        if(invalid_place(puyo_x,puyo_direction)){
            if(puyo_x ==6) puyo_x--;else puyo_x++;
        }
    }
    public void turnright(){
        if(puyo_direction==3)puyo_direction=0;
        else puyo_direction++;
        if(invalid_place(puyo_x,puyo_direction)){
            if(puyo_x ==6) puyo_x--;else puyo_x++;
        }
    }
    public void newtumo(int col1, int col2){
        //ツモ出現
        puyo_x =default_x;
        puyo_y =default_y;
        puyo_direction=default_direction;
        exist_flag=true;
        puyo_x =3;
        puyo_y =1;puyo_direction=0;
        puyo_color=col1;
        second_puyo_color=col2;
    }
    public boolean invalid_place(int x, int dir) {
        //無効な配置だったらtrueを返す
        if(x<1 || x>6)return true;
        if(x==1 && dir==3)return true;
        if(x==6 && dir==1)return true;
        return false;
    }

    public int getDirection() {
        return puyo_direction;
    }
    public int getSubColor(){
        return second_puyo_color;
    }
}
