package com.bac.bacplatform.utils.tools;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bac.bacplatform.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Treaker on 2015/11/21.
 */
public class ToolBarUtil {
    private List<View> list = new ArrayList<>();

    public void createToolBar(LinearLayout container, String[] toolBarTitleArr, int[] iconArr, ColorStateList colorStateList) {

        for (int i = 0; i < toolBarTitleArr.length; i++) {

            final View view = View.inflate(container.getContext(), R.layout.toolbar_btn_item, null);
            final ViewHolder viewHolder;
            Object tag = view.getTag();
            if (tag != null) {
                viewHolder = (ViewHolder) tag;
            } else {
                viewHolder = new ViewHolder(view);
            }
            view.setTag(viewHolder);
            viewHolder.setData(toolBarTitleArr[i], iconArr[i],colorStateList);


            int width = 0;
            int height = LinearLayout.LayoutParams.MATCH_PARENT;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            //设置weight属性
            params.weight = 1;
            container.addView(view, params);
            //保存textView到集合中
            list.add(view);

            //设置点击事件
            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //不同模块之间传值需要接口回调
                    //3.需要传值的地方，用接口对象调用接口方法
                    mOnToolBarClickListener.onToolBarClick(finalI, viewHolder);

                }
            });
        }
    }

    public class ViewHolder {

        private ImageView iv;
        private TextView tvLabel;
        private TextView tvCount;
        private FrameLayout frameLayout;

        public ViewHolder(View view) {
            iv = (ImageView) view.findViewById(R.id.iv);
            tvLabel = (TextView) view.findViewById(R.id.tv);
            frameLayout = (FrameLayout) view.findViewById(R.id.fl);
            tvCount = (TextView) view.findViewById(R.id.tv_01);
        }

        public void setData(String s, int icon, ColorStateList colorStateList) {
            tvLabel.setText(s);
            tvLabel.setTextColor(colorStateList);
            iv.setImageResource(icon);
        }

        public void setSelected(boolean b) {
            tvLabel.setSelected(b);
            iv.setSelected(b);
        }

        public void setCount(int count) {
            if (count > 0) {
                frameLayout.setVisibility(View.VISIBLE);
                tvCount.setText(count + "");
            } else {
                frameLayout.setVisibility(View.GONE);
            }
        }
    }

    public List<View> getList() {
        return list;
    }

    public void changeColor(int position) {
        //还原所有的颜色
        for (View view : list) {
            ((ViewHolder) view.getTag()).setSelected(false);
        }
        ((ViewHolder) list.get(position).getTag()).setSelected(true);
    }

    //1.创建接口和方法
    public interface OnToolBarClickListener {
        void onToolBarClick(int position, ViewHolder viewHolder);
    }

    //2.定义接口变量
    OnToolBarClickListener mOnToolBarClickListener;

    //4.暴露一个公共的方法
    public void setmOnToolBarClickListener(OnToolBarClickListener mOnToolBarClickListener) {
        this.mOnToolBarClickListener = mOnToolBarClickListener;
    }
}
