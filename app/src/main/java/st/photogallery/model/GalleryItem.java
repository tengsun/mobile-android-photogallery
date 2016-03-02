package st.photogallery.model;

/**
 * Created by tengsun on 3/2/16.
 */
public class GalleryItem {

    private String id;
    private String caption;
    private String url;

    public GalleryItem(String id, String caption, String url) {
        this.id = id;
        this.caption = caption;
        this.url = url;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toString() {
        return caption;
    }

}
