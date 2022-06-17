package tqs.g11.zap.client;

import com.google.gson.JsonObject;

import java.io.IOException;

public interface ISimpleHttpClient {
    String doHttpGet(String query) throws IOException;

    JsonObject doHttpPost(String url, Object obj, String token) throws IOException;
}