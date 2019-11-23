package com.example.timetablemaster.timetableView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.timetablemaster.R;
import com.zhuangfei.timetable.TimetableView;
import com.zhuangfei.timetable.listener.ISchedule;
import com.zhuangfei.timetable.listener.IWeekView;
import com.zhuangfei.timetable.listener.OnDateBuildAapter;
import com.zhuangfei.timetable.listener.OnItemBuildAdapter;
import com.zhuangfei.timetable.listener.OnSlideBuildAdapter;
import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.utils.ScreenUtils;
import com.zhuangfei.timetable.view.WeekView;

import java.util.List;

/**
 * 描述: 课程表主工程
 * 作者|时间: djj on 2019/10/29 0029 上午 11:33
 * 博客地址: http://www.jianshu.com/u/dfbde65a03fc
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String AD_URL = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1545749786636&di=fd5483be8b08b2e1f0485e772dadace4&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2F5f9fae85770bb289f790e08d778516d128f0492a114a8-TNyOSi_fw658";

    private WeekView mWeekView;
    private TimetableView mTimetableView;
    private LinearLayout layout;
    private TextView titleTextView;
    private List<MySubject> mySubjects;

    //记录切换的周次，不一定是当前周
    int target = -1;
    AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleTextView = findViewById(R.id.id_title);
        layout = findViewById(R.id.id_layout);
        layout.setOnClickListener(this);
        initTimetableView();

        requestData();
    }

    /**
     * 2秒后刷新界面，模拟网络请求
     */
    private void requestData() {
        alertDialog = new AlertDialog.Builder(this)
                .setMessage("模拟请求网络中..")
                .setTitle("Tips").create();
        alertDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x123);
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (alertDialog != null) alertDialog.hide();
            mySubjects = SubjectRepertory.loadDefaultSubjects();
//            //增加广告
//            MySubject adSubject = new MySubject();
//            adSubject.setName("【广告】");
//            adSubject.setStart(1);
//            adSubject.setStep(2);
//            adSubject.setDay(7);
//            List<Integer> list = new ArrayList<>();
//            for (int i = 1; i <= 20; i++) list.add(i);
//            adSubject.setWeekList(list);
//            adSubject.setUrl(AD_URL);
//            mySubjects.add(adSubject);

            mWeekView.source(mySubjects).showView();
            mTimetableView.source(mySubjects).showView();

        }
    };

    /**
     * 初始化课程控件
     */
    private void initTimetableView() {
        //获取控件
        mWeekView = findViewById(R.id.id_weekview);
        mTimetableView = findViewById(R.id.id_timetableView);
        //显示课程时间
        showTime();
        //影藏周末
        hideWeekends();
        //设置周次选择属性
        mWeekView.curWeek(1)
                .callback(new IWeekView.OnWeekItemClickedListener() {
                    @Override
                    public void onWeekClicked(int week) {
                        int cur = mTimetableView.curWeek();
                        //更新切换后的日期，从当前周cur->切换的周week
                        mTimetableView.onDateBuildListener()
                                .onUpdateDate(cur, week);
                        mTimetableView.changeWeekOnly(week);
                    }
                })
                .callback(new IWeekView.OnWeekLeftClickedListener() {
                    @Override
                    public void onWeekLeftClicked() {
                        onWeekLeftLayoutClicked();
                    }
                })
                .isShow(false)//设置隐藏，默认显示
                .showView();
        //隐藏日期
//        hideDateView();
        //自定义日期栏
        customDateView();
        // 修改侧边栏背景
        modifySlideBgColor(getResources().getColor(R.color.app_gray2));
        mTimetableView.curWeek(1)
                .curTerm("大三下学期")
                .maxSlideItem(7) //左边栏的最大节次
                .monthWidthDp(65) //左边栏的宽度设置
                //透明度
                //日期栏0.1f、侧边栏0.1f，周次选择栏0.6f
                //透明度范围为0->1，0为全透明，1为不透明
//                .alpha(0.1f, 0.1f, 0.6f)
                .callback(new ISchedule.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, List<Schedule> scheduleList) {
                        display(scheduleList);
                    }
                })
                .callback(new ISchedule.OnItemLongClickListener() {
                    @Override
                    public void onLongClick(View v, int day, int start) {
                        Toast.makeText(MainActivity.this, "长按:周" + day + ",第" + start + "节", Toast.LENGTH_SHORT).show();
                    }
                })
                .callback(new ISchedule.OnWeekChangedListener() {
                    @Override
                    public void onWeekChanged(int curWeek) {
                        titleTextView.setText("第" + curWeek + "周");
                    }
                })
                .callback(new OnItemBuildAdapter() {
                    @Override
                    public void onItemUpdate(FrameLayout layout, TextView textView, TextView countTextView, Schedule schedule, GradientDrawable gd) {
                        super.onItemUpdate(layout, textView, countTextView, schedule, gd);
                        if (schedule.getName().equals("【广告】")) {
                            layout.removeAllViews();
                            ImageView imageView = new ImageView(MainActivity.this);
                            imageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                            layout.addView(imageView);
                            String url = (String) schedule.getExtras().get(MySubject.EXTRAS_AD_URL);

                            Glide.with(MainActivity.this)
                                    .load(url)
                                    .into(imageView);

                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(MainActivity.this, "进入广告网页链接", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                })
                .callback(new ISchedule.OnSpaceItemClickListener() {
                    @Override
                    public void onSpaceItemClick(int day, int start) {
                        Toast.makeText(MainActivity.this, "空白:周" + (day + 1) + ",第" + start + "节", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onInit(LinearLayout flagLayout, int monthWidth, int itemWidth, int itemHeight, int marTop, int marLeft) {

                    }
                })
                .showView();

    }

    /**
     * 更新一下，防止因程序在后台时间过长（超过一天）而导致的日期或高亮不准确问题。
     */
    @Override
    protected void onStart() {
        super.onStart();
        mTimetableView.onDateBuildListener()
                .onHighLight();
    }

    /**
     * 周次选择布局的左侧被点击时回调<br/>
     * 对话框修改当前周次
     */
    protected void onWeekLeftLayoutClicked() {
        final String items[] = new String[20];
        int itemCount = mWeekView.itemCount();
        for (int i = 0; i < itemCount; i++) {
            items[i] = "第" + (i + 1) + "周";
        }
        target = -1;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置当前周");
        builder.setSingleChoiceItems(items, mTimetableView.curWeek() - 1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        target = i;
                    }
                });
        builder.setPositiveButton("设置为当前周", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (target != -1) {
                    mWeekView.curWeek(target + 1).updateView();
                    mTimetableView.changeWeekForce(target + 1);
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    /**
     * 显示内容
     *
     * @param beans
     */
    protected void display(List<Schedule> beans) {
        String str = "";
        for (Schedule bean : beans) {
            str += bean.getName() + "," + bean.getWeekList().toString() + "," + bean.getStart() + "," + bean.getStep() + "\n";
        }
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_layout:
                //如果周次选择已经显示了，那么将它隐藏，更新课程、日期
                //否则，显示
                if (mWeekView.isShowing()) hideWeekView();
                else showWeekView();
                break;
        }
    }

    /**
     * 隐藏周次选择，此时需要将课表的日期恢复到本周并将课表切换到当前周
     */
    public void hideWeekView() {
        mWeekView.isShow(false);
        titleTextView.setTextColor(getResources().getColor(R.color.app_course_textcolor_blue));
        int cur = mTimetableView.curWeek();
        mTimetableView.onDateBuildListener()
                .onUpdateDate(cur, cur);
        mTimetableView.changeWeekOnly(cur);
    }

    public void showWeekView() {
        mWeekView.isShow(true);
        titleTextView.setTextColor(getResources().getColor(R.color.app_red));
    }

    /**
     * 删除课程
     * 内部使用集合维护课程数据，操作集合的方法来操作它即可
     * 最后更新一下视图（全局更新）
     */
    protected void deleteSubject(int position) {
        int size = mTimetableView.dataSource().size();
//        int pos = (int) (Math.random() * size);
        if (size > 0) {
            mTimetableView.dataSource().remove(position);
            mTimetableView.updateView();
        }
    }

    /**
     * 添加课程
     * 内部使用集合维护课程数据，操作集合的方法来操作它即可
     * 最后更新一下视图（全局更新）
     */
    protected void addSubject() {
        List<Schedule> dataSource = mTimetableView.dataSource();
        int size = dataSource.size();
        if (size > 0) {
            Schedule schedule = dataSource.get(0);
            dataSource.add(schedule);
            mTimetableView.updateView();
        }
    }

    /**
     * 隐藏非本周课程
     * 修改了内容的显示，所以必须更新全部（性能不高）
     * 建议：在初始化时设置该属性
     * <p>
     * updateView()被调用后，会重新构建课程，课程会回到当前周
     */
    protected void hideNonThisWeek() {
        mTimetableView.isShowNotCurWeek(false).updateView();
    }

    /**
     * 显示非本周课程
     * 修改了内容的显示，所以必须更新全部（性能不高）
     * 建议：在初始化时设置该属性
     */
    protected void showNonThisWeek() {
        mTimetableView.isShowNotCurWeek(true).updateView();
    }

    /**
     * 设置侧边栏最大节次，只影响侧边栏的绘制，对课程内容无影响
     *
     * @param num
     */
    protected void setMaxItem(int num) {
        mTimetableView.maxSlideItem(num).updateSlideView();
    }

    /**
     * 显示时间
     * 设置侧边栏构建监听，TimeSlideAdapter是控件实现的可显示时间的侧边栏
     */
    protected void showTime() {
        String[] times = new String[]{
                "8:00\n|8:45", "9:00-9:45", "10:10-10:55", "11:00-11:45",
                "15:00-15:45", "16:00-16:45", "17:00-17:45"
        };
        OnSlideBuildAdapter listener = (OnSlideBuildAdapter) mTimetableView.onSlideBuildListener();
        listener.setTimes(times)
                .setTimeTextColor(Color.GRAY);
        mTimetableView.updateSlideView();
    }

    /**
     * 隐藏时间
     * 将侧边栏监听置Null后，会默认使用默认的构建方法，即不显示时间
     * 只修改了侧边栏的属性，所以只更新侧边栏即可（性能高），没有必要更新全部（性能低）
     */
    protected void hideTime() {
        mTimetableView.callback((ISchedule.OnSlideBuildListener) null);
        mTimetableView.updateSlideView();
    }

    /**
     * 修改侧边栏背景,默认的使用的是OnSlideBuildAdapter，
     * 所以可以强转类型
     *
     * @param color
     */
    protected void modifySlideBgColor(int color) {
        OnSlideBuildAdapter listener = (OnSlideBuildAdapter) mTimetableView.onSlideBuildListener();
        listener.setBackground(color);
        mTimetableView.updateSlideView();
    }

    /**
     * 设置月份宽度
     */
    private void setMonthWidth() {
        mTimetableView.monthWidthDp(50).updateView();
    }

    /**
     * 设置月份宽度,默认40dp
     */
    private void resetMonthWidth() {
        mTimetableView.monthWidthDp(40).updateView();
    }

    /**
     * 隐藏周末
     */
    private void hideWeekends() {
        mTimetableView.isShowWeekends(false).updateView();
    }

    /**
     * 显示周末
     */
    private void showWeekends() {
        mTimetableView.isShowWeekends(true).updateView();
    }

    /**
     * 隐藏日期栏
     */
    protected void hideDateView() {
        mTimetableView.hideDateView();
    }

    /**
     * 显示日期栏
     */
    protected void showDateView() {
        mTimetableView.showDateView();
    }

    /**
     * 恢复默认日期栏
     */
    protected void cancelCustomDateView() {
        mTimetableView.callback((ISchedule.OnDateBuildListener) null)
                .updateDateView();
    }

    /**
     * 自定义日期栏
     * 该段代码有点长，但是很好懂，仔细看看会有收获的，嘻嘻
     */
    protected void customDateView() {
        mTimetableView.callback(
                new OnDateBuildAapter() {
                    @Override
                    public View onBuildDayLayout(LayoutInflater mInflate, int pos, int width, int height) {
                        int newHeight = ScreenUtils.dip2px(MainActivity.this, 50);
                        View v = mInflate.inflate(R.layout.item_custom_dateview, null, false);
                        TextView dayTextView = v.findViewById(R.id.id_week_day);
                        int month = Integer.parseInt(weekDates.get(0));
//                        for (int i = 1; i < 8; i++) {
//                            if (textViews[i] != null) {
//                                textViews[i].setText(weekDates.get(i) + "日");
//                            }
//                        }
                        dayTextView.setText(dateArray[pos] + "\n" +
                                month + "-");
                        layouts[pos] = v.findViewById(R.id.id_week_layout);

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, newHeight);
                        layouts[pos].setLayoutParams(lp);

                        return v;
                    }

                    @Override
                    public View onBuildMonthLayout(LayoutInflater mInflate, int width, int height) {
                        int newHeight = ScreenUtils.dip2px(MainActivity.this, 30);
                        View first = mInflate.inflate(R.layout.item_custom_dateview_first, null, false);
                        //月份设置
                        textViews[0] = first.findViewById(R.id.id_week_month);
                        layouts[0] = null;

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, newHeight);

                        int month = Integer.parseInt(weekDates.get(0));
                        first.setLayoutParams(lp);
                        textViews[0].setText(month + "");//+ "月"
                        return first;

                    }
                })
                .updateDateView();
    }

}




