package com.example.project1;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class omokPage extends AppCompatActivity {
    private static final String TAG = "OmokPage";
    View v;

    private Button[][] buttons = new Button[10][10];
    private String name;
    private boolean player1Turn = true;
    private int roundCount;
    private int player1Points;
    private int player2Points;
    private int tiePoints;
    private TextView textViewPlayer1;
    private TextView textViewPlayer2;
    private TextView textViewTie;
    private TextView timer;
    RetroClient retroClient = RetroClient.getInstance(this).createBaseApi();






    private CountDownTimer countDownTimer = new CountDownTimer(20000, 1000) {
        public void onTick(long millisUntilFinished) {
            timer.setText(String.format(Locale.getDefault(), "%d sec left.", millisUntilFinished / 1000L));
        }
        public void onFinish() {
            timer.setText("Done.");
            if (player1Turn){
                player2Win();
            }else{
                player1Win();
            }
        }
    };

    public omokPage() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.omok_page);


        // BGM
//        SoundPool sound = new SoundPool(1, AudioManager.STREAM_ALARM, 0);// maxStreams, streamType, srcQuality
//        int soundId = sound.load(this, R.raw.cut_whisper, 1);
//        sound.play(soundId, 1.0F, 1.0F,  1,  -1,  1.5F);

        MediaPlayer mMediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.whistle);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();



        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);
        textViewTie = findViewById(R.id.text_view_tie);
        timer = findViewById(R.id.text_view_timer);

        for (int i=0; i<10; i++){
            for (int j=0; j<10; j++){
                String buttonID ="button_"+i+j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onButtonClicked(v);
                    }
                });
            }
        }
        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                resetGame();
            }
        });



    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        return super.onCreateView(parent, name, context, attrs);
    }


    public void onButtonClicked(View v){
        countDownTimer.start();
        if (!((Button) v).getText().toString().equals("")){

            return;
        }
        if (player1Turn){
            ((Button) v).setText("O");

        }else {
            ((Button) v).setText("X");
        }

        roundCount++;

        if (checkForWin()){
            if (player1Turn){
                player1Wins();
            }else{
                player2Wins();
            }
        }else if (roundCount == 100){
            draw();
        }else{
            updatePointsText();
            player1Turn = !player1Turn;
            if (player1Turn){
                textViewPlayer1.setTextColor(Color.RED);
                textViewPlayer2.setTextColor(Color.BLACK);
            }else{
                textViewPlayer1.setTextColor(Color.BLACK);
                textViewPlayer2.setTextColor(Color.RED);
            }
        }

    }

    public void onClick(View v){

    }

    private Boolean checkForWin(){
        String[][] field = new String[10][10];
        for (int i=0; i<10; i++){
            for (int j=0; j<10; j++){
                field[i][j] = buttons[i][j].getText().toString();
            }
        }
        for (int i=0; i<10; i++){
            for (int j=0; j<6; j++){
                if (field[i][j].equals(field[i][j+1]) && field[i][j].equals(field[i][j+2]) && field[i][j].equals(field[i][j+3]) && field[i][j].equals(field[i][j+4]) && !field[i][j].equals("")){
                    return true;
                }
            }
        }
        for (int i=0; i<10; i++){
            for (int j=0; j<6; j++){
                if (field[j][i].equals(field[j+1][i]) && field[j][i].equals(field[j+2][i]) && field[j][i].equals(field[j+3][i]) && field[j][i].equals(field[j+4][i]) && !field[j][i].equals("")){
                    return true;
                }
            }
        }
        for (int i=0; i<6; i++){
            for (int j=0; j<6; j++){
                if (field[i][j].equals(field[i+1][j+1]) && field[i][j].equals(field[i+2][j+2]) && field[i][j].equals(field[i+3][j+3]) && field[i][j].equals(field[i+4][j+4]) && !field[i][j].equals("")){
                    return true;
                }
            }
        }
        for (int i=0; i<6; i++){
            for (int j=4; j<10; j++){
                if (field[i][j].equals(field[i+1][j-1]) && field[i][j].equals(field[i+2][j-2]) && field[i][j].equals(field[i+3][j-3]) && field[i][j].equals(field[i+4][j-4]) && !field[i][j].equals("")){
                    return true;
                }
            }
        }
        return false;
    }

    private void player1Wins(){
        countDownTimer.cancel();
        player1Points++;
        Toast toast = Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
        toast.show();
        updatePointsText();
        timer.setText("timer: ");
        for (int i=0; i<10; i++){
            for (int j=0; j<10; j++){
                buttons[i][j].setEnabled(false);
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetBoard();
                for (int i=0; i<10; i++){
                    for (int j=0; j<10; j++){
                        buttons[i][j].setEnabled(true);
                    }
                }
            }},2000);
    }

    private void player2Wins(){
        countDownTimer.cancel();
        player2Points++;
        Toast toast2 = Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT);
        toast2.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
        toast2.show();
        updatePointsText();
        timer.setText("timer: ");
        for (int i=0; i<10; i++){
            for (int j=0; j<10; j++){
                buttons[i][j].setEnabled(false);
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetBoard();
                for (int i=0; i<10; i++){
                    for (int j=0; j<10; j++){
                        buttons[i][j].setEnabled(true);
                    }
                }
            }},2000);
    }

    private void player1Win(){
        countDownTimer.cancel();
        player1Points++;
        Toast toast = Toast.makeText(this, "Time is up! Player 1 wins", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
        toast.show();
        updatePointsText();
        timer.setText("timer: ");
        for (int i=0; i<10; i++){
            for (int j=0; j<10; j++){
                buttons[i][j].setEnabled(false);
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetBoard();
                for (int i=0; i<10; i++){
                    for (int j=0; j<10; j++){
                        buttons[i][j].setEnabled(true);
                    }
                }
            }},2000);
    }

    private void player2Win(){
        countDownTimer.cancel();
        player2Points++;
        Toast toast2 = Toast.makeText(this, "Time is up! Player 2 wins", Toast.LENGTH_SHORT);
        toast2.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
        toast2.show();
        updatePointsText();
        timer.setText("timer: ");
        for (int i=0; i<10; i++){
            for (int j=0; j<10; j++){
                buttons[i][j].setEnabled(false);
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetBoard();
                for (int i=0; i<10; i++){
                    for (int j=0; j<10; j++){
                        buttons[i][j].setEnabled(true);
                    }
                }
            }},2000);
    }

    private void draw(){
        tiePoints++;
        Toast.makeText(this, "Again!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    private void updatePointsText(){
        textViewPlayer1.setText("Player1: " + player1Points);
        textViewPlayer2.setText("Player2: " + player2Points);
        textViewTie.setText("Tie: " + tiePoints);
    }

    private void resetBoard(){
        for (int i=0 ; i<10 ; i++){
            for (int j=0; j<10; j++){
                buttons[i][j].setText("");
            }
        }
        roundCount=0;
        player1Turn = true;
        textViewPlayer1.setTextColor(Color.RED);
        textViewPlayer2.setTextColor(Color.BLACK);

        retroClient.deleteBoard( new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.e("error", "aaaaaaaaaa");
            }
            @Override
            public void onSuccess(int code, Object receivedData) {

            }
            @Override
            public void onFailure(int code) {
                Log.e("error", "ddddd");
            }
        });
    }

    private void resetGame(){
        countDownTimer.cancel();
        player1Points = 0;
        player2Points = 0;
        tiePoints = 0;
        timer.setText("timer: ");
        updatePointsText();
        resetBoard();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState);
        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putInt("tie2Points", tiePoints);
        outState.putBoolean("player1Turn", player1Turn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        roundCount=savedInstanceState.getInt("roundCount");
        player1Points=savedInstanceState.getInt("player1Points");
        player2Points=savedInstanceState.getInt("player2Points");
        tiePoints=savedInstanceState.getInt("tiePoints");
        player1Turn=savedInstanceState.getBoolean("player1Turn");
    }
}
