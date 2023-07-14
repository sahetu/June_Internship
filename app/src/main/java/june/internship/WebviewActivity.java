package june.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class WebviewActivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView = findViewById(R.id.webview);

        webView.loadUrl("https://brainybeaminfotech.com/PaymentPolicy.php");
        webView.getSettings().setJavaScriptEnabled(true);

    }
}