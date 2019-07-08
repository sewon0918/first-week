package com.example.project1;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class omokPage2 extends AppCompatActivity {
    private static final String TAG = "OmokPage2";
    View v;

    private Button[][] buttons = new Button[10][10];
    private String name= ((MainActivity)MainActivity.context).name;
    private boolean playerTurn;
    private boolean CanStart = false;
    private int Player=2;
    private int roundCount;
    private int player1Points;
    private int player2Points;
    private int tiePoints;
    private TextView textViewPlayer1;
    private TextView textViewPlayer2;
    private TextView textViewTie;
    private TextView timer;



    public ArrayList<coordinates> Board;

    RetroClient retroClient = RetroClient.getInstance(this).createBaseApi();

    TimerTask timerTask = new TimerTask() {
        int cnt = 0;
        @Override
        public void run() {
            retroClient.getBoard(new RetroCallback() {
                @Override
                public void onError(Throwable t) {
                    Log.e("error", "initcontacterror");
                }
                @Override
                public void onSuccess(int code, Object receivedData) {
                    List<coordinates> board;
                    board = (List<coordinates>) receivedData;
                    int itemCount = board.size();
                    if(cnt!=itemCount) {
                        countDownTimer.start();
                        makeBoard(board);
                        cnt = itemCount;

                        if (checkForWin()==1){
                            playerWins(1);
                            updatePointsText();

                            MediaPlayer mediaPlayer2 = MediaPlayer.create(getBaseContext(), R.raw.biryong_cut);
                            //mMediaPlayer.stop();
                            mediaPlayer2.start();

                            textViewPlayer1.setTextColor(Color.RED);
                            textViewPlayer2.setTextColor(Color.BLACK);
                        }
                        else if(checkForWin()==2){
                            playerWins(2);
                            updatePointsText();
                            textViewPlayer1.setTextColor(Color.BLACK);
                            textViewPlayer2.setTextColor(Color.RED);
                           // mMediaPlayer.stop();
                            MediaPlayer mediaPlayer2 = MediaPlayer.create(getBaseContext(), R.raw.biryong_cut);
                            mediaPlayer2.start();
                        }
                        else if (roundCount == 100){
                            draw();
                        }
                        else{
                            updatePointsText();

                            if (playerTurn){
                                textViewPlayer1.setTextColor(Color.RED);
                                textViewPlayer2.setTextColor(Color.BLACK);
                            }else{
                                textViewPlayer1.setTextColor(Color.BLACK);
                                textViewPlayer2.setTextColor(Color.RED);
                            }
                        }
                    }
                }
                @Override
                public void onFailure(int code) {
                    Log.e("error", "ddddd");
                }
            });
        }
    };

    private CountDownTimer countDownTimer = new CountDownTimer(20000, 1000) {
        public void onTick(long millisUntilFinished) {
            timer.setText(String.format(Locale.getDefault(), "%d sec left.", millisUntilFinished / 1000L));
        }
        public void onFinish() {
            timer.setText("Done.");
            if (playerTurn){
                player2Win();
            }else{
                player1Win();
            }
        }
    };

    public void makeBoard(List<coordinates> elements){
        for (coordinates var:elements
             ) {
            String id = var.getId();
            int x = var.getX();
            int y = var.getY();
            Log.d(TAG, "makeBoard: id "+id+" name "+name);
            if(id.equals(name)){
                buttons[y][x].setText("O");
            }
            else{
                buttons[y][x].setText("X");
            }
        }
    }

    public omokPage2() {

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.omok_page);

        MediaPlayer mMediaPlayer = MediaPlayer.create(this, R.raw.cut_whisper);

        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();

        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);
        textViewTie = findViewById(R.id.text_view_tie);
        timer = findViewById(R.id.text_view_timer);

        retroClient.addUser(name, new RetroCallback() {
            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onSuccess(int code, Object receivedData) {

            }

            @Override
            public void onFailure(int code) {

            }
        });

        retroClient.getUser(name, new RetroCallback() {
            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                List<String> users = (List<String>) receivedData;
                if (users.size() == 1) {
                    playerTurn = true;
                    CanStart = false;
                    Player = 1;
                } else if (users.size() == 2) {
                    playerTurn = false;
                    CanStart = true;
                    youCanStart();
                }
                Log.d(TAG, "onSuccess: "+Player);
            }

            @Override
            public void onFailure(int code) {

            }
        });


        Timer timer2 = new Timer(true);
        timer2.scheduleAtFixedRate(timerTask, 0, 100);

        String player1 = name;

        for (int i=0; i<10; i++){
            for (int j=0; j<10; j++){
                String buttonID ="button_"+i+j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(playerTurn) {
                            onButtonClicked(v);
                        }}
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

    public void onClick(){}

    public void onButtonClicked(View v){
        if (!((Button) v).getText().toString().equals("")){
            return;
        }
        // Send to Server
        coordinates coordinates = (coordinates) new coordinates();
        coordinates.setX((int)v.getResources().getResourceEntryName(v.getId()).charAt(8)-48);
        coordinates.setY((int)v.getResources().getResourceEntryName(v.getId()).charAt(7)-48);
        coordinates.setId(name);
        Log.d(TAG, "onClick:"+coordinates.x+' '+coordinates.y+' '+coordinates.id);;
        retroClient.addPoint(coordinates, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.e("error", "initcontacterror");
            }
            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "send successfully");
            }
            @Override
            public void onFailure(int code) {
                Log.e("error", "ddddd");
            }
        });

        roundCount++;

    }

    private boolean youCanStart(){
        if(CanStart){
            Toast toast = Toast.makeText(this, "You Can Start", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
            toast.show();
            return true;
        }
        return false;
    }

    private int checkForWin(){
        String[][] field = new String[10][10];
        for (int i=0; i<10; i++){
            for (int j=0; j<10; j++){
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        //가로체크
        for (int i=0; i<10; i++){
            for (int j=0; j<6; j++){
                if (field[i][j].equals(field[i][j+1]) && field[i][j].equals(field[i][j+2]) && field[i][j].equals(field[i][j+3]) && field[i][j].equals(field[i][j+4]) && !field[i][j].equals("")){
                    {
                        if(field[i][j].equals("O")){
                            return 1;
                        }else return 2;
                    }

                }
            }
        }
        //세로체크
        for (int i=0; i<10; i++){
            for (int j=0; j<6; j++){
                if (field[j][i].equals(field[j+1][i]) && field[j][i].equals(field[j+2][i]) && field[j][i].equals(field[j+3][i]) && field[j][i].equals(field[j+4][i]) && !field[j][i].equals("")){
                    {
                        if(field[j][i].equals("O")){
                            return 1;
                        }else return 2;
                    }
                }
            }
        }

        for (int i=0; i<6; i++){
            for (int j=0; j<6; j++){
                if (field[i][j].equals(field[i+1][j+1]) && field[i][j].equals(field[i+2][j+2]) && field[i][j].equals(field[i+3][j+3]) && field[i][j].equals(field[i+4][j+4]) && !field[i][j].equals("")){
                    {
                        if(field[i][j].equals("O")){
                            return 1;
                        }else return 2;
                    }
                }
            }
        }
        for (int i=0; i<6; i++){
            for (int j=4; j<10; j++){
                if (field[i][j].equals(field[i+1][j-1]) && field[i][j].equals(field[i+2][j-2]) && field[i][j].equals(field[i+3][j-3]) && field[i][j].equals(field[i+4][j-4]) && !field[i][j].equals("")){
                    {
                        if(field[i][j].equals("O")){
                            return 1;
                        }else return 2;
                    }
                }
            }
        }
        return 0;
    }
    private void playerWins(int who){
        retroClient.delUser(name, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
            }

            @Override
            public void onFailure(int code) {
            }
        });
        countDownTimer.cancel();
        if (who==1){
            player1Points++;
        }
        else{
            player2Points++;
        }
        Toast toast = Toast.makeText(this, "Player "+who+" wins!", Toast.LENGTH_SHORT);
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

    private void player1Win(){
        retroClient.delUser(name, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
            }

            @Override
            public void onFailure(int code) {
            }
        });
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
        retroClient.delUser(name, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
            }

            @Override
            public void onFailure(int code) {
            }
        });
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
        outState.putBoolean("playerTurn", playerTurn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        roundCount=savedInstanceState.getInt("roundCount");
        player1Points=savedInstanceState.getInt("player1Points");
        player2Points=savedInstanceState.getInt("player2Points");
        tiePoints=savedInstanceState.getInt("tiePoints");
        playerTurn=savedInstanceState.getBoolean("playerTurn");
    }
}

