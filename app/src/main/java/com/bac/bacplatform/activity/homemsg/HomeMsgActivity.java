package com.bac.bacplatform.activity.homemsg;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.bac.bacplatform.R;
import com.bac.bacplatform.activity.base.SimpleBaseActivity;
import com.bac.bacplatform.utils.ui.UIUtils;

public class HomeMsgActivity extends SimpleBaseActivity {
    private TextView txtReadAll;
    private RecyclerView recyclerView;
    private HomeMsgModel model;

    @Override
    public int setLayoutId() {
        return R.layout.activity_home_msg_layout;
    }

    @Override
    public void attachViewListener() {
        findViewById(R.id.bt_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txtReadAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.allMsgMarkReaded();
            }
        });
    }

    private void intiRecycler() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(model.getAdapter());
        model.refreshMsgList();
    }

    @Override
    public void initDataBeforeBindView() {
        model = new HomeMsgModel(this);
    }

    public void bindView() {
        txtReadAll = findViewById(R.id.txt_read_all);
        txtReadAll.setCompoundDrawablePadding(5);
        txtReadAll.setCompoundDrawables(UIUtils.getDrawable(this,R.mipmap.ic_trash,20),null,null,null);
        recyclerView = findViewById(R.id.recycler_view);
        intiRecycler();
    }

}
