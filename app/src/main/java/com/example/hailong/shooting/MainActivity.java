package com.example.hailong.shooting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements GameView.Callback{
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        gameView.setCallback(this);

        setContentView(gameView);


    }

    @Override
    public void onGameOver(long score) {
        gameView.stopDrawThread();
        Toast.makeText(this,"Game Over スコア"+score,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.stopDrawThread();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.startDrawThread();
    }
}
