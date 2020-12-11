import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.ResponseTransformer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;

import static spark.Spark.*;

public class Main {
    private static String quizKey = Main.quizKeyFromEnv("quiz");

    public static void main(String[] args) {
        get("/", "application/json", (req, res) -> {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .header("Accept", "application/json")
                    .uri(URI.create("http://unicorns.idioti.se/3"))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();


            System.out.println(response.body());


            Unicorn unicorn = gson.fromJson(response.body(), Unicorn.class);
            System.out.println(unicorn.toString());


//            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
//                    .thenApply(HttpResponse::body)
//                    .thenAccept(System.out::println)
//                    .join();

            res.type("application/json");

            return unicorn;
        }, new JsonTransformer());

        get("/", (req, res) -> {
            return "no accept header.";
        });

        get("/hello", "application/json", (request, response) -> {
            return new MyMessage("Hello World");
        }, new JsonTransformer());
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

class JsonTransformer implements ResponseTransformer {
    private Gson gson = new Gson();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }
}


/**
 * A simple class representing a unicorn.
 *
 * @author "Johan Holmberg, Malmö university"
 * @since 1.0
 */
class Unicorn {
    public int id = 0;
    public String name = "";
    public String description = "";
    public String reportedBy = "";
    public Location spottedWhere = new Location();
    public Timestamp spottedWhen = new Timestamp(0);
    public String image = "";

    public Unicorn() {

    }

    @Override
    public String toString() {
        return "Unicorn{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", reportedBy='" + reportedBy + '\'' +
                ", spottedWhere=" + spottedWhere +
                ", spottedWhen=" + spottedWhen +
                ", image='" + image + '\'' +
                '}';
    }
}

/**
 * A simple class representing a location.
 *
 * @author "Johan Holmberg, Malmö university"
 * @since 1.0
 */
class Location {
    public String name;
    public double lat;
    public double lon;
}

class MyMessage {
    private String message;

    public MyMessage(String message) {
        this.message = message;
    }

    public MyMessage() {

    }

    public String getMessage() {
        return message;
    }
}