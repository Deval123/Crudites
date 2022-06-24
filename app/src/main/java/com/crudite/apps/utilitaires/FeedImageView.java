package com.crudite.apps.utilitaires;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/**
 * Created by desmond on 13/7/14.
 */
public class FeedImageView extends ImageView {

    private String mUrl;
    private int mDefaultImageId;
    private ImageLoader mImageLoader;

    public interface ResponseObserver {
        public void onError();
        public void onSuccess();
    }

    public FeedImageView(Context context) {
        this(context, null);
    }

    public FeedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FeedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setResponseObserver(ResponseObserver observer) {
    }


    public void setImageUrl(String url, ImageLoader imageLoader) {
        mUrl = url;
        mImageLoader = imageLoader;
        //The URL might have changed, see if we need to load it
        loadImageIfNecessary(false);
    }

    /**
     * Sets the default image resource ID to be sued for this view until the
     * attempt to load it completes
     * @param defaultImage
     */
    public void setDefaultImageResId(int defaultImage) {
        mDefaultImageId = defaultImage;
    }

    /**
     * Sets the error image resource ID to be used for this view in the event
     * that the image requested fails to load
     * @param errorImage
     */
    public void setErrorImageResId(int errorImage) {
    }

    /**
     * Loads the image for the view if it isn't already loaded
     *
     * @param isInLayoutPass True if this was invoked from a
     *                       layout pass, false otherwise
     */
    private void loadImageIfNecessary(final boolean isInLayoutPass) {
        final int width = getWidth();
        int height = getHeight();

        boolean isFullyWrapContent = getLayoutParams() != null
                && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT
                && getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT;

        //if the view's bounds aren't known yet, and this is not a
        //wrap-content/wrap-content view, hold off on loading the image
        if (width == 0 && height == 0 && !isFullyWrapContent)
            return;

        //If the URL to be loaded in this view is empty, cancel any old
        //requests and clear the currently loaded image.
        if (TextUtils.isEmpty(mUrl)) {
            setDefaultImageOrNull();
            return;
        }
        
        //The pre-existing content of this view didn't match the current Url
        //Load the new image from the network
        mImageLoader.loadImage(mUrl, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                int bWidth = 0, bHeight = 0;
                if (loadedImage != null) {
                    setImageBitmap(loadedImage);
                    bWidth = loadedImage.getWidth();
                    bHeight = loadedImage.getHeight();
                    adjustImageAspect(bWidth, bHeight);

                } else if (mDefaultImageId != 0) {
                    setImageResource(mDefaultImageId);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    private void setDefaultImageOrNull() {
        if (mDefaultImageId != 0) {
            setImageResource(mDefaultImageId);
        } else {
            setImageBitmap(null);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        loadImageIfNecessary(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    /**
     * Key different between this imageView and NetworkImageView provided by Volley
     *
     * Adjusting imageView height
     */
    private void adjustImageAspect(int bWidth, int bHeight) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();

        if (bWidth == 0 || bHeight == 0)
            return;

        int sWidth = getWidth();
        int new_height = 0;
        new_height = sWidth * bHeight / bWidth;
        params.width = sWidth;
        params.height = new_height;
        setLayoutParams(params);
    }
}