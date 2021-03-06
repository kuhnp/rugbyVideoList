package kuhn.pierre.com.rugbyappnews.rest;

import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by pierre
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

