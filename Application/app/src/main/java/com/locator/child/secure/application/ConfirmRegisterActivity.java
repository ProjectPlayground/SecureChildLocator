package com.locator.child.secure.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConfirmRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_register);

        final Button button1 = (Button) findViewById(R.id.buttonConfirm);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = getIntent();
                String mail = intent.getStringExtra("MAIL");
                String pass = intent.getStringExtra("PASS");
                EditText code   = (EditText)findViewById(R.id.editTextCode);

                Manager m = LocalMemory.getInstance().getManager();
                Result r = m.confirmRegistration(mail,pass,code.getText().toString());

                if (r.getResult()) {
                    Intent myIntent = new Intent(ConfirmRegisterActivity.this, MainParentActivity.class);
                    ConfirmRegisterActivity.this.startActivity(myIntent);
                    finish();
                }
                else
                    Toast.makeText(ConfirmRegisterActivity.this,r.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
