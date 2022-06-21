package tqs.g11.zap.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


public class TqsBasicHttpClient implements ISimpleHttpClient {
    @Override
    public JsonObject doHttpGet(String url, String token) throws IOException {
        System.out.println("Get URL: " + url);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        return getResponse(request, client, token);
    }

    @Override
    public JsonObject doHttpPost(String url, Object obj, String token) throws IOException {
        System.out.println("Post URL: " + url);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(url);
        ObjectMapper objectMapper = new ObjectMapper();
        StringEntity objJsonString = new StringEntity(objectMapper.writeValueAsString(obj), ContentType.APPLICATION_JSON);
        request.setEntity(objJsonString);
        return getResponse(request, client, token);

    }
    private JsonObject getResponse(HttpRequestBase request, CloseableHttpClient client, String token) throws IOException {
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        request.setHeader("Access-Control-Allow-Origin", "*");
        if (token != null)
            request.setHeader("Authorization", "Bearer " + token);

        CloseableHttpResponse response = client.execute(request);
        try {
            HttpEntity entity = response.getEntity();
            String resp = EntityUtils.toString(entity);
            System.out.println("Response:");
            System.out.println(resp);

            return new JsonParser().parse(resp).getAsJsonObject();

        } finally {
            response.close();
            client.close();
        }
    }
}
