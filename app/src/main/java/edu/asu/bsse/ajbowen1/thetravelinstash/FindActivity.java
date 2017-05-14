package edu.asu.bsse.ajbowen1.thetravelinstash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
public class FindActivity extends AppCompatActivity {
    private String queryString;
    private Spinner wtSpinner, typeSpinner, colorSpinner;
    private EditText manufText, nameText, quantText;
    private boolean hasMan, hasNam, hasType, hasCol, hasWt, hasQuant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        Button mainMenu = (Button)findViewById(R.id.mainM);
        Button findButton = (Button)findViewById(R.id.findB);
        wtSpinner = (Spinner)findViewById(R.id.search_weightSpinner);
        typeSpinner = (Spinner)findViewById(R.id.search_typeSpinner);
        colorSpinner = (Spinner)findViewById(R.id.search_colorSpinner);
        manufText = (EditText) findViewById(R.id.search_manufText);
        nameText = (EditText) findViewById(R.id.search_nameText);
        quantText = (EditText) findViewById(R.id.search_quantText);
    }

    public void viewSome(View v){
        queryString = makeQuery("SELECT name, color, quantity FROM yarns WHERE ");
        Intent viewIntent = new Intent(this, ViewAllActivity.class);
        viewIntent.putExtra("view", "some");
        viewIntent.putExtra("query", queryString);
        startActivity(viewIntent);
    }

    private String makeQuery(String query){
        int count = 0;
        setBools();
        if(hasMan){
            count++;
            query = query + "manufacturer='" + manufText.getText().toString() + "'";
        }
        if(hasNam && count > 0){
            query = query + " AND name='" + nameText.getText().toString() + "'";
        }
        else if(hasNam && count == 0){
            query = query + "name='" + nameText.getText().toString() + "'";
            count++;
        }
        if(hasWt && count > 0){
            query = query + " AND weight='" + wtSpinner.getSelectedItem().toString() + "'";
        }
        else if(hasWt && count == 0){
            query = query + "weight='" + wtSpinner.getSelectedItem().toString() + "'";
            count++;
        }
        if(hasType && count > 0){
            query = query + " AND type='" + typeSpinner.getSelectedItem().toString() + "'";
        }
        else if(hasType && count == 0){
            query = query + "type='" + typeSpinner.getSelectedItem().toString() + "'";
            count++;
        }
        if(hasCol && count > 0){
            query = query + " AND color='" + colorSpinner.getSelectedItem().toString() + "'";
        }
        else if(hasCol && count == 0){
            query = query + "color='" + colorSpinner.getSelectedItem().toString() + "'";
            count++;
        }
        if(hasQuant && count > 0){
            query = query + " AND quantity='" + quantText.getText().toString() + "'";
        }
        else if(hasQuant && count == 0){
            query = query + "name='" + nameText.getText().toString() + "'";
        }
        return query;
    }

    private void setBools(){
        if(!manufText.getText().toString().equals("Manufacturer")){
            hasMan = true;
        }
        if(!nameText.getText().toString().equals("Name")){
            hasNam = true;
        }
        if(!wtSpinner.getSelectedItem().toString().equals("Weight")){
            hasWt = true;
        }
        if(!typeSpinner.getSelectedItem().toString().equals("Fiber Type")){
            hasType = true;
        }
        if(!colorSpinner.getSelectedItem().toString().equals("Color")){
            hasCol = true;
        }
        if(!quantText.getText().toString().equals("Quantity on hand")){
            hasQuant = true;
        }
    }

    public void mainClicked(View v) {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }
}
