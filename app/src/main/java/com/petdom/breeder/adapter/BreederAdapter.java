package com.petdom.breeder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.petdom.breeder.R;
import com.petdom.breeder.modal.Breeder;
import com.petdom.breeder.modal.BreederList;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by diwakar.mishra on 9/21/2015.
 */
public class BreederAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_EMPTY_VIEW = 1;
    private static final int TYPE_DATA_VIEW = 2;

    private Context context;
    private ArrayList<Breeder> breeders = new ArrayList<>();
    private LayoutInflater inflater;
    private OnItemClickListener itemClickListener;

    public BreederAdapter(Context context, OnItemClickListener itemClickListener) {
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setBreeders(BreederList list) {
        if (list.isEmpty()){
            return;
        }
        this.breeders.clear();
        for (int i=0;i<list.size();i++){
            breeders.add(list.get(i));
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_DATA_VIEW) {

            View v = inflater.inflate(R.layout.layout_breeder_list_item, parent, false);
            BreederViewHolder vh = new BreederViewHolder(v);
            return vh;
        } else {
            View v = inflater.inflate(R.layout.layout_empty_list_item, parent, false);
            EmptyViewHolder vh = new EmptyViewHolder(v);
            return vh;
        }

    }

    @Override
    public int getItemViewType(int position) {
        return breeders.isEmpty() ? TYPE_EMPTY_VIEW : TYPE_DATA_VIEW;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof BreederViewHolder) {
            BreederViewHolder vh = (BreederViewHolder) viewHolder;
            vh.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null && !breeders.isEmpty()) {
                        itemClickListener.onItemClicked(breeders.get(position), position);
                    }
                }
            });
            vh.tvName.setText(breeders.get(position).getName());
            vh.tvId.setText(String.valueOf(breeders.get(position).getId()));
            if (!TextUtils.isEmpty(breeders.get(position).getOwnerName())) {
                vh.tvOwnerName.setText(breeders.get(position).getOwnerName());
            }

        } else {
            EmptyViewHolder vh = (EmptyViewHolder) viewHolder;
            vh.tvMsg.setText(context.getResources().getString(R.string.lets_create_breeder));
            vh.tvDesc.setText(context.getResources().getString(R.string.msg_click_add_breeder_button));
        }

    }

    @Override
    public int getItemCount() {
        return breeders.isEmpty() ? 1 : breeders.size();
    }

    public static class BreederViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvOwnerName;
        TextView tvId;
        View root;

        public BreederViewHolder(View root) {
            super(root);
            this.root = root;
            tvName = (TextView) root.findViewById(R.id.tv_name);
            tvId = (TextView) root.findViewById(R.id.tv_id);
            tvOwnerName = (TextView) root.findViewById(R.id.tv_owner_name);

        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        View root;
        TextView tvMsg;
        TextView tvDesc;

        public EmptyViewHolder(View root) {
            super(root);
            this.root = root;
            tvMsg = (TextView) root.findViewById(R.id.tv_msg);
            tvDesc = (TextView) root.findViewById(R.id.tv_description);

        }
    }

    public static interface OnItemClickListener {

        public void onItemClicked(Breeder breeder, int position);
    }

}
