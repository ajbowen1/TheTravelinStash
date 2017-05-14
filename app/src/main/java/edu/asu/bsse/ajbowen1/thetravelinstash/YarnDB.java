package edu.asu.bsse.ajbowen1.thetravelinstash;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Copyright © 2017 Avery J. Bowen,
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Purpose: Class to provide methods to access the user's yarn database.
 * (NOTE: Some of this code was adapted from code provided by Timothy Lindquist
 * as part of course sample code, and used in compliance with its license.)
 *
 * @author Avery J. Bowen ajbowen1@asu.edu
 * @version April 28, 2017
 */

public class YarnDB extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static String dbName = "yarndb";
    private String dbPath;
    private SQLiteDatabase stashDB;
    private final Context context;

    public YarnDB(Context context){
        super(context, dbName, null, DATABASE_VERSION);
        this.context = context;
        dbPath = context.getFilesDir().getPath() + "/";
    }

    public void createDB() throws IOException{
        this.getReadableDatabase();
        try{
            copyDB();
        }
        catch(IOException e){
            e.getMessage();
        }
    }

    public void copyDB() throws IOException{
        try{
            if(!checkDB()){
                InputStream is = context.getResources().openRawResource(R.raw.yarndb);
                File aFile = new File(dbPath);
                if(!aFile.exists()){
                    aFile.mkdirs();
                }
                String op = dbPath + dbName + ".db";
                OutputStream os = new FileOutputStream(op);
                byte[] buffer = new byte[1024];
                int length;
                while((length = is.read(buffer)) > 0){
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
            }
        }
        catch (IOException e){
            e.getMessage();
        }
    }

    private boolean checkDB(){
        SQLiteDatabase checkDB = null;
        boolean ret = false;
        try{
            String path = dbPath + dbName + ".db";
            File aFile = new File(path);
            if(aFile.exists()){
                checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
                if(checkDB != null){
                    Cursor tabChk = checkDB.rawQuery("SELECT name FROM sqlite_master where type='table' and name='yarns';", null);
                    boolean yarnTabExists = false;
                    if(tabChk != null){
                        tabChk.moveToNext();
                        yarnTabExists = !tabChk.isAfterLast();
                    }
                    if(yarnTabExists){
                        ret = true;
                    }
                }
            }
        }
        catch (SQLiteException e){
            e.getMessage();
        }
        if(checkDB != null){
            checkDB.close();
        }
        return ret;
    }

    public SQLiteDatabase openDB() throws SQLiteException{
        String myPath = dbPath + dbName + ".db";
        if(checkDB()){
            stashDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        }
        else{
            try{
                this.copyDB();
                stashDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            }
            catch(Exception e){
                e.getMessage();
            }
        }
        return stashDB;
    }

    @Override
    public synchronized void close(){
        if(stashDB != null){
            stashDB.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
