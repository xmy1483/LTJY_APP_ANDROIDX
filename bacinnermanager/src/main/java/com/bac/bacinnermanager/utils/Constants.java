package com.bac.bacinnermanager.utils;

import com.bac.bacinnermanager.ApiHost;

/**
 * Created by wujiazhen on 2017/8/14.
 */

public class Constants {
    public static final String SECODELAST = "^O.";
    public static final int XX = 1;
    public static final class URL {
        // 正式服
//        public static final String SCHEME = "https://";
//        public static final String URL = "app5.bac365.com:10443";
        // 正式服
        public static final String SCHEME = ApiHost.SCHEME;
        public static final String URL = ApiHost.HOST_PORT;

        // 测试服
//        public static final String SCHEME = "http://";
//        public static final String URL = "192.168.0.85:20002";
//        public static final String URL = "intranet.camel.bac365.com:20002";

        public static final String BASEURL = SCHEME + URL + "/app.pay/";
        public static final String UPLOAD_FILE = BASEURL + "UploadFileServer";
    }
}
