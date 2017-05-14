package edu.asu.bsse.ajbowen1.thetravelinstash;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.HashMap;


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
 * Purpose: An app to maintain a user's yarn stash
 *
 * @author Avery J. Bowen ajbowen1@asu.edu
 * @version April 28, 2017
 */
public class YarnDisplayActivity extends AppCompatActivity {

    private TextView manufTV, nameTV, weightTV, typeTV, colorTV, quantTV;
    private YarnDB ydb;
    private SQLiteDatabase stashDB;
    private Button mainB, modB;
    private HashMap<String, String> cursorMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yarn_display);
        manufTV = (TextView)findViewById(R.id.yarn_manufView);
        nameTV = (TextView)findViewById(R.id.yarn_nameView);
        weightTV = (TextView)findViewById(R.id.yarn_weightView);
        typeTV = (TextView)findViewById(R.id.yarn_typeView);
        colorTV = (TextView)findViewById(R.id.yarn_colorView);
        quantTV = (TextView)findViewById(R.id.yarn_quantView);
        mainB = (Button)findViewById(R.id.mainB);
        modB = (Button)findViewById(R.id.modifyB);

        Intent i = getIntent();
        String yarnNameToDisplay = i.getStringExtra("yarnName");
        String select = "SELECT manufacturer, name, weight, type, color, quantity FROM yarns WHERE name='" + yarnNameToDisplay + "';";

        try{
            ydb = new YarnDB((Context)this);
            stashDB = ydb.openDB();
            Cursor c = stashDB.rawQuery(select, null);
            cursorMap = new HashMap<String, String>();

            while (c.moveToNext()) {
                cursorMap.put("Manufacturer", c.getString(0));
                cursorMap.put("Name", c.getString(1));
                cursorMap.put("Weight", c.getString(2));
                cursorMap.put("Type", c.getString(3));
                cursorMap.put("Color", c.getString(4));
                cursorMap.put("Quantity", c.getString(5));
            }

            manufTV.setText(getString(R.string.manuf) + ": " + cursorMap.get("Manufacturer").toString());
            nameTV.setText(getString(R.string.name) + ": " + cursorMap.get("Name").toString());
            weightTV.setText(getString(R.string.w) + ": " + cursorMap.get("Weight").toString());
            typeTV.setText(getString(R.string.ft) + ": " + cursorMap.get("Type").toString());
            colorTV.setText(getString(R.string.c) + ": " + cursorMap.get("Color").toString());
            quantTV.setText(getString(R.string.quant) + ": " + cursorMap.get("Quantity").toString());
        }
        catch (Exception e){
            e.getMessage();
        }

    }

    public void mainClicked(View v) {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

    public void remClicked(View v){
        try {
            ydb = new YarnDB((Context) this);
            stashDB = ydb.openDB();
            stashDB.delete("yarns", "name=?", new String[] {cursorMap.get("Name").toString()});
            Intent remSuccess = new Intent(this, AddActivitySuccess.class);
            remSuccess.putExtra("success", "remove");
            startActivity(remSuccess);
        }
        catch(Exception e){
            e.getMessage();
        }
    }

    public void modClicked(View v){
        Intent modIntent = new Intent(this, AddActivity.class);
        modIntent.putExtra("command", "modify");
        modIntent.putExtra("yarnName", cursorMap.get("Name").toString());
        startActivity(modIntent);
    }
}
