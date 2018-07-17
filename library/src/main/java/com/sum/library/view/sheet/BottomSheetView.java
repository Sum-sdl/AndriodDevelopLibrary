package com.sum.library.view.sheet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.sum.library.R;
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
public class BottomSheetView extends AbstractBottomSheetFragment {

    public void setListener(SheetListener listener) {
        this.mListener = listener;
    }

    public interface SheetListener {
        void onConfirm(int pos, String content);
    }

    private SheetListener mListener;

    private Builder mData;

    //month 需要+1
    private int mYear, mMonth, mDay;
    private String mHours, mMin;

    @Override
    public int getBottomLayoutId() {
        return R.layout.cus_bs_single_view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.tv_cancel).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.tv_ok).setOnClickListener(v -> dismiss());

        if (mData == null) {
            return;
        }
        TextView tv_title = view.findViewById(R.id.tv_title);
        if (!TextUtils.isEmpty(mData.mTitle)) {
            tv_title.setText(mData.mTitle);
        }

        if (mData.mIsShowTime) {
            if (mData.mHoursType == 1) {
                initHours(view);
            } else {
                initTime(view);
            }
        } else {
            List<String> list = Arrays.asList(mData.mItems);
            LoopView loop_view_1 = view.findViewById(R.id.loop_view_1);
            setLoopData(loop_view_1, list, mData.mChooseIndex);
            view.findViewById(R.id.tv_ok).setOnClickListener(v -> {
                dismiss();
                if (mListener != null) {
                    int index = loop_view_1.getSelectedItem();
                    mListener.onConfirm(index, list.get(index));
                }
            });
        }
    }

    private void initHours(@NonNull View view) {
        String h, m;
        if (!TextUtils.isEmpty(mData.mCurHour) && mData.mCurHour.contains(":")) {
            int index = mData.mCurHour.indexOf(":");
            h = mData.mCurHour.substring(0, index);
            m = mData.mCurHour.substring(index + 1, mData.mCurHour.length());
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
        ArrayList<String> time = getRangeHasZero(1, 23);
        int index_hour = time.indexOf(h);
        mHours = time.get(index_hour);
        setLoopData(loop_view_4, time, index_hour);
        loop_view_4.setListener(index -> mHours = time.get(index));

        //分
        ArrayList<String> min = getRangeHasZero(1, 59);
        LoopView loop_view_5 = view.findViewById(R.id.loop_view_5);
        loop_view_5.setUnit("分");
        int index_min = min.indexOf(m);
        mMin = min.get(index_min);
        setLoopData(loop_view_5, min, index_min);
        loop_view_5.setListener(index -> mMin = min.get(index));

        view.findViewById(R.id.tv_ok).setOnClickListener(v -> {
            dismiss();
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

            if (mData.mMaxDate > 0) {
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
            dismiss();
            if (mListener != null) {
                String time = mYear + "-" + (mMonth + 1) + "-" + mDay;
                if (mData.mHoursType == 2) {
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
            mLoopDay.setInitPosition(0);
            mLoopDay.setTotalScrollYPosition(newIndex);

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

    private void setLoopData(LoopView view, List<String> items, int index) {
        view.setItems(items);
        view.setViewPadding(SizeUtils.dp2px(20), SizeUtils.dp2px(15), SizeUtils.dp2px(20), SizeUtils.dp2px(15));
        view.setNotLoop();
        view.setTextSize(17);
        view.setInitPosition(index);
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

        private boolean mIsShowTime;//时间单选标识

        private long mMaxDate, mMinDate;
        private String mCurDate;//当前日期

        private int mHoursType;//1:时间选择器
        private String mCurHour;//当前时间

        public Builder() {
            //默认是时间选择器
            mIsShowTime = true;
        }

        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public Builder setListener(SheetListener listener) {
            this.mListener = listener;
            return this;
        }


        public Builder setItems(String[] items) {
            this.mItems = items;
            mIsShowTime = false;
            return this;
        }

        public Builder setChooseIndex(int chooseIndex) {
            this.mChooseIndex = chooseIndex;
            return this;
        }

        //时间选器
        public Builder setShowHours() {
            this.mIsShowTime = true;
            this.mHoursType = 1;
            return this;
        }

        public Builder setHours(String hours) {
            this.mCurHour = hours;
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

        public void show(FragmentActivity activity) {
            show(activity.getSupportFragmentManager());
        }

        public void show(Fragment fragment) {
            show(fragment.getFragmentManager());
        }

        public void show(FragmentManager manager) {
            BottomSheetView sheet = new BottomSheetView();
            sheet.mListener = mListener;
            sheet.mData = this;
            sheet.show(manager, "sheet");
        }
    }
}
