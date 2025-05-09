package com.example.control.classement;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.control.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class LeaderboardActivity extends AppCompatActivity {
    private ListView listViewLeaderboard;
    private Button buttonBack;
    private ArrayList<User> userList;
    private LeaderboardAdapter adapter;
    private DatabaseReference databaseReference;
    private String currentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_leaderboard);
        View rootView = findViewById(android.R.id.content);
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
// Obtenir l'ID de l'utilisateur actuel
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        } else {
            currentUserId = ""; // Si l'utilisateur n'est pas connecté
        }

        // Initialiser Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Initialiser les vues
        listViewLeaderboard = findViewById(R.id.listViewLeaderboard);
        buttonBack = findViewById(R.id.buttonBackToScore);

        // Initialiser la liste des utilisateurs
        userList = new ArrayList<>();
        adapter = new LeaderboardAdapter(this, userList, currentUserId);
        listViewLeaderboard.setAdapter(adapter);

        // Charger les données depuis Firebase
        loadLeaderboardData();

        // Gérer le bouton retour
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Ferme l'activité actuelle et revient à l'activité précédente
            }
        });
    }

    private void loadLeaderboardData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Récupérer les données utilisateur de Firebase
                    String userId = snapshot.getKey();
                    String username = snapshot.child("username").getValue(String.class);
                    String email = "";

                    // Récupérer l'email s'il existe
                    if (snapshot.hasChild("email")) {
                        email = snapshot.child("email").getValue(String.class);
                    }

                    // Vérifier si le champ score existe
                    Integer score = 0;
                    if (snapshot.hasChild("score")) {
                        score = snapshot.child("score").getValue(Integer.class);
                        if (score == null) {
                            score = 0;
                        }
                    }

                    User user = new User(userId, username, email, score);
                    userList.add(user);
                }

                // Trier les utilisateurs par score (du plus élevé au plus bas)
                Collections.sort(userList, new Comparator<User>() {
                    @Override
                    public int compare(User user1, User user2) {
                        return user2.getScore() - user1.getScore();
                    }
                });

                // Mettre à jour l'adaptateur
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LeaderboardActivity.this, "Erreur lors du chargement du classement: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("LeaderboardActivity", "Erreur de base de données: " + databaseError.getMessage());
            }
        });
    }
}