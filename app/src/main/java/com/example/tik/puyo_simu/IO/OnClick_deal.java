package com.example.tik.puyo_simu.IO;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CheckBox;

import com.example.tik.puyo_simu.R;
import com.example.tik.puyo_simu.Setting;
import com.example.tik.puyo_simu.TOKO.toko_simu;
import com.example.tik.puyo_simu.Toko;


/**
 * Created by Takumi on 15/12/11.
 */
public class OnClick_deal
        implements View.OnClickListener {
    private Toko toko1;

    public OnClick_deal(Toko toko){
        toko1=toko;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.button:
                if(Setting.retry_dialog==false){
                    toko1.retry_toko();
                    break;
                }
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(toko1);
                alertDlg.setTitle("retryしますか？");
                alertDlg.setMessage("現在の状況は保存されません。");
                alertDlg.setPositiveButton(
                        "はい",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                toko1.retry_toko();
                            }
                        });
                alertDlg.setNeutralButton(
                        "今後聞かない",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                toko1.setRetryDialogFalse();
                                toko1.retry_toko();
                            }
                        });
                alertDlg.setNegativeButton(
                        "いいえ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                alertDlg.create().show();
                break;
            case R.id.checkbox:
                CheckBox checkbox = (CheckBox) v;
                Setting.changetumo = checkbox.isChecked();
                break;
        }
    }
}
