package com.bac.bacplatform.module.center.adapter;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by Wjz on 2017/5/4.
 */

public class UserCenterSectionBean extends SectionEntity<UserCenterSectionInnerBean> {

    private String more;

    public UserCenterSectionBean(boolean isHeader, String header, String more) {
        super(isHeader, header);
        this.more = more;
    }

    public UserCenterSectionBean(UserCenterSectionInnerBean innerBean) {
        super(innerBean);
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

}
