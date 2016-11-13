package com.locator.chield.secure.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        final Button button1 = (Button) findViewById(R.id.buttonParentSection);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(StartActivity.this, ParentLogActivity.class);
                StartActivity.this.startActivity(myIntent);            }
        });

        final Button button2 = (Button) findViewById(R.id.buttonKidsSection);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(StartActivity.this, MainKidsActivity.class);
                StartActivity.this.startActivity(myIntent);            }
        });
    }

}
