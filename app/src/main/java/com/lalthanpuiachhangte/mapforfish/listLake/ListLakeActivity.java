package com.lalthanpuiachhangte.mapforfish.listLake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.lalthanpuiachhangte.mapforfish.R;
import com.lalthanpuiachhangte.mapforfish.entity.LakeEntity;

import java.util.List;

public class ListLakeActivity extends AppCompatActivity {
    private String mURL = String.valueOf(R.string.URL_PATH)+"users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_lake2);

        Ion.with(this)
                .load(mURL)
                .as(new TypeToken<List<LakeEntity>>(){})
                .setCallback(new FutureCallback<List<LakeEntity>>() {
                    @Override
                    public void onCompleted(Exception e, List<LakeEntity> result) {

                        Log.e("TAG", "AALL liST::"+ result);
                    }
                });

    }
}
