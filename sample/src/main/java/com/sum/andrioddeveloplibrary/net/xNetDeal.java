package com.sum.andrioddeveloplibrary.net;

import android.text.TextUtils;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;

/**
 * Created by sdl on 2018/5/22.
 */
public class xNetDeal {
//appid：Xiade.jiu08  Xiade.jiu09  Xiade.jiu10  Xiade.jiu11

    /**
     * {"id":"2043","0":"2043",
     * "url":"http://bxvip.oss-cn-zhangjiakou.aliyuncs.com/xycai/xycaizy.apk()com.bxvip.app.xycaizy",
     * "1":"http://bxvip.oss-cn-zhangjiakou.aliyuncs.com/xycai/xycaizy.apk()com.bxvip.app.xycaizy",
     * "type":"android","2":"android",
     * "show_url":"1","3":"1",
     * "appid":"Xiade.jiu08","4":"Xiade.jiu08",
     * "comment":"?","5":"?","request_num":"0","6":"0",
     * createAt":"2018-05-22 23:42:43","7":"2018-05-22 23:42:43","updateAt":"2018-05-22 23:42:43","8":"2018-05-22 23:42:43"}
     */


    private String url = "http://vipapp.01appddd.com/Lottery_server/get_init_data.php?type=android";

    public interface Listener {

        void onSuccess(String url, String type);

        void onFailed();
    }

    private Listener mListener;

    public void start(Listener listener) {
        mListener = listener;
        RequestParams params = new RequestParams(url);
        params.addParameter("appid", "Xiade.jiu08");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                dealData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mListener.onFailed();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                mListener.onFailed();
            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void dealData(String data) {
        if (TextUtils.isEmpty(data)) {
            mListener.onFailed();
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(data);
            String res = jsonObject.getString("data");
            if (!TextUtils.isEmpty(res)) {
                byte[] decode = Base64.decode(res.getBytes(), Base64.NO_WRAP);
                String info = new String(decode, "UTF-8");
                JSONObject object = new JSONObject(info);

                String url = object.getString("url");

                String show_url = object.getString("show_url");
                if (url.contains("()")) {
                    String targetUrl = url.substring(0, url.indexOf("()"));
                    mListener.onSuccess(targetUrl, show_url);
                } else {
                    mListener.onFailed();
                }
            } else {
                mListener.onFailed();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
