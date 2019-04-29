package com.example.sanjana.scarnesdice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View;
import android.widget.TextView;
import 	android.os.Handler;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
public class MainActivity extends AppCompatActivity {

    int uturnscore=0,uoverallscore=0,cturnscore=0,coverallscore=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button roll = (Button) findViewById(R.id.roll);
        roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roll();
            }
        });

        Button reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });



    }

   public void reset(){
       Button roll = (Button) findViewById(R.id.roll);
        roll.setEnabled(true);
        Button hold = (Button) findViewById(R.id.hold);
        hold.setEnabled(true);
        TextView update = (TextView) findViewById(R.id.who);
        update.setText("YOUR TURN!");
        TextView update1 = (TextView) findViewById(R.id.turnsvalue);
        update1.setText("0");
        TextView update2 = (TextView) findViewById(R.id.value1);
        update2.setText("0");
        TextView update3 = (TextView) findViewById(R.id.value2);
        update3.setText("0");
        uturnscore=0;
        uoverallscore=0;
        cturnscore=0;
        coverallscore=0;
    }



    public void roll() {

        int x=rolldice();
        uturnscore += x;
        TextView update = (TextView) findViewById(R.id.turnsvalue);
        update.setText(String.valueOf(uturnscore));
        if (x == 1)
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    uturnscore=0;
                    computerTurn();
                }
            }, 3000);

        }
        else {

            Button hold = (Button) findViewById(R.id.hold);
            hold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hold();
                }

            });
        }


    }

    public void hold() {

        uoverallscore += uturnscore;
        TextView update = (TextView) findViewById(R.id.value1);
        update.setText(String.valueOf(uoverallscore));
        uturnscore = 0;
        if (uoverallscore >= 100) {
            TextView victory = (TextView) findViewById(R.id.who);
            victory.setText("YOU WIN!");
            Button roll = (Button) findViewById(R.id.roll);
            roll.setEnabled(false);
            Button hold = (Button) findViewById(R.id.hold);
            hold.setEnabled(false);

        } else{

            computerTurn();
        TextView update1 = (TextView) findViewById(R.id.turnsvalue);
        update1.setText(String.valueOf(0));
    }
    }

    public void computerTurn() {

        final TextView update = (TextView) findViewById(R.id.who);
        final TextView update1 = (TextView) findViewById(R.id.turnsvalue);
        final TextView update2 = (TextView) findViewById(R.id.value2);
        update.setText("COMPUTER'S TURN!");
        final Button roll = (Button) findViewById(R.id.roll);
        roll.setEnabled(false);
        final Button hold = (Button) findViewById(R.id.hold);
        hold.setEnabled(false);
        int x;
        x = rolldice();
        cturnscore += x;
        update1.setText(String.valueOf(cturnscore));




            if(x ==1)

            {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cturnscore = 0;
                        update.setText("YOUR TURN!");
                        update1.setText(String.valueOf(cturnscore));
                        roll.setEnabled(true);
                        hold.setEnabled(true);
                    }
                }, 5000);

            }
            else

            {
                Random rand = new Random();
                int c = rand.nextInt(2);
                if (c == 1) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            computerTurn();
                        }
                    }, 5000);

                }
                else {
                    coverallscore += cturnscore;
                    update2.setText(String.valueOf(coverallscore));
                    if (coverallscore >= 100) {
                        update.setText("COMPUTER WINS!");
                    } else {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                roll.setEnabled(true);
                                hold.setEnabled(true);
                                cturnscore = 0;
                                update.setText("YOUR TURN!");
                                update1.setText(String.valueOf(cturnscore));
                            }
                        }, 5000);

                    }
                }
            }



    }

    public int rolldice() {
        ImageView dice = (ImageView) findViewById(R.id.dice1);
        Random rand = new Random();
        int c = rand.nextInt(6)+1;
        String value = "dice" + String.valueOf(c);
        int resource = getResources().getIdentifier(value, "drawable", "com.example.sanjana.scarnesdice");
        dice.setImageResource(resource);
        return c;
    }


}








