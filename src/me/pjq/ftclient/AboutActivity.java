
package me.pjq.ftclient;

import android.os.Bundle;

import me.pjq.widget.WebViewActivity;

public class AboutActivity extends WebViewActivity {
    public static final String TAG = AboutActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = "https://216.24.194.197/t/twitese/index.php";
        loadurl(url);
    }
}
