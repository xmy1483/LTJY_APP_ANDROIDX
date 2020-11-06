package com.bac.bacplatform.tst;

import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.commonlib.utils.tools._Build;

import java.lang.reflect.Field;

/**
 * Created by Wjz on 2017/6/2.
 *
 * 反射 获取 build 文件
 */

public class _ClazzBuildActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_acitivity);
        tv = (TextView) findViewById(R.id.tv);

        String s = _Build.getPhoneParameter();
        System.out.println(s);

        try {
            getPhoneParameter();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void getPhoneParameter() throws ClassNotFoundException, IllegalAccessException {

        // 反射获取 android.os.Build 文件
        StringBuilder sb = new StringBuilder();
        Class<?> aClass = Class.forName("android.os.Build");
        getFields(sb, aClass);

        // 得到内部类
        Class<?>[] declaredClasses = aClass.getDeclaredClasses();

        for (int i = 0; i < declaredClasses.length; i++) {

            getFields(sb, declaredClasses[i]);
        }


        sb.append("Secure                                           ")
                .append(Settings.Secure.getString(BacApplication.getBacApplication().getContentResolver(),
                        Settings.Secure.ANDROID_ID));

        System.out.println(sb);

    }

    private void getFields(StringBuilder sb, Class<?> aClass) throws IllegalAccessException {
        // 得到成员变量
        Field[] fields = aClass.getDeclaredFields();

        for (int i = fields.length - 1; i >= 0; i--) {

            Field f = fields[i];
            f.setAccessible(true);
            String name = f.getName();
            Object o = f.get(name);

            String s = f.getType().toString();

            if (s.contains("[L")) {
                String[] arrayStr = (String[]) o;
                for (int i1 = 0; i1 < arrayStr.length; i1++) {

                    String s1 = arrayStr[i1];
                    sb
                            .append(name)
                            .append("                                                    ")
                            .append(s1)
                            .append("\n");

                }
            }else{
                sb
                        .append(name)
                        .append("                                                       ")
                        .append(o)
                        .append("\n");
            }
        }
    }


}
