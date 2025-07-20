package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Teacher class representing a teacher user in the system
 */
public class Teacher extends User implements Serializable {
    private ArrayList<Course> assignedCourses;
    private HashMap<Course, QuestionBank> questionBanks;
    private ArrayList<Quiz> quizzes;

    public Teacher(String username, String password, String name) {
        super(username, password, name);
        this.assignedCourses = new ArrayList<>();
        this.questionBanks = new HashMap<>();
        this.quizzes = new ArrayList<>();
    }

    public void assignCourse(Course course) {
        if (!assignedCourses.contains(course)) {
            assignedCourses.add(course);
        }
    }

    public void createQuestionBank(Course course) {
        if (!questionBanks.containsKey(course)) {
            questionBanks.put(course, new QuestionBank(course));
        }
    }

    public void addQuestion(Course course, Question question) {
        QuestionBank questionBank = questionBanks.get(course);
        if (questionBank != null) {
            questionBank.addQuestion(question);
        }
    }

    public Quiz createQuiz(Course course, String title, Date startTime, int duration) {
        QuestionBank questionBank = questionBanks.get(course);
        if (questionBank != null) {
            Quiz quiz = new Quiz(title, course, startTime, duration);
            // Set default question count for each topic
            for (String topic : questionBank.getTopics()) {
                quiz.setTopicQuestionCount(topic, 2); // Default 2 questions per topic
            }
            quiz.generateQuestions(questionBank);
            quizzes.add(quiz);
            ExamSystem.getInstance().addQuiz(quiz); // Add quiz to ExamSystem
            return quiz;
        }
        return null;
    }

    public ArrayList<Quiz> getQuizzes() {
        return quizzes;
    }

    public ArrayList<Course> getAssignedCourses() {
        return assignedCourses;
    }

    public QuestionBank getQuestionBank(Course course) {
        return questionBanks.get(course);
    }
}