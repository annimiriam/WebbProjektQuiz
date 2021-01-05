package quiz;

import java.util.List;

/**
 * This class is a representation of the quiz questions used in our API
 * @author Fredrik Jeppsson
 */
public class OurQuizQuestion {
    public String question = "";
    public List<OurAnswer> answers;

    public OurQuizQuestion(String question, List<OurAnswer> answers) {
        this.question = question;
        this.answers = answers;
    }
}
