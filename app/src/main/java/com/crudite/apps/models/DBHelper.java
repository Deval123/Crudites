
package com.crudite.apps.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper {

	DataBaseHelper Datahelp;
	SQLiteDatabase db;
	Context context;
	public static final String DataBaseName = "Crudites"; // Base de donnée

	
	public DBHelper(Context context) {
		this.context = context;
		Datahelp = new DataBaseHelper(context);
		
	}
	
	public class DataBaseHelper extends SQLiteOpenHelper {

		Context context;

		public DataBaseHelper(Context context) {
			super(context, DataBaseName, null, 1);
			this.context = context;
		}
	 
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			BaseModel.initDb(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
            /*if(oldVersion < 2){
                BaseModel.rebuildDb(db);
            }*/
		}

	}
	public DBHelper Open() {
		db = Datahelp.getWritableDatabase();
		db = Datahelp.getReadableDatabase();
		return this;
	}
	public void close() {
		db.close();
	}
	public SQLiteDatabase getDB() {
        return db;
    }
}
