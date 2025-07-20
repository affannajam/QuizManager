package main;

import java.io.Serializable;
import java.util.HashMap;

/**
 * QuizAttempt class to track student answers and calculate scores
 */
public class QuizAttempt implements Serializable {
    private Quiz quiz;
    private Student student;
    private HashMap<Question, String> answers;
    private double score;
    private boolean scored;

    public QuizAttempt(Quiz quiz, Student student, HashMap<Question, String> answers) {
        this.quiz = quiz;
        this.student = student;
        this.answers = answers;
        this.scored = false;
    }

    public double calculateScore() {
        if (!scored) {
            score = 0.0;
            for (Question question : quiz.getQuestions()) {
                String answer = answers.get(question);
                if (answer != null && question.checkAnswer(answer)) {
                    score += question.getMarks();
                }
            }
            scored = true;
        }
        return score;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public Student getStudent() {
        return student;
    }

    public HashMap<Question, String> getAnswers() {
        return answers;
    }

    public double getScore() {
        return calculateScore();
    }
}