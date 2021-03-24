package com.example.tik.puyo_simu3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tik.puyo_simu3.field_data.FieldAdapter;
import com.example.tik.puyo_simu3.field_data.FieldStatus;
import com.example.tik.puyo_simu3.field_data.FieldStatus_brief;
import com.example.tik.puyo_simu3.simu.field;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SimuSelectActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ListView.OnItemClickListener , ListView.OnItemLongClickListener, Button.OnClickListener{

    private FieldAdapter fieldList_adapter =null;
    private ArrayList<FieldStatus_brief> fieldList =null;
    ListView Field_ListView;

    private CountDownTimer keyEventTimer;
    private boolean pressed = false;

    private int selectedMode=0;
    private int selectedIndex;

    private int mode=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simu_select);
        setTitle("シミュレーター");

        //ツールバーやら何やらの設定
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        deleteFile("puyosave.dat");
        loadFieldList();

        //フィールドのリストについて
        Field_ListView=(ListView)findViewById(R.id.simu_list);
        fieldList_adapter =new FieldAdapter(this);
        fieldList_adapter.setFieldList(fieldList);
        Field_ListView.setAdapter(fieldList_adapter);
        Field_ListView.setOnItemClickListener(this);
        Field_ListView.setOnItemLongClickListener(this);


        keyEventTimer = new CountDownTimer(1000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                pressed = false;
            }
        };

        Button btn=(Button)findViewById(R.id.make_button);
        btn.setOnClickListener(this);
        return;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (id == -99) {
//            addField();
//            return;
//        }
        int index = -1;
        for (int i = 0; i < fieldList.size(); i++) {
            if (fieldList.get(i).getId() == id)
                index = i;
        }
        if (index == -1) return;
        Intent intent;
        //TODO:ここ修正
        switch (fieldList.get(index).getMode()) {
            case SIMU:
                intent = new Intent(this, SimulatorActivity.class);
                break;
            case TOKO:
                intent = new Intent(this, SimulatorActivity.class);
                break;
            case NAZO:
                intent = new Intent(this, SimulatorActivity.class);
                break;
            default:
                return;
        }
        intent.putExtra("ID", id);
        startActivity(intent);
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        int index = -1;
        for (int i = 0; i < fieldList.size(); i++) {
            if (fieldList.get(i).getId() == id)
                index = i;
        }
        if (index == -1) return false;
        selectedIndex=index;
        final CharSequence[] items={"名前を変更", "削除する"};
        AlertDialog.Builder alertDlg=new AlertDialog.Builder(this);
        alertDlg.setItems(
                items,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        field_Action(which);
                    }
                }
        );
        alertDlg.create().show();
        return true;
    }
    private void field_Action(int which){
        switch(which){
            case 0:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("フィールド名を入力して下さい");
                LayoutInflater inflater = LayoutInflater.from(this);
                final View view = inflater.inflate(R.layout.edit_text_dialog, null);
                builder.setView(view);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int idx) {
                        EditText et = (EditText) view.findViewById(R.id.editText);
                        setFieldName(selectedIndex, et.getText().toString());
                    }
                });
                builder.show();
                break;
            case 1:
                long id=fieldList.get(selectedIndex).getId();
                deleteFile(id+".Apyf");
                fieldList.remove(selectedIndex);
                fieldList_adapter.notifyDataSetChanged();
                saveFieldList();
                break;
        }
    }
    private void setFieldName(int index, String name){
        fieldList_adapter.rename(selectedIndex, name);
        saveFieldList();
        long id=fieldList.get(index).getId();
        try {
            FileInputStream fis = openFileInput(id + ".Apyf");
            ObjectInputStream ois=new ObjectInputStream(fis);
            FieldStatus fieldStatus=(FieldStatus)ois.readObject();
            ois.close();

            fieldStatus.setFieldName(name);

            FileOutputStream fos=openFileOutput(id + ".Apyf", MODE_PRIVATE);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(fieldStatus);
            oos.close();
        } catch(Exception e){}
    }
    @Override
    public void onClick(View v){
        if(v.getId()==R.id.make_button) {
            addField();
        }
    }
    private void createDummyData(){
        this.fieldList = new ArrayList();
        FieldStatus_brief item1 =new FieldStatus_brief();
        item1.setFieldName("GTR基本形");
        item1.setText("S級などでもよく見る形");
        item1.setId(400);
        item1.setMode(FieldStatus.Mode.SIMU);
        fieldList.add(item1);

        FieldStatus item1_=new FieldStatus();
        item1_.setFieldName("GTR基本形");
        item1_.setText("S級などでもよく見る形");
        item1_.setId(400);
        try {
            FileOutputStream fos=openFileOutput("400.Apyf",MODE_PRIVATE);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(item1_);
            oos.close();
        } catch(Exception e){
        }
        fieldList_adapter.setFieldList(fieldList);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.simu_select, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK ) {
            if(!pressed) {
                keyEventTimer.cancel();
                keyEventTimer.start();

                Toast.makeText(this, "終了する場合は、もう一度バックボタンを押してください", Toast.LENGTH_SHORT).show();
                pressed = true;
                return false;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent=new Intent(this, GeneralSettingsActivity.class);
            startActivity(intent);
            return true;
        } else if(id==R.id.about_thisapp){
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
            alertDlg.setTitle("このアプリについて");
            alertDlg.setMessage("ぷよぷよシミュレーター(β)ver0.4\nプログラム等：TIK\n画像：「白魔空間」様より");
            alertDlg.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            alertDlg.create().show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_puyoall) {
            mode=0;
            setTitle("フィールド一覧");
        }
        //TODO:リストの更新
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addField(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("フィールド名を入力して下さい");
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.edit_text_dialog, null);
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int idx) {
                EditText et = (EditText) view.findViewById(R.id.editText);
                createNewField(et.getText().toString());
            }
        });
        builder.show();
    }
    private void createNewField(String name){
//        if ( name == null || name.length() == 0 ){
//            name="名称未設定";
//        }

        long id=generateID();
        if(id==-1){
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
            alertDlg.setMessage("フィールドをこれ以上作れません。");
            alertDlg.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            alertDlg.create().show();
        }

        Date date = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("作成日時 yyyy/MM/dd HH:mm:ss");

        FieldStatus_brief item1 = new FieldStatus_brief();
        item1.setFieldName(name);
        item1.setText(sdf1.format(date));
        item1.setId(id);
        item1.setMode(mode);
        fieldList.add(item1);

        fieldList_adapter.setFieldList(fieldList);

        FieldStatus item1_ =new FieldStatus();
        item1_.setFieldName(name);
        item1_.setText(sdf1.format(date));
        item1_.setId(id);
        try {
            FileOutputStream fos=openFileOutput(id+".Apyf",MODE_PRIVATE);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(item1_);
            oos.close();
        } catch(Exception e){
        }
        saveFieldList();
    }
    private long generateID(){
        for(long i=0;i<100000;i++){
            boolean flag=false;
            for(int j=0;j<fieldList.size();j++){
                if(fieldList.get(j).getId()==i)
                    flag=true;
            }
            if(!flag){
                return i;
            }
        }return -1;
    }
    private void saveFieldList(){
        try {
            FileOutputStream fos=openFileOutput("puyosave.dat", MODE_PRIVATE);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(fieldList);
            oos.close();
        } catch(Exception e){
        }
    }
    private void loadFieldList(){
        try {
            FileInputStream fis = openFileInput("puyosave.dat");
            ObjectInputStream ois=new ObjectInputStream(fis);
            fieldList =(ArrayList<FieldStatus_brief>)ois.readObject();
            ois.close();
        } catch(Exception e){
            this.fieldList = new ArrayList();
//            makeNewData();
        }
    }
    private void makeNewData(){
        FieldStatus_brief item1 =new FieldStatus_brief();
        item1.setFieldName("新しく作成する");
        item1.setText("");
        item1.setId(-99);
        item1.setStateOther();
        item1.setMode(FieldStatus.Mode.SIMU);
        fieldList.add(item1);
    }
}
