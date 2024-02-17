package com.spark.swarajyabiz.Extra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.spark.swarajyabiz.ModelClasses.CouponModel;
import com.spark.swarajyabiz.R;

import java.util.List;

public class CustomSpinnerAdapter extends BaseAdapter {
    private Context mContext;
    private List<CouponModel> mItems;

    public CustomSpinnerAdapter(Context context, List<CouponModel> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.cpnlist, parent, false);
        }

        TextView cpnId,cpnAmt;
        ImageView cpnfront,cpnback;
        CardView mainCard;


        cpnId = convertView.findViewById(R.id.cpnIdxc);
        cpnAmt = convertView.findViewById(R.id.cpnAmtXXX);
        cpnfront = convertView.findViewById(R.id.imgfront1);
        cpnback = convertView.findViewById(R.id.imgback1);
        mainCard = convertView.findViewById(R.id.cpnCardxx);

        CouponModel categoryModel = mItems.get(position);
        cpnId.setText("# "+categoryModel.getCpnId());
        cpnAmt.setText("₹ "+categoryModel.getCpnAmt());

        Glide.with(mContext)
                .load(categoryModel.getCpnFront())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(cpnfront);

        Glide.with(mContext)
                .load(categoryModel.getCpnBack())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(cpnback);

//        TextView textViewTitle = convertView.findViewById(R.id.textViewTitle);
//        textViewTitle.setText(mItems.get(position).getTitle());



        // Set other views if needed

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent); // Use the same custom layout for dropdown items
    }
}
