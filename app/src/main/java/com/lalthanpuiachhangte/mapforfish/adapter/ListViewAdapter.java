//package com.lalthanpuiachhangte.mapforfish.adapter;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.lalthanpuiachhangte.mapforfish.R;
//import com.lalthanpuiachhangte.mapforfish.entity.LakeEntity;
//
//import java.util.List;
//
//
//public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.MyViewHolder> {
//    private List<LakeEntity>  mDataset;
//
//    // Provide a reference to the views for each data item
//    // Complex data items may need more than one view per item, and
//    // you provide access to all the views for a data item in a view holder
//    public static class MyViewHolder extends RecyclerView.ViewHolder {
//        // each data item is just a string in this case
//        public TextView nameTv,latTv,lngTv,districtTv,imageTv;
//        public MyViewHolder(TextView v) {
//            super(v);
//            //textView = v;
//        }
//    }
//
//    // Provide a suitable constructor (depends on the kind of dataset)
//    public ListViewAdapter(List<LakeEntity> myDataset) {
//        mDataset = myDataset;
//    }
//
//    // Create new views (invoked by the layout manager)
//    @Override
//    public ListViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
//                                                     int viewType) {
//        // create a new view
//        TextView v = (TextView) LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.lake_list, parent, false);
//
//        MyViewHolder vh = new MyViewHolder(v);
//        return vh;
//
//    }
//
//    // Replace the contents of a view (invoked by the layout manager)
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position) {
//        // - get element from your dataset at this position
//        // - replace the contents of the view with that element
//        holder.textView.setText(m);
//
//    }
//
//    // Return the size of your dataset (invoked by the layout manager)
//    @Override
//    public int getItemCount() {
//        return mDataset.length;
//    }
//}
