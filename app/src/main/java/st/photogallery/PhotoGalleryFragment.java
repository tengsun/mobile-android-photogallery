package st.photogallery;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import st.photogallery.model.GalleryItem;
import st.photogallery.network.FlickrFetcher;
import st.photogallery.thread.ThumbnailDownloader;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoGalleryFragment extends Fragment {

    private static final String TAG = "PhotoGalleryFragment";

    private GridView gridView;
    private List<GalleryItem> items;
    private ThumbnailDownloader<ImageView> thumbnailDownloader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemTask().execute();

        thumbnailDownloader = new ThumbnailDownloader<ImageView>();
        thumbnailDownloader.start();
        thumbnailDownloader.getLooper();
        Log.i(TAG, "Background thread thumbnail downloader started");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        gridView = (GridView) view.findViewById(R.id.photo_gallery_grid_view);
        setupAdapter();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        thumbnailDownloader.quit();
        Log.i(TAG, "Background thread thumbnail downloader destroyed");
    }

    void setupAdapter() {
        if (getActivity() == null || gridView == null) return;

        if (items != null) {
            gridView.setAdapter(new GalleryItemAdapter(items));
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

    private class GalleryItemAdapter extends ArrayAdapter<GalleryItem> {

        public GalleryItemAdapter(List<GalleryItem> items) {
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.gallery_item, parent, false);
            }
            ImageView imageView = (ImageView) convertView
                    .findViewById(R.id.photo_gallery_image_view);
            imageView.setImageResource(R.drawable.placeholder);

            GalleryItem item = getItem(position);
            thumbnailDownloader.queueThumbnail(imageView, item.getUrl());

            return convertView;
        }
    }

}
