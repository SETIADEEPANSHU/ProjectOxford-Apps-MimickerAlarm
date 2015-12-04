package com.microsoft.smartalarm;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RepeatingDaysPreference extends Preference{

    private boolean mChanged;
    private DayView[] mDayViews;

    public RepeatingDaysPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        String[] entryLabels = context.getResources().getStringArray(R.array.pref_ring_repeating_labels);
        mDayViews = new DayView[entryLabels.length];
        for(int d = 0; d < entryLabels.length; d++){
            DayView dayView = new DayView(getContext(), this);
            dayView.setText(entryLabels[d]);
            mDayViews[d] = dayView;
        }
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        LinearLayout container = (LinearLayout) holder.findViewById(R.id.pref_repeating_container);
        for(DayView day : mDayViews){
            day.setLayoutParams(new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            container.addView(day);
        }
        super.onBindViewHolder(holder);
    }

    public void setRepeatingDay(int index, boolean doesRepeat){
        mDayViews[index].setRepeating(doesRepeat);
    }

    public boolean hasChanged() {
        return mChanged;
    }

    public void setChanged(boolean changed) {
        mChanged = changed;
    }

    public boolean[] getRepeatingDays() {
        boolean[] repeatingDays = new boolean[mDayViews.length];
        for(int i = 0; i < mDayViews.length; i++){
            repeatingDays[i] = mDayViews[i].getRepeating();
        }
        return repeatingDays;
    }


    private class DayView extends TextView {
        private boolean mRepeating = false;
        private Paint mPaint;
        private final static int PADDING = 20;
        private RepeatingDaysPreference mParent;

        public DayView(Context context, RepeatingDaysPreference parent) {
            super(context);
            mParent = parent;
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setColor(Color.RED);

            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleRepeating();
                }
            });
            setGravity(Gravity.CENTER);
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            setHeight(getMeasuredWidth());
            setPadding(PADDING, PADDING, PADDING, PADDING);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (mRepeating){
                /*
                float centerX = getWidth() / 2;
                float centerY = getHeight() / 2;
                canvas.drawCircle(centerX, centerY, centerX - PADDING, mPaint);
                */
                setTypeface(null, Typeface.BOLD);
            }
            else{
                setTypeface(null, Typeface.NORMAL);
            }
            super.onDraw(canvas);
        }

        public void setRepeating(boolean repeating){
            mRepeating = repeating;
            mParent.setChanged(true);
            invalidate();
        }

        public void toggleRepeating(){
            setRepeating(!getRepeating());
        }

        public boolean getRepeating() {
            return mRepeating;
        }
    }
}
