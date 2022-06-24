package com.crudite.apps.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.crudite.apps.models.BaseModel;
import com.crudite.apps.models.modelAnotationType;

import org.jdeferred.DoneCallback;
import org.jdeferred.android.AndroidExecutionScope;
import org.jdeferred.android.AndroidFailCallback;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Admin on 23/05/2018.
 */

public class Synchronise {
    public static int synchCmpt = 0;
    public static boolean isSynch=false;
    public static void synch(){
        try {
            List<String> classes = BaseModel.getClassesOfPackage(BaseModel.Config.entitePackageName);
            final ArrayList<SynchItem> listItems = new ArrayList<>();
            for (String c : classes) {
                Class cl = Class.forName(BaseModel.Config.entitePackageName + "." + c);
                if (cl.getSuperclass() == BaseModel.class) {
                    Constructor<?> cons = cl.getConstructor();
                    Object item = cons.newInstance();
                    SynchItem item1 =  new SynchItem();
                    item1.nom = c;
                    item1.item = (BaseModel)item;
                    try {
                        item1.order = ((modelAnotationType) cl.getAnnotation(modelAnotationType.class)).synchOrder();
                    }catch (Exception e){
                        item1.order = 0;
                    }
                    listItems.add(item1);
                }
            }
            Collections.sort(listItems,  new Comparator<SynchItem>() {
                @Override
                public int compare(SynchItem o1, SynchItem o2) {
                    return o1.order.compareTo(o2.order);
                }
            });

            synchCmpt =0;

           final BroadcastReceiver broadcast_reciever = new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent intent) {
                    String action = intent.getAction();
                    if (action.equals("synch_process")) {
                        if(synchCmpt<listItems.size()) {
                            Log.i("Syncb", listItems.get(synchCmpt).nom+"");
                            listItems.get(synchCmpt).item.synch().done(new DoneCallback<String>() {
                                @Override
                                public void onDone(String result) {
                                    synchCmpt++;
                                    Intent intent = new Intent("synch_process");
                                    AppController.getInstance().sendBroadcast(intent);
                                }
                            }).fail(new AndroidFailCallback<String>() {
                                @Override
                                public void onFail(String result) {
                                    synchCmpt++;
                                    Intent intent = new Intent("synch_process");
                                    AppController.getInstance().sendBroadcast(intent);
                                }

                                @Override
                                public AndroidExecutionScope getExecutionScope() {
                                    return null;
                                }
                            });
                        }
                        else {
                            AppController.getInstance().getApplicationContext().unregisterReceiver(this);
                            isSynch = false;
                        }
                    }
                }
            };
            AppController.getInstance().getApplicationContext().registerReceiver(broadcast_reciever, new IntentFilter("synch_process"));
            Intent intent = new Intent("synch_process");
            AppController.getInstance().sendBroadcast(intent);
            isSynch = true;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    static class SynchItem{
        public BaseModel item;
        public Integer order;
        public String nom;
    }
}
