package com.spark.swarajyabiz.Adapters;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.spark.swarajyabiz.CommInfo;
import com.spark.swarajyabiz.CommInfoGlobal;
import com.spark.swarajyabiz.ModelClasses.CategoryModel;
import com.spark.swarajyabiz.ModelClasses.CommModel;
import com.spark.swarajyabiz.R;
import com.spark.swarajyabiz.ui.CommunityFragment;

import java.util.ArrayList;

public class CommAdapter extends RecyclerView.Adapter<CommAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<CommModel> commModels;
    private OnItemClickListener onItemClickListener;

    public CommAdapter(Context mContext, ArrayList<CommModel> commModels, OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.commModels = commModels;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public CommAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.commlist, parent, false);
        return new CommAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return commModels.size();
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Setter method for the click listener
    public void setOnItemClickListener(CommAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView commName,mbrCnt;
        ImageView imageView;
        CardView mainCard;

        // Define an array of colors
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commName = itemView.findViewById(R.id.comuName);
            mbrCnt = itemView.findViewById(R.id.mbcnt);
            imageView = itemView.findViewById(R.id.comuImg);
            mainCard = itemView.findViewById(R.id.clikcc);

            // Set click listener for the item view
            mainCard.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getAdapterPosition());
                        CommModel categoryModel = commModels.get(getBindingAdapterPosition());
                        ArrayList<String> data=new ArrayList<>();
                        data.add(categoryModel.getCommId());
                        data.add(categoryModel.getCommName());
                        data.add(categoryModel.getCommAdmin());
                        data.add(categoryModel.getCommImg());
                        data.add(categoryModel.getCommDesc());
                        data.add(categoryModel.getMbrCount());
                        data.add(categoryModel.getCommLink());

                        if(categoryModel.isChecked()){
                            Intent intent=new Intent(mContext, CommInfo.class);
                            intent.putStringArrayListExtra("Data",data);
                            mContext.startActivity(intent);
                        }else {
                            Intent intent=new Intent(mContext, CommInfoGlobal.class);
                            intent.putStringArrayListExtra("Data",data);
                            mContext.startActivity(intent);
                        }

                    }
                }
            });
        }

        public void bind(int position) {
            CommModel categoryModel = commModels.get(position);
            commName.setText(categoryModel.getCommName());
            mbrCnt.setText(categoryModel.getMbrCount());
            Glide.with(mContext)
                    .load(categoryModel.getCommImg())
                    .into(imageView);

        }

    }
}
