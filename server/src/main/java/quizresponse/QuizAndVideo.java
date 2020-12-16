package quizresponse;

import Quiz.QuizQuestion;

import java.util.List;

public class QuizAndVideo {
    public QuizAndVideo(List<QuizQuestion> questions, List<String> videoLinks) {
        this.questions = questions;
        this.videoLinks = videoLinks;
    }

    public List<QuizQuestion> questions;
    public List<String> videoLinks;
}
