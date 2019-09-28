package com.sum.library.view.sheet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.sum.library.R;
import com.sum.library.app.BaseBottomSheetFragment;
import com.sum.library.view.widget.wheelview.LoopView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by sdl on 2018/5/14.
 * 时间,单选，操作界面
 */
public class DialogTimeChooseView extends BaseBottomSheetFragment {

    public void setListener(SheetListener listener) {
        this.mListener = listener;
    }

    //点击数据回调
    public interface SheetListener {
        void onConfirm(int pos, String content);
    }

    private SheetListener mListener;

    private Builder mData;

    //month 需要+1
    private int mYear, mMonth, mDay;
    private String mHours, mMin;

    @Override
    protected int getLayoutId() {
        return R.layout.cus_bs_single_view;
    }

    private TextView mTvTitle;

    //设置标题
    public void setTitle(String msg) {
        if (mTvTitle != null) {
            mTvTitle.setText(msg);
        }
    }

    private int mColumnNum = 0;

    @Override
    protected void initParams(View view) {
        view.findViewById(R.id.tv_cancel).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.tv_ok).setOnClickListener(v -> dismiss());

        if (mData == null) {
            return;
        }
        mTvTitle = view.findViewById(R.id.tv_title);
        if (!TextUtils.isEmpty(mData.mTitle)) {
            mTvTitle.setText(mData.mTitle);
        }

        try {
            //0:单日期选择 1:单时间选择 2:时间+日期 3:自定义数据
            int chooseType = mData.mHoursType;
            if (chooseType == 0) {
                mColumnNum = 3;
                initTime(view);
            } else if (chooseType == 1) {
                mColumnNum = 2;
                initHours(view);
            } else if (chooseType == 4) {
                mColumnNum = 2;
                initMonth(view);
            } else if (chooseType == 3) {
                mColumnNum = 1;
                List<String> list = Arrays.asList(mData.mItems);
                LoopView loop_view_1 = view.findViewById(R.id.loop_view_1);
                setLoopData(loop_view_1, list, mData.mChooseIndex);
                view.findViewById(R.id.tv_ok).setOnClickListener(v -> {
                    if (mData.mClickDismiss) {
                        dismiss();
                    }
                    if (mListener != null) {
                        int index = loop_view_1.getSelectedItem();
                        mListener.onConfirm(index, list.get(index));
                    }
                });
            } else if (chooseType == 2) {
                mColumnNum = 5;
                initHours(view);
                initTime(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initMonth(@NonNull View view) {
        int month, day;
        String curDate = mData.mCurDate;

        Calendar calendar_choose = Calendar.getInstance();//当前
        int year = calendar_choose.get(Calendar.YEAR);
        mYear = year;
        if (!TextUtils.isEmpty(curDate) && curDate.contains("-")) {
            int index = curDate.indexOf("-");
            try {
                mMonth = month = Integer.parseInt(curDate.substring(0, index)) - 1;
                mDay = day = Integer.parseInt(curDate.substring(index + 1));
            } catch (NumberFormatException e) {
                mMonth = month = calendar_choose.get(Calendar.MONTH);
                mDay = day = calendar_choose.get(Calendar.DAY_OF_MONTH);
            }
        } else {
            mMonth = month = calendar_choose.get(Calendar.MONTH);
            mDay = day = calendar_choose.get(Calendar.DAY_OF_MONTH);
        }
//        Logger.e("mMonth->" + mMonth + " mDay:" + mDay);

        final ArrayList<String> months = getRange(1, 12);
        final ArrayList<String> days = getRange(1, getDaysInMonth(month, year));
        int day_index = day - 1;
        mCurDays = days;

        //月
        LoopView loop_view_2 = view.findViewById(R.id.loop_view_2);
        loop_view_2.setUnit("月");
        setLoopData(loop_view_2, months, month);
        loop_view_2.setListener(index -> {
            mMonth = Integer.parseInt(months.get(index)) - 1;
            checkDay();
        });

        //日
        LoopView loop_view_3 = view.findViewById(R.id.loop_view_3);
        loop_view_3.setUnit("日");
        mLoopDay = loop_view_3;
        setLoopData(loop_view_3, days, day_index);
        loop_view_3.setListener(index -> mDay = Integer.parseInt(mCurDays.get(index)));

        view.findViewById(R.id.tv_ok).setOnClickListener(v -> {
            if (mData.mClickDismiss) {
                dismiss();
            }
            if (mListener != null) {
                String time = (mMonth + 1) + "-" + mDay;
                mListener.onConfirm(0, time);
            }
        });
    }

    private void initHours(@NonNull View view) {
        String h, m;
        String time = mData.mCurDate;
        if (!TextUtils.isEmpty(time) && time.contains(":") && time.contains(" ")) {
            int index = time.indexOf(":");
            if (mData.mHoursType == 2) {//时间+日期
                h = time.substring(time.indexOf(" ") + 1, index);
            } else {
                h = time.substring(0, index);
            }
            m = time.substring(index + 1);
        } else {
            Calendar instance = Calendar.getInstance();
            h = instance.get(Calendar.HOUR_OF_DAY) + "";
            m = instance.get(Calendar.MINUTE) + "";
            if (h.length() == 1) {
                h = "0" + h;
            }
            if (m.length() == 1) {
                m = "0" + m;
            }
        }

        //时
        LoopView loop_view_4 = view.findViewById(R.id.loop_view_4);
        loop_view_4.setUnit("时");
        ArrayList<String> hour = getRangeHasZero(1, 23);
        int index_hour = hour.indexOf(h);
        if (index_hour == -1) {
            index_hour = 0;
        }
        mHours = hour.get(index_hour);
        setLoopData(loop_view_4, hour, index_hour);
        loop_view_4.setListener(index -> mHours = hour.get(index));

        //分
        ArrayList<String> min = getRangeHasZero(1, 59);
        LoopView loop_view_5 = view.findViewById(R.id.loop_view_5);
        loop_view_5.setUnit("分");
        int index_min = min.indexOf(m);
        if (index_min == -1) {
            index_min = 0;
        }
        mMin = min.get(index_min);
        setLoopData(loop_view_5, min, index_min);
        loop_view_5.setListener(index -> mMin = min.get(index));

        view.findViewById(R.id.tv_ok).setOnClickListener(v -> {
            if (mData.mClickDismiss) {
                dismiss();
            }
            if (mListener != null) {
                mListener.onConfirm(0, mHours + ":" + mMin);
            }
        });
    }

    private void initTime(@NonNull View view) {

        Calendar calendar_start = Calendar.getInstance();//开始
        Calendar calendar_end = Calendar.getInstance();//结束
        Calendar calendar_choose = Calendar.getInstance();//当前

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            if (mData.mMinDate > 0) {//最小时间
                calendar_start.setTimeInMillis(mData.mMinDate);
            } else {
                calendar_start.setTime(format.parse("1950-01-01"));
            }

            if (mData.mMaxDate > 0) {//最大时间
                calendar_end.setTimeInMillis(mData.mMaxDate);
            } else {
                calendar_end.setTime(format.parse("2100-12-31"));
            }

            if (!TextUtils.isEmpty(mData.mCurDate)) {
                calendar_choose.setTime(format.parse(mData.mCurDate));
            } else {
                calendar_choose.setTime(new Date());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        int year = calendar_choose.get(Calendar.YEAR);
        mYear = year;
        int month = calendar_choose.get(Calendar.MONTH);
        mMonth = month;
        int day = calendar_choose.get(Calendar.DAY_OF_MONTH);
        mDay = day;

        final List<String> years = getRange(calendar_start.get(Calendar.YEAR), calendar_end.get(Calendar.YEAR));
        int year_index = years.indexOf(year + "");

        final ArrayList<String> months = getRange(1, 12);

        final ArrayList<String> days = getRange(1, getDaysInMonth(month, year));
        int day_index = day - 1;
        mCurDays = days;

        //年
        LoopView loop_view_1 = view.findViewById(R.id.loop_view_1);
        loop_view_1.setUnit("年");
        setLoopData(loop_view_1, years, year_index);
        loop_view_1.setListener(index -> {
            mYear = Integer.parseInt(years.get(index));
            checkDay();
        });

        //月
        LoopView loop_view_2 = view.findViewById(R.id.loop_view_2);
        loop_view_2.setUnit("月");
        setLoopData(loop_view_2, months, month);
        loop_view_2.setListener(index -> {
            mMonth = Integer.parseInt(months.get(index)) - 1;
            checkDay();
        });

        //日
        LoopView loop_view_3 = view.findViewById(R.id.loop_view_3);
        loop_view_3.setUnit("日");
        mLoopDay = loop_view_3;
        setLoopData(loop_view_3, days, day_index);
        loop_view_3.setListener(index -> mDay = Integer.parseInt(mCurDays.get(index)));

        view.findViewById(R.id.tv_ok).setOnClickListener(v -> {
            if (mData.mClickDismiss) {
                dismiss();
            }
            if (mListener != null) {
                String time = mYear + "-" + (mMonth + 1) + "-" + mDay;
                if (mData.mHoursType == 2) {//时间+日期
                    time = time + " " + mHours + ":" + mMin;
                }
                mListener.onConfirm(0, time);
            }
        });
    }

    private List<String> mCurDays;
    private LoopView mLoopDay;

    private void checkDay() {
        int daysInMonth = getDaysInMonth(mMonth, mYear);
        int oldSize = mCurDays.size();
        if (daysInMonth != oldSize) {
            int newIndex = mLoopDay.getSelectedItem();

            ArrayList<String> range = getRange(1, daysInMonth);
            if (newIndex >= range.size()) {
                newIndex = range.size() - 1;
            }

            mDay = Integer.parseInt(range.get(newIndex));
            mLoopDay.setItems(range);
            mLoopDay.setInitPosition(newIndex);
//            mLoopDay.setTotalScrollYPosition(newIndex);

            mCurDays = range;
        }
    }

    private ArrayList<String> getRangeHasZero(int start, int end) {
        ArrayList<String> data = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            if (i < 10) {
                data.add("0" + i);
            } else {
                data.add(String.valueOf(i));
            }
        }
        return data;
    }

    private ArrayList<String> getRange(int start, int end) {
        ArrayList<String> data = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            data.add(String.valueOf(i));
        }
        return data;
    }

    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        if (wm != null) {
            wm.getDefaultDisplay().getRealSize(point);
        }
        return point.x;
    }

    private void setLoopData(LoopView view, List<String> items, int index, int columnNum) {
        Context context = getContext();
        if (context == null) {
            return;
        }
        int screenWidth = getScreenWidth(getContext());
        int oneWidth = screenWidth / 3;
        if (columnNum > 1) {
            oneWidth = screenWidth / columnNum - dp2px(20);
        }
        view.setItems(items);
        int top = dp2px(15);
        int left;
        if (mColumnNum == 5) {
            left = oneWidth / 2 - 30;
        } else {
            left = oneWidth / 2 - 20;
        }
        view.setViewPadding(left, top, left, top);
        view.setNotLoop();
        view.setTextSize(17);
        view.setInitPosition(index);

    }

    private void setLoopData(LoopView view, List<String> items, int index) {
        if (getContext() == null) {
            return;
        }
        if (mColumnNum > 3) {
            setLoopData(view, items, index, mColumnNum);
            return;
        }
        view.setItems(items);
        view.setViewPadding(dp2px(20), dp2px(15), dp2px(20), dp2px(15));
        view.setNotLoop();
        view.setTextSize(17);
        view.setInitPosition(index);
    }

    private static int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int getDaysInMonth(int month, int year) {
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.MARCH:
            case Calendar.MAY:
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.OCTOBER:
            case Calendar.DECEMBER:
                return 31;
            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
            case Calendar.NOVEMBER:
                return 30;
            case Calendar.FEBRUARY:
                return (year % 4 == 0) ? 29 : 28;
            default:
                throw new IllegalArgumentException("Invalid Month");
        }
    }

    public static class Builder {
        private SheetListener mListener;
        private String mTitle;

        private String[] mItems;
        private int mChooseIndex;

        private long mMaxDate, mMinDate;
        private String mCurDate;//当前日期或者时间

        private int mHoursType;//0:单日期选择 1:单时间选择 2:时间+日期 3:自定义数据 4:只显示月份选择

        private boolean mClickDismiss = true;//点集合自动关闭

        public Builder() {
            //默认是日期选择
            mHoursType = 0;
        }

        //时间日期选器
        public Builder setShowTimeAndHours() {
            this.mHoursType = 2;
            return this;
        }

        //单时间选器
        public Builder setShowHours() {
            this.mHoursType = 1;
            return this;
        }

        //月份选择
        public Builder setShowMonth() {
            this.mHoursType = 4;
            return this;
        }

        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public Builder setListener(SheetListener listener) {
            this.mListener = listener;
            return this;
        }

        public Builder setClickDismiss(boolean clickDismiss) {
            mClickDismiss = clickDismiss;
            return this;
        }

        //自定义数据源
        public Builder setCustomItems(String[] items) {
            this.mItems = items;
            mHoursType = 3;
            return this;
        }

        public Builder setChooseIndex(int chooseIndex) {
            this.mChooseIndex = chooseIndex;
            return this;
        }

        public Builder setCurrentTime(String currentTime) {
            mCurDate = currentTime;
            return this;
        }

        public Builder setMaxDate(long maxDate) {
            mMaxDate = maxDate;
            return this;
        }

        public Builder setMinDate(long minDate) {
            mMinDate = minDate;
            return this;
        }

        public DialogTimeChooseView showFast(Context activity) {
            if (activity instanceof FragmentActivity) {
                DialogTimeChooseView sheet = new DialogTimeChooseView();
                sheet.mListener = mListener;
                sheet.mData = this;
                sheet.show(((FragmentActivity) activity).getSupportFragmentManager(), "sheet");
                return sheet;
            }
            return null;
        }

    }
}
