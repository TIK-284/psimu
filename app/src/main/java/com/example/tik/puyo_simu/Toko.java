package com.example.tik.puyo_simu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tik.puyo_simu.IO.NumberPicker_deal;
import com.example.tik.puyo_simu.IO.OnClick_deal;
import com.example.tik.puyo_simu.IO.Spinner_deal;
import com.example.tik.puyo_simu.TOKO.toko_simu;
import com.example.tik.puyo_simu.general.method;


public class Toko extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

    static private toko_field tokofield1=null;
    static private toko_panel tokopanel1=null;
    static private touch_analyser touch_analyser1=null;
    static private toko_simu tokosimu1=null;

    static Spinner_deal spi1;
    static OnClick_deal clk1;
    static NumberPicker_deal np1;

    private CountDownTimer keyEventTimer;
    private boolean pressed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //removeSetting();

        super.onCreate(savedInstanceState);

        setSetting(false);
        setContentView(R.layout.activity_toko);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        np1=new NumberPicker_deal();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.content_top);
        analyse_touch = new toko_simu(this);
        linearLayout.addView(tokoSimu1);

        clk1 = new OnClick_deal(this);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(clk1);
        CheckBox checkbox = (CheckBox) findViewById(R.id.checkbox);
        checkbox.setChecked(true);
        checkbox.setOnClickListener(clk1);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spi1 = new Spinner_deal();
        spinner.setOnItemSelectedListener(spi1);

        keyEventTimer = new CountDownTimer(1000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                pressed = false;
            }
        };

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toko, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(Toko.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if(id==R.id.action_ozyamaland){
            final CharSequence[] items={"1段","2段","3段","4段","5段"};
            AlertDialog.Builder alertDlg=new AlertDialog.Builder(this);
            alertDlg.setTitle("お邪魔量選択");
            alertDlg.setItems(
                    items,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            tokoSimu1.OzyamaStep(which+1,-1,true);
                        }
                    }
            );
            alertDlg.create().show();
        }
        if (id == R.id.action_IPSTweet) {
            if(tokoSimu1.getShowRensa_count()>0){
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
                alertDlg.setTitle("連鎖開始前に戻してからツイートしますか？");
                alertDlg.setPositiveButton(
                        "はい",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                tokoSimu1.reverse();
                                startActivity(method.tweet(tokoSimu1.getfieldURL(false)));
                            }
                        });
                alertDlg.setNegativeButton(
                        "いいえ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(method.tweet(tokoSimu1.getfieldURL(false)));
                            }
                        });

                alertDlg.create().show();
            } else {
                startActivity(method.tweet(tokoSimu1.getfieldURL(false)));
            }
        }
        if (id == R.id.action_About) {
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
            alertDlg.setTitle("このアプリについて");
            alertDlg.setMessage("多機能ぷよシミュ(仮)ver0.25\nプログラム等：TIK\n画像：「白魔空間」様より");
            alertDlg.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // OK ボタンクリック処理
                        }
                    });
            alertDlg.create().show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_toko) {

        } else if (id == R.id.nav_editer) {

        } else if (id == R.id.nav_nazoedit) {

        } else if (id == R.id.nav_nazotoku) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        setSetting(tokoSimu1 != null);
    }

    private void setSetting(boolean invalid) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String temp = sharedPreferences.getString("next_num", "2");
        int t=Integer.parseInt(temp);
        Setting.next_num=t;
        temp = sharedPreferences.getString("scroll_sen", "80");
        t=Integer.parseInt(temp);
        Setting.scroll_sensitivity=t;
        temp = sharedPreferences.getString("score_version", "0");
        t=Integer.parseInt(temp);
        Setting.score_version =t;
        Setting.expansion=sharedPreferences.getBoolean("expansion_field", true);
        Setting.BACK_reverse=sharedPreferences.getBoolean("BACK_reverse", false);
        temp = sharedPreferences.getString("extrashow_01", "0");
        t=Integer.parseInt(temp);
        Setting.extrashow_01=t;
        temp = sharedPreferences.getString("extrashow_02", "0");
        t=Integer.parseInt(temp);
        Setting.extrashow_02=t;
        temp = sharedPreferences.getString("extrashow_03", "0");
        t=Integer.parseInt(temp);
        Setting.extrashow_03=t;
        Setting.retry_dialog=sharedPreferences.getBoolean("retry_dialog", true);
        Setting.AutoRensa=sharedPreferences.getBoolean("AutoRensa", true);
        Setting.twoP_place=sharedPreferences.getBoolean("2p_place", false);
        Setting.OzyamaLand=sharedPreferences.getBoolean("OzyamaLand", false);
        temp = sharedPreferences.getString("ozyama_1k", "0");
        t=Integer.parseInt(temp);
        Setting.ozyama_1k=t;
        temp = sharedPreferences.getString("ozyama_3k", "0");
        t=Integer.parseInt(temp);
        Setting.ozyama_3k=t;
        temp = sharedPreferences.getString("ozyama_1d", "0");
        t=Integer.parseInt(temp);
        Setting.ozyama_1d=t;
        temp = sharedPreferences.getString("ozyama_2d", "0");
        t=Integer.parseInt(temp);
        Setting.ozyama_2d=t;
        temp = sharedPreferences.getString("ozyama_3d", "0");
        t=Integer.parseInt(temp);
        Setting.ozyama_3d=t;
        temp = sharedPreferences.getString("ozyama_5d", "0");
        t=Integer.parseInt(temp);
        Setting.ozyama_5d=t;
        temp = sharedPreferences.getString("ozyama_Tesu", "0");
        t=Integer.parseInt(temp);
        Setting.ozyama_tesu=t;
        temp = sharedPreferences.getString("Rensa_Rapid", "400");
        t=Integer.parseInt(temp);
        Setting.Rensa_Rapid=t;
        Setting.SecondMode=sharedPreferences.getBoolean("SecondMode", false);
        temp = sharedPreferences.getString("SecondKosu", "9");
        t=Integer.parseInt(temp);
        Setting.SecondKosu=t;
        temp = sharedPreferences.getString("SecondFlat", "40");
        t=Integer.parseInt(temp);
        Setting.SecondFlat=t;
        temp = sharedPreferences.getString("SecondEdge", "1");
        t=Integer.parseInt(temp);
        Setting.SecondEdge=t;
        temp = sharedPreferences.getString("SecondConnect", "60");
        t=Integer.parseInt(temp);
        Setting.SecondConnect=t;

        if(invalid && tokoSimu1!=null)tokoSimu1.invalidate();
    }
    private void removeSetting(){
        //SharedPreferences削除用
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("next_num");
        editor.remove("scroll_sen");
        editor.remove("score_version");
        editor.remove("expansion_field");
        editor.remove("BACK_reverse");
        editor.remove("extrashow_01");
        editor.remove("extrashow_02");
        editor.remove("extrashow_03");
        editor.remove("retry_dialog");
        editor.remove("AutoRensa");
        editor.remove("2p_place");
        editor.remove("OzyamaLand");
        editor.remove("ozyama_1k");
        editor.remove("ozyama_3k");
        editor.remove("ozyama_1d");
        editor.remove("ozyama_2d");
        editor.remove("ozyama_3d");
        editor.remove("ozyama_5d");
        editor.remove("ozyama_Tesu");
        editor.remove("Rensa_Rapid");
        editor.remove("SecondMode");
        editor.remove("SecondKosu");
        editor.remove("SecondFlat");
        editor.remove("SecondEdge");
        editor.remove("SecondConnect");


        editor.commit();
    }
    public void retry_toko(){
        tokoSimu1.retry(Setting.changetumo, spi1.getTumo_type());
    }
    public void setRetryDialogFalse(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("retry_dialog", false);
        editor.apply();
        setSetting(true);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // Backボタン検知
        if(keyCode == KeyEvent.KEYCODE_BACK ) {
            if (Setting.BACK_reverse == true) {
                tokoSimu1.reverse();
                tokoSimu1.invalidate();
                return false;
            } else {
                if(!pressed) {
                    // Timerを開始
                    keyEventTimer.cancel();
                    keyEventTimer.start();

                    Toast.makeText(this, "終了する場合は、もう一度バックボタンを押してください", Toast.LENGTH_SHORT).show();
                    pressed = true;
                    return false;
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }
    private void saveIntArray(int[] array,String PrefKey){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        for(int i=0;i<array.length;i++){
            editor.putInt(PrefKey+i,array[i]).commit();
        }
    }
    private int[] getIntArray(int max, String PrefKey){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int array[]=new int[max];
        for(int i=0;i<max;i++)array[i]=0;
        for(int i=0;i<max;i++){
            array[i]=sharedPreferences.getInt(PrefKey+i,0);
        }
        return array;
    }
}
