package com.example.androidintegration.coachmark;

import android.content.Context;
import android.graphics.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.example.androidintegration.R;
import com.example.androidintegration.databinding.CoachmarkitemCoachmarkBinding;

public class CoachMarkOverlay extends FrameLayout {

    public Builder mBuilder;
    private Context mContext;
    private Bitmap mBaseBitmap;
    private Canvas mLayer;
    private final Paint mOverlayTintPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mOverlayTransparentPaint = new Paint();

    private CoachmarkitemCoachmarkBinding binding;

    public CoachMarkOverlay(Context context) {
        super(context);
        this.mContext = context;
        binding = CoachmarkitemCoachmarkBinding.inflate(LayoutInflater.from(mContext), this, true);
        init();
    }

    public CoachMarkOverlay(Context context, Builder builder) {
        super(context);
        this.mContext = context;
        binding = CoachmarkitemCoachmarkBinding.inflate(LayoutInflater.from(mContext), this, true);
        init();
        mBuilder = builder;
    }

    private void init() {
        binding.view.setVisibility(View.VISIBLE);
        binding.view.setOnClickListener(null);
        this.setWillNotDraw(false);
        mOverlayTransparentPaint.setColor(Color.TRANSPARENT);
        mOverlayTransparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBuilder != null && mBuilder.getOverlayClickListener() != null) {
                    mBuilder.getOverlayClickListener().onOverlayClick(CoachMarkOverlay.this, binding);
                }
            }
        });

        binding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBuilder != null && mBuilder.getSkipClickListener() != null) {
                    mBuilder.getSkipClickListener().onSkipClick(CoachMarkOverlay.this);
                    binding.view.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawOverlayTint();
        drawTransparentOverlay();
        if (mBaseBitmap != null) {
            canvas.drawBitmap(mBaseBitmap, 0f, 0f, null);
        }
        super.onDraw(canvas);
    }

    private void drawOverlayTint() {
        if (mBuilder != null) {
            mBaseBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            if (mBaseBitmap != null) {
                mLayer = new Canvas(mBaseBitmap);
                mOverlayTintPaint.setColor(mBuilder.getOverlayColor());
                int alpha = mBuilder.getOverlayOpacity();
                mOverlayTintPaint.setAlpha(alpha);
                mLayer.drawRect(new Rect(0, 0, getWidth(), getHeight()), mOverlayTintPaint);
            }
        }
    }

    private void drawTransparentOverlay() {
        if (mBuilder != null && mLayer != null) {
            Rect targetViewSize = new Rect();
            if (mBuilder.getOverlayTargetView() != null) {
                mBuilder.getOverlayTargetView().getGlobalVisibleRect(targetViewSize);
            } else {
                targetViewSize.set(mBuilder.getOverlayTargetCoordinates());
            }
            
            targetViewSize.left -= mBuilder.getOverlayTransparentPadding().left;
            targetViewSize.top -= mBuilder.getOverlayTransparentPadding().top;
            targetViewSize.right += mBuilder.getOverlayTransparentPadding().right;
            targetViewSize.bottom += mBuilder.getOverlayTransparentPadding().bottom;

            targetViewSize.left += mBuilder.getOverlayTransparentMargin().left;
            targetViewSize.top += mBuilder.getOverlayTransparentMargin().top;
            targetViewSize.right += mBuilder.getOverlayTransparentMargin().right;
            targetViewSize.bottom += mBuilder.getOverlayTransparentMargin().bottom;

            int layerWidth = mLayer.getWidth();
            int layerHeight = mLayer.getHeight();
            
            if (mBuilder.getOverlayTransparentShape() == Shape.BOX) {
                mLayer.drawRoundRect(
                    new RectF(targetViewSize),
                    mBuilder.getOverlayTransparentCornerRadius(),
                    mBuilder.getOverlayTransparentCornerRadius(),
                    mOverlayTransparentPaint
                );

                int halfWidthScreen = layerWidth / 2;
                int halfHeightScreen = layerHeight / 2;

                GravityIn startMiddleEnd = positionLeftMiddleRight(
                    targetViewSize.left,
                    targetViewSize.right,
                    halfWidthScreen
                );
                GravityIn topBottom = positionTopBottom(targetViewSize.bottom, halfHeightScreen);

                binding.txvTitle.setText(mBuilder.getTitle());
                binding.txvSubTitle.setText(mBuilder.getSubTitle());
                
                if (mBuilder.getMax() == 0) {
                    binding.txvLimit.setVisibility(View.GONE);
                } else {
                    binding.txvLimit.setText(mContext.getString(
                        R.string.coachmarkLabel_value_limit,
                        String.valueOf(mBuilder.getLimit() + 1),
                        String.valueOf(mBuilder.getMax() + 1)
                    ));
                    binding.txvLimit.setVisibility(View.VISIBLE);
                }
                
                binding.btnNext.setText(mBuilder.getTextBtnPositive());
                binding.btnNext.setBackgroundColor(mBuilder.getTextBtnPositiveBGColor());
                binding.btnNext.setTextColor(mBuilder.getTextBtnPositiveTextColor());
                
                if (mBuilder.getSkipBtn() == null) {
                    binding.btnSkip.setVisibility(View.GONE);
                } else {
                    binding.btnSkip.setText(mBuilder.getSkipBtn());
                    binding.btnSkip.setBackgroundColor(mBuilder.getSkipBtnBGColor());
                    binding.btnSkip.setTextColor(mBuilder.getSkipBtnTextColor());
                }
                
                if (mBuilder.getGravity() == Gravity.NULL) {
                    // automatic
                    if (topBottom == GravityIn.TOP) {
                        if (startMiddleEnd == GravityIn.START) {
                            new GravityHelper.EndBottomGravity().gravity(binding, targetViewSize);
                        } else if (startMiddleEnd == GravityIn.CENTER) {
                            new GravityHelper.BottomGravity().gravity(binding, targetViewSize);
                        } else if (startMiddleEnd == GravityIn.END) {
                            new GravityHelper.StartBottomGravity().gravity(binding, targetViewSize);
                        }
                    } else if (topBottom == GravityIn.BOTTOM) {
                        if (startMiddleEnd == GravityIn.START) {
                            new GravityHelper.EndTopGravity().gravity(binding, targetViewSize);
                        } else if (startMiddleEnd == GravityIn.CENTER) {
                            new GravityHelper.TopGravity().gravity(binding, targetViewSize);
                        } else if (startMiddleEnd == GravityIn.END) {
                            new GravityHelper.StartTopGravity().gravity(binding, targetViewSize);
                        }
                    } else if (topBottom == GravityIn.CENTER) {
                        if (startMiddleEnd == GravityIn.CENTER) {
                            new GravityHelper.BottomGravity().gravity(binding, targetViewSize);
                        }
                    }
                } else {
                    // manual
                    switch (mBuilder.getGravity()) {
                        case TOP:
                            new GravityHelper.TopGravity().gravity(binding, targetViewSize);
                            break;
                        case BOTTOM:
                            new GravityHelper.BottomGravity().gravity(binding, targetViewSize);
                            break;
                        case START_TOP:
                            new GravityHelper.StartTopGravity().gravity(binding, targetViewSize);
                            break;
                        case END_TOP:
                            new GravityHelper.EndTopGravity().gravity(binding, targetViewSize);
                            break;
                        case END_BOTTOM:
                            new GravityHelper.EndBottomGravity().gravity(binding, targetViewSize);
                            break;
                        case START_BOTTOM:
                            new GravityHelper.StartBottomGravity().gravity(binding, targetViewSize);
                            break;
                    }
                }
            }
        }
    }

    private GravityIn positionLeftMiddleRight(int left, int right, int halfWidth) {
        if (halfWidth >= left && halfWidth <= right) {
            return GravityIn.CENTER;
        } else if (left >= 1 && left < halfWidth) {
            return GravityIn.START;
        } else {
            return GravityIn.END;
        }
    }

    private GravityIn positionTopBottom(int bottom, int halfHeight) {
        if (bottom >= 1 && bottom < halfHeight) {
            return GravityIn.TOP;
        } else {
            return GravityIn.BOTTOM;
        }
    }

    public void show(ViewGroup root) {
        root.addView(this);
    }

    public static class Builder {
        private Context mContext;
        private View mOverlayTargetView;
        private int mOverlayColor = Color.BLACK;
        private int mOverlayOpacity = 150;
        private Shape mOverlayTransparentShape = Shape.BOX;
        private float mOverlayTransparentCornerRadius;
        private Rect mOverlayTransparentMargin = new Rect();
        private Rect mOverlayTransparentPadding = new Rect();
        private OverlayClickListener mOverlayClickListener;
        private SkipClickListener mSkipClickListener;
        private Rect mTargetCoordinates = new Rect();
        private int mBaseTabPosition = -1;
        private String mSetTitle = "";
        private String mSetSubTitle = "";
        private int mLimit = 0;
        private String mTextBtnPositive = "";
        private String mSkipBtn;
        private Gravity mGravity = Gravity.NULL;
        private int mMax = 0;

        private Integer mTextBtnPositiveBGColor;
        private Integer mTextBtnPositiveTextColor;
        private Integer mSkipBtnBGColor;
        private Integer mSkipBtnTextColor;

        public Builder(Context context) {
            this.mContext = context;
            this.mOverlayTransparentCornerRadius = context.getResources().getDimension(R.dimen.margin_07);
        }

        public View getOverlayTargetView() { return mOverlayTargetView; }
        public int getOverlayColor() { return mOverlayColor; }
        public int getOverlayOpacity() { return mOverlayOpacity; }
        public Shape getOverlayTransparentShape() { return mOverlayTransparentShape; }
        public float getOverlayTransparentCornerRadius() { return mOverlayTransparentCornerRadius; }
        public int getTabPosition() { return mBaseTabPosition; }
        public Rect getOverlayTransparentMargin() { return mOverlayTransparentMargin; }
        public Rect getOverlayTransparentPadding() { return mOverlayTransparentPadding; }
        public Rect getOverlayTargetCoordinates() { return mTargetCoordinates; }
        public OverlayClickListener getOverlayClickListener() { return mOverlayClickListener; }
        public SkipClickListener getSkipClickListener() { return mSkipClickListener; }
        public String getTitle() { return mSetTitle; }
        public String getSubTitle() { return mSetSubTitle; }
        public int getLimit() { return mLimit; }
        public String getTextBtnPositive() { return mTextBtnPositive; }
        public String getSkipBtn() { return mSkipBtn; }
        public Gravity getGravity() { return mGravity; }
        public int getMax() { return mMax; }

        public int getTextBtnPositiveBGColor() { return mTextBtnPositiveBGColor; }
        public int getSkipBtnBGColor() { return mSkipBtnBGColor; }
        public int getTextBtnPositiveTextColor() { return mTextBtnPositiveTextColor; }
        public int getSkipBtnTextColor() { return mSkipBtnTextColor; }

        public Builder setOverlayTargetCoordinates(Rect coordinates) {
            mTargetCoordinates.set(coordinates);
            return this;
        }

        public Builder setOverlayTargetView(View view) {
            mOverlayTargetView = view;
            return this;
        }

        public Builder setTabPosition(int position) {
            mBaseTabPosition = position;
            return this;
        }

        public Builder setOverlayTransparentPadding(int left, int top, int right, int bottom) {
            mOverlayTransparentPadding.left = Math.round(Utils.dpToPx(mContext, left));
            mOverlayTransparentPadding.top = Math.round(Utils.dpToPx(mContext, top));
            mOverlayTransparentPadding.right = Math.round(Utils.dpToPx(mContext, right));
            mOverlayTransparentPadding.bottom = Math.round(Utils.dpToPx(mContext, bottom));
            return this;
        }

        public Builder setOverlayClickListener(OverlayClickListener listener) {
            mOverlayClickListener = listener;
            return this;
        }

        public Builder setSkipClickListener(SkipClickListener listener) {
            mSkipClickListener = listener;
            return this;
        }

        public Builder setInfoText(String title, String subTitle, int limit) {
            mSetTitle = title;
            mSetSubTitle = subTitle;
            mLimit = limit;
            return this;
        }

        public Builder setTextBtnPositive(String text, Integer bgColor, Integer textColor) {
            mTextBtnPositive = text;
            mTextBtnPositiveBGColor = bgColor;
            mTextBtnPositiveTextColor = textColor;
            return this;
        }

        public Builder setTextBtnPositive(String text) {
            return setTextBtnPositive(text, null, null);
        }

        public Builder setSkipBtn(String text, Integer bgColor, Integer textColor) {
            mSkipBtn = text;
            mSkipBtnBGColor = bgColor;
            mSkipBtnTextColor = textColor;
            return this;
        }

        public Builder setSkipBtn(String text) {
            return setSkipBtn(text, null, null);
        }

        public Builder setGravity(Gravity gravity) {
            mGravity = gravity;
            return this;
        }

        public CoachMarkOverlay build(int max) {
            mMax = max;
            return new CoachMarkOverlay(mContext, this);
        }
    }
}