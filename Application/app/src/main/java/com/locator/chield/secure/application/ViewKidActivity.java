package com.locator.chield.secure.application;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewKidActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_kid);

        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        String pass = intent.getStringExtra("PASS");
        String code = intent.getStringExtra("CODE");


        TextView nameV = (TextView) findViewById(R.id.textViewKidNameView);
        TextView passV = (TextView) findViewById(R.id.textViewKidPassView);
        TextView codeV = (TextView) findViewById(R.id.textViewKidCodeView);


        nameV.setText(name);
        passV.setText(pass);
        codeV.setText(code);

    }
}
