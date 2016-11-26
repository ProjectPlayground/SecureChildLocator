package com.locator.chield.secure.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

public class ViewKidActivity extends AppCompatActivity {

    LocalMemory m = LocalMemory.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_kid);

        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        String pass = intent.getStringExtra("PASS");
        String code = intent.getStringExtra("CODE");


        TextView nameV = (TextView) findViewById(R.id.textViewKidNameView);
        TextView passV = (TextView) findViewById(R.id.textViewKidPassView);
        TextView codeV = (TextView) findViewById(R.id.textViewKidCodeView);

        nameV.setText(name);
        passV.setText(pass);
        codeV.setText(code);

        populateListView(name);

    }

    private void populateListView(String name) {
        MyLocationsAdapter adapter = new MyLocationsAdapter(m.getManager().getLocations(name),this);
        ListView list = (ListView) findViewById(R.id.listViewLocations);
        list.setAdapter(adapter);
    }

}
