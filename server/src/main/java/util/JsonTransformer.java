package util;

import com.google.gson.Gson;
import spark.ResponseTransformer;

/**
 * Utility class that is used to enable JSON responses in Spark Java
 * https://sparkjava.com/documentation#response-transformer
 */
public class JsonTransformer implements ResponseTransformer {
    private Gson gson = new Gson();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }
}
