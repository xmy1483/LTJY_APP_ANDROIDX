package com.bac.bacplatform.old.module.bihupapa.adapter;


import android.content.res.Resources;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bac.bacplatform.R;
import com.bac.bacplatform.old.module.bihupapa.domain.CarAdvHomeBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.bac.bacplatform.utils.tools.CountDown.format3;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.adapter
 * 创建人：Wjz
 * 创建时间：2017/4/13
 * 类描述：
 */

public class CarAdvHomeAdapter extends BaseQuickAdapter<CarAdvHomeBean, BaseViewHolder> {

    public CarAdvHomeAdapter(@LayoutRes int layoutResId, @Nullable List<CarAdvHomeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CarAdvHomeBean advsBean) {
        TextView tv03 = baseViewHolder.getView(R.id.tv_03);
        ImageView iv01 = baseViewHolder.getView(R.id.iv_01);
        Resources resources = baseViewHolder.convertView.getResources();
        String start = format3.format(advsBean.getStart_time());
        String end = format3.format(advsBean.getEnd_time());

        baseViewHolder.setText(R.id.tv_01, advsBean.getAdv_title())
                .setText(R.id.tv_02, new StringBuilder().append("有效期：")
                        .append(start)
                        .append(" ~ ")
                        .append(end).toString());

        switch (advsBean.getStatus()) {
            case 40:
                tv03.setText("已发放");
                tv03.setTextColor(resources.getColor(R.color.colorPrimary));
                iv01.setImageResource(R.mipmap.car_adv_40);
                break;
            case 3:
                tv03.setText("已过期");
                tv03.setTextColor(resources.getColor(R.color.gray_8f));
                iv01.setImageResource(R.mipmap.car_adv_3);
                break;
            case 50:
                tv03.setText("审核失败");
                tv03.setTextColor(resources.getColor(R.color.colorPrimary));
                iv01.setImageResource(R.mipmap.car_adv_1);
                break;
            case 30:
                tv03.setText("审核成功");
                tv03.setTextColor(resources.getColor(R.color.colorPrimary));
                iv01.setImageResource(R.mipmap.car_adv_1);
                break;
            case 2:
                tv03.setText("未开始");
                iv01.setImageResource(R.mipmap.car_adv_2);
                break;
            case 10:
                tv03.setText("提交成功");
                tv03.setTextColor(resources.getColor(R.color.colorPrimary));
                iv01.setImageResource(R.mipmap.car_adv_1);
                break;
            default:
                tv03.setText("");
                iv01.setImageResource(R.mipmap.car_adv_1);
                break;
        }
    }
}
