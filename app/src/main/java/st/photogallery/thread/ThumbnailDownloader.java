package st.photogallery.thread;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import st.photogallery.model.GalleryItem;
import st.photogallery.network.FlickrFetcher;

/**
 * Created by tengsun on 3/3/16.
 */
public class ThumbnailDownloader<Token> extends HandlerThread {

    private static final String TAG = "ThumbnailDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;

    private Handler requestHandler;
    private Map<Token, GalleryItem> requestMap =
            Collections.synchronizedMap(new HashMap<Token, GalleryItem>());
    private Handler responseHandler;
    private Listener<Token> listener;

    public interface Listener<Token> {
        void onThumbnailDownloaded(Token token, String id, Bitmap thumbnail);
    }

    public void setListener(Listener<Token> listener) {
        this.listener = listener;
    }

    public ThumbnailDownloader(Handler responseHandler) {
        super(TAG);
        this.responseHandler = responseHandler;
    }

    public void queueThumbnail(Token token, GalleryItem item) {
        Log.i(TAG, "Got an URL: " + item.getUrl());
        requestMap.put(token, item);

        requestHandler.obtainMessage(MESSAGE_DOWNLOAD, token).sendToTarget();
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared() {
        requestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    Token token = (Token) msg.obj;
                    Log.i(TAG, "Got a request for item: " + requestMap.get(token));
                    handleRequest(token);
                }
            }
        };
    }

    private void handleRequest(final Token token) {
        try {
            final GalleryItem item = requestMap.get(token);
            if (item == null) {
                return;
            }

            byte[] bitmapBytes = new FlickrFetcher().getUrlBytes(item.getUrl());
            final Bitmap bitmap =
                    BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            Log.i(TAG, "Bitmap created");

            // response handler process image
            responseHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (requestMap.get(token) != item) {
                        return;
                    }

                    requestMap.remove(token);
                    listener.onThumbnailDownloaded(token, item.getId(), bitmap);
                }
            });
        } catch (IOException e) {
            Log.e(TAG, "Error downloading image", e);
        }
    }

    public void clearQueue() {
        requestHandler.removeMessages(MESSAGE_DOWNLOAD);
        requestMap.clear();
    }

}
