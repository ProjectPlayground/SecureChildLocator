package com.locator.chield.secure.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ParentLogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_log);

        final Button button1 = (Button) findViewById(R.id.buttonLogin);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(ParentLogActivity.this, MainParentActivity.class);
                ParentLogActivity.this.startActivity(myIntent);            }
        });

        final Button button2 = (Button) findViewById(R.id.buttonparentRegister);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(ParentLogActivity.this, RegisterActivity.class);
                ParentLogActivity.this.startActivity(myIntent);            }
        });
    }
}
