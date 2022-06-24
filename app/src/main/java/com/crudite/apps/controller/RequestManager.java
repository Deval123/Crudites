package com.crudite.apps.controller;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.crudite.apps.utilitaires.AlertDialogManager;
import com.crudite.apps.utilitaires.Standard;

import org.jdeferred.Deferred;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chahdro on 11/05/2018.
 */

public class RequestManager {
    public static Promise<String, VolleyError, Void> get(String url, final Map<String, String> param_s, boolean cache){
        final Deferred<String,VolleyError, Void> deferred = new DeferredObject<>();

        String  tag_string_req = "string_req";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                Standard.adresse_serveur+url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                deferred.resolve(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                deferred.reject(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Access-token", Standard.getToken(AppController.getInstance().getApplicationContext()));
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                return param_s;
            }

        };
        // Adding request to volley request queue
        if(cache==false)
            AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req, cache);
        return deferred;
    }
        public static Promise<String, VolleyError, Void> get(String url, final Map<String, String> param_s){

            final Deferred<String,VolleyError, Void> deferred = new DeferredObject<>();

            String  tag_string_req = "string_req";
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    Standard.adresse_serveur+url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                deferred.resolve(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                deferred.reject(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Access-token", Standard.getToken(AppController.getInstance().getApplicationContext()));
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                return param_s;
            }

        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        return deferred;
    }
    public static Promise<String, VolleyError, Void> post(String url, final Map<String, String> param_s){

        final Deferred<String,VolleyError, Void> deferred = new DeferredObject<>();

        String  tag_string_req = "string_req";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Standard.adresse_serveur+url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                deferred.resolve(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                deferred.reject(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Access-token", Standard.getToken(AppController.getInstance().getApplicationContext()));

                return params;
            }
            @Override
            protected Map<String, String> getParams() {
                return param_s;
            }

        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        return deferred;
    }
    public static Promise<String, VolleyError, Void> post(String url, final Map<String, String> param_s, final String body_json){

        final Deferred<String,VolleyError, Void> deferred = new DeferredObject<>();

        String  tag_string_req = "string_req";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Standard.adresse_serveur+url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                deferred.resolve(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                deferred.reject(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Access-token", Standard.getToken(AppController.getInstance().getApplicationContext()));

                return params;
            }
            @Override
            public byte[] getBody()throws AuthFailureError {
                return body_json.getBytes();
            }
            @Override
            protected Map<String, String> getParams() {
                return param_s;
            }

        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        return deferred;
    }
    public static Promise<JSONObject, VolleyError, Void> getData(String url, final AppCompatActivity appAct, final Map<String, String> param_s){

        final Deferred<JSONObject,VolleyError, Void> deferred = new DeferredObject<>();

        String  tag_string_req = "string_req";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                Standard.adresse_serveur+url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        JSONObject myJson = new JSONObject(response);
                        if(!myJson.optString("status").equals("success")){
                            AlertDialogManager alert = new AlertDialogManager();
                            alert.showAlertDialog(appAct, "Problème rencontré", myJson.optString("reason"));
                            alert.show();
                        }
                        else {
                            deferred.resolve(myJson);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                deferred.reject(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Access-token", Standard.getToken(AppController.getInstance().getApplicationContext()));
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                return param_s==null ? new HashMap<String, String>() : param_s;
            }

        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        return deferred;
    }
    public static Promise<JSONObject, VolleyError, Void> postData(String url, final AppCompatActivity appAct, final Map<String, String> param_s){

        final Deferred<JSONObject,VolleyError, Void> deferred = new DeferredObject<>();

        String  tag_string_req = "string_req";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Standard.adresse_serveur+url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    Log.i("Mes", response+"sdf");
                    try {
                        JSONObject myJson = new JSONObject(response);
                        if(!myJson.optString("status").equals("success")){
                            AlertDialogManager alert = new AlertDialogManager();
                            alert.showAlertDialog(appAct, "Problème rencontré", myJson.optString("reason"));
                            alert.show();
                        }
                        else {
                            deferred.resolve(myJson);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                deferred.reject(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Access-token", Standard.getToken(AppController.getInstance().getApplicationContext()));
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                return param_s;
            }

        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        return deferred;
    }
}
