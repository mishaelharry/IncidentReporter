package com.app.incidentreporter.activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.app.incidentreporter.R;

import java.io.IOException;
import java.io.InputStream;

public class CPRStepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cprstep);

        WebView webView = new WebView(this);
        setContentView(webView);

        try{
            InputStream inputStream = getAssets().open("guide.html");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            webView.loadData(new String(buffer), "text/html", "UTF-8");
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
