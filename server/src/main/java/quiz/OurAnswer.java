package quiz;

/**
 * A quiz question as used it's used in our API.
 * @author Fredrik Jeppsson
 */
public class OurAnswer {
    String text;
    String correct;

    public OurAnswer(String text, String correct) {
        this.text = text;
        this.correct = correct;
    }
}
