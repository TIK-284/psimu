package com.example.tik.puyo_simu3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tik.puyo_simu3.View.Field_View;
import com.example.tik.puyo_simu3.View.PuyoSelectListener;
import com.example.tik.puyo_simu3.View.UseBitmap;
import com.example.tik.puyo_simu3.field_data.FieldStatus;
import com.example.tik.puyo_simu3.simu.field;
import com.example.tik.puyo_simu3.simu.puyo_data.PuyoAdapter;
import com.example.tik.puyo_simu3.simu.puyo_data.PuyoKind;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class SimulatorActivity extends Simulator
    implements ListView.OnItemClickListener, Button.OnClickListener{
    ImageView PrevSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulator);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout ll=(LinearLayout)findViewById(R.id.content_puyofield);
        fv=new Field_View(this);
        ll.addView(fv);
        TransField(field_);

        PrevSelected=(ImageView)findViewById(R.id.preview_puyo);
        PrevSelected.setImageBitmap(UseBitmap.getIMG(0));

        ListView pks=(ListView)findViewById(R.id.puyo_list);
        PuyoAdapter pka =new PuyoAdapter(this);
        ArrayList<PuyoKind> temp=new ArrayList();
        for(int i=0;i<10;i++)temp.add(new PuyoKind(0));
        pka.setItems(temp);
        pks.setAdapter(pka);
        pks.setOnItemClickListener(this);

        Button btn=(Button)findViewById(R.id.play_button);
        btn.setOnClickListener(this);
        btn=(Button)findViewById(R.id.back_button);
        btn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.simulator, menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }else if (id == R.id.action_settings) {
            Intent intent=new Intent(this, GeneralSettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v){
        writeField(field_id);
        if(v.getId()==R.id.play_button) {
            rensaProgress();
            fv.invalidate();
        } else if(v.getId()==R.id.back_button){
            backRensa();
        }
        fv.invalidate();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK ) {
            backRensa();
            fv.invalidate();
        }
        return false;
    }
    private void rensaProgress(){
        //TODO:何とかする
        if (rensa_state == 0) {
            fv.OFF_ValidTouch();
            if (!field_.dogravity()) {
                if(deleteConnection())
                    rensa_state=2;
                else
                    rensa_state=4;
            } else {
                rensa_state = 1;
            }
        } else if(rensa_state==1){
            if(deleteConnection())
                rensa_state=2;
            else
                rensa_state=4;
        } else if(rensa_state==2){
            field_.deleteComplete();
            field_.dogravity();
            rensa_state=3;
        } else if(rensa_state==3){
            if(deleteConnection())
                rensa_state=2;
            else
                rensa_state=4;
        }
    }
    private void backRensa(){
        readField(field_id);
        field_=fieldStatus.getField();
        TransField(field_);
        fv.ON_ValidTouch();
        rensa_state=0;
        rensa_sum=0;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        fv.setSelected_color(UseBitmap.IntEToColor(position));
        PrevSelected.setImageBitmap(UseBitmap.getIMG4Edit(position));
    }

}
