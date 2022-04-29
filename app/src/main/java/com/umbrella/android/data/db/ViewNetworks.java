package com.umbrella.android.data.db;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.umbrella.android.R;
import com.umbrella.android.data.neuralNetwork.network.Network;

import java.util.ArrayList;

public class ViewNetworks extends AppCompatActivity {

    private ArrayList<Network> courseModalArrayList;
    private DataBaseHendler dbHandler;
    private NetworkRVAdapter courseRVAdapter;
    private RecyclerView coursesRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course);

        // initializing our all variables.
        courseModalArrayList = new ArrayList<>();
        dbHandler = new DataBaseHendler(ViewNetworks.this);

        // getting our course array
        // list from db handler class.
        courseModalArrayList = dbHandler.readCourses();

        // on below line passing our array lost to our adapter class.
        courseRVAdapter = new NetworkRVAdapter(courseModalArrayList, ViewNetworks.this);
        coursesRV = findViewById(R.id.idRVCourses);

        // setting layout manager for our recycler view.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewNetworks.this, RecyclerView.VERTICAL, false);
        coursesRV.setLayoutManager(linearLayoutManager);

        // setting our adapter to recycler view.
        coursesRV.setAdapter(courseRVAdapter);
    }
}