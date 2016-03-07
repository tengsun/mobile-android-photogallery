package st.photogallery.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;

import st.photogallery.model.GalleryItem;
import st.photogallery.network.FlickrFetcher;

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
        // check network status
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getBackgroundDataSetting() &&
                cm.getActiveNetworkInfo() != null;
        if (!isNetworkAvailable) {
            Log.e(TAG, "Network is not available.");
            return;
        }

        // get shared preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String query = prefs.getString(FlickrFetcher.PREF_SEARCH_QUERY, null);
        String lastResultId = prefs.getString(FlickrFetcher.PREF_LAST_RESULT_ID, null);

        // search or fetch items
        List<GalleryItem> items;
        if (query != null) {
            items = new FlickrFetcher().search(query);
        } else {
            items = new FlickrFetcher().fetchItems();
        }

        if (items.size() == 0) {
            return;
        }

        // check if any updates
        String resultId = items.get(0).getId();
        if (!resultId.equals(lastResultId)) {
            Log.i(TAG, "Got a new result: " + resultId);
        } else {
            Log.i(TAG, "Got an old result: " + resultId);
        }

        prefs.edit().putString(FlickrFetcher.PREF_LAST_RESULT_ID, resultId).commit();
    }
}
