package st.photogallery.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by tengsun on 3/7/16.
 */
public class PollService extends IntentService {

    private static final String TAG = "PollService";

    public PollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Received an intent: " + intent);
    }
}
