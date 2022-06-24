package com.crudite.apps.utilitaires;

import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

public class AlertDialogManager {
    AlertDialog.Builder builder;
    @SuppressWarnings("deprecation")
	public void showAlertDialog(AppCompatActivity context, String title, String message) {

        builder = new AlertDialog.Builder(context);

        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);

    }
    public void show(){
    	builder.show();
    }
    public void show(FragmentActivity fragmnt){
        builder.show();
    }
    public void show(FragmentManager fragmnt){
        builder.show();
    }
}