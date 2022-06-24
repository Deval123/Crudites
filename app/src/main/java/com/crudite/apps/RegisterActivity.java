package com.crudite.apps;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.crudite.apps.controller.AppController;
import com.crudite.apps.utilitaires.AddPictureLayout;
import com.crudite.apps.utilitaires.Standard;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    SlidingUpPanelLayout mLayout;
    private static ImageView selectedImage;
    private static Bitmap bitmap;
    private static AddPictureLayout mCurrentFragment;
    private Uri mCropImageUri;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setCancelable(false);
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        //categorie = (Spinner) findViewById(R.id.categorie);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i("AddRecette", "onPanelSlide, offset " + slideOffset);
            }
            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i("AddRecette", "onPanelStateChanged " + newState);
                if(mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED){
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                }
            }
        });

        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        });
        selectedImage = (ImageView) findViewById(R.id.selectImage);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        findViewById(R.id.selectImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(RegisterActivity.this);
            }
        });
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    boolean isSend = false;
    Map<String, String> post_params = new HashMap<String, String>();
    public void save(){
        boolean cancel = false;
        post_params.clear();
        if(((EditText)findViewById(R.id.nom)).getText().toString().length()==0){
            cancel = true;
            ((EditText)findViewById(R.id.nom)).setError("Champs réquis");
        }
        else
            post_params.put("nom", ((EditText)findViewById(R.id.nom)).getText().toString());

        if(((EditText)findViewById(R.id.prenom)).getText().toString().length()==0){
        }
        else
            post_params.put("prenom", ((EditText)findViewById(R.id.prenom)).getText().toString());

        if(((EditText)findViewById(R.id.date_naissance)).getText().toString().length()==0){
            cancel = true;
            ((EditText)findViewById(R.id.date_naissance)).setError("Champs réquis");
        }
        else
            post_params.put("date_naissance", ((EditText)findViewById(R.id.date_naissance)).getText().toString());

        if(((EditText)findViewById(R.id.lieu_naissance)).getText().toString().length()==0){
            cancel = true;
            ((EditText)findViewById(R.id.lieu_naissance)).setError("Champs réquis");
        }
        else
            post_params.put("lieu_naissance", ((EditText)findViewById(R.id.lieu_naissance)).getText().toString());

        post_params.put("profession", ((EditText)findViewById(R.id.profession)).getText().toString());

        post_params.put("entreprise", ((EditText)findViewById(R.id.entreprise)).getText().toString());
        post_params.put("localisation_entreprise", ((EditText)findViewById(R.id.localisation_entreprise)).getText().toString());

        if(((EditText)findViewById(R.id.ville)).getText().toString().length()==0){
            cancel = true;
            ((EditText)findViewById(R.id.ville)).setError("Champs réquis");
        }
        else
            post_params.put("ville", ((EditText)findViewById(R.id.ville)).getText().toString());

        if(((EditText)findViewById(R.id.quartier)).getText().toString().length()==0){
            cancel = true;
            ((EditText)findViewById(R.id.quartier)).setError("Champs réquis");
        }
        else
            post_params.put("quartier", ((EditText)findViewById(R.id.quartier)).getText().toString());

        if(((EditText)findViewById(R.id.email)).getText().toString().length()==0){
            cancel = true;
            ((EditText)findViewById(R.id.email)).setError("Champs réquis");
        }
        else
            post_params.put("email", ((EditText)findViewById(R.id.email)).getText().toString());

        if(((EditText)findViewById(R.id.telephone)).getText().toString().length()==0){
            cancel = true;
            ((EditText)findViewById(R.id.telephone)).setError("Champs réquis");
        }
        else
            post_params.put("telephone", ((EditText)findViewById(R.id.telephone)).getText().toString());

        if(cancel==false){

            //post_params.put("regid", Standard.getGCMToken(getContext()));
            new AsyncTask<Void, Void, String>() {

                protected void onPreExecute() {
                    progressDialog.setMessage("Enregistrement en cours...");
                    progressDialog.show();
                };

                @Override
                protected String doInBackground(Void... params) {
                    if(AddPictureLayout.bitmap!=null){
                        bitmap  = Standard.getResizedBitmap(AddPictureLayout.bitmap, 800);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        // Must compress the Image to reduce image size to make upload easy
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byte_arr = stream.toByteArray();
                        post_params.put("image", Base64.encodeToString(byte_arr, 0));
                    }
                    return "";
                }

                @Override
                protected void onPostExecute(String msg) {
                    if(!isSend){
                        isSend = true;
                        String  tag_string_req = "string_AddRepas";
                        String URL_ADD_REPAS = Standard.adresse_serveur+"Apk/register";
                        StringRequest strReq = new StringRequest(Request.Method.POST,
                                URL_ADD_REPAS, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String reponse) {
                                isSend = false;
                                if (reponse != null) {
                                    progressDialog.hide();
                                    Log.i("response", reponse);
                                    try {
                                        JSONObject myJson = new JSONObject(reponse);

                                        if(!myJson.optString("status").equals("success")){

                                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

                                            builder.setTitle("Alerte")
                                                    .setMessage(myJson.optString("reason"))
                                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // continue with delete
                                                            finish();
                                                        }
                                                    })
                                                    .setIcon(android.R.drawable.ic_dialog_alert);

                                            builder.show();
                                        }
                                        else {
                                            if(!myJson.optString("register").equals("success")){
                                                ((EditText)findViewById(R.id.email)).setError("Cet email ou ce numéro de téléhone existe déja.");
                                                ((EditText)findViewById(R.id.email)).requestFocus();
                                            }
                                            else {
                                                try {
                                                    JSONObject json_data = new JSONObject(myJson.getString("user"));
                                                    SharedPreferences.Editor e = AppController.getInstance().getPrefManager().pref.edit();

                                                    /*e.putString("token", myJson.optString("token"));

                                                    e.putInt("id_utilisateur", json_data.getInt("idutilisateur"));
                                                    e.putString("nom", json_data.getString("nom"));
                                                    e.putString("photo_user", json_data.getString("photo"));
                                                    e.putString("prenom", json_data.getString("prenom"));*/
                                                    e.putString("numero", json_data.getString("telephone"));
                                                    /*e.putString("adresse", json_data.getString("adresse"));
                                                    e.putString("email", json_data.getString("email"));*/

                                                    e.commit();
                                                } catch (JSONException e1) {
                                                    // TODO Auto-generated catch block
                                                    e1.printStackTrace();
                                                }
                                                final Handler handler=new Handler() {
                                                    @Override
                                                    public void handleMessage(Message msg) {
                                                        finish();
                                                        startActivity(new Intent(RegisterActivity.this, VerificationActivity.class));
                                                    }
                                                };
                                                Thread splash = new Thread(){
                                                    public void run(){
                                                        try{
                                                            sleep(1000);
                                                        }
                                                        catch(Exception ex){
                                                            ex.printStackTrace();
                                                        }
                                                        finally{
                                                            handler.sendMessage(handler.obtainMessage());
                                                        }
                                                    }
                                                };
                                                splash.start();
                                            }
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d("tag", "Error: " + error.getMessage());
                                progressDialog.hide();
                                isSend = false;
                            }
                        }){

                            @Override
                            protected Map<String, String> getParams() {
                                return post_params;
                            }

                        };

                        // Adding request to request queue
                        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
                    }
                }
            }.execute();
        }
			/*if(cancel==false){
				mPager.setCurrentItem(1);
			}*/
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mCurrentFragment.setImageUri(mCropImageUri);
        } else {
            Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }
    // When Image is selected from Gallery
    boolean requirePermissions = false;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage,
            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
            mCurrentFragment = new AddPictureLayout(this);
            mCurrentFragment.setImageReceiver(selectedImage);
            mCurrentFragment.setLayoutReceiver(mLayout);
            mCurrentFragment.setBitmapReceiver(bitmap);
            final Handler handler=new Handler() {
                @Override
                public void handleMessage(Message msg) {

                    if (CropImage.isReadExternalStoragePermissionsRequired(RegisterActivity.this, imageUri)) {

                        // request permissions and handle the result in onRequestPermissionsResult()
                        requirePermissions = true;
                        mCropImageUri = imageUri;
                    } else {
                        mCurrentFragment.setImageUri(imageUri);
                    }

                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                }
            };
            Thread splash = new Thread(){
                public void run(){
                    try{
                        sleep(1000);
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                    finally{
                        handler.sendMessage(handler.obtainMessage());
                    }
                }
            };
            splash.start();

        }


    }
}
