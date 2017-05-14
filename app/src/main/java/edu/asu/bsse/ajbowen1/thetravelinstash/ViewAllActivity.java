package edu.asu.bsse.ajbowen1.thetravelinstash;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class ViewAllActivity extends AppCompatActivity implements ListView.OnItemClickListener{
    private ListView yarnLV;
    private YarnDB ydb;
    private SQLiteDatabase stashDB;
    private List<HashMap<String, String>> cursorMap;
    private String queryString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        yarnLV = (ListView)findViewById(R.id.yarn_list_view);
        Intent i = getIntent();
        if(i.getStringExtra("view").equals("all")) {
            queryString = "SELECT name, color, quantity FROM yarns ORDER BY color ASC;";
        }
        if(i.getStringExtra("view").equals("some")){
            queryString = i.getStringExtra("query");

        }
        this.popListView(queryString);
    }

    private void popListView(String select){
        try {
            ydb = new YarnDB(this);
            stashDB = ydb.openDB();
            String[] colHeaders = this.getResources().getStringArray(R.array.col_header);
            int[] toViewIDs = new int[]{R.id.yarn_nameTV, R.id.yarn_colorTV, R.id.yarn_quantTV};
            Cursor c = stashDB.rawQuery(select, null);
            cursorMap = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> colTitles = new HashMap<>();
            colTitles.put("Name", "Name");
            colTitles.put("Color", "Color");
            colTitles.put("Quantity", "Quantity");
            cursorMap.add(colTitles);
            while (c.moveToNext()) {
                HashMap<String, String> map = new HashMap<>();
                map.put("Name", c.getString(0));
                map.put("Color", c.getString(1));
                map.put("Quantity", c.getString(2));
                cursorMap.add(map);
            }
            c.close();
            stashDB.close();
            ydb.close();
            SimpleAdapter sa = new SimpleAdapter(this, cursorMap, R.layout.yarn_list_item, colHeaders, toViewIDs);
            yarnLV.setAdapter(sa);
            yarnLV.setOnItemClickListener(this);
        }
        catch (Exception e){
            e.getMessage();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Intent displayDetail = new Intent(this, YarnDisplayActivity.class);
        HashMap o = (HashMap<String, String>)parent.getItemAtPosition(position);
        String yarnName = o.get("Name").toString();
        displayDetail.putExtra("yarnName", yarnName);
        startActivity(displayDetail);
    }
}
