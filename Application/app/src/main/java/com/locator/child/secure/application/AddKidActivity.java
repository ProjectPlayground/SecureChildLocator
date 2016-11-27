package com.locator.child.secure.application;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddKidActivity extends AppCompatActivity {
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kid);
        context=this;

        final Button button = (Button) findViewById(R.id.buttonAddKid);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Manager m = LocalMemory.getInstance().getManager();
                EditText name   = (EditText)findViewById(R.id.editTextAddKidName);
                EditText pass   = (EditText)findViewById(R.id.editTextAddKidPass);

                m.addKid(context,name.getText().toString(),pass.getText().toString());
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
