package com.bac.commonlib.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wujiazhen on 2017/8/9.
 */

public class MoveAssertsService extends IntentService {

    private InputStream in;
    private FileOutputStream out;

    public static Intent newIntent(Context context) {
        return new Intent(context, MoveAssertsService.class);
    }

    public MoveAssertsService() {
        super(MoveAssertsService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // 保存 weex 文件的上级路径
        String sdPath = this.getExternalFilesDir(null).toString();

        try {
            if (!checkFileExists("dist", sdPath)) {
                assetToSD("dist", sdPath);
            }
        } catch (Exception e) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }

            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        /*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // 查找数据
        HttpHelperLib.getInstance().net(
                new BacHttpBean()
                .setMethodName("BASEXML.QUERY_WEEX_VERSION")
                .put("file_version", preferences.getString("bhpp_version","1.0.0.000")),null,null)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {

                        System.out.println(s);
                    }
                });
*/

    }

    private boolean checkFileExists(String assetDir, String sdDir) {
        File file = new File(sdDir, assetDir);
        if (file.exists()) {

            // 判断文件夹中是否存在文件
            return file.list().length > 0;

        } else {
            return false;
        }
    }

    /**
     * @param assetDir asset 目录
     * @param sdDir    sd目录
     * @throws IOException
     */
    private void assetToSD(String assetDir, String sdDir) throws Exception {
        // assets
        AssetManager asset = this.getAssets();
        String[] fileArray = asset.list(assetDir);
        // 目录
        if (fileArray.length > 0) {

            // 创建根目录
            if (createDis(assetDir, sdDir)) {
                // 根据 asset 目录，在 sd 中创建目录
                for (String fileName : fileArray) {
                    assetToSD(assetDir + "/" + fileName, sdDir);
                }
            }

        } else {
            // 文件 直接复制到 sd 目录中

            /*
            dist/activity/activityResult.js
            dist/activity/otherRequest.js
            dist/activity/representActivityDetail.js
            dist/address/addressList.js
            dist/address/detailsAddress.js
            dist/address/inputAddress.js
            dist/common/api.js
            dist/index.js
            dist/index.web.js
            dist/main/main.js
            dist/main/myRepresent.js
            dist/main/representActivity.js
            dist/me/aboutUs.js
            dist/me/me.js
            dist/me/representDetail.js
            dist/me/representDetailProcess.js
            dist/message/message.js
            dist/myCar/myCar.js
            dist/ticket/myTicket.js
            dist/ticket/ticketDetail.js
            dist/wallet/wallet.js
            * */
            File file = new File(sdDir, assetDir);

            in = asset.open(assetDir);
            out = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int byteCount = 0;
            while ((byteCount = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteCount);
            }
            out.flush();
        }
    }

    private boolean createDis(String assetDir, String sdDir) {
        boolean b = false;
        // dist 文件
        File file = new File(sdDir, assetDir);
        // 判断当前 文件|文件夹 是否存在
        if (file.exists()) {
            b = true;
        } else {
            b = file.mkdirs();
        }
        return b;
    }
}
