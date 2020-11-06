package com.bac.bacplatform.conf;

import android.provider.BaseColumns;

import com.bac.bacplatform.BuildConfig;
import com.bac.bacplatform.http.ApiHost;

import static com.bac.bacplatform.conf.Constants.CommonProperty._1;
import static com.bac.bacplatform.conf.Constants.CommonProperty._1000;
import static com.bac.bacplatform.conf.Constants.CommonProperty._2;
import static com.bac.bacplatform.conf.Constants.CommonProperty._2000;
import static com.bac.bacplatform.conf.Constants.CommonProperty._3;
import static com.bac.bacplatform.conf.Constants.CommonProperty._30;
import static com.bac.bacplatform.conf.Constants.CommonProperty._3000;
import static com.bac.bacplatform.conf.Constants.CommonProperty._4;
import static com.bac.bacplatform.conf.Constants.CommonProperty._60;

/**
 * 项目名称：Bacplatform
 * 包名：com.bac.bacplatform.conf
 * 创建人：Wjz
 * 创建时间：2017/4/7
 * 类描述：
 */

public class Constants {


    public static final String SECODELAST = "^O.";
    public static final int XX = _1;
    public static final int SECOND_2 = _1;// 0 1
    public static final int SECOND_30 = _30;
    public static final int SECOND_60 = _60;

    public static final class CommonProperty {

        public static final int __1 = -1;
        public static final int _0 = 0;
        public static final int _1 = 1;
        public static final int _2 = 2;
        public static final int _3 = 3;
        public static final int _4 = 4;
        public static final int _5 = 5;
        public static final int _6 = 6;
        public static final int _7 = 7;
        public static final int _8 = 8;
        public static final int _9 = 9;
        public static final int _10 = 10;
        public static final int _11 = 11;
        public static final int _12 = 12;
        public static final int _13 = 13;
        public static final int _14 = 14;
        public static final int _20 = 20;

        public static final int _30 = 30;
        public static final int _60 = 59;
        public static final int _150 = 150;
        public static final int _1000 = 1000;
        public static final int _2000 = 2000;
        public static final int _3000 = 3000;

    }

    public static final class URL {
        // 正式服
//        public static final String SCHEME = "https://";
//        public static final String URL = "app5.bac365.com:10443";

        // 测试服
//        public static final String SCHEME = "http://";
//        public static final String URL = "192.168.0.85:20002";

        // 测试服
        public static final String SCHEME = ApiHost.SCHEME;
        public static final String URL = ApiHost.HOST_PORT;

        public static final String BASEURL = SCHEME+URL+"/app.pay/";
        public static final String UPLOAD_FILE = BASEURL + "UploadFileServer";
    }

    public static final class APP {

        public static final boolean DEBUG = BuildConfig.DEBUG;
        public static final String VERSION_NAME = BuildConfig.VERSION_NAME;// 版本名
        public static final int VERSION_CODE = BuildConfig.VERSION_CODE;// 版本名

    }

    public static final class LocalDb implements BaseColumns {

        public static final String LOCAL_TABLE_NAME = "local_data";
        public static final String LOCAL_JSON = "local_json";
        public static final String LOCAL_MD5 = "local_md5";
        public static final String LOCAL_KEY = "local_key";
        public static final String LOCAL_TIME = "local_time";
    }

    public static final class ContentTable implements BaseColumns {
        public static final String KEY = "key";
        public static final String VALUE = "value";
        public static final String PHONE = "phone";
        public static final String _MD5 = "_md5";
    }

    public static final class IndexTable implements BaseColumns {
        public static final String KEY = "key";
        public static final String TEMP = "temp";
    }

    public static final class Adapter {
        public static final int HEADER = _1000;
        public static final int TITLE = _2000;
        public static final int CONTEXT = _3000;

        public static final int SPANSIZE_1 = _1;
        public static final int SPANSIZE_2 = _2;
        public static final int SPANSIZE_3 = _3;
        public static final int SPANSIZE_4 = _4;
    }

    public static boolean is_open = false;
    public static boolean bo = false;

}
