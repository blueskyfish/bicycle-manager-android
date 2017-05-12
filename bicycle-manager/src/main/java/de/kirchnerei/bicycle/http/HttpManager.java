/*
 * The MIT License (MIT)
 * Copyright (c) 2015 BlueSkyFish
 *
 * bicycle-manager-android - https://github.com/blueskyfish/bicycle-manager-android.git
 */
package de.kirchnerei.bicycle.http;

import android.content.Context;

import de.kirchnerei.bicycle.SettingRepository;
import de.kirchnerei.bicycle.helper.Logger;
import de.kirchnerei.bicycle.helper.StringUtil;
import de.blueskyfish.httpclient.Definition;
import de.blueskyfish.httpclient.HttpClient;
import de.blueskyfish.httpclient.HttpClientStore;
import de.blueskyfish.httpclient.HttpConfiguration;
import de.blueskyfish.httpclient.HttpRequest;
import de.blueskyfish.httpclient.HttpResponse;

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
        HttpResponse response = mHttpClient.execute(request);
        Logger.debug("request %s %s -> %s (%s byte) %s ms",
            request.getMethod(), request.getUrl(),
            response.getStatusCode(), StringUtil.length(response.getContent()),
            response.getDuration());
        return response;
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
