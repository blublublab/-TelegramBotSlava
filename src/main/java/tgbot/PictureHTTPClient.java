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

    private final OkHttpClient okHttpClientSend = new OkHttpClient();


    private static final String RAPID_KEY = "89b3ea805bmsh5a88d264a2dbd96p16d752jsnb8c10be4448c";
    private static final String RAPID_HOST = "contextualwebsearch-websearch-v1.p.rapidapi.com";

        public String getImageLink(String inputSearchText) throws IOException, JSONException {
        int pageNumber  = (int) (Math.random()*10 + 1);
        Request request = new Request.Builder()
                .url("https://contextualwebsearch-websearch-v1.p.rapidapi.com/api/Search/ImageSearchAPI?q="  + inputSearchText +"&pageNumber="+ pageNumber +"&pageSize=50&autoCorrect=true&safeSearch=false")
                .get()
                .addHeader("x-rapidapi-key", RAPID_KEY)
                .addHeader("x-rapidapi-host", RAPID_HOST)
                .build();

        response = okHttpClientSend.newCall(request).execute();
             responseBody = response.body().string();
             jsonResponse = new JSONObject(responseBody);
             int imageNumber = (int) (Math.random() * (jsonResponse.getJSONArray("value").length()-1) + 1);

            return jsonResponse.getJSONArray("value").getJSONObject(imageNumber).getString("url");
    }



}




