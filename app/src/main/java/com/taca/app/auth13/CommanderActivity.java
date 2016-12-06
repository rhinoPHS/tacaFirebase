package com.taca.app.auth13;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//방향성 액티비티 : 화면 전환
public class CommanderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this,MatchActivity.class);
        startActivity(intent);
        finish();
    }
}
