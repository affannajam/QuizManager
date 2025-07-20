package main;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Main system class to manage users and courses
 */
public class ExamSystem implements Serializable {
    private ArrayList<User> users;
    private ArrayList<Course> courses;
    private ArrayList<Quiz> quizzes;
    private static final String DATA_FILE = "exam_system.dat";
    private static volatile ExamSystem instance;
    private static final long serialVersionUID = 1L;

    public static synchronized ExamSystem getInstance() {
        if (instance == null) {
            synchronized (ExamSystem.class) {
                if (instance == null) {
                    File file = new File(DATA_FILE);
                    if (file.exists()) {
                        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                            instance = (ExamSystem) in.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            instance = new ExamSystem();
                        }
                    } else {
                        instance = new ExamSystem();
                    }
                }
            }
        }
        return instance;
    }

    private Object readResolve() {
        instance = this;
        return instance;
    }

    private ExamSystem() {
        users = new ArrayList<>();
        courses = new ArrayList<>();
        quizzes = new ArrayList<>();
        initializeCourses();
    }

    private void initializeCourses() {
        courses.add(new Course("CS101", "Introduction to Programming", "Theory"));
        courses.add(new Course("CS102", "Object Oriented Programming", "Theory"));
        courses.add(new Course("CS103", "Data Structures", "Theory"));
        courses.add(new Course("CS104", "Database Systems", "Theory"));
        courses.add(new Course("CS105", "Operating Systems", "Theory"));
        courses.add(new Course("CS106", "Computer Networks", "Theory"));
        courses.add(new Course("CS107", "Software Engineering", "Theory"));
    }

    public void registerTeacher(String username, String password, String name) {
        Teacher teacher = new Teacher(username, password, name);
        users.add(teacher);
        saveData();
    }

    public void registerStudent(String username, String password, String name) {
        Student student = new Student(username, password, name);
        users.add(student);
        saveData();
    }

    public User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.checkPassword(password)) {
                return user;
            }
        }
        return null;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public ArrayList<Quiz> getAvailableQuizzes() {
        ArrayList<Quiz> availableQuizzes = new ArrayList<>();
        Date now = new Date();

        for (Quiz quiz : quizzes) {
            if (quiz.getStartTime().before(now) && 
                now.before(new Date(quiz.getStartTime().getTime() + quiz.getDuration() * 60 * 1000))) {
                availableQuizzes.add(quiz);
            }
        }

        return availableQuizzes;
    }

    public void addQuiz(Quiz quiz) {
        quizzes.add(quiz);
        saveData();
    }

    private void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(this);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
}