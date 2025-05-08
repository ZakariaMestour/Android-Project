package com.example.control;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class quiz2 extends AppCompatActivity {
    Button bNext;
    RadioGroup rg;
    RadioButton rb;
    int score;
    String CorrectResp="Jupiter";
    TextView timerTextView;
    CountDownTimer countDownTimer;
    long timeLeftInMillis = 12000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        timerTextView = findViewById(R.id.timer);
        startTimer();
        score = getIntent().getIntExtra("Score", 0);
        bNext=findViewById(R.id.button2);
        rg=findViewById(R.id.rg);
        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rg.getCheckedRadioButtonId()==-1){
                    Toast.makeText(getApplicationContext(),"Veuillez choisir une réponse svp!",Toast.LENGTH_SHORT).show();
                }else {
                    rb=findViewById(rg.getCheckedRadioButtonId());
                    if (rb.getText().toString().equals(CorrectResp)){
                        score+=1;
                    }
                    countDownTimer.cancel();
                    Intent i1=new Intent(quiz2.this,quiz3.class);
                    i1.putExtra("Score",score);
                    startActivity(i1);
                    finish();
                }
            }
        });
    }
    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                timerTextView.setText("Temps écoulé !");
                Toast.makeText(getApplicationContext(), "Temps écoulé !", Toast.LENGTH_SHORT).show();

                // Tu peux choisir de passer automatiquement à la question suivante, ou de bloquer
                Intent i1 = new Intent(quiz2.this, quiz3.class);
                i1.putExtra("Score", score);  // Même si aucune réponse n’a été donnée
                startActivity(i1);
                finish(); // Pour que l'utilisateur ne revienne pas en arrière
            }
        }.start();
    }
    private void updateTimerText() {
        int seconds = (int) (timeLeftInMillis / 1000);
        timerTextView.setText("00:" + (seconds < 10 ? "0" + seconds : seconds));
    }
}