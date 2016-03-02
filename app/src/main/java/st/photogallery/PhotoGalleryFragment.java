package st.photogallery;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import st.photogallery.model.GalleryItem;
import st.photogallery.network.FlickrFetcher;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoGalleryFragment extends Fragment {

    private static final String TAG = "PhotoGalleryFragment";

    private GridView gridView;
    private List<GalleryItem> items;

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
        setupAdapter();
        return view;
    }

    void setupAdapter() {
        if (getActivity() == null || gridView == null) return;

        if (items != null) {
            gridView.setAdapter(new ArrayAdapter<GalleryItem>(getActivity(),
                    android.R.layout.simple_gallery_item, items));
        } else {
            gridView.setAdapter(null);
        }
    }

    private class FetchItemTask extends AsyncTask<Void, Void, List<GalleryItem>> {
        @Override
        protected List<GalleryItem> doInBackground(Void... params) {
            return new FlickrFetcher().fetchItems();
        }

        @Override
        protected void onPostExecute(List<GalleryItem> galleryItems) {
            items = galleryItems;
            setupAdapter();
        }
    }

}
