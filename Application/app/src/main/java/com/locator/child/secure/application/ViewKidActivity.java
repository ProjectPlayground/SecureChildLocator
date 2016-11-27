package com.locator.child.secure.application;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViewKidActivity extends AppCompatActivity {

    LocalMemory m = LocalMemory.getInstance();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_kid);
        context=this;

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

        populateListView(context,code);

    }

    private void populateListView(Context context,String code) {
        List<String>  l = new ArrayList<String>();
        MyLocationsAdapter adapter = new MyLocationsAdapter(l,this);
        ListView list = (ListView) findViewById(R.id.listViewLocations);
        list.setAdapter(adapter);
        m.getManager().getLocations(context,code);
    }

}
