package com.locator.child.secure.application;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddParentActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parent);
        context=this;

        final Button button = (Button) findViewById(R.id.buttonAddParent);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Manager m = LocalMemory.getInstance().getManager();
                EditText mail   = (EditText)findViewById(R.id.editTextAddParentMail);
                EditText pass   = (EditText)findViewById(R.id.editTextAddParentPass);
                EditText sharedPass   = (EditText)findViewById(R.id.editTextAddParentSharedPass);
                EditText code   = (EditText)findViewById(R.id.editTextAddParentCode);

                m.addParent(context,mail.getText().toString(),pass.getText().toString(),sharedPass.getText().toString(),code.getText().toString());

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
