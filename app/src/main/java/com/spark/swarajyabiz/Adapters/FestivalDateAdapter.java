package com.spark.swarajyabiz.Adapters;

import static com.rd.utils.DensityUtils.dpToPx;

import static com.rd.utils.DensityUtils.dpToPx;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spark.swarajyabiz.R;

import java.util.List;

    public class FestivalDateAdapter extends RecyclerView.Adapter<FestivalDateAdapter.MonthViewHolder> {

        private List<String> months;
        private LayoutInflater inflater;

        OnClick onClick;

        public FestivalDateAdapter(Context context, List<String> months, OnClick onClick) {
            this.months = months;
            inflater = LayoutInflater.from(context);
            this.onClick = onClick;
        }

        public interface OnClick{
            void onClick(String months);
        }
        @NonNull
        @Override
        public FestivalDateAdapter.MonthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new FestivalDateAdapter.MonthViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull FestivalDateAdapter.MonthViewHolder holder, int position) {
            String month = months.get(position);
            holder.monthTextView.setText(month);
            holder.monthTextView.setBackgroundResource(R.color.white);

            // Set margins programmatically
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.monthTextView.getLayoutParams();
            layoutParams.topMargin = dpToPx(5); // Convert dp to pixels
            layoutParams.bottomMargin = dpToPx(5); // Convert dp to pixels
            holder.monthTextView.setLayoutParams(layoutParams);

            System.out.println("5yehdnv "+month);

            holder.monthTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.onClick(holder.monthTextView.getText().toString());
                }
            });
        }

        @Override
        public int getItemCount() {
            return months.size();
        }

        static class MonthViewHolder extends RecyclerView.ViewHolder {
            TextView monthTextView;

            MonthViewHolder(View itemView) {
                super(itemView);
                monthTextView = itemView.findViewById(android.R.id.text1);
            }
        }
    }
