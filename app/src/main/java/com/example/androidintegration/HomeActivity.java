package com.example.androidintegration;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.CTInboxListener;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.clevertap.android.sdk.inbox.CTInboxMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements CTInboxListener, DisplayUnitListener {

    private CleverTapAPI clevertapDefaultInstance;
    private Handler autoScrollHandler;
    private Runnable autoScrollRunnable;
    private List<String> imageUrls = new ArrayList<>();
    private List<CTInboxMessage> inboxMessages = new ArrayList<>();

    private ViewPager2 carouselViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        carouselViewPager = findViewById(R.id.viewPager);

        clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(this);

        if (clevertapDefaultInstance != null) {
            clevertapDefaultInstance.setCTNotificationInboxListener(this);
            clevertapDefaultInstance.initializeInbox();
            clevertapDefaultInstance.setDisplayUnitListener(this);
        }

        // Custom Event
        Button customEvent = findViewById(R.id.customEvent);
        customEvent.setOnClickListener(v -> {
            clevertapDefaultInstance.pushEvent("Product Viewed");
            Toast.makeText(this, "Product Viewed", Toast.LENGTH_SHORT).show();
        });

        // Charged Event
        Button chargedEvent = findViewById(R.id.chargedEvent);
        chargedEvent.setOnClickListener(v -> {
            HashMap<String, Object> chargeDetails = new HashMap<>();
            chargeDetails.put("Amount", 300);
            chargeDetails.put("Payment Mode", "Credit card");
            chargeDetails.put("Charged ID", 24052013);

            ArrayList<HashMap<String, Object>> items = new ArrayList<>();
            HashMap<String, Object> item1 = new HashMap<>();
            item1.put("Product category", "books");
            item1.put("Book name", "The Millionaire next door");
            item1.put("Quantity", 1);
            items.add(item1);

            clevertapDefaultInstance.pushChargedEvent(chargeDetails, items);
            Toast.makeText(this, "Charged Event", Toast.LENGTH_SHORT).show();
        });

        Button nativeButton = findViewById(R.id.nativeDisplayButton);
        nativeButton.setOnClickListener(v -> clevertapDefaultInstance.pushEvent("Native Event"));

        /* Button customInbox = findViewById(R.id.customInboxButton);
        customInbox.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CustomInboxActivity.class);
            startActivity(intent);
        }); */

        Button spotlightsButton = findViewById(R.id.spotlightsButton);
        spotlightsButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SpotlightsActivity.class);
            startActivity(intent);
        });

        Button coachmark_page = findViewById(R.id.coachmark_page);
        coachmark_page.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CoachMarkActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public void inboxDidInitialize() {
        Button appInbox = findViewById(R.id.appInboxButton);
        appInbox.setOnClickListener(v -> clevertapDefaultInstance.showAppInbox());
    }

    @Override
    public void inboxMessagesDidUpdate() {
//        loadInboxMessages(); // Reload messages
    }


    // Custom App Inbox
    /*private void setupInboxRecyclerView(List<CTInboxMessage> messages) {
        RecyclerView recyclerView = findViewById(R.id.inboxRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        InboxAdapter adapter = new InboxAdapter(this, messages);
        recyclerView.setAdapter(adapter);
    }

    private void loadInboxMessages() {
        inboxMessages = clevertapDefaultInstance.getAllInboxMessages();
        if (!inboxMessages.isEmpty()) {
            Log.d("AppInbox", "Inbox messages fetched: " + inboxMessages.size());
            setupInboxRecyclerView(inboxMessages);
        } else {
            Log.e("AppInbox", "No inbox messages found");
        }
    }*/

    // Native Display
    @Override
    public void onDisplayUnitsLoaded(ArrayList<CleverTapDisplayUnit> units) {
        if (units != null && !units.isEmpty()) {
            // Get all the images in carousel
            for (CleverTapDisplayUnit unit : units) {
                if (unit.getContents() != null && !unit.getContents().isEmpty()) {
                    for (int i = 0; i < unit.getContents().size(); i++) {
                        String imageUrl = unit.getContents().get(i).getMedia();
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            imageUrls.add(imageUrl);
                            Log.d("Carousel", "Loaded Image URL: " + imageUrl);
                        }
                    }
                }
            }

            if (!imageUrls.isEmpty()) {
                Log.d("Carousel", "Loaded image URLs: " + imageUrls);
                setupViewPager(imageUrls);
            } else {
                Log.e("Carousel", "No valid image URLs found");
            }
        }
    }

    private void setupViewPager(List<String> imageUrls) {
        CarouselAdapter adapter = new CarouselAdapter(this, imageUrls);
        carouselViewPager.setAdapter(adapter);
        carouselViewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        setupAutoScroll(imageUrls.size());
    }

    private void setupAutoScroll(int itemCount) {
        if (autoScrollHandler != null && autoScrollRunnable != null) {
            autoScrollHandler.removeCallbacks(autoScrollRunnable);
        }
        autoScrollHandler = new Handler(Looper.getMainLooper());
        autoScrollRunnable = new Runnable() {
            int currentPage = 0;
            @Override
            public void run() {
                if (currentPage >= itemCount) {
                    currentPage = 0;
                }
                carouselViewPager.setCurrentItem(currentPage++, true);
                autoScrollHandler.postDelayed(this, 5000); // ✅ 5 seconds interval
            }
        };
        autoScrollHandler.postDelayed(autoScrollRunnable, 5000);
    }

    @Override
    protected void onDestroy() {
        if (autoScrollHandler != null && autoScrollRunnable != null) {
            autoScrollHandler.removeCallbacks(autoScrollRunnable);
        }
        super.onDestroy();
    }
}
