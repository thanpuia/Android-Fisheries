package com.lalthanpuiachhangte.mapforfish;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.lalthanpuiachhangte.mapforfish.adapter.ListViewAdapter;
import com.lalthanpuiachhangte.mapforfish.entity.LakeEntity;

import java.util.List;

public class ListLakeActivity extends AppCompatActivity {

    ListView listView;
    List<LakeEntity> myLakes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_lake);

        listView = findViewById(R.id.listLake);

        Ion.with(this)
                .load("http://fisheries.ap-south-1.elasticbeanstalk.com/api/getAll")
                .as(new TypeToken<List<LakeEntity>>(){})
                .setCallback(new FutureCallback<List<LakeEntity>>() {
                    @Override
                    public void onCompleted(Exception e, List<LakeEntity> lakes) {
                        Log.e("TAG","tweets: "+lakes.get(0).getName());
                        myLakes = lakes;

                        ListViewAdapter lakeAdapter = new ListViewAdapter(myLakes);
                        listView.setAdapter(lakeAdapter);
                    }
                });


    }
}
