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
public class BottomSheetView extends BottomSheetFragment {

    public void setListener(SheetListener listener) {
        this.mListener = listener;
    }

    public interface SheetListener {
        void onConfirm(int pos, String content);
    }

    private SheetListener mListener;

    private Builder mData;

    //month 需要+1
    private int mYear, mMonth, mDay, mHours, mMin;

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
            initTime(view);
        } else {
            List<String> list = Arrays.asList(mData.mItems);
            LoopView loop_view_1 = view.findViewById(R.id.loop_view_1);
            loop_view_1.setItems(list);
            loop_view_1.setViewPadding(SizeUtils.dp2px(22), SizeUtils.dp2px(16), SizeUtils.dp2px(22), SizeUtils.dp2px(16));
            loop_view_1.setInitPosition(mData.mChooseIndex);
            loop_view_1.setNotLoop();

            view.findViewById(R.id.tv_ok).setOnClickListener(v -> {
                dismiss();
                if (mListener != null) {
                    int index = loop_view_1.getSelectedItem();
                    mListener.onConfirm(index, list.get(index));
                }
            });
        }
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

            if (!TextUtils.isEmpty(mData.mCurTime)) {
                calendar_choose.setTime(format.parse(mData.mCurTime));
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
        setLoopData(loop_view_1, years, year_index);
        loop_view_1.setListener(index -> {
            mYear = Integer.parseInt(years.get(index));
            checkDay();
        });

        //月
        LoopView loop_view_2 = view.findViewById(R.id.loop_view_2);
        setLoopData(loop_view_2, months, month);
        loop_view_2.setListener(index -> {
            mMonth = Integer.parseInt(months.get(index)) - 1;
            checkDay();
        });

        //日
        LoopView loop_view_3 = view.findViewById(R.id.loop_view_3);
        mLoopDay = loop_view_3;
        setLoopData(loop_view_3, days, day_index);
        loop_view_3.setListener(index -> mDay = Integer.parseInt(mCurDays.get(index)));

        view.findViewById(R.id.tv_ok).setOnClickListener(v -> {
            dismiss();
            if (mListener != null) {
                String time = mYear + "-" + (mMonth + 1) + "-" + mDay;
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
        view.setTextSize(18);
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
        private String mCurTime;//当前时间

        private boolean mHasHours;

        public Builder() {
            //默认是时间选择器
            mIsShowTime = true;
            mHasHours = false;
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

        //是否有小时分钟时间
        public Builder setShowTimeHasHours(boolean hasHours) {
            this.mIsShowTime = true;
            this.mHasHours = hasHours;
            return this;
        }

        public Builder setCurrentTime(String currentTime) {
            mCurTime = currentTime;
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
