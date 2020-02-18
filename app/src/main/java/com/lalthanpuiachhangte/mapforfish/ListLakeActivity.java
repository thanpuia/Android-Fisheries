package com.lalthanpuiachhangte.mapforfish;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.lalthanpuiachhangte.mapforfish.entity.LakeEntity;

import java.util.List;

public class ListLakeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<LakeEntity> myLakes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_lake);

        recyclerView = findViewById(R.id.my_recycler_view);

        Ion.with(this)
                .load("http://fisheries.ap-south-1.elasticbeanstalk.com/api/getAll")
                .as(new TypeToken<List<LakeEntity>>(){})
                .setCallback(new FutureCallback<List<LakeEntity>>() {
                    @Override
                    public void onCompleted(Exception e, List<LakeEntity> lakes) {
                        Log.e("TAG","tweets: "+lakes.get(0).getName());
                        myLakes = lakes;

//                        // use this setting to improve performance if you know that changes
//                        // in content do not change the layout size of the RecyclerView
//                        recyclerView.setHasFixedSize(true);
//
//                        // use a linear layout manager
//                        layoutManager = new LinearLayoutManager(getApplicationContext());
//                        recyclerView.setLayoutManager(layoutManager);
//
//                        // specify an adapter (see also next example)
//                        mAdapter = new ListViewAdapter (lakes);
//                        recyclerView.setAdapter(mAdapter);

                    }
                });


    }
}
