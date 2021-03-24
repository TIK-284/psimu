package com.example.tik.puyo_simu3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.tik.puyo_simu3.View.Field_View;
import com.example.tik.puyo_simu3.field_data.FieldStatus;
import com.example.tik.puyo_simu3.simu.field;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by TIK on 2016/02/20.
 */
public class Simulator extends AppCompatActivity {
    protected FieldStatus fieldStatus =null;
    protected field field_;
    protected int rensa_state=0;
    //0:ノーマル　1:落下後・連鎖前　2:連鎖中消去後　3:連鎖中落下後　4:連鎖後
    protected int rensa_sum=0;
    //連鎖数

    protected long field_id;
    protected Field_View fv;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        field_id=getIntent().getLongExtra("ID",-1);
        if(readField(field_id)==false){
            return;
        }
        setTitle(fieldStatus.getFieldName());
        field_=fieldStatus.getField();
    }

    protected boolean deleteConnection(){
        int temp[];
        temp = field_.deleteConnection();
        rensa_sum++;
        if(temp[0]==0)return false;
        else return true;
    }


    protected boolean readField(long id){
        try {
            FileInputStream fis = openFileInput(id + ".Apyf");
            ObjectInputStream ois=new ObjectInputStream(fis);
            fieldStatus =(FieldStatus)ois.readObject();
            ois.close();
        } catch(Exception e){
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
            alertDlg.setMessage("ファイルの読み込みに失敗しました。\nid="+id+"\n"+e.getLocalizedMessage());
            alertDlg.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            alertDlg.create().show();
            return false;
        }
        return true;
    }
    protected void writeField(long id){
        if(rensa_state!=0)return;
        try {
            FileOutputStream fos=openFileOutput(field_id + ".Apyf", MODE_PRIVATE);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(fieldStatus);
            oos.close();
        } catch(Exception e){
        }
    }
    protected void TransField(field field_){
        fv.setField(field_);
    }
    protected void SaveFieldState(){
        fieldStatus.setField(field_);
        writeField(field_id);
    }
    @Override
    public void onPause(){
        writeField(field_id);
        super.onPause();
    }
    @Override
    public void onDestroy(){
        writeField(field_id);
        super.onDestroy();
    }
}
