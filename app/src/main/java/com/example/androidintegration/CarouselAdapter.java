package com.example.androidintegration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RatingBar;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ArrayList;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.ViewHolder> {
    private List<String> imageUrls;
    private List<ProductModel> products;
    private Context context;
    private boolean isAmazonStyle;

    // Original constructor for image URLs
    public CarouselAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.isAmazonStyle = false;
        // Convert image URLs to ProductModel objects for Amazon style
        this.products = new ArrayList<>();
        if (imageUrls != null) {
            for (String imageUrl : imageUrls) {
                products.add(new ProductModel(imageUrl));
            }
        }
    }

    // New constructor for Amazon-style products
    public CarouselAdapter(Context context, List<ProductModel> products, boolean isAmazonStyle) {
        this.context = context;
        this.products = products;
        this.isAmazonStyle = isAmazonStyle;
        // Extract image URLs for compatibility
        this.imageUrls = new ArrayList<>();
        if (products != null) {
            for (ProductModel product : products) {
                imageUrls.add(product.getImageUrl());
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (isAmazonStyle) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_amazon_product, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carousel, parent, false);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            view.setLayoutParams(layoutParams);
        }
        return new ViewHolder(view, isAmazonStyle);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (isAmazonStyle && products != null && position < products.size()) {
            ProductModel product = products.get(position);
            holder.bindProduct(product, context);
        } else if (imageUrls != null && position < imageUrls.size()) {
            String imageUrl = imageUrls.get(position);
            Glide.with(context).load(imageUrl).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        if (isAmazonStyle && products != null) {
            return products.size();
        }
        return imageUrls != null ? imageUrls.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        // Amazon-style views
        ImageView productImageView;
        TextView productTitleTextView;
        TextView productDescriptionTextView;
        TextView brandTextView;
        RatingBar productRatingBar;
        TextView reviewCountTextView;
        TextView currencySymbol;
        TextView priceTextView;
        TextView priceDecimal;
        TextView originalPriceTextView;
        TextView shippingInfoTextView;
        Button addToCartButton;
        ImageView favoriteIcon;
        ImageView shareIcon;
        
        boolean isAmazonStyle;

        ViewHolder(@NonNull View itemView, boolean isAmazonStyle) {
            super(itemView);
            this.isAmazonStyle = isAmazonStyle;
            
            if (isAmazonStyle) {
                // Amazon-style views
                productImageView = itemView.findViewById(R.id.productImageView);
                productTitleTextView = itemView.findViewById(R.id.productTitleTextView);
                brandTextView = itemView.findViewById(R.id.brandTextView);
                productRatingBar = itemView.findViewById(R.id.productRatingBar);
                reviewCountTextView = itemView.findViewById(R.id.reviewCountTextView);
                currencySymbol = itemView.findViewById(R.id.currencySymbol);
                priceTextView = itemView.findViewById(R.id.priceTextView);
                priceDecimal = itemView.findViewById(R.id.priceDecimal);
                originalPriceTextView = itemView.findViewById(R.id.originalPriceTextView);
                shippingInfoTextView = itemView.findViewById(R.id.shippingInfoTextView);
                addToCartButton = itemView.findViewById(R.id.addToCartButton);
                favoriteIcon = itemView.findViewById(R.id.favoriteIcon);
                shareIcon = itemView.findViewById(R.id.shareIcon);
            } else {
                // Carousel view
                imageView = itemView.findViewById(R.id.carouselImageView);
            }
        }

        void bindProduct(ProductModel product, Context context) {
            if (!isAmazonStyle) return;

            // Load product image
            Glide.with(context)
                    .load(product.getImageUrl())
                    .into(productImageView);

            // Set product details
            productTitleTextView.setText(product.getTitle());
            brandTextView.setText("Premium Brand");
            productRatingBar.setRating(product.getRating());
            reviewCountTextView.setText("(" + product.getReviewCount() + ")");
            
            // Format price display like Amazon (split dollars and cents)
            String priceStr = String.format("%.2f", product.getPrice());
            String[] priceParts = priceStr.split("\\.");
            priceTextView.setText(priceParts[0]);
            priceDecimal.setText(priceParts[1]);

            // Handle original price and discount
            if (product.hasDiscount()) {
                originalPriceTextView.setVisibility(View.VISIBLE);
                originalPriceTextView.setText(String.format("$%.2f", product.getOriginalPrice()));
                originalPriceTextView.setPaintFlags(
                    originalPriceTextView.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                );
            } else {
                originalPriceTextView.setVisibility(View.GONE);
            }

            // Add to cart button click listener
            addToCartButton.setOnClickListener(v -> {
                android.widget.Toast.makeText(context, "Added " + product.getTitle() + " to cart", 
                    android.widget.Toast.LENGTH_SHORT).show();
            });

            // Favorite icon click listener
            favoriteIcon.setOnClickListener(v -> {
                android.widget.Toast.makeText(context, "Added to favorites", 
                    android.widget.Toast.LENGTH_SHORT).show();
            });

            // Share icon click listener
            shareIcon.setOnClickListener(v -> {
                android.widget.Toast.makeText(context, "Share feature", 
                    android.widget.Toast.LENGTH_SHORT).show();
            });
        }
    }
}




