package com.locator.chield.secure.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ParentLogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_log);

        final Button button1 = (Button) findViewById(R.id.buttonLogin);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Manager m = LocalMemory.getInstance().getManager();
                EditText mail   = (EditText)findViewById(R.id.editTextEmailLog);
                EditText pass   = (EditText)findViewById(R.id.editTextPassLog);
                Result r = m.login(mail.getText().toString(),pass.getText().toString());

                if (r.getResult()){
                    Intent myIntent = new Intent(ParentLogActivity.this, MainParentActivity.class);
                    ParentLogActivity.this.startActivity(myIntent);
                    finish();
                }
                else{
                    Toast.makeText(ParentLogActivity.this,r.getMessage(),Toast.LENGTH_LONG).show();
                }
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
