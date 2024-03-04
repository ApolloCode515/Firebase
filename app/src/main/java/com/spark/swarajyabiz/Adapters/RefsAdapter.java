package com.spark.swarajyabiz.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.spark.swarajyabiz.CommInfo;
import com.spark.swarajyabiz.CommInfoGlobal;
import com.spark.swarajyabiz.ModelClasses.CommModel;
import com.spark.swarajyabiz.ModelClasses.RefModel;
import com.spark.swarajyabiz.R;
import com.spark.swarajyabiz.RefsData;

import java.util.ArrayList;

public class RefsAdapter extends RecyclerView.Adapter<RefsAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<RefModel> refModels;
    private OnItemClickListener onItemClickListener;

    public RefsAdapter(Context mContext, ArrayList<RefModel> refModels, OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.refModels = refModels;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RefsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.referlist, parent, false);
        return new RefsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RefsAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return refModels.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Setter method for the click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mobno,plan;
        CardView mainCard;
        LinearLayout statuslist;

        // Define an array of colors
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mobno = itemView.findViewById(R.id.mobnorrr);
            plan = itemView.findViewById(R.id.planrrr);
            mainCard = itemView.findViewById(R.id.llll);
            statuslist = itemView.findViewById(R.id.statuslayout);

            // Set click listener for the item view
            mainCard.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    if (onItemClickListener != null) {
//                        onItemClickListener.onItemClick(getAdapterPosition());
//                        RefModel categoryModel = refModels.get(getBindingAdapterPosition());
//                        ArrayList<String> data=new ArrayList<>();
//                        Intent intent=new Intent(mContext, CommInfo.class);
//                        intent.putStringArrayListExtra("Data",data);
//                        mContext.startActivity(intent);

                    }
                }
            });
        }

        public void bind(int position) {
            RefModel categoryModel = refModels.get(position);
            mobno.setText(categoryModel.getMobno());

            if(categoryModel.getCplan().equals("-")){
                plan.setText("Installed");
                statuslist.setBackgroundColor(Color.parseColor("#29BBFF"));
            }else {
                plan.setText(categoryModel.getCplan());
                statuslist.setBackgroundColor(Color.parseColor("#239328"));
            }
        }
    }
}
