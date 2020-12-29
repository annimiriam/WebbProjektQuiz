package quizresponse;

import Quiz.OurQuizQuestion;

import java.util.List;

/**
 * This class the questions and the video-id connected to a category
 * @author Fredrik Jeppsson
 */
public class QuizAndVideo {
    public List<OurQuizQuestion> questions;
    public List<String> videoLinks;

    public QuizAndVideo(List<OurQuizQuestion> questions, List<String> videoLinks) {
        this.questions = questions;
        this.videoLinks = videoLinks;
    }
}
