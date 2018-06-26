package com.an_liberty.luguianview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.an_liberty.luguianview.R;

/**
 * Created by liberty on 2018/2/11.
 */

public class BeautyGroup extends ViewGroup {

    private static final String TAG = BeautyGroup.class.getSimpleName();

    //角度
    private float btnAngle;

    public BeautyGroup(Context context) {
        this(context, null);
    }

    public BeautyGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BeautyGroup);
        btnAngle = typedArray.getFloat(R.styleable.BeautyGroup_btnAngle, 90);
        typedArray.recycle();
    }

    private void init() {
        if (getChildCount() > 2) {
            throw new IllegalStateException("the count of view child could not be more than 2.");
        }
        float[] angleArr = {btnAngle, 180 - btnAngle};
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (!(v instanceof BeautyView)) {
                throw new IllegalStateException("the child can only be BeautyView");
            }
            //只能自定义view
            BeautyView beautyView = (BeautyView) v;
            //设置view的角度
            beautyView.setBtnChildAngle(angleArr[i]);
            //0是左 1是右
            beautyView.btnIndex = i;
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        this.measureChildren(widthMeasureSpec, heightMeasureSpec);
        //测量宽和高
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

        Log.d(TAG, "measuredWidth : " + width);
        Log.d(TAG, "measuredHeight : " + height);

        setMeasuredDimension(width, height);

    }

    private int measureWidth(int widthMeasure) {
        int size = MeasureSpec.getSize(widthMeasure);
        int mode = MeasureSpec.getMode(widthMeasure);


        int width = 0;

        switch (mode) {
            case MeasureSpec.EXACTLY: {
                width = size;
            }
            break;
            case MeasureSpec.AT_MOST: {
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    width += child.getMeasuredWidth();
                }
                if (width>size){
                    width=size;
                }
                width+=getPaddingLeft()+getPaddingRight();
            }
            break;
        }
        return width;
    }

    private int measureHeight(int heightMeasure) {
        int size = MeasureSpec.getSize(heightMeasure);
        int mode = MeasureSpec.getMode(heightMeasure);

        int height = 0;

        switch (mode) {
            case MeasureSpec.EXACTLY: {
                height = size;
            }
            break;
            case MeasureSpec.AT_MOST: {
                int childHeight1 = getChildAt(0).getMeasuredHeight();
                int childHeight2 = getChildAt(1).getMeasuredHeight();
                height = Math.max(childHeight1, childHeight2);
                if (height>size){
                    height=size;
                }
                height+=getPaddingTop()+getPaddingBottom();
            }
            break;
        }
        return height;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int topPadding = getPaddingTop();
        int bottomPadding = getPaddingBottom();
        int leftPadding = getPaddingLeft();
        int rightPadding = getPaddingRight();

        double distance = 0.0;
        for (int i = 0; i < getChildCount(); i++) {
            BeautyView child = (BeautyView) getChildAt(i);
            double cot = Math.cos(child.getBtnChildAngle() / 180 * Math.PI) / Math.sin(child.getBtnChildAngle() / 180 * Math.PI);
            //如果cot为0证明是90度 两边都是矩形
            if (cot == 0) {
                if (i == 0) {
                    //确定左上右下点
                    layoutChildFrame(child,leftPadding,topPadding,child.getMeasuredWidth()+leftPadding,child.getMeasuredHeight() +topPadding);
                } else if (i == 1) {
                    layoutChildFrame(child,getChildAt(i - 1).getRight()+leftPadding,topPadding,getChildAt(i - 1).getRight() + child.getMeasuredWidth()+leftPadding,child.getMeasuredHeight() +topPadding);
                }
            } else {
                //两个子view不是矩形
                if (distance > 0) {
                    distance = Math.min(distance, Math.abs(cot * child.getMeasuredHeight()));
                    Log.d("sds",distance+"");
                } else {

                    distance = Math.abs(cot * child.getMeasuredHeight());
                }
//                distance = Math.abs(cot * child.getMeasuredHeight()) ;
                if (i == 0) {
                    layoutChildFrame(child,leftPadding, topPadding,(int) (child.getMeasuredWidth() + distance / 2)+leftPadding, child.getMeasuredHeight() +topPadding);
                } else if (i == 1) {
                    layoutChildFrame(child,(int) (getChildAt(i - 1).getRight() - distance)+leftPadding,topPadding,getMeasuredWidth(), child.getMeasuredHeight() +topPadding);
//                    child.layout((int) (getChildAt(i - 1).getRight() - distance), topPadding, (int) (getChildAt(i - 1).getRight() + child.getMeasuredWidth() - distance / 2) - rightPadding, child.getMeasuredHeight() - bottomPadding);
                }
            }
        }

    }

    /**
     *
     * @param child 子view
     * @param l 子试图左边距到父类左边距的距离
     * @param t 子试图上边距到父类上边距的距离
     * @param r 子试图右边距到父类边距的距离
     * @param b  子试图底部边距到父类上边距的距离
     */
    private void layoutChildFrame(View child,int l,int t,int r,int b){
        child.layout(l,t,r,b);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init();

    }


}
