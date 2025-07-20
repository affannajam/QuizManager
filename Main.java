package main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.HashMap;

/**
 * Main class with console-based user interface
 */
public class Main {
    private static ExamSystem examSystem;
    private static Scanner scanner;
    private static SimpleDateFormat dateFormat;

    public static void main(String[] args) {
        examSystem = ExamSystem.getInstance();
        scanner = new Scanner(System.in);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        while (true) {
            System.out.println("\nExamination System");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    handleRegistration();
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static void handleLogin() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = examSystem.login(username, password);
        if (user != null) {
            if (user instanceof Teacher) {
                handleTeacherMenu((Teacher) user);
            } else if (user instanceof Student) {
                handleStudentMenu((Student) user);
            }
        } else {
            System.out.println("Invalid credentials!");
        }
    }

    private static void handleRegistration() {
        System.out.println("\nRegistration");
        System.out.println("1. Register as Teacher");
        System.out.println("2. Register as Student");
        System.out.print("Choose an option: ");

        int choice = getIntInput();
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();

        switch (choice) {
            case 1:
                examSystem.registerTeacher(username, password, name);
                System.out.println("Teacher registered successfully!");
                break;
            case 2:
                examSystem.registerStudent(username, password, name);
                System.out.println("Student registered successfully!");
                break;
            default:
                System.out.println("Invalid option!");
        }
    }

    private static void handleTeacherMenu(Teacher teacher) {
        while (true) {
            System.out.println("\nTeacher Menu");
            System.out.println("1. Create Question Bank");
            System.out.println("2. Create Quiz");
            System.out.println("3. View Quiz Analytics");
            System.out.println("4. Generate Attendance Sheet");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                    handleCreateQuestionBank(teacher);
                    break;
                case 2:
                    handleCreateQuiz(teacher);
                    break;
                case 3:
                    handleViewQuizAnalytics(teacher);
                    break;
                case 4:
                    handleGenerateAttendanceSheet(teacher);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static void handleStudentMenu(Student student) {
        while (true) {
            System.out.println("\nStudent Menu");
            System.out.println("1. View Available Quizzes");
            System.out.println("2. Attempt Quiz");
            System.out.println("3. View Results");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                    handleViewAvailableQuizzes(student);
                    break;
                case 2:
                    handleAttemptQuiz(student);
                    break;
                case 3:
                    student.viewResults();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static void handleCreateQuestionBank(Teacher teacher) {
        ArrayList<Course> courses = examSystem.getCourses();
        System.out.println("\nAvailable Courses:");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i));
        }

        System.out.print("Select course number: ");
        int courseIndex = getIntInput() - 1;

        if (courseIndex >= 0 && courseIndex < courses.size()) {
            Course course = courses.get(courseIndex);
            teacher.createQuestionBank(course);
            
            while (true) {
                System.out.println("\nAdd Question:");
                System.out.println("1. MCQ");
                System.out.println("2. True/False");
                System.out.println("3. Subjective");
                System.out.println("4. Finish");
                System.out.print("Choose question type: ");

                int type = getIntInput();
                if (type == 4) break;

                System.out.print("Enter question text: ");
                String questionText = scanner.nextLine();
                System.out.print("Enter marks: ");
                double marks = Double.parseDouble(scanner.nextLine());
                System.out.print("Enter topic: ");
                String topic = scanner.nextLine();

                Question question;
                switch (type) {
                    case 1:
                        ArrayList<String> options = new ArrayList<>();
                        System.out.print("Enter number of options: ");
                        int optionCount = getIntInput();
                        for (int i = 0; i < optionCount; i++) {
                            System.out.print("Option " + (i + 1) + ": ");
                            options.add(scanner.nextLine());
                        }
                        System.out.print("Enter correct option number: ");
                        int correctOption = getIntInput() - 1;
                        question = new MCQQuestion(questionText, marks, topic, options, correctOption);
                        break;

                    case 2:
                        System.out.print("Enter correct answer (true/false): ");
                        boolean correctAnswer = Boolean.parseBoolean(scanner.nextLine());
                        question = new TrueFalseQuestion(questionText, marks, topic, correctAnswer);
                        break;

                    case 3:
                        System.out.print("Enter model answer: ");
                        String modelAnswer = scanner.nextLine();
                        question = new SubjectiveQuestion(questionText, marks, topic, modelAnswer);
                        break;

                    default:
                        System.out.println("Invalid question type!");
                        continue;
                }

                teacher.addQuestion(course, question);
                System.out.println("Question added successfully!");
            }
        } else {
            System.out.println("Invalid course selection!");
        }
    }

    private static void handleCreateQuiz(Teacher teacher) {
        try {
            System.out.print("Enter quiz title: ");
            String title = scanner.nextLine();

            ArrayList<Course> courses = examSystem.getCourses();
            System.out.println("\nAvailable Courses:");
            for (int i = 0; i < courses.size(); i++) {
                System.out.println((i + 1) + ". " + courses.get(i));
            }

            System.out.print("Select course number: ");
            int courseIndex = getIntInput() - 1;

            if (courseIndex >= 0 && courseIndex < courses.size()) {
                Course course = courses.get(courseIndex);
                QuestionBank questionBank = teacher.getQuestionBank(course);
                
                if (questionBank == null) {
                    System.out.println("Please create a question bank for this course first!");
                    return;
                }

                if (questionBank.getTopics().isEmpty()) {
                    System.out.println("Please add questions to the question bank first!");
                    return;
                }

                System.out.print("Enter quiz date and time (dd/MM/yyyy HH:mm): ");
                String dateStr = scanner.nextLine();
                Date startTime = dateFormat.parse(dateStr);

                System.out.print("Enter duration in minutes: ");
                int duration = getIntInput();

                Quiz quiz = teacher.createQuiz(course, title, startTime, duration);
                if (quiz != null) {
                    System.out.println("Quiz created successfully!");
                } else {
                    System.out.println("Failed to create quiz!");
                }
            } else {
                System.out.println("Invalid course selection!");
            }
        } catch (ParseException e) {
            System.out.println("Invalid date format!");
        }
    }

    private static void handleViewQuizAnalytics(Teacher teacher) {
        ArrayList<Quiz> quizzes = teacher.getQuizzes();
        if (quizzes.isEmpty()) {
            System.out.println("No quizzes available!");
            return;
        }

        System.out.println("\nSelect a quiz to view analytics:");
        for (int i = 0; i < quizzes.size(); i++) {
            System.out.println((i + 1) + ". " + quizzes.get(i).getTitle());
        }

        System.out.print("Enter quiz number: ");
        int quizIndex = getIntInput() - 1;

        if (quizIndex >= 0 && quizIndex < quizzes.size()) {
            Quiz quiz = quizzes.get(quizIndex);
            quiz.generateAnalytics();
        } else {
            System.out.println("Invalid quiz selection!");
        }
    }

    private static void handleGenerateAttendanceSheet(Teacher teacher) {
        ArrayList<Quiz> quizzes = teacher.getQuizzes();
        if (quizzes.isEmpty()) {
            System.out.println("No quizzes available!");
            return;
        }

        System.out.println("\nSelect a quiz to generate attendance sheet:");
        for (int i = 0; i < quizzes.size(); i++) {
            System.out.println((i + 1) + ". " + quizzes.get(i).getTitle());
        }

        System.out.print("Enter quiz number: ");
        int quizIndex = getIntInput() - 1;

        if (quizIndex >= 0 && quizIndex < quizzes.size()) {
            Quiz quiz = quizzes.get(quizIndex);
            quiz.generateAttendanceSheet();
        } else {
            System.out.println("Invalid quiz selection!");
        }
    }

    private static void handleViewAvailableQuizzes(Student student) {
        ArrayList<Quiz> quizzes = examSystem.getAvailableQuizzes();
        if (quizzes.isEmpty()) {
            System.out.println("No quizzes available!");
            return;
        }

        System.out.println("\nAvailable Quizzes:");
        for (int i = 0; i < quizzes.size(); i++) {
            Quiz quiz = quizzes.get(i);
            System.out.println((i + 1) + ". " + quiz.getTitle() + " - " + quiz.getCourse().getCode());
            System.out.println("   Start: " + dateFormat.format(quiz.getStartTime()));
            System.out.println("   Duration: " + quiz.getDuration() + " minutes");
        }
    }

    private static void handleAttemptQuiz(Student student) {
        ArrayList<Quiz> quizzes = examSystem.getAvailableQuizzes();
        if (quizzes.isEmpty()) {
            System.out.println("No quizzes available!");
            return;
        }

        System.out.println("\nAvailable Quizzes:");
        for (int i = 0; i < quizzes.size(); i++) {
            Quiz quiz = quizzes.get(i);
            System.out.println((i + 1) + ". " + quiz.getTitle() + " - " + quiz.getCourse().getCode());
        }

        System.out.print("Select quiz number: ");
        int quizIndex = getIntInput() - 1;

        if (quizIndex >= 0 && quizIndex < quizzes.size()) {
            Quiz quiz = quizzes.get(quizIndex);
            ArrayList<Question> questions = quiz.getQuestions();
            HashMap<Question, String> answers = new HashMap<>();

            System.out.println("\nQuiz: " + quiz.getTitle());
            System.out.println("Duration: " + quiz.getDuration() + " minutes");
            System.out.println("Press Enter to start the quiz...");
            scanner.nextLine();

            for (Question question : questions) {
                System.out.println("\n" + question.getText());
                if (question instanceof MCQQuestion) {
                    MCQQuestion mcq = (MCQQuestion) question;
                    ArrayList<String> options = mcq.getOptions();
                    for (int i = 0; i < options.size(); i++) {
                        System.out.println((i + 1) + ". " + options.get(i));
                    }
                    System.out.print("Your answer (enter option number): ");
                    int answerIndex = getIntInput() - 1;
                    if (answerIndex >= 0 && answerIndex < options.size()) {
                        answers.put(question, String.valueOf(answerIndex));
                    }
                } else if (question instanceof TrueFalseQuestion) {
                    System.out.print("Your answer (true/false): ");
                    String answer = scanner.nextLine().toLowerCase();
                    if (answer.equals("true") || answer.equals("false")) {
                        answers.put(question, answer);
                    }
                } else if (question instanceof SubjectiveQuestion) {
                    System.out.print("Your answer: ");
                    String answer = scanner.nextLine();
                    answers.put(question, answer);
                }
            }

            QuizAttempt attempt = student.attemptQuiz(quiz, answers);
            if (attempt != null) {
                System.out.println("\nQuiz submitted successfully!");
                System.out.println("Score: " + attempt.calculateScore());
            } else {
                System.out.println("Failed to submit quiz!");
            }
        } else {
            System.out.println("Invalid quiz selection!");
        }
    }

    private static int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}