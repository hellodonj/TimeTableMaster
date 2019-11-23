package com.example.timetablemaster.timetable3;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.timetablemaster.R;

import java.util.Random;

public class TimetableActivity3 extends AppCompatActivity {

    /**
     * 第一个无内容的格子
     */
    protected TextView empty;
    /**
     * 星期一的格子
     */
    protected TextView monColum;
    /**
     * 星期二的格子
     */
    protected TextView tueColum;
    /**
     * 星期三的格子
     */
    protected TextView wedColum;
    /**
     * 星期四的格子
     */
    protected TextView thrusColum;
    /**
     * 星期五的格子
     */
    protected TextView friColum;
    /**
     * 星期六的格子
     */
    protected TextView satColum;
    /**
     * 星期日的格子
     */
    protected TextView sunColum;
    /**
     * 课程表body部分布局
     */
    protected RelativeLayout course_table_layout;
    /**
     * 屏幕宽度
     **/
    protected int screenWidth;
    /**
     * 课程格子平均宽度
     **/
    protected int aveWidth;
    int gridHeight1 = 0;
    //(0)对应12节；(2)对应34节；(4)对应56节；(6)对应78节；(8)对应于9 10节
    int[] jieci = {0, 2, 4, 6, 8};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable3);
        //获得列头的控件
        empty = (TextView) this.findViewById(R.id.test_empty);
        monColum = (TextView) this.findViewById(R.id.test_monday_course);
        tueColum = (TextView) this.findViewById(R.id.test_tuesday_course);
        wedColum = (TextView) this.findViewById(R.id.test_wednesday_course);
        thrusColum = (TextView) this.findViewById(R.id.test_thursday_course);
        friColum = (TextView) this.findViewById(R.id.test_friday_course);
        satColum = (TextView) this.findViewById(R.id.test_saturday_course);
        sunColum = (TextView) this.findViewById(R.id.test_sunday_course);
        course_table_layout = (RelativeLayout) this.findViewById(R.id.test_course_rl);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //屏幕宽度
        int width = dm.widthPixels;
        //平均宽度
        int aveWidth = width / 8;
        //第一个空白格子设置为25宽
        empty.setWidth(aveWidth * 3 / 4);
        monColum.setWidth(aveWidth * 33 / 32 + 1);
        tueColum.setWidth(aveWidth * 33 / 32 + 1);
        wedColum.setWidth(aveWidth * 33 / 32 + 1);
        thrusColum.setWidth(aveWidth * 33 / 32 + 1);
        friColum.setWidth(aveWidth * 33 / 32 + 1);
        satColum.setWidth(aveWidth * 33 / 32 + 1);
        sunColum.setWidth(aveWidth * 33 / 32 + 1);
        this.screenWidth = width;
        this.aveWidth = aveWidth;
        int height = dm.heightPixels;
        int gridHeight = height / 10;
        gridHeight1 = gridHeight;
        //设置课表界面
        //动态生成10 * maxCourseNum个textview
        for (int i = 1; i <= 10; i++) {

            for (int j = 1; j <= 8; j++) {

                TextView tx = new TextView(TimetableActivity3.this);
                tx.setId((i - 1) * 8 + j);
                //除了最后一列，都使用course_text_view_bg背景（最后一列没有右边框）
                if (j < 8)
                    tx.setBackgroundDrawable(TimetableActivity3.this.
                            getResources().getDrawable(R.drawable.course_table_last_colum));
                else
                    tx.setBackgroundDrawable(TimetableActivity3.this.
                            getResources().getDrawable(R.drawable.course_table_last_colum));
                //相对布局参数
                RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
                        aveWidth * 33 / 32 + 1,
                        gridHeight);
                //文字对齐方式
                tx.setGravity(Gravity.CENTER);
                //字体样式
                tx.setTextAppearance(this, R.style.courseTableText);
                //如果是第一列，需要设置课的序号（1 到 12）
                if (j == 1) {
                    tx.setText(String.valueOf(i) + "\n" + "8:00");
                    rp.width = aveWidth * 3 / 4;
                    //设置他们的相对位置
                    if (i == 1)
                        rp.addRule(RelativeLayout.BELOW, empty.getId());
                    else
                        rp.addRule(RelativeLayout.BELOW, (i - 1) * 8);
                } else {
                    rp.addRule(RelativeLayout.RIGHT_OF, (i - 1) * 8 + j - 1);
                    rp.addRule(RelativeLayout.ALIGN_TOP, (i - 1) * 8 + j - 1);
                    tx.setText("");
                }

                tx.setLayoutParams(rp);
                course_table_layout.addView(tx);
            }
        }

        setCourseMessage(1, jieci[1], "地图制图基础\n9-19(3,4)\n于焕菊\nJ14-305室");
        setCourseMessage(2, jieci[1], "大学英语(3-3)\n1-19(3,4)\n徐育新\nJ1-310室");
        setCourseMessage(3, jieci[1], "概率论与数理统计\n1-9(3,4)\n郑艳林\nJ1-117室");
        setCourseMessage(3, jieci[1], "概率论与数理统计\n1-9(3,4)\n郑艳林\nJ1-117室");
        setCourseMessage(4, jieci[1], "线性代数\n1-11(3,4)\n王新赠\nJ14-121室");
        setCourseMessage(5, jieci[1], "大学物理（B）（2-2）\n1-19(3,4)\n周明东\nJ1-307室");
        setCourseMessage(6, jieci[0], "大学物理（B）（2-2）\n1-19(3,4)\n周明东\nJ1-307室");
        setCourseMessage(7, jieci[2], "大学物理（B）（2-2）\n1-19(3,4)\n周明东\nJ1-307室");

        setCourseMessage(1, jieci[0], "地图制图基础\n9-19(3,4)\n于焕菊\nJ14-305室");
        setCourseMessage(2, jieci[2], "大学英语(3-3)\n1-19(3,4)\n徐育新\nJ1-310室");
        setCourseMessage(3, jieci[3], "概率论与数理统计\n1-9(3,4)\n郑艳林\nJ1-117室");
        setCourseMessage(4, jieci[2], "线性代数\n1-11(3,4)\n王新赠\nJ14-121室");
        setCourseMessage(5, jieci[0], "大学物理（B）（2-2）\n1-19(3,4)\n周明东\nJ1-307室");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void setCourseMessage(int xingqi, int jieci, String courseMessage) {
        //五种颜色的背景
        int[] background = {R.drawable.course_info_blue, R.drawable.course_info_blue,
                R.drawable.course_info_blue, R.drawable.course_info_blue,
                R.drawable.course_info_blue};
        // 添加课程信息
        TextView courseInfo = new TextView(this);
        courseInfo.setText(courseMessage);
        //该textview的高度根据其节数的跨度来设置
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                aveWidth * 31 / 32,
                (gridHeight1 - 5) * 2);
        //textview的位置由课程开始节数和上课的时间（day of week）确定
        rlp.topMargin = 5 + jieci * gridHeight1;
        rlp.leftMargin = 1;
        // 偏移由这节课是星期几决定
        rlp.addRule(RelativeLayout.RIGHT_OF, xingqi);
        //字体剧中
        courseInfo.setGravity(Gravity.CENTER);
        // 设置一种背景
        Random random = new Random();
        courseInfo.setBackgroundResource(background[random.nextInt(5)]);
        courseInfo.setTextSize(12);
        courseInfo.setLayoutParams(rlp);
        courseInfo.setTextColor(Color.WHITE);
        //设置不透明度
        courseInfo.getBackground().setAlpha(222);
        course_table_layout.addView(courseInfo);
    }
}
