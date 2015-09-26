package com.petdom.breeder.ui;

import android.app.Activity;
import android.app.ProgressDialog;

import com.petdom.breeder.events.EventListener;

/**
 * Created by diwakar.mishra on 9/21/2015.
 */
public class BreederBaseActivity extends Activity implements EventListener {

    private ProgressDialog progressDialog;

    protected void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(this, null, msg);
        } else {
            progressDialog.setMessage(msg);
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }

    protected void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}