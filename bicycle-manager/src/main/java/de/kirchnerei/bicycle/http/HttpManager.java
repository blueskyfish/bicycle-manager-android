package de.kirchnerei.bicycle.http;

import android.content.Context;

import de.kirchnerei.bicycle.SettingRepository;
import kirchnerei.httpclient.Definition;
import kirchnerei.httpclient.HttpClient;
import kirchnerei.httpclient.HttpClientStore;
import kirchnerei.httpclient.HttpConfiguration;
import kirchnerei.httpclient.HttpRequest;
import kirchnerei.httpclient.HttpResponse;

public class HttpManager {

    private static final int BUFFER_SIZE = 20 * 1024; // 20 kb
    private static final String HEADER_FIELD_NAME = "x-bicycle-token";

    private final Context mContext;
    private final SettingRepository mSetting;
    private final HttpClient mHttpClient;

    public HttpManager(SettingRepository setting, Context context) {
        this.mContext = context;
        this.mSetting = setting;

        HttpClientStore mHttpStore = new HttpClientStore() {
            @Override
            public void putToken(String token) {
                HttpManager.this.mSetting.verifyToken(token);
            }

            @Override
            public String getToken() {
                return HttpManager.this.mSetting.getToken();
            }
        };
        this.mHttpClient = createHttClient(setting, mHttpStore, context);
    }

    public HttpResponse execute(HttpRequest request) {
        return mHttpClient.execute(request);
    }

    static HttpClient createHttClient(SettingRepository setting, HttpClientStore store, Context context) {

        HttpConfiguration config = new HttpConfiguration();
        config.setContentType(Definition.DEFAULT_CONTENT_TYPE);
        config.setHeaderTokenName(HEADER_FIELD_NAME);
        config.setUserAgent("bicycleHttpClient: v0.1 (http://www.blueskyfish.de)");
        config.setInputEncoding(Definition.DEFAULT_INPUT_ENCODING);
        config.setOutputEncoding(Definition.DEFAULT_OUTPUT_ENCODING);
        config.setBufferSize(BUFFER_SIZE);
        config.setBaseUrl(setting.getBaseUrl());
        return new HttpClient(config, store);
    }

}
