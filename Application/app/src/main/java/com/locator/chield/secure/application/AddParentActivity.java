package com.locator.chield.secure.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddParentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parent);

        final Button button = (Button) findViewById(R.id.buttonAddParent);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LocalMemory m = LocalMemory.getInstance();
                EditText mail   = (EditText)findViewById(R.id.editTextAddParentMail);
                EditText name   = (EditText)findViewById(R.id.editTextAddParentName);
                EditText pass   = (EditText)findViewById(R.id.editTextAddParentPass);

                m.addParent(new Parent(mail.getText().toString(),name.getText().toString(),pass.getText().toString()));
                Intent myIntent = new Intent(AddParentActivity.this, MainKidsActivity.class);
                AddParentActivity.this.startActivity(myIntent);
            }
        });
    }
}
