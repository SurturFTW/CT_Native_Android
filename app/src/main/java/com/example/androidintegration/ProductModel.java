package com.example.androidintegration;

public class ProductModel {
    private String imageUrl;
    private String title;
    private String description;
    private float rating;
    private int reviewCount;
    private double price;
    private double originalPrice;
    private boolean hasDiscount;

    public ProductModel(String imageUrl, String title, String description, 
                       float rating, int reviewCount, double price, 
                       double originalPrice, boolean hasDiscount) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.price = price;
        this.originalPrice = originalPrice;
        this.hasDiscount = hasDiscount;
    }

    // Simple constructor for just image URL (for compatibility)
    public ProductModel(String imageUrl) {
        this.imageUrl = imageUrl;
        
        // Sample product titles for Amazon-style display
        String[] sampleTitles = {
            "Premium Wireless Bluetooth Headphones with Noise Cancellation",
            "Smart Watch with Heart Rate Monitor and GPS",
            "Portable Bluetooth Speaker - Waterproof Design",
            "USB-C Fast Charging Cable - 6 Feet Long",
            "Wireless Phone Charger Stand - Qi Compatible",
            "Laptop Stand with Adjustable Height and Angle",
            "Ergonomic Wireless Mouse with Precision Tracking",
            "Mechanical Gaming Keyboard with RGB Backlight"
        };
        
        String[] descriptions = {
            "High-quality audio experience with premium sound",
            "Track your fitness and health metrics all day",
            "Perfect for outdoor adventures and parties",
            "Fast and reliable charging for all devices",
            "Convenient wireless charging for modern phones",
            "Improve your workspace ergonomics instantly",
            "Comfortable grip for extended computer use",
            "Enhanced gaming experience with tactile feedback"
        };
        
        int randomIndex = (int)(Math.random() * sampleTitles.length);
        this.title = sampleTitles[randomIndex];
        this.description = descriptions[randomIndex];
        this.rating = 4.0f + (float)(Math.random() * 1.0f); // Random rating between 4.0-5.0
        this.reviewCount = (int)(Math.random() * 5000) + 100; // Random reviews 100-5100
        this.price = 20.0 + (Math.random() * 200.0); // Random price $20-220
        this.originalPrice = this.price + (Math.random() * 50.0); // Original price higher
        this.hasDiscount = Math.random() > 0.3; // 70% chance of discount (Amazon style)
    }

    // Getters
    public String getImageUrl() { return imageUrl; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public float getRating() { return rating; }
    public int getReviewCount() { return reviewCount; }
    public double getPrice() { return price; }
    public double getOriginalPrice() { return originalPrice; }
    public boolean hasDiscount() { return hasDiscount; }

    // Setters
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setRating(float rating) { this.rating = rating; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }
    public void setPrice(double price) { this.price = price; }
    public void setOriginalPrice(double originalPrice) { this.originalPrice = originalPrice; }
    public void setHasDiscount(boolean hasDiscount) { this.hasDiscount = hasDiscount; }
}
