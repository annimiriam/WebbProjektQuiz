import Quiz.OurQuizQuestion;
import Quiz.QuizQuestion;
import YouTube.VideoItem;
import YouTube.YoutubeSearchResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import quizresponse.QuizAndVideo;
import spark.Request;
import spark.ResponseTransformer;

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


        get("/api/categories", (req, res) -> {
            res.type("application/json");
            return categoryURIs;
        }, new JsonTransformer());

        get("/api/categories/:id", (req, res) -> {
            res.type("application/json");

            // todo: place in its own thing that's always run before response is sent. see Spark Java documentation.
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Headers", "Cache-Control, Pragma, Origin, Authorization, Content-Type, X-Requested-With");
            res.header("Access-Control-Allow-Methods", "GET, PUT, POST");
            var id = req.params("id");

            if (!categories.contains(id)) {
                // If we get here the api has been called at an invalid URL.
                // Use some kind of http status code instead of this string.
                res.status(404);
                // todo: looks ugly.
                // todo: fix this todo.
                return "couldn't find category.";
            }

            // Protection against calling the Youtube API too many times and exhausting our quota.
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

    private static QuizAndVideo extractQuizAndVideoData(QuizQuestion[] questions, YoutubeSearchResult ytResults) {
        List<VideoItem> items = ytResults.items;
        List<String> videoIds = new ArrayList<>();
        for (var item : items) {
            videoIds.add(item.id.videoId);
        }

        List<OurQuizQuestion> ourQuizQuestions = new ArrayList<>();
        for (var question : questions) {
            var quest = question.question;
            var answers = question.answers;
            var multiple_correct = question.multiple_correct_answers;
            var correct_answers = question.correct_answers;
            var ourQuestion = new OurQuizQuestion(quest, answers, multiple_correct,
                    correct_answers);
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

class JsonTransformer implements ResponseTransformer {
    private Gson gson = new Gson();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }
}


