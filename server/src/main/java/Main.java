import quiz.OurAnswer;
import quiz.OurQuizQuestion;
import quiz.QuizQuestion;
import youtube.VideoItem;
import youtube.YoutubeSearchResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import quizresponse.QuizAndVideo;
import quizresponse.ResponseError;
import spark.Request;
import util.JsonTransformer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static spark.Spark.*;

/**
 * This class handles the requests to our API and the communication with the Quiz API and YouTube API.
 * @author Fredrik Jeppsson, Anni Johansson
 */
public class Main {
    private static String SERVER_ADDRESS = "http://localhost:4567/";
    private static String quizKey = Main.keyFromEnv("quiz");
    private static String youtubeKey = Main.keyFromEnv("youtube");
    private static Set<String> categories = Set.of("html", "php", "javascript", "wordpress");
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private static Map<String, QuizAndVideo> cache = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        List<String> categoryURIs = new ArrayList<>();
        for (var category : categories) {
            categoryURIs.add(SERVER_ADDRESS + "api/categories/" + category);
        }

        after((req, res) -> {
            // Enables CORS functionality.
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Headers", "Cache-Control, Pragma, Origin, Authorization, Content-Type, X-Requested-With");
            res.header("Access-Control-Allow-Methods", "GET, PUT, POST");
        });

        get("/api/categories", (req, res) -> {
            res.type("application/json");
            return categoryURIs;
        }, new JsonTransformer());

        get("/api/categories/:id", (req, res) -> {
            res.type("application/json");

            var id = req.params("id");

            if (!categories.contains(id)) {
                res.status(404);
                return new ResponseError("Could not find category: {" + id + "}");
            }

            /* The Youtube API has a limited number of usages per day at the free tier.
            We implement a cache that saves a particular response while the
            server is running to minimize the number of Youtube requests. */
            if (cache.containsKey(id)) {
                return cache.get(id);
            }

            HttpClient client = HttpClient.newHttpClient();

            var qURI = quizURI(req, id);
            HttpRequest quizRequest = HttpRequest.newBuilder()
                    .header("Accept", "application/json")
                    .header("x-api-key", quizKey)
                    .uri(URI.create(qURI))
                    .build();

            var ytURI = youtubeURI(req, id);
            HttpRequest youtubeRequest = HttpRequest.newBuilder()
                    .header("Accept", "application/json")
                    .uri(URI.create(ytURI))
                    .build();

            HttpResponse<String> quizResponse =
                    client.send(quizRequest, HttpResponse.BodyHandlers.ofString());

            HttpResponse<String> ytResponse =
                    client.send(youtubeRequest, HttpResponse.BodyHandlers.ofString());

            QuizQuestion[] questions = gson.fromJson(quizResponse.body(), QuizQuestion[].class);
            YoutubeSearchResult yt = gson.fromJson(ytResponse.body(), YoutubeSearchResult.class);
            QuizAndVideo quizAndVideo = extractQuizAndVideoData(questions, yt);

            cache.put(id, quizAndVideo);
            return quizAndVideo;
        }, new JsonTransformer());
    }

    // Returns the URI for the YouTube request
    private static String youtubeURI(Request req, String id) {
        var nbrOfVideos = req.queryParams("nbrOfVideos");
        String youtubeURI;
        try {
            Integer.parseInt(nbrOfVideos);
            youtubeURI = "https://youtube.googleapis.com/youtube/v3/search?part=snippet&maxResults="+
                    nbrOfVideos + "&q=" + id + "&key=" + youtubeKey;
        } catch (NumberFormatException e) {
            youtubeURI = "https://youtube.googleapis.com/youtube/v3/search?part=snippet&maxResults=5&q="
                    + id + "&key=" + youtubeKey;
        }
        return youtubeURI;
    }

    // Returns the URI for the QuizAPI request
    private static String quizURI(Request req, String id) {
        var nbrOfQuestions = req.queryParams("nbrOfQuestions");
        var quizURI = "";
        if (nbrOfQuestions == null) {
            quizURI = "https://quizapi.io/api/v1/questions?tags=" + id;
        } else {
            quizURI = "https://quizapi.io/api/v1/questions?tags=" + id + "&limit=" + nbrOfQuestions;
        }
        return quizURI;
    }

    // Combines the result of the Youtube and Quizapi.io responses into the
    // object that is returned from an api/categories/{category} request.
    private static QuizAndVideo extractQuizAndVideoData(QuizQuestion[] questions, YoutubeSearchResult ytResults) {
        // Extract videoID from the youtube search result.
        List<VideoItem> items = ytResults.items;
        List<String> videoIds = new ArrayList<>();
        for (var item : items) {
            videoIds.add(item.id.videoId);
        }

        // Transforming the quiz questions from quizapi.io into our own format.
        List<OurQuizQuestion> ourQuizQuestions = new ArrayList<>();
        for (var question : questions) {
            var questionText = question.question;
            var answers = question.answers;
            var correct_answers = question.correct_answers;

            /* The quizapi returns up to 6 questions (a to f). E.g. if a question has 3 answers, answers_a,
            answers_b and answers_c will have values and answers_d, _e and _f will be null.

            The null checks are necessary because even if an answer is null, the corresponding true/false
            'correct answer' field will exist. If the answers.answer_x field is null can throw away the
             correct_answers.answer_x_correct field */
            var ourAnswers = new ArrayList<OurAnswer>();
            if (answers.answer_a != null) {
                var answer = answers.answer_a;
                var correct = correct_answers.answer_a_correct;
                ourAnswers.add(new OurAnswer(answer, correct));
            }
            if (answers.answer_b != null) {
                var answer = answers.answer_b;
                var correct = correct_answers.answer_b_correct;
                ourAnswers.add(new OurAnswer(answer, correct));
            }
            if (answers.answer_c != null) {
                var answer = answers.answer_c;
                var correct = correct_answers.answer_c_correct;
                ourAnswers.add(new OurAnswer(answer, correct));
            }
            if (answers.answer_d != null) {
                var answer = answers.answer_d;
                var correct = correct_answers.answer_d_correct;
                ourAnswers.add(new OurAnswer(answer, correct));
            }
            if (answers.answer_e != null) {
                var answer = answers.answer_e;
                var correct = correct_answers.answer_e_correct;
                ourAnswers.add(new OurAnswer(answer, correct));
            }
            if (answers.answer_f != null) {
                var answer = answers.answer_f;
                var correct = correct_answers.answer_f_correct;
                ourAnswers.add(new OurAnswer(answer, correct));
            }

            var ourQuestion = new OurQuizQuestion(questionText, ourAnswers);
            ourQuizQuestions.add(ourQuestion);
        }

        return new QuizAndVideo(ourQuizQuestions, videoIds);
    }

    // Gets the API-key for the Quiz-API and YouTube request from an environment variable
    private static String keyFromEnv(String varName) {
        var key = System.getenv(varName);
        if (key == null) {
            System.err.println("Failed to load environmental variable: " + varName);
            System.exit(1);
        }
        return key;
    }
}




