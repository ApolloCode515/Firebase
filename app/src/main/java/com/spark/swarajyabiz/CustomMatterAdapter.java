package com.spark.swarajyabiz;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomMatterAdapter extends RecyclerView.Adapter<CustomMatterAdapter.MatterViewHolder> {

    private List<String> matterList;
    onClickListener onClickListener;

    public CustomMatterAdapter(List<String> matterList, onClickListener onClickListener) {
        this.matterList = matterList;
        this.onClickListener = onClickListener;
    }

    public void setMatterList(List<String> matterList) {
        this.matterList = matterList;
        notifyDataSetChanged();
    }

    interface onClickListener{
        void onDeleteClick(int position);
    }
    @NonNull
    @Override
    public MatterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_matter_list, parent, false);
        return new MatterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatterViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String matter = matterList.get(position);
        holder.matterTextView.setText(matter);

        holder.deletematter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onDeleteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return matterList.size();
    }

    public static class MatterViewHolder extends RecyclerView.ViewHolder {

        private TextView matterTextView;
        private ImageView deletematter;

        public MatterViewHolder(@NonNull View itemView) {
            super(itemView);
            matterTextView = itemView.findViewById(R.id.custommattertext);
            deletematter = itemView.findViewById(R.id.deleteimgview);
        }

    }
}
