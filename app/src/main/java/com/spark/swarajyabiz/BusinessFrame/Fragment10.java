package com.spark.swarajyabiz.BusinessFrame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.spark.swarajyabiz.CreateBanner;
import com.spark.swarajyabiz.R;


public class Fragment10 extends Fragment implements CreateBanner.ButtonClickListener{

    String shopname, shopimage, shopcontactnumber, shopownername;
    TextView shopnametext, shopcontactnumbertext, shopownernametext, text1, text2;
    ImageView shopimageview;
    private EditText texteditor;
    private ImageView cancelimage;
    private ImageView checkimage;
    private LinearLayout edittextlayout;
    private float lastTouchX, lastTouchY, scaleFactor = 1f;
    FrameLayout frameLayout;
    RelativeLayout relativeLayout;
    private ScaleGestureDetector scaleGestureDetector;

    public Fragment10() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_10, container, false);

        shopnametext = view.findViewById(R.id.shopnametext);
        shopcontactnumbertext = view.findViewById(R.id.shopcontactNumbertext);
        shopownernametext = view.findViewById(R.id.shopownernametext);
        shopimageview = view.findViewById(R.id.shopimageview);
        frameLayout = view.findViewById(R.id.frameLayout);
        relativeLayout = view.findViewById(R.id.relativefragment1);
        text1 = view.findViewById(R.id.text1);
        text2 = view.findViewById(R.id.text2);
        texteditor = view.findViewById(R.id.texteditor);
        cancelimage = view.findViewById(R.id.cancelimage);
        checkimage = view.findViewById(R.id.checkimage);
        edittextlayout = view.findViewById(R.id.edittextlayout);


        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);


        // Retrieve data from arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            shopimage = bundle.getString("shopimage", "default_value");
            shopname = bundle.getString("shopname", "default_value");
            shopcontactnumber = bundle.getString("shopcontactnumber", "default_value");
            shopownername = bundle.getString("shopaddress", "default_value");
            System.out.println("djfjnvsd " +shopimage);
            System.out.println("djfjnvsd " +shopname);
            System.out.println("djfjnvsd " +shopcontactnumber);
            System.out.println("djfjnvsd " +shopownername);

            // Use the data as needed
            // Check if the fragment is attached to an activity before using getActivity()
            if (getContext() != null) {
                Glide.with(this)
                        .load(shopimage)
                        .circleCrop()
                        .into(shopimageview);

                shopnametext.setText(shopname);
                shopcontactnumbertext.setText(shopcontactnumber);
                shopownernametext.setText(shopownername);
            }

        }
//        Glide.with(getActivity()).load(shopimage).circleCrop().into(shopimageview);
//        shopnametext.setText(shopname);
//        shopcontactnumbertext.setText(shopcontactnumber);
//        shopownernametext.setText(shopownername);

        setMovableZoomableTouchListener(shopnametext);
        setMovableZoomableTouchListener(shopcontactnumbertext);
        setMovableZoomableTouchListener(shopownernametext);
        setMovableZoomableTouchListener(text1);
        setMovableZoomableTouchListener(text2);
        // Initialize ScaleGestureDetector
        scaleGestureDetector = new ScaleGestureDetector(requireContext(), new Fragment10.ScaleListener());

        if (getContext() instanceof CreateBanner) {
            ((CreateBanner) getContext()).setButtonClickListener(this);
        }

        return view;
    }

    private void setMovableZoomableTouchListener(final View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);
                return moveView(event, view);
            }
        });

    }

    private boolean moveView(MotionEvent event, View view) {
        float x = event.getRawX();
        float y = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchX = x;
                lastTouchY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                float deltaX = x - lastTouchX;
                float deltaY = y - lastTouchY;

                // Update the position of the view within the bounds of frameLayout
                float newX = view.getX() + deltaX;
                float newY = view.getY() + deltaY;

                if (newX < 0) newX = 0;
                if (newY < 0) newY = 0;
                if (newX + view.getWidth() > frameLayout.getWidth())
                    newX = frameLayout.getWidth() - view.getWidth();
                if (newY + view.getHeight() > frameLayout.getHeight())
                    newY = frameLayout.getHeight() - view.getHeight();

                view.setX(newX);
                view.setY(newY);

                // Check if the moved view is the shopnametext TextView
                if (view.getId() == R.id.shopnametext) {
                    // Show frameLayout1 relativeLayout.setVisibility(View.VISIBLE);

                }

                if (view.getId() == R.id.shopcontactNumbertext) {

                }

                if (view.getId() == R.id.shopownernametext) {
                }

                if (view.getId() == R.id.text1) {

                }
                if (view.getId() == R.id.text2) {

                }

                // Update the scale of the view
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Update the last touch coordinates
                lastTouchX = x;
                lastTouchY = y;
                break;

        }

        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // Update the scaleFactor based on the pinch gesture
            scaleFactor *= detector.getScaleFactor();

            // Limit the scale range if needed
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));

            return true;
        }
    }

    @Override
    public void onAddLogoButtonClick() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

    }

    @Override
    public void onAddTextButtonClick() {

        edittextlayout.setVisibility(View.VISIBLE);
        cancelimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide the layout when the cancel image is clicked
                edittextlayout.setVisibility(View.GONE);
            }
        });

        checkimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from the edittext
                String newText = texteditor.getText().toString();

                // Check if text1 is empty
                if (TextUtils.isEmpty(text1.getText())) {
                    text1.setText(newText);
                }
                // Check if text2 is empty
                else if (TextUtils.isEmpty(text2.getText())) {
                    text2.setText(newText);
                }
                // Both text1 and text2 are not empty
                else {
                    // Hide the layout when both text views are not empty
                    edittextlayout.setVisibility(View.GONE);
                    return; // Exit the method to prevent further execution
                }

                // Hide the layout when the check image is clicked
                edittextlayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImageUri);

                    // Set the selected image to the ImageView
                    Glide.with(this)
                            .load(bitmap)
                            .circleCrop()
                            .into(shopimageview);
                    scaleFactor = 1f;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        // Set the flag to true when the fragment becomes active
        if (getActivity() instanceof CreateBanner) {
            ((CreateBanner) getActivity()).setFragmentActive(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Set the flag to false when the fragment becomes inactive
        if (getActivity() instanceof CreateBanner) {
            ((CreateBanner) getActivity()).setFragmentActive(false);
        }
    }
}