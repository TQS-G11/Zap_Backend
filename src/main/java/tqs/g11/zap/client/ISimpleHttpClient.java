package tqs.g11.zap.client;

import com.google.gson.JsonObject;

import java.io.IOException;

public interface ISimpleHttpClient {
    JsonObject doHttpGet(String query, String token) throws IOException;

    JsonObject doHttpPost(String url, Object obj, String token) throws IOException;
}