import Quiz.QuizQuestion;
import YouTube.VideoItem;
import YouTube.YoutubeSearchResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import quizresponse.QuizAndVideo;
import spark.ResponseTransformer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static spark.Spark.*;

public class Main {
   private static String quizKey = Main.quizKeyFromEnv("quiz");
   private static String youtubeKey = Main.youtubeKeyFromEnv("youtube");

    public static void main(String[] args) {

        get("/:id", "application/json", (req, res) -> {
            HttpClient client = HttpClient.newHttpClient();

            // HTTP request to quizapi.io
            String QuizURI = "https://quizapi.io/api/v1/questions?tags=" + req.params("id") + "&limit=5";
            HttpRequest quizRequest = HttpRequest.newBuilder()
                    .header("Accept", "application/json")
                    .header("x-api-key", quizKey)
                    .uri(URI.create(QuizURI))
                    .build();

            // HTTP request to Youtube.
            String YouTubeURI = "https://youtube.googleapis.com/youtube/v3/search?part=snippet&maxResult=5&q=" + req.params("id") + "&key=" + youtubeKey;
            HttpRequest youtubeRequest = HttpRequest.newBuilder()
                    .header("Accept", "application/json")
                    .uri(URI.create(YouTubeURI))
                    .build();

            HttpResponse<String> quizResponse =
                    client.send(quizRequest, HttpResponse.BodyHandlers.ofString());

            HttpResponse<String> ytResponse =
                    client.send(youtubeRequest, HttpResponse.BodyHandlers.ofString());

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

            String body = quizResponse.body();
            QuizQuestion[] questions = gson.fromJson(body, QuizQuestion[].class);

            YoutubeSearchResult yt = gson.fromJson(ytResponse.body(), YoutubeSearchResult.class);

            List<VideoItem> items = yt.items;
            List<String> videoIds = new ArrayList<>();
            for (var item : items) {
                videoIds.add(item.id.videoId);
            }

            QuizAndVideo quizAndVideo = new QuizAndVideo(Arrays.asList(questions), videoIds);

            //        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
//                    .thenApply(HttpResponse::body)
//                    .thenAccept(System.out::println)
//                    .join();

            res.type("application/json");
            return quizAndVideo;
        }, new JsonTransformer());

        get("/", (req, res) -> {
            return "no accept header.";
        });

    }

    private static String quizKeyFromEnv(String varName) {
        var key = System.getenv(varName);
        if (key == null) {
            System.err.println("Failed to load API key for quizapi.io");
            System.exit(1);
        }
        return key;
    }

    private static String youtubeKeyFromEnv(String varName) {
        var key = System.getenv(varName);
        if (key == null) {
            System.err.println("Failed to load API key for YouTube.com");
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


