package kuhn.pierre.com.rugbyappnews.utils;

/**
 * Created by pierre
 */
public class Video {

    private String title;
    private String id;
    private String thumbUrl;

    public Video(String title, String id, String thumbUrl) {
        this.title = title;
        this.id = id;
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
        return id;
    }

    /**
     * @return the thumbUrl of a still image representation of this video
     */
    public String getThumbUrl() {
        return thumbUrl;
    }
}
