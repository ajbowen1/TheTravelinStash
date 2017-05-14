package edu.asu.bsse.ajbowen1.thetravelinstash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addButton = (Button)findViewById(R.id.addButton);
        Button findButton = (Button)findViewById(R.id.findButton);
        Button viewButton = (Button)findViewById(R.id.viewStash);
    }

    public void addClicked(View v){
        Intent addIntent = new Intent(this, AddActivity.class);
        addIntent.putExtra("command", "add");
        startActivity(addIntent);
    }

    public void findClicked(View v){
        Intent findIntent = new Intent(this, FindActivity.class);
        startActivity(findIntent);
    }

    public void viewAll(View v){
        Intent viewIntent = new Intent(this, ViewAllActivity.class);
        viewIntent.putExtra("view", "all");
        startActivity(viewIntent);
    }
}
