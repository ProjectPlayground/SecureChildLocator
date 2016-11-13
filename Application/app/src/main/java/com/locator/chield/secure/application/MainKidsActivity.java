package com.locator.chield.secure.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainKidsActivity extends AppCompatActivity {

    LocalMemory m = LocalMemory.getInstance();
    List<Parent> parents = m.getParents();

    private List<String> mails = new ArrayList <>();
    private List<String> names = new ArrayList <>();
    private List<String> passes = new ArrayList <>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_kids);

        for (int i=0;i<parents.size();i++){
            mails.add(parents.get(i).getMail());
            names.add(parents.get(i).getName());
            passes.add(parents.get(i).getPass());
        }

        populateListView();

        final Button button = (Button) findViewById(R.id.buttonGoAddParent);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainKidsActivity.this, AddParentActivity.class);
                MainKidsActivity.this.startActivity(myIntent);
            }
        });
    }

    private void populateListView() {
        MyCustomAdapter adapter = new MyCustomAdapter(mails,this);
        ListView list = (ListView) findViewById(R.id.listViewParents);
        list.setAdapter(adapter);
    }
}
