package com.spark.swarajyabiz;

import java.util.List;

public class Event {
    private String eventName;
    private String currentDate;
    private List<String> imageUrls;

    public Event(String eventName, String currentDate, List<String> imageUrls) {
        this.eventName = eventName;
        this.currentDate = currentDate;
        this.imageUrls = imageUrls;
    }

    public String getEventName() {
        return eventName;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    // Add this method to get a single image URL based on position
    // Add this method to get a single image URL based on position
    public String getImageUrl(int position) {
        if (imageUrls != null && position >= 0 && position < imageUrls.size()) {
            return imageUrls.get(position);
        } else {
            return null;
        }
    }

}
