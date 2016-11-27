package com.locator.child.secure.application;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context=this;

        final Button button = (Button) findViewById(R.id.buttonRegister);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Manager m = LocalMemory.getInstance().getManager();
                EditText mail   = (EditText)findViewById(R.id.editTextEmailRegister);
                EditText pass   = (EditText)findViewById(R.id.editTextPassRegister);
                EditText phone   = (EditText)findViewById(R.id.editTextPhoneRegister);

                m.register(context,mail.getText().toString(),phone.getText().toString(),pass.getText().toString());
            }
        });
    }
}
