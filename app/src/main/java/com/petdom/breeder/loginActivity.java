package com.petdom.breeder;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.petdom.breeder.ui.BreederListActivity;


public class loginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    public void giveAccess(View view) {
        Intent intent = new Intent(this, BreederListActivity.class);
        startActivity(intent);
    }
}