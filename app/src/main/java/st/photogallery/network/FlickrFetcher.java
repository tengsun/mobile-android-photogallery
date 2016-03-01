package st.photogallery.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tengsun on 3/1/16.
 */
public class FlickrFetcher {

    byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
            // build in and out stream
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = conn.getInputStream();

            // check http response code
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            // read and write with buffer
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();

            return out.toByteArray();
        } finally {
            conn.disconnect();
        }
    }

    public String getUrl(String urlSepc) throws IOException {
        return new String(getUrlBytes(urlSepc));
    }

}
