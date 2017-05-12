/*
 * The MIT License (MIT)
 * Copyright (c) 2017 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.blueskyfish.bicycle.http;

import android.content.Context;
import android.os.Handler;

import de.blueskyfish.bicycle.SettingRepository;
import de.blueskyfish.bicycle.helper.Logger;
import de.blueskyfish.bicycle.helper.StringUtil;
import de.blueskyfish.httpclient.Definition;
import de.blueskyfish.httpclient.HttpClient;
import de.blueskyfish.httpclient.HttpClientStore;
import de.blueskyfish.httpclient.HttpConfiguration;
import de.blueskyfish.httpclient.HttpRequest;
import de.blueskyfish.httpclient.HttpResponse;

public class HttpManager {

    private static final int BUFFER_SIZE = 20 * 1024; // 20 kb
    private static final String HEADER_FIELD_NAME = "x-bicycle-token";

    private final SettingRepository mSetting;
    private final HttpClient mHttpClient;
    private HttpProcess httpProcess;
    private final Handler mainHandler;

    public HttpManager(SettingRepository setting, Context context) {
        this.mSetting = setting;
        this.mainHandler = new Handler(context.getMainLooper());

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
        onHttpStart();
        try {
            HttpResponse response = mHttpClient.execute(request);
            Logger.debug("request %s %s -> %s (%s byte) %s ms",
                    request.getMethod(), request.getUrl(),
                    response.getStatusCode(), StringUtil.length(response.getContent()),
                    response.getDuration());
            return response;
        } finally {
            onHttpFinish();
        }
    }


    private static HttpClient createHttClient(SettingRepository setting, HttpClientStore store, Context context) {

        HttpConfiguration config = new HttpConfiguration();
        config.setContentType(Definition.DEFAULT_CONTENT_TYPE);
        config.setHeaderTokenName(HEADER_FIELD_NAME);
        config.setUserAgent("bicycleHttpClient: v1.1.0 (http://www.blueskyfish.de)");
        config.setInputEncoding(Definition.DEFAULT_INPUT_ENCODING);
        config.setOutputEncoding(Definition.DEFAULT_OUTPUT_ENCODING);
        config.setBufferSize(BUFFER_SIZE);
        config.setBaseUrl(setting.getBaseUrl());
        return new HttpClient(config, store);
    }

    private void onHttpStart() {
        if (httpProcess != null) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    httpProcess.startRequest();
                }
            });
        }
    }

    private void onHttpFinish() {
        if (httpProcess != null) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    httpProcess.finishRequest();
                }
            });
        }
    }

    public static void setHttpProcess(HttpManager httpManager, HttpProcess httpProcess) {
        if (httpManager != null) {
            httpManager.httpProcess = httpProcess;
        }
    }
}
