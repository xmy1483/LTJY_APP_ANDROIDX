package com.bac.commonlib.utils.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

/**
 * Created by wujiazhen on 2017/7/7.
 */

public class BacTextWatcher  implements TextWatcher {

    private final TextView tvPhone;

    public BacTextWatcher(TextView tvPhone) {
        this.tvPhone=tvPhone;
    }

    /**
     * 格式化手机号码 xxx xxxx xxxx
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (s == null || s.length() == 0) {
            return;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                continue;
            } else {
                sb.append(s.charAt(i));
                if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                    sb.insert(sb.length() - 1, ' ');
                }
            }
        }
        if (!sb.toString().equals(s.toString())) {
            int index = start + 1;
            if (sb.charAt(start) == ' ') {
                if (before == 0) {
                    index++;
                } else {
                    index--;
                }
            } else {
                if (before == 1) {
                    index--;
                }
            }
            tvPhone.setText(sb.toString());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
