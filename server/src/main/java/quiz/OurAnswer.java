package quiz;

/**
 * A quiz question as used it's used in our API.
 * @author Fredrik Jeppsson
 */
public class OurAnswer {
    String text;
    boolean correct;

    public OurAnswer(String text, boolean correct) {
        this.text = text;
        this.correct = correct;
    }
}
