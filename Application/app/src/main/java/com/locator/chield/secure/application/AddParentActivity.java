package com.locator.chield.secure.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddParentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parent);

        final Button button = (Button) findViewById(R.id.buttonAddParent);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Manager m = LocalMemory.getInstance().getManager();
                EditText mail   = (EditText)findViewById(R.id.editTextAddParentMail);
                EditText name   = (EditText)findViewById(R.id.editTextAddParentName);
                EditText pass   = (EditText)findViewById(R.id.editTextAddParentPass);

                Result r = m.addParent(mail.getText().toString(),name.getText().toString(),pass.getText().toString());
                if (r.getResult()) {
                    Intent myIntent = new Intent(AddParentActivity.this, MainKidsActivity.class);
                    AddParentActivity.this.startActivity(myIntent);
                    finish();
                }
                else{
                    Toast.makeText(AddParentActivity.this,r.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            Intent myIntent = new Intent(AddParentActivity.this, MainKidsActivity.class);
            AddParentActivity.this.startActivity(myIntent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
