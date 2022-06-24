package com.crudite.apps.utilitaires;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crudite.apps.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class AddPictureLayout implements CropImageView.OnSetImageUriCompleteListener, CropImageView.OnGetCroppedImageCompleteListener{
	
	private AppCompatActivity context;
	SharedPreferences sharedpreferences;
    private CropImageViewOptions mCropImageViewOptions = new CropImageViewOptions();
	private CropImageView mCropImageView;
	private ShimmerFrameLayout mShimmerViewContainer;
	private Button bout_suivant;
	private LinearLayout relative_Cropper;
	private Button bout_retour;
	private ShimmerFrameLayout mShimmerViewContainer2;
	private ImageView imageSelectEtape;
	private SlidingUpPanelLayout mLayout;
	public static Bitmap bitmap=null;
	public AddPictureLayout() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AddPictureLayout(AppCompatActivity context){
		this.context = context;
		sharedpreferences = context.getSharedPreferences(Standard.MyPREFERENCES, Context.MODE_PRIVATE);
		bitmap = null;
		onCreateView();
	}
	public void setImageReceiver(ImageView image){
		this.imageSelectEtape = image;
	}public void setBitmapReceiver(Bitmap image){
		this.bitmap = image;
	}
	public void setLayoutReceiver(SlidingUpPanelLayout image){
		this.mLayout = image;
	}
    public void onCreateView() {
		
        mCropImageViewOptions.aspectRatio = new Pair<>(1, 1);
        mCropImageViewOptions.guidelines = CropImageView.Guidelines.ON;
        mCropImageViewOptions.fixAspectRatio = false;
        mCropImageView = (CropImageView) context.findViewById(R.id.cropImageView);
        setCropImageViewOptions(mCropImageViewOptions);
        mCropImageView.setOnSetImageUriCompleteListener(this);
        mCropImageView.setOnGetCroppedImageCompleteListener(this);
        mCropImageView.setImageResource(R.drawable.camera_icon);

		LinearLayout rotate = (LinearLayout) context.findViewById(R.id.rotate);
        rotate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCropImageView.rotateImage(90);
			}
		});
        
        relative_Cropper = (LinearLayout) context.findViewById(R.id.relativeCropper);


		LinearLayout crop = (LinearLayout) context.findViewById(R.id.rogner);

		ImageView load = (ImageView) context.findViewById(R.id.load);
        
        load.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isEndcaller = true;
				mCropImageView.getCroppedImageAsync();
            	relative_Cropper.setVisibility(View.VISIBLE);
			}
		});
        
        crop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
            	relative_Cropper.setVisibility(View.VISIBLE);
				mLayout.setPanelState(PanelState.HIDDEN);
				CropImage.startPickImageActivity(context);
			}
		});
	}
    public boolean isEndcaller=true;
    public boolean effect=false;
	public void setImageUri(Uri imageUri) {
        mCropImageView.setImageUriAsync(imageUri);
    }
	public void setCropImageViewOptions(CropImageViewOptions options) {
        mCropImageView.setGuidelines(options.guidelines);
        mCropImageView.setAspectRatio(options.aspectRatio.first, options.aspectRatio.second);
        mCropImageView.setFixedAspectRatio(options.fixAspectRatio);
    }
	@Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (error == null) {
            //Toast.makeText(context, "Image load successful", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("AIC", "Failed to load image by URI", error);
            Toast.makeText(context, "Image load failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
	
	Bitmap bitmapEffect;
    @Override
    public void onGetCroppedImageComplete(CropImageView view, Bitmap bitmap, Exception error) {
        if (error == null) {
        	if(imageSelectEtape!=null){
				imageSelectEtape.setImageBitmap(bitmap);
				imageSelectEtape.setVisibility(View.VISIBLE);
        	}
			mLayout.setPanelState(PanelState.HIDDEN);
			this.bitmap = bitmap;
        		
        } else {
            Log.e("AIC", "Failed to crop image", error);
            Toast.makeText(context, "Image crop failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
