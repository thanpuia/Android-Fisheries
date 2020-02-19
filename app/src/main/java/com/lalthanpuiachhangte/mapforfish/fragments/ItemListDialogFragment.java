package com.lalthanpuiachhangte.mapforfish.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lalthanpuiachhangte.mapforfish.R;
import com.lalthanpuiachhangte.mapforfish.entity.LakeEntity;
import com.squareup.picasso.Picasso;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import es.dmoral.toasty.Toasty;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     ItemListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 * <p>You activity (or fragment) needs to implement {@link ItemListDialogFragment.Listener}.</p>
 */
public class ItemListDialogFragment extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    private Listener mListener;
    private static LakeEntity mLake;

    // TODO: Customize parameters
    public static ItemListDialogFragment newInstance(LakeEntity lake) {

        mLake = lake;
        final ItemListDialogFragment fragment = new ItemListDialogFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_ITEM_COUNT, 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view;

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ItemAdapter(getArguments().getInt(ARG_ITEM_COUNT)));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (Listener) parent;
        } else {
            mListener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface Listener {
        void onItemClicked(int position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final TextView name, description, address;
        final ImageView image;


        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            // TODO: Customize the item layout
            super(inflater.inflate(R.layout.fragment_item_list_dialog_item, parent, false));
            name = itemView.findViewById(R.id.bottom_lake_name);
            description = itemView.findViewById(R.id.bottom_lake_description);
            address = itemView.findViewById(R.id.bottom_lake_address);

            image = itemView.findViewById(R.id.bottom_lake_image);

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClicked(getAdapterPosition());
                        dismiss();
                    }
                    Toasty.info(getContext(),"IMG Click!",Toasty.LENGTH_SHORT).show();
                }
            });
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final int mItemCount;

        ItemAdapter(int itemCount) {
            mItemCount = itemCount;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.name.setText(mLake.getName());
            holder.description.setText(mLake.getDescription());
            holder.address.setText(mLake.getAddress());

            Picasso.get()
                    .load("http://fisheries.ap-south-1.elasticbeanstalk.com/public/image/"+mLake.getImage())
                    .resize(500, 500)
                    .centerCrop()
                    .into(holder.image);


        }

        @Override
        public int getItemCount() {
            return mItemCount;
        }

    }

}
