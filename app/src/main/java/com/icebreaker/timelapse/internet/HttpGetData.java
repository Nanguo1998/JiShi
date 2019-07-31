package com.icebreaker.timelapse.internet;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * 发送Http请求
 * @author Marhong
 * @time 2018/5/25 0:54
 */
public class HttpGetData {
    private static final String ERROR_CODE = "0204";

    public static String GetData(final String Url, final ArrayList ArrayValues) {

        InputStream is = null;
        String jsonString = null;
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Url);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(ArrayValues, "UTF-8"));
            HttpResponse httpResponse;
            httpResponse = httpClient.execute(httpPost);
            is = httpResponse.getEntity().getContent();
            jsonString = Util.convertStreamToString(is);
           // Log.e("HttpGetData", "url=" + Url + "\nresult=" + jsonString);
            return jsonString;

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
        }

        return ERROR_CODE;
    }

}

