package tqs.g11.zap.client;

import java.io.IOException;

public interface ISimpleHttpClient {
    
    public String doHttpGet(String query) throws IOException;
}