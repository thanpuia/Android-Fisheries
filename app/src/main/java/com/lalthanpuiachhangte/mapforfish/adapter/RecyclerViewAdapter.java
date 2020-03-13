package com.lalthanpuiachhangte.mapforfish.adapter;

import android.content.ClipData;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lalthanpuiachhangte.mapforfish.R;
import com.lalthanpuiachhangte.mapforfish.entity.LakeEntity;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    //All methods in this adapter are required for a bare minimum recyclerview adapter
    private int listItemLayout;
    private ArrayList<LakeEntity> itemList;
    // Constructor of the class
    public RecyclerViewAdapter(int layoutId, ArrayList<LakeEntity> itemList) {
        listItemLayout = layoutId;
        this.itemList = itemList;
    }


    // get the size of the list
    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }


    // specify the row layout file and click for each row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(listItemLayout, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        TextView name = holder.name;
        TextView address = holder.address;
        TextView desp = holder.desp;
        TextView lat = holder.lat;
        TextView lng = holder.lng;

        name.setText(itemList.get(listPosition).getName());
    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name,address,desp,lat,lng;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

     /*
            name = (TextView) itemView.findViewById(R.id.);
            address = (TextView) itemView.findViewById(R.id.);
            desp = (TextView) itemView.findViewById(R.id.);
            lat = (TextView) itemView.findViewById(R.id.);
            lng = (TextView) itemView.findViewById(R.id.);
     */

        }
        @Override
        public void onClick(View view) {
            //Log.d("onclick", "onClick " + getLayoutPosition() + " " + item.getText());
        }
    }
}
