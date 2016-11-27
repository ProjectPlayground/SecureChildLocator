package com.locator.child.secure.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ViewParentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_parent);

        Intent intent = getIntent();
        String mail = intent.getStringExtra("MAIL");
        String name = intent.getStringExtra("NAME");
        String code = intent.getStringExtra("CODE");

        TextView mailV = (TextView) findViewById(R.id.textViewParentMailView);
        TextView nameV = (TextView) findViewById(R.id.textViewParentNameView);
        TextView codeV = (TextView) findViewById(R.id.textViewParentCodeView);


        mailV.setText(mail);
        nameV.setText(name);
        codeV.setText(code);

    }
}
