package com.crudite.apps.controller;

import android.content.Context;
import android.os.AsyncTask;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/**
 * Created by Admin on 09/07/2018.
 */

public class ImageDownloader {
    public static void download(final Context context, final String lien, final String name){
        new AsyncTask<Void, Void, String>(){

            @Override
            protected String doInBackground(Void... params) {
                try {
                    URL url = new URL(lien);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();

                    File file = new File(context.getFilesDir(),name);
                    BufferedInputStream bis = new BufferedInputStream(input);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    byte[] img = new byte[1024];

                    int current = 0;

                    while ((current = bis.read()) != -1) {
                        baos.write(current);
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(baos.toByteArray());
                    fos.flush();

                    fos.close();
                    input.close();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.execute();
    }
}
