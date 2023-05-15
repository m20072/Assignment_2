package com.example.assignment2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment2.Interfaces.movementCallback;
import com.example.assignment2.Logic.GameManager;
import com.example.assignment2.Utilities.MovementDetector;
import com.example.assignment2.Utilities.SignalGenerator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
{
    public static final String KEY_FAST = "KEY_SCORE";
    public static final String KEY_SENSORS = "KEY_SENSORS";
    private final int DELAY = 1000;
    private final int CONSTANT = 300;
    private int speed;
    private ShapeableImageView[] main_IMG_hearts;
    private MaterialTextView main_LBL_score;
    private FloatingActionButton timer_FAB_left;
    private FloatingActionButton timer_FAB_right;
    private ShapeableImageView[] main_IMG_Obstacles;
    private ShapeableImageView[] main_IMG_Players;
    private Timer timer;
    private boolean fast;
    private boolean sensors;

    private MovementDetector movementDetector;
    private MediaPlayer mediaPlayer;
    private GameManager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getMenuSettings();
        mediaPlayer = MediaPlayer.create(this,R.raw.collision);
        findViews();
        gameManager = new GameManager(main_IMG_hearts.length);
        initialUI();
        movementListeners();

        if(fast)
            speed = DELAY/2;

        else speed = DELAY;
        startTimer();
    }

    private void getMenuSettings()
    {
        Intent previousIntent = getIntent();
        fast = previousIntent.getBooleanExtra(KEY_FAST, false);
        sensors = previousIntent.getBooleanExtra(KEY_SENSORS, false); //sensors is true if we need to remove button and rely on sensors for movement
    }


    private void startTimer()
    {
        if (timer == null)
        {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask()
            {
                @Override
                public void run()
                {
                    runOnUiThread(() ->
                    {
                        refreshUI();
                    });
                }
            }, DELAY, speed); //we put delay instead of 0 so when we change speed with tilt it doesn't immediately ticks
        }
    }

    private void refreshUI()
    {
        for (int i = main_IMG_Obstacles.length - 1; i >= 0; i--) //backwards, otherwise overlaps with ones that were adjusted to higher index
        {
            if (gameManager.isObstacleVisible(i))
            {
                gameManager.setObstacleInvisible(i);
                main_IMG_Obstacles[i].setVisibility(View.INVISIBLE);
                if (!gameManager.isObstacleLastRow(i))
                {
                    //obstacle isn't on last row, so we should update the obstacle in row below image to be the same as this current row image (of the same column) and make next row same column obstacle as visible.
                        gameManager.updateImageResource(gameManager.nextRowObstacleIndex(i));//logical image resource update
                        main_IMG_Obstacles[gameManager.nextRowObstacleIndex(i)].setImageResource(gameManager.getObstacleImage(i)); //view obstacle image resource update. (SHOULDN'T ACTUALLY BE LIKE THAT IN  OBJECT ORIENTED)

                        //same but for visibility
                    gameManager.setObstacleVisible(gameManager.nextRowObstacleIndex(i));//logical obstacle visibility update
                    main_IMG_Obstacles[gameManager.nextRowObstacleIndex(i)].setVisibility(View.VISIBLE);//view obstacle visibility update (SHOULDN'T ACTUALLY BE LIKE THAT IN  OBJECT ORIENTED)
                } else //aka obstacle IS on last row
                {
                    if (gameManager.checkCollision(i)) // is the obstacle that is on last row and the player on same column?
                    {
                        if(gameManager.getObstacleImage(i) == R.drawable.ic_bitcoin)
                        {
                            mediaPlayer.reset(); //not release so as to not needlessly release resources associated with this MediaPlayer object.
                            mediaPlayer = MediaPlayer.create(this,R.raw.collision);
                            mediaPlayer.start();
                            SignalGenerator.getInstance().toast("\uD83D\uDCA5", Toast.LENGTH_SHORT);
                            SignalGenerator.getInstance().vibrate(500);
                            if (gameManager.getWrong() != 0 && gameManager.getWrong() <= 3)
                            {
                                main_IMG_hearts[main_IMG_hearts.length - gameManager.getWrong()].setVisibility(View.INVISIBLE);
                                if (gameManager.getWrong() == 3)
                                    endGame();

                            }
                        }

                        else if(gameManager.getObstacleImage(i) == R.drawable.ic_icon_heart)
                        {
                            SignalGenerator.getInstance().toast("❤️", Toast.LENGTH_SHORT);
                            if(gameManager.getWrong() >= 0) //if at least 1 heart missing and took a heart
                                main_IMG_hearts[main_IMG_hearts.length - gameManager.getWrong()-1].setVisibility(View.VISIBLE);//-1 cuz getobstacleimage did wrong--

                        }
                    }
                }
            }
        }
        if (gameManager.spawnObstacle())
        {
            int index = gameManager.randomObstacle(); //game-manager will also set that obstacle index to visible logically, aswell as set to heart with 25% chance
            main_IMG_Obstacles[index].setVisibility(View.VISIBLE);
            main_IMG_Obstacles[index].setImageResource(gameManager.getObstacleImage(index));
        }
        //add score:
        gameManager.increaseScore();
        main_LBL_score.setText("" + gameManager.getScore());

    }

    private void initialUI()
    {
        if(sensors)
        {
           timer_FAB_left.setVisibility(View.INVISIBLE);
           timer_FAB_right.setVisibility(View.INVISIBLE);
        }
        for (ShapeableImageView main_img_obstacle : main_IMG_Obstacles) //for each obstacle make invisible
        {
            main_img_obstacle.setVisibility(View.INVISIBLE);
        }

        for (ShapeableImageView main_img_player : main_IMG_Players) //for each player make invisible
        {
            main_img_player.setVisibility(View.INVISIBLE);
        }
        main_IMG_Players[gameManager.getVisiblePlayerIndex()].setVisibility(View.VISIBLE); //by default in logic, index 1 is the player index at start

    }



    private void findViews()
    {

        main_IMG_hearts = new ShapeableImageView[]
                {
                        findViewById(R.id.main_IMG_heart1),
                        findViewById(R.id.main_IMG_heart2),
                        findViewById(R.id.main_IMG_heart3)
                };

        main_IMG_Obstacles = new ShapeableImageView[]
                {
                        findViewById(R.id.main_IMG_obstacle1),
                        findViewById(R.id.main_IMG_obstacle2),
                        findViewById(R.id.main_IMG_obstacle3),
                        findViewById(R.id.main_IMG_obstacle4),
                        findViewById(R.id.main_IMG_obstacle5),
                        findViewById(R.id.main_IMG_obstacle6),
                        findViewById(R.id.main_IMG_obstacle7),
                        findViewById(R.id.main_IMG_obstacle8),
                        findViewById(R.id.main_IMG_obstacle9),
                        findViewById(R.id.main_IMG_obstacle10),
                        findViewById(R.id.main_IMG_obstacle11),
                        findViewById(R.id.main_IMG_obstacle12),
                        findViewById(R.id.main_IMG_obstacle13),
                        findViewById(R.id.main_IMG_obstacle14),
                        findViewById(R.id.main_IMG_obstacle15),
                        findViewById(R.id.main_IMG_obstacle16),
                        findViewById(R.id.main_IMG_obstacle17),
                        findViewById(R.id.main_IMG_obstacle18),
                        findViewById(R.id.main_IMG_obstacle19),
                        findViewById(R.id.main_IMG_obstacle20),
                        findViewById(R.id.main_IMG_obstacle21),
                        findViewById(R.id.main_IMG_obstacle22),
                        findViewById(R.id.main_IMG_obstacle23),
                        findViewById(R.id.main_IMG_obstacle24),
                        findViewById(R.id.main_IMG_obstacle25),
                };

        main_IMG_Players = new ShapeableImageView[]
                {
                        findViewById(R.id.main_IMG_player1),
                        findViewById(R.id.main_IMG_player2),
                        findViewById(R.id.main_IMG_player3),
                        findViewById(R.id.main_IMG_player4),
                        findViewById(R.id.main_IMG_player5)
                };

        main_LBL_score = findViewById(R.id.main_LBL_score);
        timer_FAB_left = findViewById(R.id.timer_FAB_left);
        timer_FAB_right = findViewById(R.id.timer_FAB_right);
    }

    private void movementListeners()
    {
            timer_FAB_left.setOnClickListener(v -> moveLeft());
            timer_FAB_right.setOnClickListener(v -> moveRight());

            if(sensors) //listen to movement on sensors
            {
                movementDetector = new MovementDetector(this, new movementCallback()
                {
                    @Override
                    public void left() {moveLeft();}

                    @Override
                    public void right() {moveRight();}

                    @Override
                    public void increase()
                    {
                        stopTimer();
                        speed = DELAY-CONSTANT;
                        startTimer();
                    }

                    @Override
                    public void decrease()
                    {
                        stopTimer();
                        speed = DELAY+CONSTANT;
                        startTimer();
                    }

                    @Override
                    public void normal()
                    {
                        stopTimer();
                        speed = DELAY;
                        startTimer();
                    }
                });
            }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        startTimer();
        if(sensors)
            movementDetector.start();
    }

    @Override
    protected void onPause()  //when I move to a different activity OR when I minimize the app (because when we minimize we dont always go thorugh oncreate again)
    {
        super.onPause();
        stopTimer();
        if(sensors)
            movementDetector.stop();
    }

    private void moveLeft()
    {
        main_IMG_Players[gameManager.getVisiblePlayerIndex()].setVisibility(View.INVISIBLE);
        gameManager.moveLeft();
        main_IMG_Players[gameManager.getVisiblePlayerIndex()].setVisibility(View.VISIBLE);
    }

    private void moveRight()
    {
        main_IMG_Players[gameManager.getVisiblePlayerIndex()].setVisibility(View.INVISIBLE);
        gameManager.moveRight();
        main_IMG_Players[gameManager.getVisiblePlayerIndex()].setVisibility(View.VISIBLE);
    }

    private void endGame()
    {
        stopTimer();
        if(sensors)
            movementDetector.stop();
        ScoreScreen(gameManager.getScore());
    }

    private void ScoreScreen(int score)
    {
        Intent intent = new Intent(this, ScoreActivity.class);
        intent.putExtra(ScoreActivity.KEY_SCORE,score);
        startActivity(intent);
        finish();
    }

    private void stopTimer()
    {
        if (timer != null)
            timer.cancel();
        timer = null;
    }
}