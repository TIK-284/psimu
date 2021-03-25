package com.example.tik.puyo_simu.TOKO;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.tik.puyo_simu.SIMU.Coordinates;
import com.example.tik.puyo_simu.SIMU.field;
import com.example.tik.puyo_simu.SIMU.simu;
import com.example.tik.puyo_simu.Setting;
import com.example.tik.puyo_simu.general.method;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Takumi on 15/12/07.
 */
public class toko_simu extends simu
        implements GestureDetector.OnGestureListener
{
    //TODO:UI部分が拡大しすぎ、分離するべき？
    //TODO:コントローラ対応
    //TODO:キーコンフィグ

    private GestureDetector gesdet;
    private Paint paint=new Paint();
    private rakkapuyo rkpuyo;
    private int TumoArray[];
    private int Tesuu;
    private int puyofu_max;
    private ozyamafu[] ozyama_puyofu;
    private int ozyama_puyofu_pointer;
    //ぷよ消し作業中にどの状況に有るかのフラグ
    //0：デフォ 1：消した後 2：降らした後
    private int rensa_state;
    private int puyofu[];
    private int rensa_count=0;
    private int showRensa_count=0;
    private boolean showRensaFlag=false;
    private int PTS=0;
    private int SumRensaScore=0;
    private int showRensaScore=0;
    //TODO:連鎖表示保持の方法がスマートでない
    private int tigiri=0;

    public static final int MAX_COLOR=5;
    //最低反応のフリック速度
    final float fling_sensitivity=400;
    final float flingFreeze_dy=3;
    final float TapArea_ratioR=(float)0.5;
    final float TapArea_ratioL=(float)0.5;
    //横移動をロックする縦速度
    //final float ScrollFreeze_dy=(float)10000;
    final int max_tumo=2048;//256の倍数で
    //tumo_type　ツモ補正について
    //0：ツモ補正無し  1；AC通仕様　2：クラ仕様
    private int tumo_type=0;

    //押し始められたx座標
    float syoki_x;
    //タッチ開始から移動したx座標
    float sigma_x;

    public toko_simu(){
//        super(context);
//
//        gesdet=new GestureDetector(context,this);
//        setFocusable(true);
        super();

        TumoArray=new int[max_tumo*2+10];
        for(int i=max_tumo*2;i<max_tumo*2+10;i++)TumoArray[i]=1;
        puyofu=new int[max_tumo*2];
        ozyama_puyofu=new ozyamafu[max_tumo*2];
        for(int i=0;i<max_tumo*2;i++)ozyama_puyofu[i]=new ozyamafu();
        ozyama_puyofu_pointer=0;
        generateTumo();

        rkpuyo=new rakkapuyo(3,1,0,res);

        resetRensaState();
        nexttumo();
    }

    public void onDraw(){
        paint.setAntiAlias(true);
        pyfield.draw(Tesuu, showRensa_count, PTS, canvas, paint);
        //13段目部分を暗くする
        paint.setColor(Color.argb(90, 0, 0, 0));
        canvas.drawRect(Coordinates.getfieldX(1), Coordinates.getfieldY(1),
                Coordinates.getfieldX(7), Coordinates.getfieldY(2), paint);
        paint.setColor(Color.BLACK);
        rkpuyo.draw(canvas, paint);
        for (int i = 0; i < 5; i++) {
            if (i < Setting.next_num)
                rkpuyo.nextdraw(Setting.next_num,i, TumoArray[Tesuu*2+i*2], TumoArray[Tesuu*2+1+i*2], canvas, paint);
        }
        paint.setTextSize((float) (Coordinates.puyo_size * 0.6));
        if(showRensa_count!=0){
            if(rensa_state==1)canvas.drawText(pyfield.getScore(),
                    Coordinates.getpanelX(0.5), Coordinates.getpanelY(9.1), paint);
            else canvas.drawText(showRensaScore+"",
                    Coordinates.getpanelX(0.5), Coordinates.getpanelY(9.1), paint);
            drawyokoku(showRensaScore/70,canvas,paint);
        }
        drawExtra(Setting.extrashow_01, Coordinates.getpanelX(-1), Coordinates.getpanelY(10.5), canvas);
        drawExtra(Setting.extrashow_02, Coordinates.getpanelX(-1), Coordinates.getpanelY(11.5), canvas);
        drawExtra(Setting.extrashow_03, Coordinates.getpanelX(-1), Coordinates.getpanelY(12.5), canvas);
        if(method.debug_state) method.debug_output(canvas, paint);
        return;
    }
    private void drawExtra(int kind, int x, int y, Canvas canvas){
        paint.setTextSize((float) (Coordinates.puyo_size * 0.6));
        switch (kind) {
            case 1:
                canvas.drawText("ちぎり：" + tigiri,
                       x, y , paint);
                break;
            case 2:
                canvas.drawText("タイマー：" + ((double)TimeCounter/10),
                        x, y , paint);
                break;
        }
    }
    private void drawyokoku(int puyo,Canvas canvas,Paint paint){
        int yokoku[]=new int[6];
        for(int i=0;i<6;i++)yokoku[i]=0;
        switch (Setting.score_version) {
            case 0:
                yokoku[5] = puyo / 400;
                puyo -= yokoku[5] * 400;
                yokoku[4] = puyo / 300;
                puyo -= yokoku[4] * 300;
                yokoku[3] = puyo / 200;
                puyo -= yokoku[3] * 200;
                yokoku[2] = puyo / 30;
                puyo -= yokoku[2] * 30;
                yokoku[1] = puyo / 6;
                puyo -= yokoku[1] * 6;
                yokoku[0] = puyo / 1;
                break;
            case 1:
                yokoku[2] = puyo / 30;
                puyo -= yokoku[2] * 30;
                yokoku[1] = puyo / 6;
                puyo -= yokoku[1] * 6;
                yokoku[0] = puyo / 1;
        }
        for(int i=0;i<6;i++)drawyokoku_single(yokoku,i,canvas,paint);
    }
    private void drawyokoku_single(int[] yokoku, int i,Canvas canvas, Paint paint){
        for(int j=5;j>=0;j--){
            if(yokoku[j]>0) {
                rkpuyo.drawyokoku(1, j, (float)(i*0.8-1), (float)7.2, canvas, paint);
                yokoku[j]--;
                break;
            }
        }
    }

    //以下操作系
    //onShowPressでタップ検知、回転の操作を入れる
    //onScrollでどれだけスライドしてるか検知、これによって左右に移動の操作を入れる
    //onFlingで下にフリックで落下させる、上にフリックで前の手に戻す
    @Override
    public boolean onTouchEvent(MotionEvent event){
        gesdet.onTouchEvent(event);
        if(event.getAction()==event.ACTION_UP
                || event.getAction()==event.ACTION_CANCEL
                || event.getAction()==event.ACTION_OUTSIDE){
            if(!Setting.AutoRensa && CountPress !=null){
                CountPress.cancel();
                CountPress=null;
            }
        }
        if(event.getAction()==event.ACTION_MOVE) {
            sigma_x = syoki_x - event.getX();
            rkpuyo.setScroll_Sigma((int) (sigma_x / Setting.scroll_sensitivity));
        }
        invalidate();
        return true;
    }

    public void onLongPress(MotionEvent event){
        if(!Setting.AutoRensa) {
            if(CountPress !=null){
                CountPress.cancel();
                CountPress=null;
            }
            CountPress=new Timer(false);
            CountPress.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler2.post(new Runnable() {
                        @Override
                        public void run() {
                            updateField();
                            invalidate();
                        }
                    });
                }
            }, Setting.Rensa_Rapid, Setting.Rensa_Rapid);
        }
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy){
        if(method.debug_state) method.debug_makelog("Scroll");
        return false;
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float vx, float vy){
        if((vx==0 || Math.abs(vy/vx)>flingFreeze_dy)
                && vy<(-1*fling_sensitivity)){
            reverse();
        }
        if(rensa_state!=0)return false;
        if((vx==0 || Math.abs(vy/vx)>flingFreeze_dy)
                && vy>fling_sensitivity
                && pyfield.getCanLand(rkpuyo.getPuyo_x(), rkpuyo.getDirection()))
            landing();
        if(method.debug_state) method.debug_makelog("Fling");
        return false;
    }

    public void onShowPress(MotionEvent e){
    }
    public boolean onDown(MotionEvent e) {
        sigma_x=0;
        syoki_x=e.getX();
        rkpuyo.scroll_clear();
        if(!Setting.AutoRensa && CountPress !=null){
            CountPress.cancel();
            CountPress=null;
        }
        if(method.debug_state) method.debug_makelog("Down");
        return false;
    }

    public boolean onSingleTapUp(MotionEvent e){
        if(rensa_state!=0){
            if(!Setting.AutoRensa)
                updateField();
        } else {
            float touch_x;
            touch_x = e.getX();

            if (touch_x < (Coordinates.disp_x * TapArea_ratioL))
                rkpuyo.turnleft();
            if (touch_x > (Coordinates.disp_x *(1- TapArea_ratioR)))
                rkpuyo.turnright();
        }
        if (method.debug_state) method.debug_makelog("SinTapUp");
        return false;
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int x, y;
        x=getWidth();
        y=getHeight();
        Coordinates.setViewSize(res, x, y);
        invalidate();
    }
    public boolean updateField(){
        if(rensa_state==0 && !pyfield.findConnection())return true;
        if(rensa_state==0 || rensa_state==2){
            if ((PTS=pyfield.deletepuyo(++rensa_count))>0) {
                showRensa_count = rensa_count;
                SumRensaScore += PTS;
                showRensaScore = SumRensaScore;
            }
            rensa_state = 1;
        }else if(rensa_state==1) {
            pyfield.deletecomplete();
            pyfield.dogravity();
            if(pyfield.findConnection()){
                rensa_state=2;
            } else {
                rensa_state=0;
                nexttumo();
                return false;
            }
        }
        return true;
    }
    private void generateTumo() {
        switch (tumo_type){
            case 0://補正無し
                for (int i = 0; i < 2048; i++) {
                    TumoArray[i]=method.random_int(4);
                }
                break;
            case 1://AC通
                generateACTsuu();
                break;
            case 2://クラシック
                generateClassic();
                break;
            case 3://3色
                for (int i = 0; i < 2048; i++) {
                    TumoArray[i]=method.random_int(3);
                }
                break;
        }
        pyfield.setDefaultField_Clear();
    }
    private void generateACTsuu(){
        generateTumo(128);
        if(countcolor(3)==4) {
            int nouse;
            nouse = method.random_int(4);//使わない色
            for (int i = 0; i < 6; i++) {
                int temp=method.random_int(3);
                if(temp>=nouse)temp++;
                TumoArray[i]=temp;
            }
        }
    }
    private void generateClassic(){
        generateTumo(16);
        while(countcolor(2)==1 || countcolor(2)==4) {
            int nouse;
            nouse = method.random_int(4);//使わない色
            for (int i = 0; i < 6; i++) {
                int temp=method.random_int(3);
                if(temp>=nouse)temp++;
                TumoArray[i]=temp;
            }
        }
    }
    private void generateTumo(int loop){
        //TODO:ツモ生成部分のコード酷すぎ。書き直して。
        int remain0=loop/2, remain1=loop/2, remain2=loop/2, remain3=loop/2;
        int sum_remain=0;
        int temp=0;
        for(int i=0;i<max_tumo;i++){
            if(i%(loop*2)==0){
                remain0=loop/2;remain1=loop/2;remain2=loop/2;remain3=loop/2;
            }
            sum_remain=remain0+remain1+remain2+remain3;
            temp=method.random_int(sum_remain);
            if(temp<remain0){
                TumoArray[i]=0;
                remain0--;
            } else {
                temp-=remain0;
                if(temp<remain1){
                    TumoArray[i]=1;
                    remain1--;
                } else {
                    temp-=remain1;
                    if(temp<remain2){
                        TumoArray[i]=2;
                        remain2--;
                    } else {
                        temp-=remain2;
                        if(temp<remain3){
                            TumoArray[i]=3;
                            remain3--;
                        } else {
                            TumoArray[i]=0;
                        }
                    }
                }
            }
        }
    }
    private int countcolor(int tumo){
        int counter[]=new int[MAX_COLOR];
        for(int i=0;i<tumo*2;i++){
            counter[TumoArray[i]]++;
        }
        int color = 0;
        for (int i = 0; i < MAX_COLOR;i++)
            if(counter[i]!=0)color++;
        return color;
    }
    private void nexttumo(){
        if(Tesuu>Setting.ozyama_tesu&&rensa_count==0)OzyamaLand_setting();
        rkpuyo.newtumo(TumoArray[Tesuu * 2], TumoArray[Tesuu * 2 + 1]);
        if(Tesuu==max_tumo-1){
            Tesuu=0;
        } else {
            Tesuu+=1;
        }
        if(rensa_count!=0 && showRensaFlag==false) {
            showRensa_count = rensa_count;
            showRensaFlag = true;
        }else if(rensa_count==0 && showRensaFlag==true) {
            showRensaFlag=false;
            showRensa_count=0;
            showRensaScore=0;
        }
        if(CountPress !=null){
            CountPress.cancel();
            CountPress=null;
        }
        rensa_count=0;
        SumRensaScore=0;
    }
    private void OzyamaLand_setting(){
        if(!Setting.OzyamaLand)return;
        boolean[] t=new boolean[6];

        if(method.random_int(100)<Setting.ozyama_5d) {
            OzyamaStep(5,0,true);
        }
        if(method.random_int(100)<Setting.ozyama_1k) {
            for(int i=0;i<6;i++)t[i]=false;
            t[method.random_int(6)] = true;
            landOzyama(t, 0, true);
        }
        if(method.random_int(100)<Setting.ozyama_3k) {
            for(int i=0;i<6;i++)t[i]=false;
            t[method.random_int(6)] = true;
            while(true){
                int i=method.random_int(6);
                if(!t[i]){
                    t[i]=true;
                    break;
                }
            }
            while(true){
                int i=method.random_int(6);
                if(!t[i]){
                    t[i]=true;
                    break;
                }
            }
            landOzyama(t, 0, true);
        }
        if(method.random_int(100)<Setting.ozyama_1d) {
            OzyamaStep(1,0,true);
        }
        if(method.random_int(100)<Setting.ozyama_2d) {
            OzyamaStep(2, 0, true);
        }
        if(method.random_int(100)<Setting.ozyama_3d) {
            OzyamaStep(3,0,true);
        }

    }
    public void reverse(){
        if(Tesuu<1)return;
        if(Tesuu==1) {
            ozyama_puyofu_pointer=0;
            Tesuujump(Tesuu);
            return;
        }
        if(rensa_state!=0)Tesuu+=1;
        Tesuujump(Tesuu-1);
    }
    public void Tesuujump(int t){
        resetRensaState();
        pyfield.clearField();
        for(int j=0;j<ozyama_puyofu_pointer;j++){
            if(ozyama_puyofu[j].tesu==1){
                landOzyama(ozyama_puyofu[j].place,1,false);
            }
        }
        for(int i=0;i<(t-1)*2;i+=2){
            landpuyo(puyofu[i], puyofu[i+1],
                    TumoArray[i], TumoArray[i+1]);
            pyfield.rensa_skip();
            for(int j=0;j<ozyama_puyofu_pointer;j++){
                if(ozyama_puyofu[j].tesu==i/2+2){
                    landOzyama(ozyama_puyofu[j].place,1,false);
                }
            }
        }
        rkpuyo.newtumo(TumoArray[(t - 1) * 2], TumoArray[(t - 1) * 2 + 1]);
        Tesuu=t;
        for(int i=0;i<ozyama_puyofu_pointer;i++){
            for(int j=1;j<ozyama_puyofu_pointer;j++){
                if(ozyama_puyofu[j-1].tesu>ozyama_puyofu[j].tesu){
                    ozyamafu temp;
                    temp=ozyama_puyofu[j];
                    ozyama_puyofu[j]=ozyama_puyofu[j-1];
                    ozyama_puyofu[j-1]=temp;
                }
            }
        }
        for(int i=0;i<ozyama_puyofu_pointer;i++){
            if(ozyama_puyofu[i].tesu>Tesuu){
                ozyama_puyofu_pointer=i;
                break;
            }
        }
    }
    private void landing() {
        if(Tesuu==1&&TimeCounter==0){
            if(mTimer !=null){
                mTimer.cancel();
                mTimer=null;
            }
            mTimer=new Timer(false);
            mTimer.schedule(new TimerTask(){
                @Override
                public void run(){
                    handler.post(new Runnable(){
                        @Override
                        public void run(){
                            TimeCounter++;
                            if(TimeCounter==1000000)TimeCounter=0;
                            invalidate();
                        }
                    });
                }
            },100,100);
        }
        landpuyo(rkpuyo.getPuyo_x(), rkpuyo.getDirection(),
                rkpuyo.getColor(), rkpuyo.getSubColor());
        if(!(puyofu[Tesuu*2-2]==rkpuyo.getPuyo_x() &&
                puyofu[Tesuu*2-1]==rkpuyo.getDirection()))
            puyofu_max=Tesuu;
        puyofu[Tesuu*2-2]=rkpuyo.getPuyo_x();
        puyofu[Tesuu*2-1]=rkpuyo.getDirection();
        rkpuyo.exist_set(false);
        if(!pyfield.findConnection()){
            //4つ消えない場合
            rensa_state=0;
            nexttumo();
        } else {
            if(CalculateRensa(1)>4200 && mTimer!=null) {
                mTimer.cancel();
                mTimer=null;
            }
            if(Setting.AutoRensa) {
                updateField();
                invalidate();
                if(CountPress !=null){
                    CountPress.cancel();
                    CountPress=null;
                }
                CountPress=new Timer(false);
                CountPress.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler2.post(new Runnable() {
                            @Override
                            public void run() {
                                if(updateField()==false)cancel();
                                invalidate();
                            }
                        });
                    }
                }, Setting.Rensa_Rapid, Setting.Rensa_Rapid);
            } else {
                updateField();
            }
        }
    }
    private int CalculateRensa(int return_sel){
        //0:連鎖数　1:得点
        int temp[]=new int[2];
        try {
            field f1 = pyfield.clone();
            temp=f1.rensa_skip();
        } catch(Exception e){}
        switch (return_sel){
            case 0:
                return temp[0];
            case 1:
                return temp[1];
            default:
                return 0;
        }
    }
    private void landpuyo(int x, int dir, int Col, int subCol){
        int temp1=0, temp2=0;
        switch (dir){
            case 0:
                pyfield.addpuyo(x,Col);
                pyfield.addpuyo(x,subCol);
                break;
            case 1:
                temp1=pyfield.addpuyo(x,Col);
                temp2=pyfield.addpuyo(x+1,subCol);
                break;
            case 2:
                pyfield.addpuyo(x,subCol);
                pyfield.addpuyo(x,Col);
                break;
            case 3:
                temp1=pyfield.addpuyo(x,Col);
                temp2=pyfield.addpuyo(x-1,subCol);
                break;
        }
        if(temp1!=temp2)tigiri++;
    }
    private void landOzyama(boolean[] t, int offset, boolean record){
        method.debug_makelog("landOzyama"+method.BoolArrayToString(t)+record);
        if(method.allFalse(t)){
            for(int i=0;i<6;i++){
                pyfield.addpuyo(i+1,-2);
            }
        }
        for(int i=0;i<6;i++){
            if(t[i]==true)
                pyfield.addpuyo(i+1,-2);
        }
        if(record)recordOzyamahu(Tesuu+offset+1, t);
    }
    public void OzyamaStep(int t,int offset, boolean record){
        boolean[] temp=new boolean[6];
        for(int i=0;i<6;i++)temp[i]=false;
        for(int i=0;i<t;i++)landOzyama(temp,offset,record);
    }
    private void recordOzyamahu(int Tesuu, boolean[] x){
        ozyama_puyofu[ozyama_puyofu_pointer].tesu=Tesuu;
        ozyama_puyofu[ozyama_puyofu_pointer].place=x;
        ozyama_puyofu_pointer++;
        for(int i=0;i<ozyama_puyofu_pointer;i++){
            method.debug_makelog(""+i+ozyama_puyofu[i].tesu+","+method.BoolArrayToString(ozyama_puyofu[i].place));
        }
    }
    public void retry(boolean regenerate, int tumo){
        resetRensaState();
        Tesuu=0;
        pyfield.clearField();
        tumo_type=tumo;
        puyofu_max=0;
        if(mTimer!=null) {
            mTimer.cancel();
            mTimer=null;
        }
        if(CountPress !=null){
            CountPress.cancel();
            CountPress=null;
        }
        TimeCounter=0;
        ozyama_puyofu_pointer=0;
        if(regenerate)generateTumo();
        invalidate();
        method.debug_makelog("retry" + tumo_type);
        nexttumo();
    }
    public void setScroll_sensitivity(int scroll) {
        method.debug_makelog("" + scroll);
        float temp=(float)scroll/720*Coordinates.disp_x;
        if(temp==0)temp=(float)0.001;
        Setting.scroll_sensitivity=temp;
    }
    private void resetRensaState(){
        rensa_state=0;
        rensa_count=0;
        showRensa_count=0;
        SumRensaScore=0;
        tigiri=0;
    }
    public String getfieldURL(boolean direct){
        String temp=pyfield.getURL();
        temp=temp+"_";
        for(int i=-1;i<Setting.next_num;i++) {
            temp+=method.nextToIPS(TumoArray[Tesuu*2+i*2],TumoArray[Tesuu*2+i*2+1]);
            if(direct){

            } else {
                temp+="1";
            }
        }
        return temp;
    }
    public int getmaxpuyofu(){return puyofu_max>Tesuu?puyofu_max:Tesuu;}
    public int getShowRensa_count(){return showRensa_count;}
    public int getTesuu(){return Tesuu;}
}

class ozyamafu{
    public int tesu=0;
    public boolean[] place=new boolean[6];
}