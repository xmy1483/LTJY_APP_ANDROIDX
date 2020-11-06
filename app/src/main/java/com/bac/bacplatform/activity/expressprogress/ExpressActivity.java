package com.bac.bacplatform.activity.expressprogress;

import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.bac.bacplatform.R;
import com.bac.bacplatform.activity.base.SimpleBaseActivity;

public class ExpressActivity extends SimpleBaseActivity {
    private RecyclerView recyclerView;
    private ExpressModel model;
    @Override
    public int setLayoutId() {
        return R.layout.activity_express_layout;
    }

    private void initRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(model.getAdapter());
        model.refreshMsgList();
    }

    @Override
    public void attachViewListener() {
        findViewById(R.id.bt_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void bindView() {
        recyclerView = findViewById(R.id.recycler_view);
        initRecyclerView();
    }

    @Override
    public void initDataBeforeBindView() {
        Intent it = getIntent();
        String oid = it.getStringExtra("orderId");
        model = new ExpressModel(this,oid);
    }
}
