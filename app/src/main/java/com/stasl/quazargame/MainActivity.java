package com.stasl.quazargame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.util.Random;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TickerView tickerView;
    private TickerView balance;
    private TickerView bet;
    private TickerView prize;
    private FloatingActionButton button1;
    private FloatingActionButton button2;
    private FloatingActionButton button3;
    private FloatingActionButton button4;
    private FloatingActionButton button5;
    private int currentScore;
    private int currentMoney = 0;
    private int moneyWon = 0;
    private int currentBet = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tickerView = (TickerView)findViewById(R.id.tickerView);
        tickerView.setCharacterList(TickerUtils.getDefaultNumberList());
        button1 = (FloatingActionButton) findViewById(R.id.button1);
        button2 = (FloatingActionButton) findViewById(R.id.button2);
        button3 = (FloatingActionButton) findViewById(R.id.button3);
        button4 = (FloatingActionButton) findViewById(R.id.button4);
        button5 = (FloatingActionButton) findViewById(R.id.button5);
        balance = (TickerView)findViewById(R.id.balance);
        balance.setCharacterList(TickerUtils.getDefaultListForUSCurrency());
        balance.setText("Balance: " + String.valueOf(currentMoney) + "$");
        bet = (TickerView)findViewById(R.id.bet);
        bet.setCharacterList(TickerUtils.getDefaultListForUSCurrency());
        bet.setText("Bet: " + String.valueOf(currentBet) + "$");
        prize = (TickerView)findViewById(R.id.prize);
        prize.setCharacterList(TickerUtils.getDefaultListForUSCurrency());
        prize.setText("Withdraw: " + String.valueOf(moneyWon) + "$");
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button1.setImageBitmap(textAsBitmap("1-8", 60, Color.WHITE));
        button2.setImageBitmap(textAsBitmap("4-7", 60, Color.WHITE));
        Snackbar.make(findViewById(android.R.id.content), "Start game?", Snackbar.LENGTH_INDEFINITE).setActionTextColor(Color.WHITE).setAction("YES", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeGame();
            }
        }).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.button1:
                try {
                    play(getInt(1,8));
                } catch (Exception e) {
                    Log.d("Random Exception", e.getLocalizedMessage());
                    finish();
                }
                break;
            case R.id.button2:
                try {
                    play(getInt(4,7));
                } catch (Exception e) {
                    Log.d("Random Exception", e.getLocalizedMessage());
                    finish();
                }
                break;
            case R.id.button3:
                currentBet -= 10;
                bet.setText("Bet: " + String.valueOf(currentBet) + "$");
                break;
            case R.id.button4:
                currentBet += 10;
                bet.setText("Bet: " + String.valueOf(currentBet) + "$");
                break;
            case R.id.button5:
                addMoney(moneyWon);
                prize.setText("Withdraw: " + 0 + "$");
                hideElements();
                Snackbar.make(findViewById(android.R.id.content), "You won. Try again?", Snackbar.LENGTH_INDEFINITE).setActionTextColor(Color.GREEN).setAction("YES", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        initializeGame();
                    }
                }).show();
                break;
        }
    }

    private void play(int number) {
        currentScore += number;
        tickerView.setText(String.valueOf(currentScore));
        if (currentScore < 15) {
            return;
        }
        if (currentScore > 20)
        {
            hideElements();
            Snackbar.make(findViewById(android.R.id.content), "You lost. Try again?", Snackbar.LENGTH_INDEFINITE).setActionTextColor(Color.RED).setAction("YES", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initializeGame();
                }
            }).show();
            return;
        }
        switch (currentScore) {
            case 15:
                moneyWon = currentBet / 4;
                break;
            case 16:
                moneyWon = currentBet / 2;
                break;
            case 17:
                moneyWon = currentBet;
                break;
            case 18:
                moneyWon = (currentBet / 4) + currentBet;
                break;
            case 19:
                moneyWon = (currentBet / 2) + currentBet;
                break;
            case 20:
                moneyWon = currentBet * 2;
                break;
        }
        prize.setText("Withdraw: " + String.valueOf(moneyWon) + "$");
        Snackbar.make(findViewById(android.R.id.content), "You can get " + moneyWon + " credits now", Snackbar.LENGTH_INDEFINITE).setActionTextColor(Color.GREEN).setAction("GET", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMoney(moneyWon);
                prize.setText("Withdraw: " + 0 + "$");
                hideElements();
                Snackbar.make(findViewById(android.R.id.content), "You won. Try again?", Snackbar.LENGTH_INDEFINITE).setActionTextColor(Color.GREEN).setAction("YES", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        initializeGame();
                    }
                }).show();
            }
        }).show();
    }

    private int getInt(int firstNumber, int secondNumber) throws Exception {
        if (firstNumber > secondNumber)
        {
            throw new Exception("First number should be lesser than second");
        }
        Random random = new Random(System.currentTimeMillis());
        return random.nextInt(secondNumber - firstNumber) + firstNumber;
    }
    private void initializeGame()
    {
        try {
            showElements();
            currentScore = getInt(1,5);
            addMoney(-currentBet);
            tickerView.setText(String.valueOf(currentScore));
        } catch (Exception e) {
            Log.d("Random Exception", e.getLocalizedMessage());
            finish();
        }
    }
    private void addMoney(int amount)
    {
        currentMoney += amount;
        if (currentMoney > 0)
        {
            balance.setTextColor(getResources().getColor(R.color.colorGreen));
        }
        else
        {
            balance.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        balance.setText("Balance: " + String.valueOf(currentMoney) + "$");
    }
    public static Bitmap textAsBitmap(String text, float textSize, int textColor)
    {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }
    private void hideElements()
    {
        button1.setVisibility(View.INVISIBLE);
        button2.setVisibility(View.INVISIBLE);
        button3.setVisibility(View.VISIBLE);
        button4.setVisibility(View.VISIBLE);
    }
    private void showElements()
    {
        button1.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
        button3.setVisibility(View.INVISIBLE);
        button4.setVisibility(View.INVISIBLE);
    }
}
