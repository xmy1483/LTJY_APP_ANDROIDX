package com.bac.bacplatform.module.splash;


import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.bac.bacplatform.R;
import com.bac.bacplatform.module.main.MainActivity;
import com.bac.bacplatform.utils.ui.UIUtils;

import java.util.ArrayList;

/**
 * 加载界面
 */
public class LoadingActivity extends AppCompatActivity {
	private ViewPager viewPager;
	private ArrayList<View> pageViews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.loading_activity);

		pageViews = new ArrayList<>();
		pageViews.add(View.inflate(this,R.layout.guide_view_page_1, null));
		pageViews.add(View.inflate(this,R.layout.guide_view_page_2, null));
		pageViews.add(View.inflate(this,R.layout.guide_view_page_3, null));
		pageViews.add(View.inflate(this,R.layout.guide_view_page_4, null));

		viewPager = (ViewPager) findViewById(R.id.guidePages);
		viewPager.setAdapter(new GuidePageAdapter());
	}



	private class GuidePageAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(pageViews.get(position));
			pageViews.get(pageViews.size() - 1).findViewById(R.id.loading_end)
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View arg0) {
							Intent intent = new Intent(LoadingActivity.this,MainActivity.class);
							UIUtils.startActivityInAnimAndFinishSelf(LoadingActivity.this,intent);
						}
					});
			return pageViews.get(position);
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(pageViews.get(position));
		}
	}

}
