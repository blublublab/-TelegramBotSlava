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
    private static final String RAPID_HOST = "imgur-apiv3.p.rapidapi.com";
    private static final String RAPID_AUTH = "Bearer bf3bbaf5c0d336d0b4579b09edae8f3012d80dea";

    public String getImageLink(String inputSearchText) throws IOException, JSONException {
        Request request = new Request.Builder()
                .url(" https://imgur-apiv3.p.rapidapi.com/3/gallery/t/" + inputSearchText + "/%7Bsort%7D/%7Bwindow%7D/%7Bpage%7D")
                .get()
                .addHeader("authorization", RAPID_AUTH)
                .addHeader("x-rapidapi-key", RAPID_KEY)
                .addHeader("x-rapidapi-host", RAPID_HOST)
                .build();

        response = okHttpClientSend.newCall(request).execute();
             responseBody = response.body().string();
             jsonResponse = new JSONObject(responseBody);
             String imageLink =   jsonResponse.getJSONObject("data").
                getJSONArray("items")
                .getJSONObject(findValidImage()).getString("link");


             return imageLink;

    }

    public int findValidImage() {
        //TODO: Change API . Rewrite or delete.
        int random;
        try {
            random = (int) (Math.random() * 50);
            jsonResponse.getJSONObject("data").
                    getJSONArray("items").
                    getJSONObject(random).
                    getJSONArray("images").getJSONObject(0).
                    getString("type").
                    equals("image/jpeg");
        } catch (JSONException e) {
            random = (int) (Math.random() * 50);
            findValidImage();
        }
        return random;
    }

}




