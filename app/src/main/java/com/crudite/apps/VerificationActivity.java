package com.crudite.apps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.chaos.view.PinView;
import com.crudite.apps.controller.AppController;
import com.crudite.apps.utilitaires.AlertDialogManager;
import com.crudite.apps.utilitaires.Standard;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VerificationActivity extends AppCompatActivity {
    PinView pinView;
    ProgressDialog prgDialog;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView subText = (TextView) findViewById(R.id.subtext);

        sharedpreferences = getSharedPreferences(Standard.MyPREFERENCES, Context.MODE_PRIVATE);

        if(sharedpreferences.getString("numero", "").length()>0){
            subText.setText(getString(R.string.saisir_activation_register)+sharedpreferences.getString("numero", ""));
        }
        else {
            subText.setText(R.string.saisir_activation_inscription);
        }

        prgDialog = new ProgressDialog(VerificationActivity.this);
        prgDialog.setCancelable(false);
        pinView = findViewById(R.id.secondPinView);
        pinView.setAnimationEnable(true);
        pinView.setCursorVisible(true);
        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("Crudites", "onTextChanged() called with: s = [" + s + "], start = [" + start + "], before = [" + before + "], count = [" + count + "]");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pinView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInput();
            }
        });
        showInput();
        findViewById(R.id.go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndGo();
            }
        });
    }
    public void checkAndGo(){
        //Toast.makeText(getApplicationContext(), pinView.getText().toString()+"", Toast.LENGTH_SHORT).show();
        prgDialog.setMessage("Veuillez patienter...");
        prgDialog.show();
        String  tag_string_req = "string_req";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Standard.adresse_serveur+"Apk/verificationCode", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    String reponse = response.toString();
                    Log.i("response", response);
                    prgDialog.hide();

                    try {
                        JSONObject myJson = new JSONObject(reponse);

                        if(!myJson.optString("status").equals("success")){
                            AlertDialogManager alert = new AlertDialogManager();
                            alert.showAlertDialog(VerificationActivity.this, "Problème rencontré", myJson.optString("reason"));
                            alert.show();
                        }
                        else {
                            if(!myJson.optString("login").equals("success")){
                                AlertDialogManager alert = new AlertDialogManager();
                                alert.showAlertDialog(VerificationActivity.this, "Alerte", "Code introuvable.");
                                alert.show();
                            }
                            else {
                                try {
                                    JSONObject json_data = new JSONObject(myJson.getString("user"));
                                    SharedPreferences.Editor e = sharedpreferences.edit();

                                    e.putString("token", myJson.optString("token"));

                                    e.putInt("id_utilisateur", json_data.getInt("idutilisateur"));
                                    e.putString("nom", json_data.getString("nom"));
                                    e.putBoolean("newletter", json_data.getInt("newletter")==1);
                                    e.putString("photo_user", json_data.getString("photo"));
                                    e.putString("prenom", json_data.getString("prenom"));
                                    e.putString("numero", json_data.getString("telephone"));
                                    e.putString("ville", json_data.getString("ville"));
                                    e.putString("email", json_data.getString("email"));

                                    e.commit();
                                } catch (JSONException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }
                                final Handler handler=new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        prgDialog.hide();
                                        finish();
                                        startActivity(new Intent(VerificationActivity.this, MainActivity.class));
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
                prgDialog.hide();
                Toast.makeText(getApplicationContext(), "Vérifier votre connexion internet et réessayer", Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("code", pinView.getText().toString());
                return params;
            }

        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        /*startActivity(new Intent(VerificationActivity.this, MainActivity.class));
        finish();*/
    }
    public void showInput(){
        InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
}
