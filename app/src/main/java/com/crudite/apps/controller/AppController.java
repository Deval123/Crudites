package com.crudite.apps.controller;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageCache;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.crudite.apps.R;
import com.crudite.apps.models.BaseModel;
import com.crudite.apps.models.DBHelper;
import com.crudite.apps.utilitaires.FontsOverride;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;


public class AppController extends Application {

	public static final String TAG = AppController.class.getSimpleName();

	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	LruBitmapCache mLruBitmapCache;

	  private static AppController sApp;
	private static AppController mInstance;
	private MyPreferenceManager pref;
	@Override
	public void onCreate() {
		super.onCreate();
	    sApp = this;
		mInstance = this;
		FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/OpenSans-Regular.ttf");
		FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/OpenSans-Regular.ttf");
		FontsOverride.setDefaultFont(this, "SERIF", "fonts/OpenSans-Regular.ttf");
		FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/OpenSans-Regular.ttf");
		MultiDex.install(this);
		BaseModel.dbHelper = new DBHelper(getApplicationContext());
		
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
		.resetViewBeforeLoading(false)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.imageScaleType(ImageScaleType.EXACTLY)
		.showImageForEmptyUri(R.mipmap.ic_launcher).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(
				defaultOptions).build();
		
		com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
	}
	public MyPreferenceManager getPrefManager() {
        if (pref == null) {
            pref = new MyPreferenceManager(this);
        }

        return pref;
    }
	public static SharedPreferences pref() {
	    return sApp.getSharedPreferences("sharad_toro_pref", Context.MODE_PRIVATE);
	  }

	  public static String packageName() {
	    return sApp.getPackageName();
	  }
	public static synchronized AppController getInstance() {
		return mInstance;
	}
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}
	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			getLruBitmapCache();
			mImageLoader = new ImageLoader(this.mRequestQueue, new ImageCache() {
				@Override
				public Bitmap getBitmap(String url) {
					return null;
				}

				@Override
				public void putBitmap(String url, Bitmap bitmap) {

				}

				@Override
				public void invalidateBitmap(String url) {

				}

				@Override
				public void clear() {

				}
			});
		}

		return this.mImageLoader;
	}

	public LruBitmapCache getLruBitmapCache() {
		if (mLruBitmapCache == null)
			mLruBitmapCache = new LruBitmapCache();
		return this.mLruBitmapCache;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}
	public <T> void addToRequestQueue(Request<T> req, String tag, boolean cache) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req).setShouldCache(cache);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}
}
