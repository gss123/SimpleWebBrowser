/*
    Name: Jonathan Munevar
    Application: Simple Web Browser
    Filename: WebBrowser.java
*/

package org.morisummer.WebBrowser;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;


public class WebBrowser extends Activity
{
    private WebView webBrowser;
    private WebSettings browserSettings;
    private EditText urlBar;
    private String urlString;
    private String validatedURL;
    private URL validURL;
    private String currentURL;
    private Toast errorToast;
    private Toast currentURLToast;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        webBrowser = (WebView) findViewById(R.id.browserWindow);
        webBrowser.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                showCurrentURL();
                updateURLBar();
            }
        });

        browserSettings = webBrowser.getSettings();
        browserSettings.setBuiltInZoomControls(true);
        browserSettings.setDisplayZoomControls(false);
        browserSettings.setJavaScriptEnabled(true);

        urlBar = (EditText) findViewById(R.id.urlBar);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webBrowser.canGoBack())
        {
            webBrowser.goBack();
            return true;
        }
        else if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            return super.onKeyDown(keyCode, event);
        }

        if (keyCode == KeyEvent.KEYCODE_ENTER)
        {
            loadWebURL(webBrowser);
            return true;
        }

        return false;
    }

    public void goBackHistory(View view)
    {
        if(webBrowser.canGoBack())
        {
            webBrowser.goBack();
        }
    }

    public void goForwardHistory(View view)
    {
        if(webBrowser.canGoForward())
        {
            webBrowser.goForward();
        }
    }

    public void loadWebURL(View view)
    {
        urlString = urlBar.getText().toString();

        if (!urlString.startsWith("http://"))
        {
            urlString = "http://" + urlString;
        }

        try
        {
            validURL = new URL(urlString);
        }
        catch (MalformedURLException e)
        {
            Log.e("invalidURL", e.toString());
            errorToast = Toast.makeText(getApplicationContext(),
                                              "Invalid URL",
                                              Toast.LENGTH_SHORT);
            errorToast.setGravity(Gravity.TOP, 0, 100);
            errorToast.show();
        }
        finally
        {
            validatedURL = URLUtil.guessUrl(urlString);
            webBrowser.loadUrl(validatedURL);
        }
    }

    public void showCurrentURL()
    {
        currentURLToast = Toast.makeText(getApplicationContext(),
                                         webBrowser.getUrl(),
                                         Toast.LENGTH_SHORT);
        currentURLToast.setGravity(Gravity.CENTER, 0, 0);
        currentURLToast.show();
    }

    public void updateURLBar()
    {
        currentURL = webBrowser.getUrl();
        urlBar.setText(currentURL);
    }
}
