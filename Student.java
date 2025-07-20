package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Student class representing a student user in the system
 */
public class Student extends User implements Serializable {
    private ArrayList<Course> enrolledCourses;
    private ArrayList<QuizAttempt> quizAttempts;

    public Student(String username, String password, String name) {
        super(username, password, name);
        this.enrolledCourses = new ArrayList<>();
        this.quizAttempts = new ArrayList<>();
    }

    public void enrollCourse(Course course) {
        if (!enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
        }
    }

    public QuizAttempt attemptQuiz(Quiz quiz, HashMap<Question, String> answers) {
        Date now = new Date();
        if (now.before(quiz.getStartTime())) {
            System.out.println("Quiz has not started yet!");
            return null;
        }

        Date endTime = new Date(quiz.getStartTime().getTime() + quiz.getDuration() * 60 * 1000);
        if (now.after(endTime)) {
            System.out.println("Quiz has ended!");
            return null;
        }

        QuizAttempt attempt = new QuizAttempt(quiz, this, answers);
        quizAttempts.add(attempt);
        quiz.addAttempt(attempt);  // Add this line to register the attempt with the quiz
        return attempt;
    }

    public void viewResults() {
        if (quizAttempts.isEmpty()) {
            System.out.println("No quiz attempts found!");
            return;
        }

        System.out.println("\nQuiz Results:");
        for (QuizAttempt attempt : quizAttempts) {
            Quiz quiz = attempt.getQuiz();
            System.out.println("Quiz: " + quiz.getTitle() + " - " + quiz.getCourse().getCode());
            System.out.println("Score: " + attempt.calculateScore());
            System.out.println();
        }
    }

    public ArrayList<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public ArrayList<QuizAttempt> getQuizAttempts() {
        return quizAttempts;
    }
}