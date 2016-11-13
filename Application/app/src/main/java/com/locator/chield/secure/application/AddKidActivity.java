package com.locator.chield.secure.application;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddKidActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kid);

        final Button button = (Button) findViewById(R.id.buttonAddKid);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Manager m = LocalMemory.getInstance().getManager();
                EditText name   = (EditText)findViewById(R.id.editTextAddKidName);
                EditText pass   = (EditText)findViewById(R.id.editTextAddKidPass);

                Result r = m.addKid(name.getText().toString(),pass.getText().toString());
                if (r.getResult()) {
                    Intent myIntent = new Intent(AddKidActivity.this, MainParentActivity.class);
                    AddKidActivity.this.startActivity(myIntent);
                    finish();

                }
                else{
                    Toast.makeText(AddKidActivity.this,r.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            Intent myIntent = new Intent(AddKidActivity.this, MainParentActivity.class);
            AddKidActivity.this.startActivity(myIntent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
