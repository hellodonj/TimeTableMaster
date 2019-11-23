package com.example.timetablemaster.timetablescrollview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timetablemaster.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ContentAdapter.OnContentScrollListener {

    @BindView(R.id.tv_left_title)
    TextView tvLeftTitle;
    @BindView(R.id.rv_tab_right)
    RecyclerView rvTabRight;
    @BindView(R.id.hor_scrollview)
    CustomHorizontalScrollView horScrollview;
    @BindView(R.id.ll_top_root)
    LinearLayout llTopRoot;
    @BindView(R.id.recycler_content)
    RecyclerView recyclerContent;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    private List<Entity> mEntities = new ArrayList<>();
    private List<String> rightMoveDatas = new ArrayList<>();
    private List<String> topTabs = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_4);
        ButterKnife.bind(this);
        tvLeftTitle.setOnClickListener(this);
        //处理顶部标题部分
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTabRight.setLayoutManager(linearLayoutManager);
        TopTabAdpater topTabAdpater = new TopTabAdpater(this);
        rvTabRight.setAdapter(topTabAdpater);
        for (int i = 0; i < 5; i++) {
            topTabs.add("周" + (i + 1) + "\n11-11");
        }
        topTabAdpater.setDatas(topTabs);
        //处理内容部分
        recyclerContent.setLayoutManager(new LinearLayoutManager(this));
        recyclerContent.setHasFixedSize(true);
        final ContentAdapter contentAdapter = new ContentAdapter(this);
        recyclerContent.setAdapter(contentAdapter);
        contentAdapter.setOnContentScrollListener(this);
        recyclerContent.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    Entity entity = new Entity();
                    entity.setLeftTitle(i + 1 + "");
                    rightMoveDatas.clear();
                    for (int j = 0; j < 5; j++) {
                        rightMoveDatas.add("具体课程\n高等数学" + j);
                    }
                    entity.setRightDatas(rightMoveDatas);
                    mEntities.add(entity);
                }
                contentAdapter.setDatas(mEntities);
            }
        }, 1000);


        //滚动RV时,同步所有横向位移的item
        recyclerContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                List<ContentAdapter.ItemViewHolder> viewHolderCacheList = contentAdapter.getViewHolderCacheList();
                if (null != viewHolderCacheList) {
                    int size = viewHolderCacheList.size();
                    for (int i = 0; i < size; i++) {
                        viewHolderCacheList.get(i).horItemScrollview.scrollTo(contentAdapter.getOffestX(), 0);
                    }
                }

            }
        });

        //同步顶部tab的横向scroll和内容页面的横向滚动
        //同步滚动顶部tab和内容
        horScrollview.setOnCustomScrollChangeListener(new CustomHorizontalScrollView.OnCustomScrollChangeListener() {
            @Override
            public void onCustomScrollChange(CustomHorizontalScrollView listener, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //代码重复,可以抽取/////
                List<ContentAdapter.ItemViewHolder> viewHolderCacheList = contentAdapter.getViewHolderCacheList();
                if (null != viewHolderCacheList) {
                    int size = viewHolderCacheList.size();
                    for (int i = 0; i < size; i++) {
                        viewHolderCacheList.get(i).horItemScrollview.scrollTo(scrollX, 0);
                    }
                }
            }

        });

        //下拉刷新
//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                recyclerContent.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        for (int i = 0; i < 50; i++) {
//                            Entity entity = new Entity();
//                            entity.setLeftTitle(i + 1 + "\n8:00");
//                            rightMoveDatas.clear();
//                            for (int j = 0; j < 5; j++) {
//                                rightMoveDatas.add("具体课程\n高等数学" + j);
//                            }
//                            entity.setRightDatas(rightMoveDatas);
//                            mEntities.add(entity);
//                        }
//                        contentAdapter.setDatas(mEntities);
//                        swipeRefresh.setRefreshing(false);
//                        Toast.makeText(MainActivity.this, "刷新完毕,刷新了50条数据", Toast.LENGTH_SHORT).show();
//                    }
//                }, 1500);
//            }
//        });


    }

    @Override
    public void onScroll(int offestX) {
        //处理单个item滚动时,顶部tab需要联动
        if (null != horScrollview) horScrollview.scrollTo(offestX, 0);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_left_title) {
           int2chineseNum(1);
        }
    }

    public static String int2chineseNum(int src) {
        final String num[] = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String dst = "";

        while (src > 0) {
            dst = num[src % 10]  + dst;
            src = src / 10;
        }
        String chineseNum =
                dst.replace("一零", "十");
        return chineseNum;
    }
}
