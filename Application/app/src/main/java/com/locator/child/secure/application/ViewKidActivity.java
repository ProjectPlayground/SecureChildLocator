package com.locator.child.secure.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViewKidActivity extends AppCompatActivity {

    LocalMemory m = LocalMemory.getInstance();
    private Context context;
    private String name;
    private String pass;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_kid);
        context=this;

        Intent intent = getIntent();
        name = intent.getStringExtra("NAME");
        code = intent.getStringExtra("CODE");
        pass = intent.getStringExtra("PASS");

        TextView nameV = (TextView) findViewById(R.id.textViewKidNameView);
        TextView codeV = (TextView) findViewById(R.id.textViewKidCodeView);

        nameV.setText(name);
        codeV.setText(code);

        populateListView(context,code,pass);

        final Button button = (Button) findViewById(R.id.buttonRefresh);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(context, ViewKidActivity.class);
                myIntent.putExtra("NAME", name);
                myIntent.putExtra("CODE", code);
                myIntent.putExtra("PASS", pass);
                context.startActivity(myIntent);
                ((Activity)context).finish();
            }
        });

    }

    private void populateListView(Context context,String code,String pass) {
        List<String>  l = new ArrayList<String>();
        MyLocationsAdapter adapter = new MyLocationsAdapter(l,this);
        ListView list = (ListView) findViewById(R.id.listViewLocations);
        list.setAdapter(adapter);
        m.getManager().getLocations(context,code,pass);
    }

}
