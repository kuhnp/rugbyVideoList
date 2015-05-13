package kuhn.pierre.com.rugbyappnews.rest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.List;

import kuhn.pierre.com.rugbyappnews.MainActivity;
import kuhn.pierre.com.rugbyappnews.R;
import kuhn.pierre.com.rugbyappnews.utils.Video;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by pierre
 */
public class RestClient {

    public static final String ENDPOINT = "https://www.googleapis.com/youtube/v3";
    public static final String CHANNEL_ID = "UCE28rwYoaV7jvU6GVzdu_GQ";
    public static final String PART = "snippet";
    public static final String MAX_RESULT = "50";
    public static final String TYPE = "video";
    public static final String THUMBNAIL_BASE_URL = "https://i.ytimg.com/vi/";
    public static final String THUMBNAIL_EXTENSION = "mqdefault.jpg";

    public static final String YOUTUBE_JSON_ID = "id";
    public static final String YOUTUBE_JSON_ITEM = "items";
    public static final String YOUTUBE_JSON_VIDEO_ID = "videoId";
    public static final String YOUTUBE_JSON_VIDEO_SNIPPET = "snippet";
    public static final String YOUTUBE_JSON_VIDEO_TITILE = "title";

    private RestAdapter mRestAdapter;
    private RestApi mApi;
    private List<Video> mVideoList;

    static RestClient mInstance;

    private RestClient(){
        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        mApi = mRestAdapter.create(RestApi.class);
        mVideoList = new ArrayList<>();
    }

    public static RestClient getInstance(){
        if(mInstance == null)
            mInstance = new RestClient();
        return mInstance;
    }

    /**
     * Method to get the 50 last video from the Youtube channel "World Rugby", using the Retrofit library.
     * @param context
     */
    public void getVideoListFromChannel(final Context context){

        mApi.getVideosFromChannel(PART, CHANNEL_ID, MAX_RESULT, context.getString(R.string.youtube_web_api_key), TYPE, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement json, Response response) {
                JsonObject jsonObject = json.getAsJsonObject();
                JsonArray jsonArray = jsonObject.getAsJsonArray(YOUTUBE_JSON_ITEM);
                for(int i = 0; i<jsonArray.size(); i++){
                    String id = jsonArray.get(i).getAsJsonObject().getAsJsonObject(YOUTUBE_JSON_ID).get(YOUTUBE_JSON_VIDEO_ID).toString().replace("\"","");
                    String title = jsonArray.get(i).getAsJsonObject().getAsJsonObject(YOUTUBE_JSON_VIDEO_SNIPPET).get(YOUTUBE_JSON_VIDEO_TITILE).toString().replace("\"","");
                    String thumbUrl = THUMBNAIL_BASE_URL+id+"/"+THUMBNAIL_EXTENSION;
                    Video video = new Video(title, id, thumbUrl);
                    mVideoList.add(video);
                }
                if(mVideoList.size() != 0){
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            }
            @Override
            public void failure(RetrofitError error) {

                if(error.getResponse().getStatus() == 401 || error.getResponse().getStatus() == 403)
                    Toast.makeText(context, context.getString(R.string.play_service_api_error), Toast.LENGTH_SHORT).show();
                else{
                    Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    ((Activity)context).finish();
                }

            }
        });
    }

    public List<Video> getmVideoList() {
        return mVideoList;
    }
}
