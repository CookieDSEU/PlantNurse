package com.kot32.ksimplelibrary.network;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.kot32.ksimplelibrary.manager.task.base.NetworkTask;
import com.kot32.ksimplelibrary.model.response.BaseResponse;
import com.kot32.ksimplelibrary.util.tools.NetworkUtil;
import com.litesuits.http.LiteHttpClient;
import com.litesuits.http.data.HttpStatus;
import com.litesuits.http.data.NameValuePair;
import com.litesuits.http.request.Request;
import com.litesuits.http.request.content.UrlEncodedFormBody;
import com.litesuits.http.request.param.HttpMethod;
import com.litesuits.http.response.Response;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by kot32 on 15/11/7.
 */
public class NetworkExecutor {

    private NetworkExecutor() {
    }


    public static NetworkResult doRequestAndReturnObject(Context context,
                                                         Class jasonClass, String url, HashMap<String, String> params,
                                                         int REQUEST_TYPE) {
        LiteHttpClient client = LiteHttpClient.newApacheHttpClient(context);
        Request req = new Request(url);
        if (REQUEST_TYPE == NetworkTask.GET)
            req.setMethod(HttpMethod.Get);
        else
            req.setMethod(HttpMethod.Post);
        req.setRetryMaxTimes(10);
        if (params != null) {
            Iterator iterator = params.keySet().iterator();
            if (REQUEST_TYPE == NetworkTask.GET) {
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    req.addUrlParam(key, params.get(key));
                }
            } else {
                LinkedList<NameValuePair> pList = new LinkedList<NameValuePair>();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    pList.add(new NameValuePair(key, params.get(key)));
                }
                req.setHttpBody(new UrlEncodedFormBody(pList));
            }
        }


        NetworkResult r = new NetworkResult();
        if (!NetworkUtil.isAvailable(context)) {
            Log.e("警告", "无网络连接");
            r.tips = "无网络连接";
            return r;
        }

        Response res = client.execute(req);

        r.resultCode = res.getHttpStatus();
        r.resultObject = res.getObject(jasonClass);

        if ((r.resultObject != null) && !(r.resultObject instanceof BaseResponse)) {
            Log.e("警告", "该数据模型对象没有继承自 BaseResponse");
        }

        if (r.resultCode == null) {
            Log.e("警告", "连接超时");
            r.tips = "连接超时";
            return r;
        }

        if (r.resultCode.getCode() == 200 && r.resultObject == null) {
            Log.e("警告", "转换JSON失败");
            r.tips = "转换JSON失败";
        }

        if (TextUtils.isEmpty(r.tips)) {
            r.tips = r.resultCode.getDescriptionInChinese();
        }

        return r;

    }

    public static String doRequestAndReturnString(Context context, String url,
                                                  HashMap<String, String> params, int REQUEST_TYPE) {
        try {
            LiteHttpClient client = LiteHttpClient.newApacheHttpClient(context);

            Request req = new Request(url);
            if (REQUEST_TYPE == NetworkTask.GET)
                req.setMethod(HttpMethod.Get);
            else
                req.setMethod(HttpMethod.Post);
            req.setRetryMaxTimes(10);
            if (params != null) {
                Iterator iterator = params.keySet().iterator();
                if (REQUEST_TYPE == NetworkTask.GET) {
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        req.addUrlParam(key, params.get(key));
                    }
                } else {
                    LinkedList<NameValuePair> pList = new LinkedList<NameValuePair>();
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        pList.add(new NameValuePair(key, params.get(key)));
                    }
                    req.setHttpBody(new UrlEncodedFormBody(pList));
                }
            }
            if (!NetworkUtil.isAvailable(context)) {
                Log.e("警告", "无网络连接");
                return "";
            }
            Response res = client.execute(req);
            if (res.getHttpStatus() == null) {
                Log.e("警告", "连接超时");
                return "";
            }

            return res.getString();
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * resultObject 为JSON转换之后的对象
     */
    public static class NetworkResult {
        public Object resultObject;
        public HttpStatus resultCode;
        public String tips;

        public boolean isEmpty() {
            if (resultObject == null) {
                return true;
            }
            return false;
        }
    }

}
