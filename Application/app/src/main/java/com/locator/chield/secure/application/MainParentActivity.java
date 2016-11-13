package com.locator.chield.secure.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainParentActivity extends AppCompatActivity {
    LocalMemory m = LocalMemory.getInstance();
    List<Kid> kids = m.getKids();

    private List<String> names = new ArrayList <>();
    private List<String> passes = new ArrayList <>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_parent);

        for (int i=0;i<kids.size();i++){
            names.add(kids.get(i).getName());
            passes.add(kids.get(i).getPass());
        }

        populateListView();

        final Button button = (Button) findViewById(R.id.buttonGoAddKid);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainParentActivity.this, AddKidActivity.class);
                MainParentActivity.this.startActivity(myIntent);
            }
        });
    }

    private void populateListView() {
        MyCustomAdapter adapter = new MyCustomAdapter(names,this);
        ListView list = (ListView) findViewById(R.id.listViewKids);
        list.setAdapter(adapter);
    }
}
