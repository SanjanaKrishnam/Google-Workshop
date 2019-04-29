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

package com.google.engedu.wordstack;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;
import java.util.logging.Level;

public class MainActivity extends AppCompatActivity {

    private int WORD_LENGTH = 4;
    public static final int LIGHT_BLUE = Color.rgb(176, 200, 255);
    public static final int LIGHT_GREEN = Color.rgb(200, 255, 200);
    private HashMap<Integer,ArrayList<String>>words = new HashMap<Integer,ArrayList<String>>();
    private Random random = new Random();
    private StackedLayout stackedLayout;
    private String word1, word2;
    private Stack<LetterTile> placedTiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while((line = in.readLine()) != null) {
                String word = line.trim();
                if(words.containsKey(word.length())) {
                    ArrayList<String> list = words.get(word.length());
                    list.add(word);
                    words.put(word.length(), list);
                }
                else {
                    ArrayList<String> var = new ArrayList<String>();
                    var.add(word);
                    words.put(word.length(),var);
                }
            }
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        LinearLayout verticalLayout = (LinearLayout) findViewById(R.id.vertical_layout);
        stackedLayout = new StackedLayout(this);
        verticalLayout.addView(stackedLayout, 3);

        View word1LinearLayout = findViewById(R.id.word1);
        //word1LinearLayout.setOnTouchListener(new TouchListener());
        word1LinearLayout.setOnDragListener(new DragListener());
        View word2LinearLayout = findViewById(R.id.word2);
        //word2LinearLayout.setOnTouchListener(new TouchListener());
        word2LinearLayout.setOnDragListener(new DragListener());
        placedTiles=new Stack<>();
    }

    private class TouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && !stackedLayout.empty()) {
                LetterTile tile = (LetterTile) stackedLayout.peek();
                tile.moveToViewGroup((ViewGroup) v);
                if (stackedLayout.empty()) {
                    TextView messageBox = (TextView) findViewById(R.id.message_box);
                    messageBox.setText(word1 + " " + word2);
                }
              placedTiles.push(tile);
                return true;
            }
            return false;
        }
    }

    private class DragListener implements View.OnDragListener {

        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(LIGHT_GREEN);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundColor(Color.WHITE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign Tile to the target Layout
                    LetterTile tile = (LetterTile) event.getLocalState();
                    tile.moveToViewGroup((ViewGroup) v);
                    if (stackedLayout.empty()) {
                        TextView messageBox = (TextView) findViewById(R.id.message_box);

                        messageBox.setText(word1 + " " + word2);
                    }

                    placedTiles.push(tile);
                    return true;
            }
            return false;
        }
    }

    public boolean onStartGame(View view) {


        WORD_LENGTH++;
        LinearLayout word11=(LinearLayout)findViewById(R.id.word1);
        LinearLayout word22=(LinearLayout)findViewById(R.id.word2);
        stackedLayout.clear();
        word11.removeAllViews();
        word22.removeAllViews();

        TextView messageBox = (TextView) findViewById(R.id.message_box);
        messageBox.setText("Game started");
        int a, b;
        Random Rand = new Random();
        ArrayList<String> words12=new ArrayList<>();
        words12=words.get(WORD_LENGTH);
        do {
            a = Rand.nextInt(words12.size());
            b = Rand.nextInt(words12.size());
        }
        while (a == b);
        word1 = words12.get(a);
        word2 = words12.get(b);

        String word3="" ;
        int countera = 0;
        int counterb = 0;
        int k=0;
        while(k!=2*WORD_LENGTH)
        {

            while(countera<WORD_LENGTH&&counterb<WORD_LENGTH) {
                int z = Rand.nextInt(2);
                if(z==0) {
                    word3+= word1.charAt(countera++);
                }
                if(z==1) {
                    word3+= word2.charAt(counterb++);
                }
                k++;
            }
            if(countera==WORD_LENGTH)
                while(counterb!=WORD_LENGTH) {
                    word3+= word2.charAt(counterb++);
                    k++;
                }

            if(counterb==WORD_LENGTH)
                while(countera!=WORD_LENGTH) {
                    word3+= word1.charAt(countera++);
                    k++;
                }
        }


        for(int i=word3.length()-1;i>=0;i--)

        {
            LetterTile tile= new LetterTile(this,word3.charAt(i));
            stackedLayout.push(tile);

        }


        return true;
    }

    public boolean onUndo(View view) {
        LetterTile tile=(LetterTile)placedTiles.pop();
        tile.moveToViewGroup(stackedLayout);
        return true;
    }
}
