/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private String currentWord = "";
    private FastDictionary FD;
    public static int turn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/


        try {
            FD = new FastDictionary(assetManager.open("words.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        onStart(null);

        final Button challenge = (Button)findViewById(R.id.challenge);
        challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView label = (TextView) findViewById(R.id.gameStatus);
                if (currentWord.length() > 3 && FD.isWord(currentWord)) {
                    label.setText("You win!!");
                    challenge.setEnabled(false);
                    return;
                }

                String possibleWord = FD.getGoodWordStartingWith(currentWord);

                if ( possibleWord != null){
                    challenge.setEnabled(false);
                    label.setText("The word was "+possibleWord+" COM wins!!");
                }
                else{
                    challenge.setEnabled(false);
                    label.setText("You Win!!");
                }
                return;
            }
        });


        Button restart = (Button)findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentWord = "";
                challenge.setEnabled(true);
                onStart(null);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = true;//random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText(currentWord);
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            turn = 1;
            label.setText(USER_TURN);
        } else {
            turn = 0;
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {

        //Handler delay = new Handler();
        //delay.postDelayed((Runnable) this, 500);
        Button challenge = (Button)findViewById(R.id.challenge);
        TextView label = (TextView) findViewById(R.id.gameStatus);
        TextView text = (TextView) findViewById(R.id.ghostText);
        if(currentWord.length()>3 && FD.isWord(currentWord)) {
            label.setText("You loose!!! this is a word");
            challenge.setEnabled(false);
            return;
        }
        String newWord = FD.getGoodWordStartingWith(currentWord);

        if (newWord == null){
            label.setText("You loose!!! No word can be formed");
            challenge.setEnabled(false);
            return;
        }
        else{
            currentWord = newWord.substring(0,currentWord.length()+1);
        }
        text.setText(currentWord);
        userTurn = true;
        label.setText(USER_TURN);

    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        TextView label = (TextView) findViewById(R.id.gameStatus);
        TextView text = (TextView) findViewById(R.id.ghostText);
        char c = (char)event.getUnicodeChar();
        if((int)c>96 && (int)c<123) {
            currentWord+=c;
            text.setText(currentWord);
            label.setText(COMPUTER_TURN);
            computerTurn();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
