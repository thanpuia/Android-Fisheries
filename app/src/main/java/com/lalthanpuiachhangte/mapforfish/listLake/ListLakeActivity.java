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

            TextView lake_name_list= convertView.findViewById(R.id.lake_name_list);
            TextView lake_address_list= convertView.findViewById(R.id.lake_address_list);
            TextView lake_desp_list= convertView.findViewById(R.id.lake_desp_list);
            TextView lake_lat_list= convertView.findViewById(R.id.lake_lat_list);
            TextView lake_lng_list= convertView.findViewById(R.id.lake_lng_list);
            //TextView lake_serialNo_list= convertView.findViewById(R.id.lake_serialNo_list);

            lake_name_list.setText(mLakes.get(position).getName());
            lake_address_list.setText(mLakes.get(position).getAddress());
            lake_desp_list.setText(mLakes.get(position).getDescription());
            lake_lat_list.setText(mLakes.get(position).getLat().toString());
            lake_lng_list.setText(mLakes.get(position).getLng().toString());
//            lake_serialNo_list.setText(mLakes.get(position).getId());

            return convertView;
        }
    }
}
