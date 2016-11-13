package com.locator.chield.secure.application;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final Button button = (Button) findViewById(R.id.buttonRegister);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Manager m = LocalMemory.getInstance().getManager();
                EditText mail   = (EditText)findViewById(R.id.editTextEmailRegister);
                EditText pass   = (EditText)findViewById(R.id.editTextPassRegister);
                Result r = m.register(mail.getText().toString(),pass.getText().toString());

                if (r.getResult()){
                    Intent myIntent = new Intent(RegisterActivity.this, ConfirmRegisterActivity.class);
                    myIntent.putExtra("MAIL", mail.getText().toString());
                    myIntent.putExtra("PASS", pass.getText().toString());
                    RegisterActivity.this.startActivity(myIntent);
                    finish();
                }
                else{
                    Toast.makeText(RegisterActivity.this,r.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
