package com.example.control.dynamicQuestions;

public class Question {
    private String questionText;
    private String[] options;
    private String correctAnswer;
    private int imageResId;

    public Question(String questionText, String[] options, String correctAnswer, int imageResId) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.imageResId = imageResId;
    }

    public String getQuestionText() { return questionText; }
    public String[] getOptions() { return options; }
    public String getCorrectAnswer() { return correctAnswer; }
    public int getImageResId() { return imageResId; }
}
