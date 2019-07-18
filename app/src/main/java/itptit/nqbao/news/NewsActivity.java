package itptit.nqbao.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NewsActivity extends AppCompatActivity {

    WebView wvNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        maps();
    }

    private void maps() {
        wvNews = findViewById(R.id.wv_news);

        Intent intent = getIntent();
        String url = intent.getStringExtra("link");
        wvNews.loadUrl(url);

        wvNews.setWebViewClient(new WebViewClient());
    }
}
