package com.petdom.breeder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.petdom.breeder.R;
import com.petdom.breeder.modal.Dog;

import java.util.ArrayList;

/**
 * Created by diwakar.mishra on 9/21/2015.
 */
public class DogsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_EMPTY_VIEW = 1;
    private static final int TYPE_DATA_VIEW = 2;

    private Context context;
    private ArrayList<Dog> dogs;
    private LayoutInflater inflater;
    private OnItemClickListener itemClickListener;
    public DogsAdapter(Context context, ArrayList<Dog> dogs, OnItemClickListener itemClickListener) {
        this.context = context;
        this.itemClickListener=itemClickListener;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.setDogs(dogs);
    }

    public void setDogs(ArrayList<Dog> dogs) {
        this.dogs = dogs;
        if (this.dogs == null) {
            this.dogs = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_DATA_VIEW) {
            View v = inflater.inflate(R.layout.layout_dog_list_item, parent, false);
            DogViewHolder vh = new DogViewHolder(v);
            return vh;
        }else{
            View v = inflater.inflate(R.layout.layout_empty_list_item, parent, false);
            EmptyViewHolder vh = new EmptyViewHolder(v);
            return vh;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return dogs.isEmpty() ? TYPE_EMPTY_VIEW : TYPE_DATA_VIEW;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof DogViewHolder){
            DogViewHolder vh = (DogViewHolder) viewHolder;
            vh.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null && !dogs.isEmpty()) {
                        itemClickListener.onItemClicked(dogs.get(position), position);
                    }
                }
            });
            vh.tvName.setText(dogs.get(position).getName());

        }
        else{
            EmptyViewHolder vh = (EmptyViewHolder) viewHolder;
            vh.tvMsg.setText(context.getResources().getString(R.string.lets_add_dogs));
            vh.tvDesc.setText(context.getResources().getString(R.string.msg_click_add_dog_button));
        }
    }

    @Override
    public int getItemCount() {
        return dogs.isEmpty()?1:dogs.size();
    }

    public static class DogViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        View root;

        public DogViewHolder(View root) {
            super(root);
            this.root = root;
            tvName = (TextView) root.findViewById(R.id.tv_name);
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

        public void onItemClicked(Dog dog, int position);
    }

}
