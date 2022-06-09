package tqs.g11.zap.client;

import java.io.IOException;

public interface ISimpleHttpClient {
    String doHttpGet(String query) throws IOException;

    String doHttpPost(String query, Object obj, String token) throws IOException;
}