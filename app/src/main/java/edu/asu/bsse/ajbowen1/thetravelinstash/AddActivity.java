package edu.asu.bsse.ajbowen1.thetravelinstash;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class AddActivity extends AppCompatActivity {
    Spinner wtSpinner, typeSpinner, colorSpinner;
    EditText manufText, quantText, nameText;
    TextView modNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Intent i = getIntent();
        String command = i.getStringExtra("command");
        if(command.equals("add")){
            setContentView(R.layout.activity_add);
            Button addYarn = (Button)findViewById(R.id.addYarn);
            Button mainMenu = (Button)findViewById(R.id.mainM);
            wtSpinner = (Spinner)findViewById(R.id.weightSpinner);
            typeSpinner = (Spinner)findViewById(R.id.typeSpinner);
            colorSpinner = (Spinner)findViewById(R.id.colorSpinner);
            manufText = (EditText) findViewById(R.id.manufText);
            nameText = (EditText) findViewById(R.id.mod_nameText);
            quantText = (EditText) findViewById(R.id.quantText);
        }

        if(command.equals("modify")){
            setContentView(R.layout.activity_modify);
            Button mainMenu = (Button)findViewById(R.id.mainM);
            Button modButton = (Button)findViewById(R.id.makeMod);
            wtSpinner = (Spinner)findViewById(R.id.weightSpinner);
            typeSpinner = (Spinner)findViewById(R.id.typeSpinner);
            colorSpinner = (Spinner)findViewById(R.id.colorSpinner);
            manufText = (EditText) findViewById(R.id.manufText);
            modNameText = (TextView) findViewById(R.id.mod_nameText);
            quantText = (EditText) findViewById(R.id.quantText);
            String select = "SELECT manufacturer, name, weight, type, color, quantity FROM yarns WHERE " +
                    "name='" + i.getStringExtra("yarnName") + "';";
            try{
                YarnDB ydb = new YarnDB((Context)this);
                SQLiteDatabase stashDB = ydb.openDB();
                Cursor c = stashDB.rawQuery(select, null);
                HashMap<String, String> cursorMap = new HashMap<String, String>();

                while (c.moveToNext()) {
                    cursorMap.put("Manufacturer", c.getString(0));
                    cursorMap.put("Name", c.getString(1));
                    cursorMap.put("Weight", c.getString(2));
                    cursorMap.put("Type", c.getString(3));
                    cursorMap.put("Color", c.getString(4));
                    cursorMap.put("Quantity", c.getString(5));
                }

                manufText.setText(cursorMap.get("Manufacturer").toString());
                modNameText.setText(i.getStringExtra("yarnName"));
                quantText.setText(cursorMap.get("Quantity").toString());
                wtSpinner.setSelection(getIndex(wtSpinner, cursorMap.get("Weight").toString()));
                colorSpinner.setSelection(getIndex(colorSpinner, cursorMap.get("Color").toString()));
                typeSpinner.setSelection(getIndex(typeSpinner, cursorMap.get("Type").toString()));
            }
            catch (Exception e){
                e.getMessage();
            }
        }
    }

    private int getIndex(Spinner s, String v){
        int index = 0;
        for(int i = 0; i < s.getCount(); i++){
            if(s.getItemAtPosition(i).toString().equals(v)){
                index = i;
            }
        }
        return index;
    }

    public void makeModClicked(View v){
        String toChange = "UPDATE yarns SET manufacturer='" + manufText.getText().toString() + "', weight='" + wtSpinner.getSelectedItem().toString() +
                "', type='" + typeSpinner.getSelectedItem().toString() + "', color='" + colorSpinner.getSelectedItem().toString() +
                "', quantity='" + quantText.getText().toString() + "' WHERE name='" + modNameText.getText().toString() + "';";
        try{
            YarnDB ydb = new YarnDB((Context)this);
            SQLiteDatabase stashDB = ydb.openDB();
            stashDB.execSQL(toChange);
            stashDB.close();
            ydb.close();
            Intent modSuccess = new Intent(this, AddActivitySuccess.class);
            modSuccess.putExtra("success", "modify");
            startActivity(modSuccess);
        }
        catch (Exception e){
            e.getMessage();
        }
    }

    public void addYarnClicked(View v){
        try{
            YarnDB ydb = new YarnDB((Context)this);
            SQLiteDatabase stashDB = ydb.openDB();
            String toInsert = "INSERT INTO yarns (manufacturer, name, weight, type, color, quantity) VALUES ('" +
                    manufText.getText().toString() + "','" + nameText.getText().toString() + "','" + wtSpinner.getSelectedItem().toString() +
                    "','" + typeSpinner.getSelectedItem().toString() + "','" + colorSpinner.getSelectedItem().toString() + "','" +
                    quantText.getText().toString() + "');";
            stashDB.execSQL(toInsert);
            stashDB.close();
            ydb.close();
            Intent addedSuccess = new Intent(this, AddActivitySuccess.class);
            addedSuccess.putExtra("success", "add");
            startActivity(addedSuccess);
        }
        catch (Exception e){
            e.getMessage();
        }
    }

    public void mainClicked(View v) {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }
}
