package quizresponse;

import Quiz.OurQuizQuestion;

import java.util.List;

public class QuizAndVideo {
    public List<OurQuizQuestion> questions;
    public List<String> videoLinks;

    public QuizAndVideo(List<OurQuizQuestion> questions, List<String> videoLinks) {
        this.questions = questions;
        this.videoLinks = videoLinks;
    }
}
