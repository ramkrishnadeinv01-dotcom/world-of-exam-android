package in.worldofexam.app;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.Toast;

import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getBridge() == null || getBridge().getWebView() == null) {
            return;
        }

        WebView webView = getBridge().getWebView();
        webView.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> {
            if (url == null || url.trim().isEmpty()) {
                Toast.makeText(this, "Unable to start this download.", Toast.LENGTH_LONG).show();
                return;
            }

            // Android DownloadManager can download normal HTTP/HTTPS URLs.
            // Blob URLs require a separate JavaScript-to-native transfer and are not
            // passed to DownloadManager because they are local to the WebView.
            if (url.startsWith("blob:")) {
                Toast.makeText(this, "This file uses a browser-generated download. Please report this result so we can add blob-download support.", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                request.setDescription("Downloading from World of Exam");
                request.setMimeType(mimeType == null || mimeType.isEmpty() ? "application/octet-stream" : mimeType);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setAllowedOverMetered(true);
                request.setAllowedOverRoaming(true);
                request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    URLUtil.guessFileName(url, contentDisposition, mimeType)
                );

                String cookies = CookieManager.getInstance().getCookie(url);
                if (cookies != null && !cookies.isEmpty()) {
                    request.addRequestHeader("Cookie", cookies);
                }
                if (userAgent != null && !userAgent.isEmpty()) {
                    request.addRequestHeader("User-Agent", userAgent);
                }

                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                if (manager == null) {
                    Toast.makeText(this, "Android download service is unavailable.", Toast.LENGTH_LONG).show();
                    return;
                }
                manager.enqueue(request);
                Toast.makeText(this, "Download started. Check your Downloads folder.", Toast.LENGTH_LONG).show();
            } catch (Exception error) {
                Toast.makeText(this, "Download could not be started. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
