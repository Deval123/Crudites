package com.crudite.apps.utilitaires;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.crudite.apps.R;


public class Standard {
	public static final String MyPREFERENCES = "CruditesPrefs" ;
	public static final String DEVELOPER_KEY = "AIzaSyAzQojchGutJRFxmxSpjUcOi5C3miX6Qgk" ;
	public static final String adresse_serveur = "http://huclcameroon.com/crudites/";
	public static String regId = "";
	public static void saveImage(Context context, Bitmap b,String name){
	    
	    FileOutputStream out;
	    try {
	        out = context.openFileOutput(name, Context.MODE_PRIVATE);
	        b.compress(Bitmap.CompressFormat.JPEG, 90, out);
	        out.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public static boolean appInstalledOrNot(String uri, Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
		}

		return false;
	}
	public static void hideKeyboard(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		//Find the currently focused view, so we can grab the correct window token from it.
		View view = activity.getCurrentFocus();
		//If no view currently has focus, create a new one, just so we can grab a window token from it
		if (view == null) {
			view = new View(activity);
		}
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	public static String getTimeStamp(String dateStr) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timestamp = "";
		Calendar calendar = Calendar.getInstance();
		String today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		today = today.length() < 2 ? "0" + today : today;

		try {
			Date date = format.parse(dateStr);
			SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
			String dateToday = todayFormat.format(date);
			//format = dateToday.equals(today) ? new SimpleDateFormat("HH:mm") : new SimpleDateFormat("EEEE dd LLLL\n à HH:mm");
			format = new SimpleDateFormat("EEEE dd LLLL\n à HH:mm");
			String date1 = format.format(date);
			timestamp = date1.toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timestamp;
	}
	public static String getToken(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(Standard.MyPREFERENCES, Context.MODE_PRIVATE);
		return sharedPreferences.getString("token", "");
	}
	public static String getGCMToken(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(Standard.MyPREFERENCES, Context.MODE_PRIVATE);
		return sharedPreferences.getString("gcm_token", "");
	}
    public static String getAbonnementType(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Standard.MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString("abonnement_type", "");
    }
	public static String getDuration(int seconds){
		int hr = seconds/3600;
		int rem = seconds%3600;
		int mn = rem/60;
		int sec = rem%60;
		String hrStr = (hr<10 ? "0" : "")+hr;
		String mnStr = (mn<10 ? "0" : "")+mn;
		String secStr = (sec<10 ? "0" : "")+sec;

		return (hr>0 ? " "+hrStr+" h" : "") + " "+mnStr+" min";
	}
	public static String strJoin(String[] aArr, String sSep) {
		StringBuilder sbStr = new StringBuilder();
		for (int i = 0, il = aArr.length; i < il; i++) {
			if (i > 0)
				sbStr.append(sSep);
			sbStr.append(aArr[i]);
		}
		return sbStr.toString();
	}
	public static String randomString(int MAX_LENGTH) {
		String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		SecureRandom rnd = new SecureRandom();
		StringBuilder sb = new StringBuilder(MAX_LENGTH);
		for( int i = 0; i < MAX_LENGTH; i++ )
			sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
		return sb.toString();
	}

	public static Typeface getIconFont(Context context){
		return Typeface.createFromAsset(context.getAssets(),"fonts/fontawesome-webfont.ttf");
	}
	public static Typeface getItalicFont(Context context){
		return Typeface.createFromAsset(context.getAssets(),"fonts/font.ttf");
	}
	public static Typeface getCustomFont(Context context){
		return Typeface.createFromAsset(context.getAssets(),"fonts/Lato-Light.ttf");
	}

	public static String getCurrentTimeStamp(){
	    try {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        dateFormat.setTimeZone(TimeZone.getTimeZone("Africa/Douala"));
	        String currentTimeStamp = dateFormat.format(new Date()); // Find todays date

	        return currentTimeStamp;
	    } catch (Exception e) {
	        e.printStackTrace();

	        return null;
	    }
	}
	public static Bitmap drawChoiceTextToBitmap(Context mContext,  Bitmap bitmap,  String mText, String monAvis) {
	    try {
	         Resources resources = mContext.getResources();
	            float scale = resources.getDisplayMetrics().density;

	            Bitmap.Config bitmapConfig =   bitmap.getConfig();
	            // set default bitmap config if none
	            if(bitmapConfig == null) {
	              bitmapConfig = Bitmap.Config.ARGB_8888;
	            }
	            // resource bitmaps are imutable,
	            // so we need to convert it to mutable one
	            bitmap = bitmap.copy(bitmapConfig, true);

	            Canvas canvas = new Canvas(bitmap);
	            // new antialised Paint
	            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	            // text size in pixels
	            paint.setTextSize((int) (14 * scale));
	            paint.setColor(Color.parseColor("#ffffff"));

	            Rect bounds = new Rect();
	            paint.getTextBounds(mText, 0, mText.length(), bounds);

              canvas.drawColor(Color.parseColor("#99000000"));
	          canvas.drawText(mText, 10 * scale, 40 * scale, paint);
	          
	          
	           paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	           paint.setColor(Color.parseColor("#808080"));
	            // text size in pixels
	            paint.setTextSize((int) (15 * scale));

	            bounds = new Rect();
	            paint.getTextBounds(mText, 0, mText.length(), bounds);
	          canvas.drawText("SONDAGE", 10 * scale, 15 * scale, paint);
	          
	          paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	            paint.setColor(Color.parseColor("#808080"));
	            paint.setTextSize((int) (15 * scale));

	            bounds = new Rect();
	            paint.getTextBounds(mText, 0, mText.length(), bounds);
	          canvas.drawText("MON AVIS", 10 * scale, 100 * scale, paint);
	          
	            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	            paint.setColor(Color.parseColor("#ba0000"));
	            // text size in pixels
	            paint.setTextSize((int) (17 * scale));

	            bounds = new Rect();
	            paint.getTextBounds(mText, 0, mText.length(), bounds);
	            canvas.drawText(monAvis, 10 * scale, 120 * scale, paint);
	            
	            paint=new Paint();
	            Bitmap b=BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
	            canvas.drawBitmap(b, 10 * scale, 140 * scale, paint);

	            return bitmap;
	    } catch (Exception e) {
	        return null;
	    }

	 }
	public static boolean setListViewHeightBasedOnItems(ListView listView) {

	    ListAdapter listAdapter = listView.getAdapter();
	    if (listAdapter != null) {

	        int numberOfItems = listAdapter.getCount();

	        // Get total height of all items.
	        int totalItemsHeight = 0;
	        for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
	            View item = listAdapter.getView(itemPos, null, listView);
	            item.measure(0, 0);
	            totalItemsHeight += item.getMeasuredHeight();
	        }

	        // Get total height of all item dividers.
	        int totalDividersHeight = listView.getDividerHeight() * 
	                (numberOfItems - 1);

	        // Set list height.
	        ViewGroup.LayoutParams params = listView.getLayoutParams();
	        params.height = totalItemsHeight + totalDividersHeight;
	        listView.setLayoutParams(params);
	        listView.requestLayout();

	        return true;

	    } else {
	        return false;
	    }

	}
	public static String getCurrentHeure(){
	    try {

	        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	        dateFormat.setTimeZone(TimeZone.getTimeZone("Africa/Douala"));
	        String currentTimeStamp = dateFormat.format(new Date()); // Find todays date

	        return currentTimeStamp;
	    } catch (Exception e) {
	        e.printStackTrace();

	        return null;
	    }
	}
	
	public static void scrollToView(final ScrollView scrollView, final View view) {

	    // View needs a focus
	    view.requestFocus();

	    // Determine if scroll needs to happen
	    final Rect scrollBounds = new Rect();
	    scrollView.getHitRect(scrollBounds);
	    if (!view.getLocalVisibleRect(scrollBounds)) {
	        new Handler().post(new Runnable() {
	            @Override
	            public void run() {
	                scrollView.smoothScrollTo(0, view.getBottom());
	            }
	        });
	    } 
	}
	public static String getCurrentTimeStampComplet(){
	    try {

	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        dateFormat.setTimeZone(TimeZone.getTimeZone("Africa/Douala"));
	        String currentTimeStamp = dateFormat.format(new Date()); // Find todays date

	        return currentTimeStamp;
	    } catch (Exception e) {
	        e.printStackTrace();

	        return null;
	    }
	}
	public static String getUTCTimeStampComplet(){
	    try {

	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	        String currentTimeStamp = dateFormat.format(new Date()); // Find todays date

	        return currentTimeStamp;
	    } catch (Exception e) {
	        e.printStackTrace();

	        return null;
	    }
	}
	public static Bitmap uriToBitmap(Uri selectedFileUri, Context context) {
        try {
            ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
	public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
	}
   
	public static Bitmap getImageBitmap(Context context,String name){
		  
		try{
		    FileInputStream fis = context.openFileInput(name);
		        Bitmap b = BitmapFactory.decodeStream(fis);
		        fis.close();
		        return b;
		    }
		    catch(Exception e){
		    }
		    return null;
	}
	public static String getDayOfWeek(int day, boolean minimal){
		switch (day) {
			case 1:
				if(minimal)
					return "lun";
				else
					return "Lundi";
			case 2:
				if(minimal)
					return "mar";
				else
					return "Mardi";
			case 3:
				if(minimal)
					return "mer";
				else
					return "Mercredi";
			case 4:
				if(minimal)
					return "jeu";
				else
					return "Jeudi";
			case 5:
				if(minimal)
					return "ven";
				else
					return "Vendredi";
			case 6:
				if(minimal)
					return "sam";
				else
					return "Samedi";
			case 7:
				if(minimal)
					return "dim";
				else
					return "Dimanche";

			default:
				return "";
		}
	}
	public static void deleteBitmapImage(Context context,String name){
		  context.deleteFile(name);
	}
	
}
