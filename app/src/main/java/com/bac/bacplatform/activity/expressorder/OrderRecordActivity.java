package com.bac.bacplatform.activity.expressorder;

import android.graphics.Color;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.bac.bacplatform.R;
import com.bac.bacplatform.activity.base.SimpleBaseActivity;

public class OrderRecordActivity extends SimpleBaseActivity {
    private RecyclerView recyclerView;
    private OrderRecordModel model;

    @Override
    public int setLayoutId() {
        return R.layout.activity_express_layout;
    }

    private void initRecyclerView() {
        recyclerView.setBackgroundColor(Color.TRANSPARENT);
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

    @Override
    public void bindView() {
        recyclerView = findViewById(R.id.recycler_view);
        ((TextView) findViewById(R.id.txt_title)).setText("快递查询");
        model = new OrderRecordModel(this);
        initRecyclerView();
    }
}
