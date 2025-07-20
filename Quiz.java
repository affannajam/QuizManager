package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

/**
 * Quiz class to manage quiz questions and attempts
 */
public class Quiz implements Serializable {
    private String title;
    private Course course;
    private Date startTime;
    private int duration; // in minutes
    private ArrayList<Question> questions;
    private ArrayList<QuizAttempt> attempts;
    private HashMap<String, Integer> topicQuestionCounts;

    public Quiz(String title, Course course, Date startTime, int duration) {
        this.title = title;
        this.course = course;
        this.startTime = startTime;
        this.duration = duration;
        this.questions = new ArrayList<>();
        this.attempts = new ArrayList<>();
        this.topicQuestionCounts = new HashMap<>();
    }

    public void setTopicQuestionCount(String topic, int count) {
        topicQuestionCounts.put(topic, count);
    }

    public void generateQuestions(QuestionBank questionBank) {
        questions.clear();
        Random random = new Random();

        for (String topic : topicQuestionCounts.keySet()) {
            int count = topicQuestionCounts.get(topic);
            ArrayList<Question> topicQuestions = questionBank.getRandomQuestions(topic, count);
            questions.addAll(topicQuestions);
        }
    }

    public void addAttempt(QuizAttempt attempt) {
        attempts.add(attempt);
    }

    public void generateAnalytics() {
        if (attempts.isEmpty()) {
            System.out.println("No attempts found for this quiz!");
            return;
        }

        double totalScore = 0;
        double highestScore = Double.MIN_VALUE;
        double lowestScore = Double.MAX_VALUE;

        for (QuizAttempt attempt : attempts) {
            double score = attempt.getScore();
            totalScore += score;
            highestScore = Math.max(highestScore, score);
            lowestScore = Math.min(lowestScore, score);
        }

        double averageScore = totalScore / attempts.size();

        System.out.println("\nQuiz Analytics for " + title);
        System.out.println("Total Attempts: " + attempts.size());
        System.out.println("Average Score: " + String.format("%.2f", averageScore));
        System.out.println("Highest Score: " + String.format("%.2f", highestScore));
        System.out.println("Lowest Score: " + String.format("%.2f", lowestScore));

        // Generate score distribution graph
        int[] distribution = new int[10]; // 0-10, 11-20, ..., 91-100
        for (QuizAttempt attempt : attempts) {
            double score = attempt.getScore();
            int index = Math.min(9, (int)(score / 10));
            distribution[index]++;
        }

        System.out.println("\nScore Distribution:");
        for (int i = 0; i < 10; i++) {
            System.out.printf("%d-%d: %s%n", i * 10, (i + 1) * 10 - 1, "*".repeat(distribution[i]));
        }
    }

    public void generateAttendanceSheet() {
        System.out.println("\nAttendance Sheet for " + title);
        System.out.println("Course: " + course.getCode() + " - " + course.getName());
        System.out.println("Date: " + startTime);
        System.out.println("\nStudents Present:");

        if (attempts.isEmpty()) {
            System.out.println("No students attempted this quiz.");
            return;
        }

        for (QuizAttempt attempt : attempts) {
            Student student = attempt.getStudent();
            System.out.println(student.getName());
        }
    }

    public String getTitle() {
        return title;
    }

    public Course getCourse() {
        return course;
    }

    public Date getStartTime() {
        return startTime;
    }

    public int getDuration() {
        return duration;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public ArrayList<QuizAttempt> getAttempts() {
        return attempts;
    }
}