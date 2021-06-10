package tgbot;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class PictureHTTPClient {
    private JSONObject jsonResponse;
    private Response response;
    private String responseBody;
    private OkHttpClient okHttpClientSend;
    static PictureHTTPClient instance;

    public static PictureHTTPClient getInstance() {
        if (instance == null) {
            instance = new PictureHTTPClient();
        }
        return instance;
    }

    ;

    private PictureHTTPClient() {

    }

    private static final String RAPID_KEY = System.getenv("RAPID_KEY");
    private static final String RAPID_HOST = "contextualwebsearch-websearch-v1.p.rapidapi.com";

    public String getImageLink(String inputSearchText) throws IOException, JSONException {
                 okHttpClientSend = new OkHttpClient();
                 okHttpClientSend.setRetryOnConnectionFailure(true);

            // int pageNumber  = (int) (Math.random()*10 + 1);
        int pageNumber = 1;
        Request request = new Request.Builder()
                .url("https://contextualwebsearch-websearch-v1.p.rapidapi.com/api/Search/ImageSearchAPI?q="  + inputSearchText +"&pageNumber="+ pageNumber +"&pageSize=50&autoCorrect=true&safeSearch=false")
                .get()
                .addHeader("x-rapidapi-key", RAPID_KEY)
                .addHeader("x-rapidapi-host", RAPID_HOST)
                .build();
            System.out.println(request.url().toString());
            response = okHttpClientSend.newCall(request).execute();
              responseBody = response.body().string();
             jsonResponse = new JSONObject(responseBody);
             int imageNumber = (int) (Math.random() * (jsonResponse.getJSONArray("value").length()-1) + 1);
             String answer =  jsonResponse.getJSONArray("value").getJSONObject(imageNumber).getString("url");;
            return answer;
    }



}




