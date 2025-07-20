package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * QuestionBank class to manage questions for a course
 */
public class QuestionBank implements Serializable {
    private Course course;
    private HashMap<String, ArrayList<Question>> questionsByTopic;

    public QuestionBank(Course course) {
        this.course = course;
        this.questionsByTopic = new HashMap<>();
    }

    public void addQuestion(Question question) {
        String topic = question.getTopic();
        questionsByTopic.putIfAbsent(topic, new ArrayList<>());
        questionsByTopic.get(topic).add(question);
    }

    public ArrayList<Question> getRandomQuestions(String topic, int count) {
        ArrayList<Question> result = new ArrayList<>();
        ArrayList<Question> topicQuestions = questionsByTopic.get(topic);

        if (topicQuestions == null || topicQuestions.isEmpty()) {
            return result;
        }

        Random random = new Random();
        ArrayList<Question> availableQuestions = new ArrayList<>(topicQuestions);
        int selectedCount = Math.min(count, availableQuestions.size());

        for (int i = 0; i < selectedCount; i++) {
            int index = random.nextInt(availableQuestions.size());
            result.add(availableQuestions.remove(index));
        }

        return result;
    }

    public Course getCourse() {
        return course;
    }

    public ArrayList<String> getTopics() {
        return new ArrayList<>(questionsByTopic.keySet());
    }

    public int getQuestionCount(String topic) {
        ArrayList<Question> questions = questionsByTopic.get(topic);
        return questions != null ? questions.size() : 0;
    }
}