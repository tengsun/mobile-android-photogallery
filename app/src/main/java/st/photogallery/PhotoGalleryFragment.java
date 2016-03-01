package st.photogallery;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.io.IOException;

import st.photogallery.network.FlickrFetcher;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoGalleryFragment extends Fragment {

    private static final String TAG = "PhotoGalleryFragment";

    private GridView gridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        gridView = (GridView) view.findViewById(R.id.photo_gallery_grid_view);
        return view;
    }

    private class FetchItemTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                String result = new FlickrFetcher().getUrl("http://www.baidu.com");
                Log.i(TAG, "Fetched contents: " + result);
            } catch (IOException e) {
                Log.e(TAG, "Failed to fetch URL: ", e);
            }
            return null;
        }
    }

}
