package com.spark.swarajyabiz.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.spark.swarajyabiz.CommInfo;
import com.spark.swarajyabiz.CommInfoGlobal;
import com.spark.swarajyabiz.ModelClasses.CommInsightModel;
import com.spark.swarajyabiz.ModelClasses.CommModel;
import com.spark.swarajyabiz.R;

import java.util.ArrayList;

public class CommInsightAdapter extends RecyclerView.Adapter<CommInsightAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<CommInsightModel> commInsightModels;
    private OnItemClickListener onItemClickListener;

    public CommInsightAdapter(Context mContext, ArrayList<CommInsightModel> commInsightModels, OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.commInsightModels = commInsightModels;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CommInsightAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comminsight, parent, false);
        return new CommInsightAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommInsightAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return commInsightModels.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Setter method for the click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView commName,mbrCnt,viewCnt,earnCnt,status,monit;
        ImageView imageView;
        CardView mainCard;

        // Define an array of colors
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commName = itemView.findViewById(R.id.commNameX);
            mbrCnt = itemView.findViewById(R.id.memberX);
            imageView = itemView.findViewById(R.id.commProf);
            mainCard = itemView.findViewById(R.id.commCard);
            viewCnt=itemView.findViewById(R.id.allViews);
            earnCnt=itemView.findViewById(R.id.commEarns);
            status=itemView.findViewById(R.id.visStatus);
            monit=itemView.findViewById(R.id.monitstatus);

            // Set click listener for the item view
            mainCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
//                        onItemClickListener.onItemClick(getAdapterPosition());
//                        CommModel categoryModel = commModels.get(getBindingAdapterPosition());
//                        ArrayList<String> data=new ArrayList<>();
//                        data.add(categoryModel.getCommId());
//                        data.add(categoryModel.getCommName());
//                        data.add(categoryModel.getCommAdmin());
//                        data.add(categoryModel.getCommImg());
//                        data.add(categoryModel.getCommDesc());
//                        data.add(categoryModel.getMbrCount());
//                        data.add(categoryModel.getCommLink());
//
//                        if(categoryModel.isChecked()){
//                            Intent intent=new Intent(mContext, CommInfo.class);
//                            intent.putStringArrayListExtra("Data",data);
//                            mContext.startActivity(intent);
//                        }else {
//                            Intent intent=new Intent(mContext, CommInfoGlobal.class);
//                            intent.putStringArrayListExtra("Data",data);
//                            mContext.startActivity(intent);
//                        }
                    }
                }
            });
        }

        public void bind(int position) {
            CommInsightModel categoryModel = commInsightModels.get(position);
            commName.setText(categoryModel.getCommName());
            mbrCnt.setText(categoryModel.getCommMbrCnt()+" Members");
            Glide.with(mContext)
                    .load(categoryModel.getCommProf())
                    .into(imageView);
            double updatedWallBal = Double.parseDouble(categoryModel.getCommEarns());
            String formattedValue = String.format("%.2f", updatedWallBal);
            viewCnt.setText(categoryModel.getCommViews());
            earnCnt.setText(formattedValue);
            status.setText(categoryModel.getCommStatus());
            monit.setText(categoryModel.getCommMonit());
        }

    }


}
