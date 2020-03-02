/*
package com.lalthanpuiachhangte.mapforfish.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lalthanpuiachhangte.mapforfish.R;

public class ListViewAdapter extends BaseAdapter {

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        RecyclerView.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;



            viewHolder = new RecyclerView.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);


        view = inflate(R.layout.lake_list,null);

        TextView textView1= convertView.findViewById(R.id.lake);
        TextView textView2= convertView.findViewById(R.id.lake_description);

        textView1.setText(mLakes.get(position).getName());
        textView2.setText(mLakes.get(position).getAddress());
        return convertView;
    }
}
*/
