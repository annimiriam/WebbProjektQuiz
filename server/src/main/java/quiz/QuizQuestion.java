package quiz;

/**
 * This class represents a question for a quiz
 *
 * @author Anni Johansson
 */
public class QuizQuestion {

    public int id = 0;
    public String question = "";
    public String description = "";
    public Answers answers = new Answers();
    public String multiple_correct_answers = "";
    public CorrectAnswers correct_answers = new CorrectAnswers();
    public String explanation = "";
    public String tip = "";
    public Tags[] tags;
    public String category = "";
    public String difficulty = "";

    public QuizQuestion()
    {

    }
}
