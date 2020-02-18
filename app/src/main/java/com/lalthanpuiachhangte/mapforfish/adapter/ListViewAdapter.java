package com.lalthanpuiachhangte.mapforfish.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lalthanpuiachhangte.mapforfish.R;
import com.lalthanpuiachhangte.mapforfish.entity.LakeEntity;

import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.*;

public class ListViewAdapter extends BaseAdapter {

    private List<LakeEntity> lakeList;

    @Override
    public int getCount() {
         return lakeList.size();
    }

    public ListViewAdapter(List<LakeEntity> lakes) {
        lakeList = lakes;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(convertView.getContext().getApplicationContext()).inflate(R.layout.lake_list, parent, false);
        }
        TextView lakeName = convertView.findViewById(R.id.lake_name);
        TextView lakeLat = convertView.findViewById(R.id.mLat);
        TextView lakeLng = convertView.findViewById(R.id.mLng);
        TextView lakeDistrict = convertView.findViewById(R.id.mDistrict);
        TextView lakeImage = convertView.findViewById(R.id.mImage);

        lakeName.setText(lakeList.get(position).getName());
        lakeLat.setText(lakeList.get(position).getLat().toString());
        lakeLng.setText(lakeList.get(position).getLng().toString());
        lakeDistrict.setText(lakeList.get(position).getDistrict());
        lakeImage.setText(lakeList.get(position).getImage());

        return convertView;

    }
}