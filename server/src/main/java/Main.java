import static spark.Spark.*;

public class Main {
    private static String quizKey = Main.quizKeyFromEnv("quiz");

    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World");

    }

    private static String quizKeyFromEnv(String varName) {
        var key = System.getenv(varName);
        if (key == null) {
            System.err.println("Failed to load API key for quizapi.io");
            System.exit(1);
        }
        return key;
    }
}
