package com.spark.swarajyabiz;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class TodayDinvisheshAdapter extends RecyclerView.Adapter<TodayDinvisheshAdapter.EventViewHolder> {

    private Context context;
    private List<Event> events;
    private OnDinvisheshClickListener onDinvisheshClickListener;
    private boolean showAllItems;
    private boolean isShopFragment;

    public TodayDinvisheshAdapter(Context context, OnDinvisheshClickListener onDinvisheshClickListener, boolean showAllItems) {
        this.context = context;
        this.events = new ArrayList<>();
        this.onDinvisheshClickListener = onDinvisheshClickListener;
        this.showAllItems = showAllItems;
    }

    // Add this method to update the list of events
    public void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    public void setShopFragment(boolean isShopFragment) {
        this.isShopFragment = isShopFragment;
        notifyDataSetChanged();
    }

    // Interface for item click events
    public interface OnDinvisheshClickListener {
        void onDinvisheshClick(int position, List<String> imageUrls, String thoughtsname, String currentdate) throws ExecutionException, InterruptedException;
    }

    public void setOnDinvisheshClickListener(OnDinvisheshClickListener onDinvisheshClickListener) {
        this.onDinvisheshClickListener = onDinvisheshClickListener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.business_banner_list, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Event event = events.get(position);

        // Use your preferred image loading library or method to load the image into the ImageView
        Glide.with(context).load(event.getImageUrl(0)).centerCrop().into(holder.bannerImageView);

        holder.businesstextView.setText(event.getEventName());
        holder.currentdate.setVisibility(View.VISIBLE);

        String currentdate = event.getCurrentDate();
        System.out.println("Original date: " + currentdate);

        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        try {
            Date currentDate = inputFormat.parse(currentdate);

            if (currentDate != null) {
                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                System.out.println("Current year: " + currentYear);

                calendar.setTime(currentDate);
                calendar.set(Calendar.YEAR, currentYear);

                // Check if the adjusted date is before the current date without the time component
                Calendar currentDateWithoutTime = Calendar.getInstance();
                currentDateWithoutTime.setTime(new Date());
                currentDateWithoutTime.set(Calendar.HOUR_OF_DAY, 0);
                currentDateWithoutTime.set(Calendar.MINUTE, 0);
                currentDateWithoutTime.set(Calendar.SECOND, 0);
                currentDateWithoutTime.set(Calendar.MILLISECOND, 0);

                if (calendar.getTime().before(currentDateWithoutTime.getTime())) {
                    calendar.add(Calendar.YEAR, 1);
                }

                String formattedDate = outputFormat.format(calendar.getTime());
                System.out.println("Adjusted date: " + formattedDate);

                holder.currentdate.setText(formattedDate);
            } else {
                // Handle parsing error if needed
            }
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle parsing exception if needed
        }

//        String currentdate = event.getCurrentDate();
//        System.out.println("Original date: " + currentdate);
//
//        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM", Locale.getDefault());
//        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
//
//        try {
//            Date currentDate = inputFormat.parse(currentdate);
//
//            if (currentDate != null) {
//                Calendar calendar = Calendar.getInstance();
//                int currentYear = calendar.get(Calendar.YEAR);
//                System.out.println("Current year: " + currentYear);
//
//                calendar.setTime(currentDate);
//                calendar.set(Calendar.YEAR, currentYear);
//
//                if (calendar.getTime().before(new Date())) {
//                    calendar.add(Calendar.YEAR, 1);
//                }
//
//                String formattedDate = outputFormat.format(calendar.getTime());
//                System.out.println("Adjusted date: " + formattedDate);
//
//                holder.currentdate.setText(formattedDate);
//            } else {
//                // Handle parsing error if needed
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//            // Handle parsing exception if needed
//        }




        // Apply different sizes only for ShopFragment
        if (isShopFragment) {
            // Set ImageView size to 100dp
            ViewGroup.LayoutParams layoutParams = holder.cardView.getLayoutParams();
            layoutParams.width = 275;
            layoutParams.height = 298;
            holder.cardView.setLayoutParams(layoutParams);

            // Set TextView size to 10sp
            ViewGroup.LayoutParams layoutParam = holder.businesstextView.getLayoutParams();
            layoutParam.width = 275;
            holder.businesstextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

            ViewGroup.LayoutParams layoutsParam = holder.currentdatecardview.getLayoutParams();
            layoutsParam.height = 60;
            holder.currentdatecardview.setLayoutParams(layoutsParam);
            holder.favimageview.setVisibility(View.GONE);
        }

        // Set a click listener for the item
        holder.bannerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notify the activity or fragment about the click event
                if (onDinvisheshClickListener != null) {
                    try {
                        onDinvisheshClickListener.onDinvisheshClick(position, event.getImageUrls(), event.getEventName(), event.getCurrentDate());
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerImageView, favimageview;
        TextView businesstextView, currentdate;
        CardView cardView, currentdatecardview;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerImageView = itemView.findViewById(R.id.businessimages);
            businesstextView = itemView.findViewById(R.id.businesstextviews);
            currentdate = itemView.findViewById(R.id.currentdate);
            cardView = itemView.findViewById(R.id.businessimagecard);
            currentdatecardview = itemView.findViewById(R.id.currentdatecard);
            favimageview = itemView.findViewById(R.id.favimageview);
        }
    }
}
