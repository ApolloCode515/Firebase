package com.spark.admin;

import static com.spark.admin.LoginMain.PREFS_NAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.rxjava3.annotations.NonNull;

public class PomoteShop extends AppCompatActivity {

    private DatabaseReference databaseRef;
    private DatabaseReference userRef;
    private PromoteAdapter shopAdapter;
    private List<PromoteShop> shopList;
    private List<PromoteShop> filteredList;
    private List<PromoteShop> allShopsList;
    private List<PromoteShop> promotedShopsList = new ArrayList<>();
    private RecyclerView recyclerViewShops,recyclerViewShop;
    private TextView addButton, headerText, add, promotedshoptext;
    private ImageView back, crossIcon;
    FrameLayout header, headeroverlay ;
    String contactNumber;
    int promotedShopCount;
    private Set<Integer> promotedShopPositions;
    CheckedShopsAdapter adapter;
    String shopcontactNumber;
    Shop shop;
    private static final String USER_ID_KEY = "userID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomote_shop);

        recyclerViewShops = findViewById(R.id.viewdetails);
        recyclerViewShop = findViewById(R.id.promotedshopsdetails);
        promotedshoptext = findViewById(R.id.promotedshops);
        promotedshoptext.setVisibility(View.GONE);
        recyclerViewShop.setVisibility(View.GONE);

        addButton = findViewById(R.id.add);
       // removebutton = findViewById(R.id.removeTextView);
        back = findViewById(R.id.back);
        // selectedCountTextView = findViewById(R.id.selectedCountTextView);
        header = findViewById(R.id.frameHeader);
        headeroverlay  = findViewById(R.id.headerOverlay);
         crossIcon = findViewById(R.id.crossIcon);
        headerText = findViewById(R.id.headerText);
         add = findViewById(R.id.add);


        Intent sharedIntent = IntentDataHolder.getSharedIntent();
        if (sharedIntent != null) {
            shopcontactNumber  = sharedIntent.getStringExtra("contactNumber");
            System.out.println("ContactNumbersss " + contactNumber);

        }

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference().child("Shop");
        userRef = database.getReference("Users");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        }

        // Initialize RecyclerView
        recyclerViewShops.setHasFixedSize(true);
        recyclerViewShops.setLayoutManager(new LinearLayoutManager(this));
        // Initialize shopList
        shopList = new ArrayList<>();
        allShopsList = new ArrayList<>();
        // Initialize filteredList with all shops
        filteredList = new ArrayList<>();
        filteredList.addAll(shopList);
        Set<Integer> promotedShopPositions = new HashSet<>();
        shopAdapter = new PromoteAdapter(this, filteredList,promotedShopPositions, sharedPreference );
        // shopAdapter = new PromoteAdapter(this,filteredList);

        shopAdapter.setOnItemSelectionChangedListener(new PromoteAdapter.OnItemSelectionChangedListener() {
            @Override
            public void onItemSelectionChanged(int selectedItemCount) {
                updateHeaderOverlay();
            }
        });
        recyclerViewShops.setAdapter(shopAdapter);

//        // Set item click listener
//        shopAdapter.setOnItemClickListener(new PromoteAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//                // Handle item click here
//                PromoteShop clickedShop = shopList.get(position);
//                Intent intent = new Intent(PomoteShop.this, ShopDetails.class);
//                intent.putExtra("image", clickedShop.getUrl());
//                intent.putExtra("ShopName", clickedShop.getShopName());
//                intent.putExtra("Name", clickedShop.getName());
//                intent.putExtra("Address", clickedShop.getAddress());
//                intent.putExtra("contactNumber", clickedShop.getContactNumber());
//                intent.putExtra("PhoneNumber", clickedShop.getPhoneNumber());
//                intent.putExtra("District", clickedShop.getDistrict());
//                intent.putExtra("Taluka", clickedShop.getTaluka());
//                startActivity(intent);
//            }
//        });


        addButton.setOnClickListener(v -> {
            addPromotedShops();
            // Clear selected items and update UI
            shopAdapter.clearSelectedItems();
            shopAdapter.updateSelectedState();
            shopAdapter.notifyDataSetChanged();
            updateHeaderOverlay();
        });




        // Read data from Firebase
        readDataFromFirebase();



        // Initialize RecyclerView and adapter
        recyclerViewShop = findViewById(R.id.promotedshopsdetails);
        recyclerViewShop.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CheckedShopsAdapter(this, promotedShopsList, sharedPreference);
        recyclerViewShop.setAdapter(adapter);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


        // Disable scrolling for both RecyclerViews initially
//        recyclerViewShops.setNestedScrollingEnabled(false);
//        recyclerViewShop.setNestedScrollingEnabled(false);



        userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

    // Create a ValueEventListener to listen for changes in the user's promoted shops
        ValueEventListener userValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);

                    databaseRef.child(contactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                shop = snapshot.getValue(Shop.class);

                            }
                        }

                        @Override
                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                        }
                    });

                    DatabaseReference proRef = databaseRef.child(contactNumber).child("promotedShops");
                    Log.d("TAG", "pro: " + proRef);

                    proRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            promotedShopsList.clear(); // Clear the list to avoid duplicates

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                PromoteShop promotedShop = snapshot.getValue(PromoteShop.class);
                                promotedShopsList.add(promotedShop);
                            }

                            adapter.notifyDataSetChanged(); // Notify the adapter of data changes

                            // Check if the promotedShopsList is empty
                            if (promotedShopsList.isEmpty()) {
                                // If it's empty, hide the RecyclerView and text
                               // promotedshoptext.setVisibility(View.GONE);
                               // recyclerViewShop.setVisibility(View.GONE);
                            } else {
                                // If it's not empty, show the RecyclerView and text
                              //  promotedshoptext.setVisibility(View.VISIBLE);
                                recyclerViewShop.setVisibility(View.VISIBLE);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Handle onCancelled event
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle onCancelled event
            }
        };

// Add the ValueEventListener to userRef
        userRef.addListenerForSingleValueEvent(userValueEventListener);

        SearchView searchView = findViewById(R.id.searchview);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false); // Open the SearchView for search

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        // Set an OnFocusChangeListener to the SearchView
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Open the keyboard when SearchView gains focus
                    showKeyboard(searchView);
                } else {
                    // Clear and close the SearchView when focus is lost (keyboard is dismissed)
                    searchView.setQuery("", false);
                    searchView.clearFocus();
                }
            }
        });



    }

    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }
    private void updateHeaderOverlay() {
        int selectedCount = shopAdapter.getSelectedItemCount();

        promotedShopCount = shopAdapter.getPromotedShopCount();
        headerText.setText(selectedCount + " Selected");



        if (selectedCount >= 0 || promotedShopCount >= 0) {
            // Display the updated promoted shop count
          //  add.setText("Add (" + promotedShopCount + ")");
            addButton.setVisibility(View.VISIBLE);
            // Set a click listener on the cross icon to unselect items and show header
            crossIcon.setOnClickListener(v -> {
                // Call the clearSelectedItems method from the adapter
                shopAdapter.clearSelectedItems();

                // Update selection state and notify adapter
                shopAdapter.updateSelectedState();
                shopAdapter.notifyDataSetChanged();

            });
        } else {

        }
    }

//    private void addPromotedShops() {
//        DatabaseReference promotedShopsRef = FirebaseDatabase.getInstance().getReference("Shop")
//                .child(contactNumber)
//                .child("promotedShops");
//
//        // Add this line alongside your other DatabaseReference declarations
//        DatabaseReference promotedShopsCountRef = FirebaseDatabase.getInstance().getReference("Shop")
//                .child(contactNumber)
//                .child("promotedShopsCount");
//
//
//        // Get the positions of both selected and unselected items
//        List<Integer> selectedItems = shopAdapter.getSelectedItems();
//
//        // Add or update the selected items in the Firebase node
//        for (int position : selectedItems) {
//            PromoteShop selectedShop = shopList.get(position);
//            promotedShopsRef.child(selectedShop.getContactNumber()).setValue(selectedShop);
//        }
//
//        // Update the promoted shop count as a string
//        int newPromotedShopCountStr = promotedShopsList.size() + selectedItems.size();
//        promotedShopsCountRef.setValue(newPromotedShopCountStr);
//
//
//        // Clear selected items, update UI
//        shopAdapter.clearSelectedItems();
//        shopAdapter.updateSelectedState();
//        shopAdapter.notifyDataSetChanged();
//        updateHeaderOverlay();
//    }

    private void addPromotedShops() {
        DatabaseReference promotedShopsRef = FirebaseDatabase.getInstance().getReference("Shop")
                .child(contactNumber)
                .child("promotedShops");


        // Get the positions of both selected and unselected items
        List<Integer> selectedItems = shopAdapter.getSelectedItems();

        // Add or update the selected items in the Firebase node
        for (int position : selectedItems) {
            PromoteShop selectedShop = shopList.get(position);
            promotedShopsRef.child(selectedShop.getContactNumber()).setValue(selectedShop);

                    DatabaseReference promotionRef = FirebaseDatabase.getInstance().getReference("Shop")
                            .child(selectedShop.getContactNumber())
                            .child("hePromoteYou");
                    promotionRef.child(contactNumber).setValue(shop);

//            // Calculate the new count of promoted shops
//            int newCount = promotedShopsList.size() +
            // Calculate the new count of promoted shops for the current shop
            int newCount = selectedShop.getpromotionCount() + 1; // Increment by 1

            // Update the promoted shop count in the "promotionCount" key
            DatabaseReference promotionCountRef = FirebaseDatabase.getInstance().getReference("Shop")
                    .child(selectedShop.getContactNumber())
                    .child("promotionCount");
            promotionCountRef.setValue(newCount);

            // Clear selected items, update UI
            shopAdapter.clearSelectedItems();
            shopAdapter.updateSelectedState();
            shopAdapter.notifyDataSetChanged();
            updateHeaderOverlay();
        }
    }


//    private void addPromotedShops() {
//        DatabaseReference promotedShopsRef = FirebaseDatabase.getInstance().getReference("Shop")
//                .child(contactNumber)
//                .child("promotedShops");
//
//        DatabaseReference promotedShopsCountRef = FirebaseDatabase.getInstance().getReference("Shop");
//
//        List<Integer> selectedItems = shopAdapter.getSelectedItems();
//
//        for (int position : selectedItems) {
//            PromoteShop selectedShop = shopList.get(position);
//            String selectedShopContactNumber = selectedShop.getContactNumber();
//
//            // Add or update the selected shop in the promotedShops node
//            promotedShopsRef.child(selectedShopContactNumber).setValue(selectedShop);
//
//            // Increment the promoted shop count for the selected shop
//            DatabaseReference shopCountRef = promotedShopsCountRef
//                    .child(selectedShopContactNumber)
//                    .child("promotionCount");
//
//            shopCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    // Get the current count as an integer
//                    Integer currentCount = dataSnapshot.getValue(Integer.class);
//
//                    // If the current count is null, set it to 0
//                    if (currentCount == null) {
//                        currentCount = 0;
//                        String count = Integer.toString(currentCount);
//                    }
//
//                    // Increment the count by 1
//                    int newCount = currentCount + 1;
//                    String count = Integer.toString(newCount);
//                    // Update the promoted shop count
//                    shopCountRef.setValue(count);
//
//                    // Clear selected items, update UI
//                    shopAdapter.clearSelectedItems();
//                    shopAdapter.updateSelectedState();
//                    shopAdapter.notifyDataSetChanged();
//                    updateHeaderOverlay();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    // Handle any errors here
//                }
//            });
//        }
//    }

    private void filter(String query) {
        filteredList.clear();

        if (query.isEmpty()) {
            filteredList.addAll(shopList); // Add all items if the query is empty
        } else {
            for (PromoteShop shop : shopList) {
                // Retrieve the values of different fields from the Shop object
                String name = shop.getName().toLowerCase();
                String shopName = shop.getShopName().toLowerCase();
                String address = shop.getAddress().toLowerCase();
                String district = shop.getDistrict();
                String taluka = shop.getTaluka();

                // Check if any field contains the search query
                if (name.contains(query.toLowerCase())
                        || shopName.contains(query.toLowerCase())
                        || address.contains(query.toLowerCase())
                        || district.contains(query)
                        || taluka.contains(query)) {
                    filteredList.add(shop);
                }
            }

        }

        shopAdapter.setFilteredList(filteredList); // Update the adapter's filtered list
        shopAdapter.notifyDataSetChanged(); // Notify the adapter about the data change
    }


    private void readDataFromFirebase() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the existing shop list
                shopList.clear();
                allShopsList.clear();
                filteredList.clear();

                // Process the retrieved data
                for (DataSnapshot shopSnapshot : dataSnapshot.getChildren()) {
                    // Retrieve shop details
                    String name = shopSnapshot.child("name").getValue(String.class);
                    String shopName = shopSnapshot.child("shopName").getValue(String.class);

                    String contactNumber = shopSnapshot.child("contactNumber").getValue(String.class);
                    String address = shopSnapshot.child("address").getValue(String.class);
                    String url = shopSnapshot.child("url").getValue(String.class);
                    String service = shopSnapshot.child("service").getValue(String.class);
                    String taluka = shopSnapshot.child("taluka").getValue(String.class);
                    String district = shopSnapshot.child("district").getValue(String.class);
                    System.out.println("retrive " + taluka);
                  // boolean isShopPromoted = shopSnapshot.child(shopName).exists();

                    int promotedShopCount = shopSnapshot.child("promotionCount").getValue(Integer.class);

                    // Create a Shop object and add it to the shop list
                    PromoteShop shop = new PromoteShop(name, shopName, contactNumber, address, url, service, district, taluka, promotedShopCount);
                    shopList.add(shop);
                    allShopsList.add(shop);
                }
                // Update the filtered list with the original list
                filteredList.clear();
                filteredList.addAll(shopList);

                // Fetch promoted shop positions
                fetchPromotedShopPositions();

                // Notify the adapter about the data change
                shopAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Log.e("FirebaseError", "Failed to read value: " + databaseError.getMessage());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



private void fetchPromotedShopPositions() {
    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                DatabaseReference shopRef = databaseRef.child(contactNumber).child("promotedShops");
                shopRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Set<Integer> promotedShopPositions = new HashSet<>();
                        for (DataSnapshot shopSnapshot : snapshot.getChildren()) {
                            String shopName = shopSnapshot.child("shopName").getValue(String.class);
                            for (int i = 0; i < shopList.size(); i++) {
                                if (shopList.get(i).getShopName().equals(shopName)) {
                                    promotedShopPositions.add(i);
                                    break;
                                }
                            }
                        }

                        shopAdapter.setPromotedShopPositions(promotedShopPositions);
                        shopAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled if needed
                    }
                });
            }
        }

        @Override
        public void onCancelled(DatabaseError error) {
            // Handle onCancelled if needed
        }
    });
}

    @Override
    public void onBackPressed() {
//        if (shopAdapter.getSelectedItemCount() > 0) {
//            // Clear selected items and update UI
//            shopAdapter.clearSelectedItems();
//            shopAdapter.updateSelectedState();
//            shopAdapter.notifyDataSetChanged();
//            updateHeaderOverlay();
//
//            // Handle any other necessary back button actions
//        } else {
//            super.onBackPressed(); // If no items are selected, perform default back action
//        }

        super.onBackPressed(); // If no items are selected, perform default back action
    }


}

