package com.bac.bacplatform.bean;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bac.bacplatform.R;
import com.bac.bacplatform.module.main.view.HomeFragment;
import com.bac.bacplatform.utils.ui.UIUtils;

public class FuncBean {
    private String name;
    private int icon;
    private TextView txtBt;
    private HomeFragment fragment;
    public void setTxtBt(TextView txtBt,HomeFragment fragment) {
        this.txtBt = txtBt;
        this.fragment = fragment;
        initClick();
    }

    private void initClick() {
        txtBt.setCompoundDrawables(null, getIcon(),null,null);
        txtBt.setCompoundDrawablePadding(10);
        txtBt.setText(name);
        txtBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (name) {
                    case "油卡充值": fragment.turnToOilRecharge();break;
                    case "实体油卡": fragment.shihuaka();break;
                    case "话费购油": fragment.huafeigouyou();break;
                    case "手机充值": fragment.huafeichongzhi();break;
                    case "京东E卡": fragment.jingDongKa();break;
                    case "快递查询": fragment.kuaiDiChaXun();break;
                    case "联系我们": fragment.contactUs();break;
                    case "常见问题": fragment.changJianWenTi();break;
                }

//                Toast.makeText(fragment.activity, name, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public FuncBean(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Drawable getIcon() {
        return UIUtils.getDrawable(fragment.activity, icon,40);
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
