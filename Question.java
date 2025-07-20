package main;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Abstract Question class with subclasses for different question types
 */
public abstract class Question implements Serializable {
    private String text;
    private double marks;
    private String topic;

    public Question(String text, double marks, String topic) {
        this.text = text;
        this.marks = marks;
        this.topic = topic;
    }

    public abstract boolean checkAnswer(String answer);
    public abstract String getCorrectAnswer();
    public abstract String getType();

    public String getText() {
        return text;
    }

    public double getMarks() {
        return marks;
    }

    public String getTopic() {
        return topic;
    }
}

/**
 * MCQ Question with multiple choice options
 */
class MCQQuestion extends Question {
    private ArrayList<String> options;
    private int correctOptionIndex;

    public MCQQuestion(String text, double marks, String topic, ArrayList<String> options, int correctOptionIndex) {
        super(text, marks, topic);
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    @Override
    public boolean checkAnswer(String answer) {
        try {
            int selectedOption = Integer.parseInt(answer);
            return selectedOption == correctOptionIndex;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String getCorrectAnswer() {
        return String.valueOf(correctOptionIndex);
    }

    @Override
    public String getType() {
        return "MCQ";
    }

    public ArrayList<String> getOptions() {
        return options;
    }
}

/**
 * True/False Question
 */
class TrueFalseQuestion extends Question {
    private boolean correctAnswer;

    public TrueFalseQuestion(String text, double marks, String topic, boolean correctAnswer) {
        super(text, marks, topic);
        this.correctAnswer = correctAnswer;
    }

    @Override
    public boolean checkAnswer(String answer) {
        return Boolean.parseBoolean(answer) == correctAnswer;
    }

    @Override
    public String getCorrectAnswer() {
        return String.valueOf(correctAnswer);
    }

    @Override
    public String getType() {
        return "True/False";
    }
}

/**
 * Subjective Question with model answer
 */
class SubjectiveQuestion extends Question {
    private String modelAnswer;

    public SubjectiveQuestion(String text, double marks, String topic, String modelAnswer) {
        super(text, marks, topic);
        this.modelAnswer = modelAnswer;
    }

    @Override
    public boolean checkAnswer(String answer) {
        // For subjective questions, manual checking is required
        // This is a simplified implementation
        return answer != null && !answer.trim().isEmpty();
    }

    @Override
    public String getCorrectAnswer() {
        return modelAnswer;
    }

    @Override
    public String getType() {
        return "Subjective";
    }
}