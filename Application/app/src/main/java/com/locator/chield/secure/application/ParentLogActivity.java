package com.locator.chield.secure.application;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ParentLogActivity extends AppCompatActivity {

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_log);
        context=this;

        final Button button1 = (Button) findViewById(R.id.buttonLogin);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Manager m = LocalMemory.getInstance().getManager();
                EditText mail   = (EditText)findViewById(R.id.editTextEmailLog);
                EditText pass   = (EditText)findViewById(R.id.editTextPassLog);
                m.login(context,mail.getText().toString(),pass.getText().toString());

            }
        });

        final Button button2 = (Button) findViewById(R.id.buttonparentRegister);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(ParentLogActivity.this, RegisterActivity.class);
                ParentLogActivity.this.startActivity(myIntent);
            }
        });
    }
}
