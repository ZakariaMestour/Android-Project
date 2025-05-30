package com.example.control.dynamicQuestions;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.control.R;
import com.example.control.activity_score;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private List<Question> questionList;
    private int currentIndex = 0;
    private int score = 0;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 12000;

    // UI Elements
    private TextView questionTextView, timerTextView, questionNumberTextView;
    private ImageView imageView;
    private RadioGroup rg;
    private Button nextButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialisation des vues
        initializeViews();

        // Récupérer le score initial (si venant d'une autre activité)
        score = getIntent().getIntExtra("Score", 0);

        // Initialiser les questions
        questionList = getQuestions();

        // Afficher la première question
        showQuestion();

        // Listener pour le bouton suivant
        nextButton.setOnClickListener(v -> {
            if (rg.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Veuillez choisir une réponse !", Toast.LENGTH_SHORT).show();
            } else {
                handleAnswer();
            }
        });
    }

    private void initializeViews() {
        questionTextView = findViewById(R.id.textView3);
        timerTextView = findViewById(R.id.timer);
        imageView = findViewById(R.id.imageView);
        rg = findViewById(R.id.rg);
        nextButton = findViewById(R.id.button2);
        questionNumberTextView = findViewById(R.id.questionNumber);
        progressBar = findViewById(R.id.progressBar);

        // Configurer la barre de progression
        if (progressBar != null) {
            progressBar.setMax(questionList != null ? questionList.size() : 5);
        }
    }

    private void showQuestion() {
        if (currentIndex >= questionList.size()) {
            finishQuiz();
            return;
        }

        Question currentQuestion = questionList.get(currentIndex);

        // Afficher le texte de la question
        questionTextView.setText(currentQuestion.getQuestionText());

        // Afficher l'image si elle existe
        if (currentQuestion.getImageResId() != 0) {
            imageView.setImageResource(currentQuestion.getImageResId());
            imageView.setVisibility(ImageView.VISIBLE);
        } else {
            imageView.setVisibility(ImageView.GONE);
        }

        // Afficher le numéro de question
        if (questionNumberTextView != null) {
            questionNumberTextView.setText("Question " + (currentIndex + 1) + "/" + questionList.size());
        }

        // Mettre à jour la barre de progression
        if (progressBar != null) {
            progressBar.setProgress(currentIndex + 1);
        }

        // Créer les options dynamiquement
        createRadioButtons(currentQuestion.getOptions());

        // Mettre à jour le texte du bouton
        if (currentIndex == questionList.size() - 1) {
            nextButton.setText("Terminer");
        } else {
            nextButton.setText("Suivant");
        }

        // Démarrer le timer
        startTimer();
    }

    private void createRadioButtons(String[] options) {
        rg.removeAllViews();

        for (String option : options) {
            RadioButton rb = new RadioButton(this);
            rb.setText(option);
            rb.setTextSize(18);
            rb.setPadding(32, 24, 32, 24);
            rb.setButtonTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary));

            // Appliquer le background personnalisé si disponible
            try {
                rb.setBackgroundResource(R.drawable.radio_button_background);
            } catch (Exception e) {
                // Si le drawable n'existe pas, utiliser un style par défaut
                rb.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
            }

            // Ajouter des marges
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 24);
            rb.setLayoutParams(params);

            rg.addView(rb);
        }
    }

    private void handleAnswer() {
        // Annuler le timer
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Vérifier la réponse
        RadioButton selectedButton = findViewById(rg.getCheckedRadioButtonId());
        String selectedAnswer = selectedButton.getText().toString();
        String correctAnswer = questionList.get(currentIndex).getCorrectAnswer();

        if (selectedAnswer.equals(correctAnswer)) {
            score++;
            // Optionnel : afficher un feedback positif
            Toast.makeText(this, "Bonne réponse !", Toast.LENGTH_SHORT).show();
        } else {
            // Optionnel : afficher la bonne réponse
            Toast.makeText(this, "Mauvaise réponse. La bonne réponse était : " + correctAnswer, Toast.LENGTH_SHORT).show();
        }

        // Passer à la question suivante après un petit délai
        nextButton.postDelayed(() -> {
            currentIndex++;
            showQuestion();
        }, 1500); // Délai de 1.5 secondes pour laisser voir le feedback
    }

    private void startTimer() {
        timeLeftInMillis = 12000; // Reset à 12 secondes
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(), "Temps écoulé !", Toast.LENGTH_SHORT).show();
                // Passer automatiquement à la question suivante
                currentIndex++;
                showQuestion();
            }
        }.start();
    }

    private void updateTimerText() {
        int seconds = (int) (timeLeftInMillis / 1000);
        String timeFormatted = String.format("00:%02d", seconds);
        timerTextView.setText(timeFormatted);

        // Changer la couleur si le temps est critique
        if (seconds <= 5) {
            timerTextView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
        } else {
            timerTextView.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        }
    }

    private void finishQuiz() {
        // Annuler le timer s'il est actif
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Sauvegarder le score dans Firebase (si nécessaire)
        saveScoreToFirebase();

        // Aller vers l'activité de score
        Intent resultIntent = new Intent(QuizActivity.this, activity_score.class);
        resultIntent.putExtra("Score", score);
        resultIntent.putExtra("TotalQuestions", questionList.size());
        startActivity(resultIntent);
        finish();
    }

    private void saveScoreToFirebase() {
        try {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Leaderboard");

                // Créer un objet score avec timestamp
                ScoreData scoreData = new ScoreData(email, score, questionList.size(), System.currentTimeMillis());

                // Sauvegarder dans Firebase
                dbRef.child(userId).setValue(scoreData);
            }
        } catch (Exception e) {
            // Gérer l'erreur silencieusement
            e.printStackTrace();
        }
    }

    private List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();

        // Question 1 (équivalent à quiz1)
        questions.add(new Question(
                "Quelle est la planète la plus proche du Soleil ?",
                new String[]{"Vénus", "Mars", "Mercure", "Terre"},
                "Mercure",
                R.drawable.imageq1 // Assurez-vous que cette image existe
        ));

        // Question 2
        questions.add(new Question(
                "Quelle planète est la plus grande du système solaire ?",
                new String[]{"Terre", "Jupiter", "Saturne", "Uranus"},
                "Jupiter",
                R.drawable.imageq2
        ));

        // Question 3
        questions.add(new Question(
                "Combien de lunes a la planète Mars ?",
                new String[]{"0", "1", "2", "3"},
                "2",
                R.drawable.backgroudquestions3
        ));

        // Question 4
        questions.add(new Question(
                "Quelle est la galaxie la plus proche de la Voie lactée ?",
                new String[]{"Andromède", "Centaure", "Orion", "Cassiopée"},
                "Andromède",
                R.drawable.backgroudquestions4
        ));

        // Question 5 (équivalent à quiz5)
        questions.add(new Question(
                "Quel instrument utilise-t-on pour observer les étoiles ?",
                new String[]{"Microscope", "Télescope", "Périscope", "Stéthoscope"},
                "Télescope",
                R.drawable.backgroudquestions5
        ));

        return questions;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    // Classe interne pour les données de score
    private static class ScoreData {
        public String email;
        public int score;
        public int totalQuestions;
        public long timestamp;

        public ScoreData() {} // Constructeur vide requis pour Firebase

        public ScoreData(String email, int score, int totalQuestions, long timestamp) {
            this.email = email;
            this.score = score;
            this.totalQuestions = totalQuestions;
            this.timestamp = timestamp;
        }
    }

}