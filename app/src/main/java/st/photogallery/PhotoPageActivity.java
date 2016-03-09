package st.photogallery;

import android.support.v4.app.Fragment;

/**
 * Created by tengsun on 3/9/16.
 */
public class PhotoPageActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new PhotoPageFragment();
    }
}
