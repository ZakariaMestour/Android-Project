package com.example.control;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.control.classement.LeaderboardActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.color.utilities.Score;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_score extends AppCompatActivity {
    Button blogout, TryAgain,rank;
    TextView tvScore;
    ProgressBar pb;
    int score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_score);
        blogout=findViewById(R.id.button3);
        TryAgain=findViewById(R.id.button4);
        tvScore=findViewById(R.id.textView6);
        rank=findViewById(R.id.button5);
        pb=findViewById(R.id.pb);
        Intent i=getIntent();
        score=i.getIntExtra("Score",0);;
        tvScore.setText(score*100/5+"%");
        pb.setProgress(score*100/5);
        ////////////////////////////
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userRef.child("score").setValue(score*100/5)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(activity_score.this, "Score enregistré avec succès!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity_score.this, "Erreur lors de l'enregistrement du score: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        ////////////////////////
        blogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Merci pour votre participation",
                Toast.LENGTH_SHORT).show();
                startActivity(new Intent(activity_score.this,MainActivity.class));
            }
        });
        TryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_score.this,quiz1.class));
            }
        });
        rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_score.this, LeaderboardActivity.class);
                startActivity(intent);

            }
        });
    }
}