package com.bac.bihupapa.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.bac.bihupapa.R;
import com.jakewharton.rxbinding.widget.RxTextView;

/**
 * 项目名称：Bacplatform
 * 包名：com.bac.bacplatform.utils.ui
 * 创建人：Wjz
 * 创建时间：2017/4/19
 * 类描述：
 */

public class UIUtils {

    public static int calculateInSampleSize(BitmapFactory.Options op, int reqWidth,
                                            int reqHeight) {

        int originalWidth  = op.outWidth;
        int originalHeight = op.outHeight;

        int inSampleSize = 1;
        if (originalWidth > reqWidth || originalHeight > reqHeight) {
            int halfWidth  = originalWidth / 2;
            int halfHeight = originalHeight / 2;
            while ((halfWidth / inSampleSize > reqWidth)
                    && (halfHeight / inSampleSize > reqHeight)) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    public static void startActivityInAnim(AppCompatActivity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.cl_slide_right_in, R.anim.cl_slide_left_out);
    }

    public static void startActivityInAnimAndFinishSelf(AppCompatActivity activity, Intent intent) {
        activity.onBackPressed();
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.cl_slide_right_in, R.anim.cl_slide_left_out);
    }

    public static Bitmap callbackBitmap_4(String filePath) {
        Bitmap bitmap = null;
        // 不管是拍照还是选择图片 每张图片都有在数据库中存储也 存储有对应旋转角度orientation值
        // 所以我们在取出图片时 把角度值取出以便能正确的显示图片,没有旋转时的效果观看
        if (filePath != null) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            BitmapFactory.decodeFile(filePath, options);//根据Path读取资源图片
            options.inSampleSize = calculateInSampleSize(options, 480, 300);
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(filePath, options);//根据Path读取资源图片

        }
        return bitmap;
    }

    public static String callbackBitmap_3(Context context, Uri mImageCaptureUri) {
        String filePath = null;
        // 不管是拍照还是选择图片 每张图片都有在数据库中存储也 存储有对应旋转角度orientation值
        // 所以我们在取出图片时 把角度值取出以便能正确的显示图片,没有旋转时的效果观看
        ContentResolver cr             = context.getContentResolver();
        String[]        filePathColumn = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.ORIENTATION};
        Cursor cursor         = cr.query(mImageCaptureUri, filePathColumn, null, null, null);// 根据Uri从数据库中找
        if (cursor != null) {
            cursor.moveToFirst();// 把游标移动到首位，因为这里的Uri是包含ID的所以是唯一的不需要循环找 指向第一个就是了
            filePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));// 获取图片路
            //readPicDegree(filePath); //根据文件路径 读取图片的旋转角度

            cursor.close();
        }

        return filePath;
    }

    /**
     * 获取设备高
     *
     * @param context
     * @return
     */
    public static int getHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);

        return dm.heightPixels;
    }

    /**
     * fragment 的toolbar
     *
     * @param activity
     * @param view
     * @param s
     * @param listener
     * @return
     */
    public static TextView initToolBar(final AppCompatActivity activity, View view,String s,
                                                     View.OnClickListener listener) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(activity.getString(R.string.toolbar_label));
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (listener == null) {
            listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onBackPressed();
                }
            };
        }
        toolbar.setNavigationOnClickListener(listener);
        TextView title = view.findViewById(R.id.tv_center);
        RxTextView.text(title).call(s == null ? "" : s);
        return title;
    }

    /**
     * activity 的toolbar
     * @param activity
     * @param s
     * @param listener
     * @return
     */
    public static TextView initToolBar(final AppCompatActivity activity, String s,
                                                     View.OnClickListener listener) {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setTitle(activity.getString(R.string.toolbar_label));
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (listener == null) {
            listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onBackPressed();
                }
            };
        }
        toolbar.setNavigationOnClickListener(listener);
        TextView title = activity.findViewById(R.id.tv_center);
        RxTextView.text(title).call(s == null ? "" : s);
        return title;
    }

    public static <T extends Fragment> T fragmentUtil(AppCompatActivity activity, T fragment, int id) {
        // 替换对应的fragment
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(id, fragment);
        transaction.commit();
        return fragment;
    }
}
