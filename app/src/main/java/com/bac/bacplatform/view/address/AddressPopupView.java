package com.bac.bacplatform.view.address;

import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.bac.bacplatform.R;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bacplatform.view.address.adapter.AddressAdapter1;
import com.bac.bacplatform.view.address.adapter.AddressAdapter2;
import com.bac.bacplatform.view.address.adapter.AddressAdapter3;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Wjz on 2017/5/11.
 */

public class AddressPopupView {

    private RecyclerView mRv01;
    private RecyclerView mRv02;
    private RecyclerView mRv03;
    private PopupWindow mPopupWindow;
    private AddressAdapter1 mAddressAdapter01;
    private AddressAdapter2 mAddressAdapter02;
    private AddressAdapter3 mAddressAdapter03;

    private AppCompatActivity activity;

    public AddressPopupView(AppCompatActivity activity, final HashMap mHashMap) {
        this.activity = activity;

        View popup = View.inflate(activity, R.layout.order_home_pop_address, null);
        mRv01 = (RecyclerView) popup.findViewById(R.id.rv_01);
        mRv02 = (RecyclerView) popup.findViewById(R.id.rv_02);
        mRv03 = (RecyclerView) popup.findViewById(R.id.rv_03);
        mRv01.setLayoutManager(new LinearLayoutManager(activity));
        mRv02.setLayoutManager(new LinearLayoutManager(activity));
        mRv03.setLayoutManager(new LinearLayoutManager(activity));

        mAddressAdapter01 = new AddressAdapter1(android.R.layout.simple_list_item_1);
        mAddressAdapter02 = new AddressAdapter2(android.R.layout.simple_list_item_1);
        mAddressAdapter03 = new AddressAdapter3(android.R.layout.simple_list_item_1);

        mRv01.setAdapter(mAddressAdapter01);
        mRv02.setAdapter(mAddressAdapter02);
        mRv03.setAdapter(mAddressAdapter03);

        mPopupWindow = new PopupWindow(popup, ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.getHeight(activity) / 3);
        // 设置获取焦点
        mPopupWindow.setFocusable(true);
        // 设置边缘点击收起
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());

        mRv01.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int i) {
                List<InsuranceAddressBean> data = baseQuickAdapter.getData();
                for (InsuranceAddressBean insuranceBean : data) {
                    insuranceBean.setSelect(false);
                }
                InsuranceAddressBean insuranceAddress = data.get(i);
                insuranceAddress.setSelect(true);
                baseQuickAdapter.notifyDataSetChanged();

                mAddressAdapter02.getData().clear();
                List<InsuranceAddressBean.CitysBean> citys = insuranceAddress.getCitys();
                for (InsuranceAddressBean.CitysBean city : citys) {
                    city.setSelect(false);
                }
                mAddressAdapter02.addData(citys);
                mAddressAdapter03.getData().clear();
                mHashMap.put("province", insuranceAddress.getProvince());
                mHashMap.put("province_name", insuranceAddress.getProvince_name());
            }
        });
        mRv02.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int i) {
                List<InsuranceAddressBean.CitysBean> data = baseQuickAdapter.getData();
                for (InsuranceAddressBean.CitysBean citysBean : data) {
                    citysBean.setSelect(false);
                }

                InsuranceAddressBean.CitysBean citysBean = data.get(i);
                citysBean.setSelect(true);

                baseQuickAdapter.notifyDataSetChanged();

                mAddressAdapter03.getData().clear();
                mAddressAdapter03.addData(citysBean.getAreas());

                mHashMap.put("city", citysBean.getCity());
                mHashMap.put("city_name", citysBean.getCity_name());
            }
        });
        mRv03.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int i) {
                InsuranceAddressBean.CitysBean.AreasBean areasBean = (InsuranceAddressBean.CitysBean.AreasBean) baseQuickAdapter.getData().get(i);
                mHashMap.put("area", areasBean.getArea());
                mHashMap.put("area_name", areasBean.getArea_name());
                if (onShowAddress != null) {
                    onShowAddress.onShowAddress();
                }
                mPopupWindow.dismiss();
            }

        });
    }
    public void setPopupData(List<InsuranceAddressBean> insuranceAddressBeenList){
        mAddressAdapter01.getData().clear();
        mAddressAdapter01.addData(0,insuranceAddressBeenList);
    }

    public void showPopup(View view, int gravity, int x, int y) {
        mPopupWindow.showAtLocation(view, gravity, x, y);
    }

    public interface OnShowAddress {
        void onShowAddress();
    }

    private OnShowAddress onShowAddress;

    public void setOnShowAddress(OnShowAddress onShowAddress) {
        this.onShowAddress = onShowAddress;
    }
}
