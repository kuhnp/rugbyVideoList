package kuhn.pierre.com.rugbyappnews.rest;

import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by pierre on 09/05/2015.
 */
public interface RestApi {

    @GET("/search")
        public void getVideosFromChannel(
                @Query("part") String part,
                @Query("channelId") String channelId,
                @Query("maxResults") String maxResult,
                @Query("key") String apiKey,
                @Query("type") String type,
                Callback<JsonElement> response
                );




}

