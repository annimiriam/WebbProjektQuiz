package quiz;

/**
 * This class is a representation of the quiz questions used in our API
 * @author Fredrik Jeppsson
 */
public class OurQuizQuestion {
    public String question = "";
    public Answers answers;
    public String multiple_correct_answers = "";
    public CorrectAnswers correct_answers;

    public OurQuizQuestion(String question, Answers answers, String multiple_correct_answers,
                           CorrectAnswers correct_answers) {
        this.question = question;
        this.answers = answers;
        this.multiple_correct_answers = multiple_correct_answers;
        this.correct_answers = correct_answers;
    }
}
