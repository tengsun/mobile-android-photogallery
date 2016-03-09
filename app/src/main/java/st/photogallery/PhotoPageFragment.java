package st.photogallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by tengsun on 3/9/16.
 */
public class PhotoPageFragment extends VisibleFragment {

    private String url;
    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        url = getActivity().getIntent().getData().toString();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_page, container, false);

        final ProgressBar progressBar =
                (ProgressBar) view.findViewById(R.id.photo_gallery_progress_bar);
        progressBar.setMax(100);
        final TextView titleView = (TextView) view.findViewById(R.id.photo_gallery_progress_text);

        webView = (WebView) view.findViewById(R.id.photo_gallery_web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.INVISIBLE);
                    titleView.setText("");
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                titleView.setText(title);
            }
        });
        webView.loadUrl(url);

        return view;
    }
}
