package com.locator.chield.secure.application;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddKidActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kid);

        final Button button = (Button) findViewById(R.id.buttonAddKid);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LocalMemory m = LocalMemory.getInstance();
                EditText name   = (EditText)findViewById(R.id.editTextAddKidName);
                EditText pass   = (EditText)findViewById(R.id.editTextAddKidPass);

                m.addKid(new Kid(name.getText().toString(),pass.getText().toString()));
                Intent myIntent = new Intent(AddKidActivity.this, MainParentActivity.class);
                AddKidActivity.this.startActivity(myIntent);
            }
        });
    }
}
