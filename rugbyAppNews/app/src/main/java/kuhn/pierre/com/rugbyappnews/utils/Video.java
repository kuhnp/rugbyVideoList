package kuhn.pierre.com.rugbyappnews.utils;

/**
 * Created by pierre on 09/05/2015.
 */
public class Video {

    private String title;
    private String url;
    private String thumbUrl;

    public Video(String title, String url, String thumbUrl) {
        this.title = title;
        this.url = url;
        this.thumbUrl = thumbUrl;
    }

    /**
     * @return the title of the video
     */
    public String getTitle(){
        return title;
    }

    /**
     * @return the url to this video on youtube
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return the thumbUrl of a still image representation of this video
     */
    public String getThumbUrl() {
        return thumbUrl;
    }
}
