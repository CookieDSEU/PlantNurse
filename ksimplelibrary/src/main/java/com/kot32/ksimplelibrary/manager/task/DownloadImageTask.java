package com.kot32.ksimplelibrary.manager.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.InputStream;

/**
 * Created by kot32 on 15/10/26.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    /**
     * 图片加载异步任务
     */
    OnSuccessCallBack onSuccesscallBack;

    public DownloadImageTask(OnSuccessCallBack onSuccesscallBack) {
        this.onSuccesscallBack = onSuccesscallBack;
    }

    protected Bitmap doInBackground(String... urls) {
        String imageURL = urls[0];
        if (TextUtils.isEmpty(imageURL)) {
            Log.e("警告", "图片URL为空");
            return null;
        }
        Bitmap b = null;
        try {
            InputStream in = new java.net.URL(imageURL).openStream();
            b = BitmapFactory.decodeStream(in);
            in.close();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return b;

    }

    protected void onPostExecute(Bitmap result) {
        onSuccesscallBack.onLoaded(result);
    }


    public interface OnSuccessCallBack {
        void onLoaded(Bitmap bitmap);
    }

}

