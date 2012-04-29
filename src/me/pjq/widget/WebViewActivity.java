
package me.pjq.widget;

import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import me.pjq.ftclient.BaseActivity;
import me.pjq.ftclient.R;

public class WebViewActivity extends BaseActivity {
    public final static String TAG = WebViewActivity.class.getSimpleName();
    public final static String INTENT_EXTRA_URL = "intent_extra_webview_url";

    private WebView mWebView;
    private WebSettings webSettings;
    private Handler handler;
    private String mOriginalUrl;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.webview);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mOriginalUrl = bundle.getString(INTENT_EXTRA_URL);
        }

        mWebView = (WebView) findViewById(R.id.webview);
        mProgressBar = (ProgressBar) findViewById(R.id.webview_progressbar);
        webSettings = mWebView.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);

        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (!Thread.currentThread().isInterrupted()) {
                    switch (msg.what) {
                        case 0:
                            // m_tvEmptyProgress.setVisibility(View.VISIBLE);
                            webSettings.setBlockNetworkImage(true);
                            mWebView.setVisibility(View.GONE);
                            break;
                        case 1:
                            // m_tvEmptyProgress.setVisibility(View.GONE);
                            mWebView.setVisibility(View.VISIBLE);
                            webSettings.setBlockNetworkImage(false);
                            break;
                    }
                }
                super.handleMessage(msg);
            }
        };

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                loadurl(view, url);
                return true;
            }

            // 1.6 不支持导入，但是引入应该没问题，待测
            public void onReceivedSslError(WebViewActivity view,
                    SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        // Enable JavaScript Alerts
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                    JsResult result) {
                return false;
            }

            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    handler.sendEmptyMessage(1);
                }

                if (100 == progress) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(progress);
                }

                super.onProgressChanged(view, progress);
            }
        });
        loadurl(mWebView, mOriginalUrl);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void loadurl(final WebView view, final String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                handler.sendEmptyMessage(0);
                view.loadUrl(url);
            }
        });

    }

    public void loadurl(final String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                handler.sendEmptyMessage(0);
                mWebView.loadUrl(url);
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
