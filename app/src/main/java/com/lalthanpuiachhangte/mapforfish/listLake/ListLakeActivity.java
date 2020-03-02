package com.lalthanpuiachhangte.mapforfish.listLake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.lalthanpuiachhangte.mapforfish.R;
import com.lalthanpuiachhangte.mapforfish.entity.LakeEntity;

import java.util.List;

public class ListLakeActivity extends AppCompatActivity {
    private String mURL = String.valueOf(R.string.URL_PATH)+"users";
    private List<LakeEntity> mLakes;
    private ListView listView;
    LayoutInflater layoutInflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_lake);
        layoutInflater = (LayoutInflater.from(getApplicationContext()));
        listView = findViewById(R.id.mListView);

        Ion.with(this)
                .load("http://fisheries.ap-south-1.elasticbeanstalk.com/api/getAll")
                .as(new TypeToken<List<LakeEntity>>(){})
                .setCallback(new FutureCallback<List<LakeEntity>>() {
                    @Override
                    public void onCompleted(Exception e, List<LakeEntity> result) {
                        mLakes = result;
                        CustomAdapter mAdapter = new CustomAdapter();
                        listView.setAdapter(mAdapter);
                    }
                });

    }



    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mLakes.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = layoutInflater.inflate(R.layout.list_of_lakes,null);

            TextView textView1= convertView.findViewById(R.id.text77);
            TextView textView2= convertView.findViewById(R.id.text88);

            textView1.setText(mLakes.get(position).getName());
            textView2.setText(mLakes.get(position).getAddress());
            return convertView;
        }
    }
}
