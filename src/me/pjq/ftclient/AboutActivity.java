
package me.pjq.ftclient;

import android.os.Bundle;

import me.pjq.widget.WebViewActivity;

public class AboutActivity extends WebViewActivity {
    public static final String TAG = AboutActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = "http://pjq.me/wap";
        loadurl(url);
    }
}
