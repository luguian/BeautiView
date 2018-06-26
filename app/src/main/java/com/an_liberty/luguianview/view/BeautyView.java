package com.an_liberty.luguianview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.an_liberty.luguianview.R;

/**
 * Created by liberty on 2018/2/11.
 */

public class BeautyView extends View {

    int btnIndex = 0;
    //左边
    private static final int LEFT = 0;
    //右边
    private static final int RIGHT  = 1;

    private static final String TAG = BeautyView.class.getSimpleName();
    //角度
    private float btnChildAngle;
    private Paint shapePaint;
    //字体
    private String btnTxt;
    //字体颜色
    private int btnTxtColor;
    //默认字体颜色是白色
    private static final int defaultTextColor = Color.parseColor("#FFFFFF");
    //字体大小
    private int textSize;
    //文字
    private TextPaint textPaint;
    //边框
    private Paint mBorderPaint;
    //边框宽度
    private int BorderWidth = 10;
    //绘制内部背景
    private Path shapePath;
    //左边框的路径
    private Path mBroderLeftPath;
    //右边框的路径
    private Path mBroderRightPath;
    //距离
    double distance;

    //三角形区域偏移量
    private double thirdAngleDistance = 0;

    //默认是左边被点击
    private boolean LeftisSelect = true;
    //默认是右边不被点击
    private boolean RightSelect = false;

    //被点击后的颜色
    private static final int pressFillColor = Color.parseColor("#fe7c94");
    //没被点击后的颜色
    private static final int defaultFillColor = Color.parseColor("#FFFFFF");
    //处理文字换行的一个工具类
    private StaticLayout staticLayout;

    public BeautyView(Context context) {
        this(context,null);
    }

    public BeautyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BeautyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public float getBtnChildAngle() {
        return btnChildAngle;
    }

    public void setBtnChildAngle(float btnChildAngle) {
        this.btnChildAngle = btnChildAngle;
    }

    private void init(AttributeSet attrs){
        shapePaint = new Paint();
        shapePaint.setAntiAlias(true);
        shapePaint.setStyle(Paint.Style.FILL);
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.BeautyView);
        btnTxt = array.getString(R.styleable.BeautyView_btnTxt);
        btnTxtColor = array.getColor(R.styleable.BeautyView_btnTxtColor,defaultTextColor);
        if(btnTxt == null){
            btnTxt = "";
        }
        //字体大小
        textSize = array.getDimensionPixelSize(R.styleable.BeautyView_btnTxtSize,15);
        array.recycle();
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        textPaint.setColor(defaultTextColor);
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(BorderWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width,height);
    }


    private int measureWidth(int widthMeasureSpec){
        int width = 0;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        switch(mode){
            case MeasureSpec.EXACTLY:
            {
                width = size;
            }
            break;
            case MeasureSpec.AT_MOST:{//这个值是父尺寸给我们的参考，因为是一半 所以要除以2
                width = size / ((ViewGroup)getParent()).getChildCount();
            }
            break;

        }
        return width;
    }

    private int measureHeight(int heightMeasureSpec){
        int height = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        switch(mode){
            case MeasureSpec.EXACTLY:{
                height = size;
            }
            break;
            case MeasureSpec.AT_MOST:{

            }
            break;
        }
        return height;


    }


    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        //画里面的颜色
        drawShape(canvas);
        //文字
        drawTxt(canvas);
        //画边框
        drawSide(canvas);

    }


    private void drawShape(Canvas canvas){
        LinearGradient gradient = null;
        gradient = new LinearGradient(0, 0, getWidth(), getHeight(), new int[]{Color.parseColor("#FDAEC3"), Color.parseColor("#FE7EA0")}, new float[]{0f, 1f}, Shader.TileMode.CLAMP);
        int save = canvas.save();
        if(shapePath == null){
            shapePath = new Path();
            double cot = Math.cos(btnChildAngle / 180 * Math.PI) / Math.sin(btnChildAngle / 180 * Math.PI);
            distance = cot * getHeight();
            //描绘字体要用
            thirdAngleDistance = Math.abs(distance);
            if(btnChildAngle == 90 || btnChildAngle == 0){
                distance = 0;
            }
            if(distance > 0 ){
                switch (btnIndex){
                    case LEFT:
                        shapePath.moveTo(0,getTop());
                        shapePath.lineTo((float) (Math.round(getRight() - Math.abs(distance))),getTop());
                        shapePath.lineTo(getRight(),getBottom());
                        shapePath.lineTo(getLeft(),getBottom());
                        break;
                    case RIGHT:
                        shapePath.moveTo((float) distance,getTop());
                        shapePath.lineTo(getRight(),getTop());
                        shapePath.lineTo(getRight(),getBottom());
                        shapePath.lineTo(0,getBottom());
                        break;
                }
            }else if(distance < 0){
                switch (btnIndex){
                    case LEFT:
                        shapePath.moveTo(getLeft(),getTop());
                        shapePath.lineTo(getRight(),getTop());
                        shapePath.lineTo((float)(getRight() + distance),getBottom());
                        shapePath.lineTo(getLeft(),getBottom());
                        break;
                    case RIGHT:
                        shapePath.moveTo(0,getTop());
                        shapePath.lineTo(getRight() - getLeft(),getTop());
                        shapePath.lineTo(getRight() - getLeft(),getBottom());
                        shapePath.lineTo((float) Math.abs(distance) ,getBottom());
                        break;
                }
            }else {//如果是直角 或者180度
                shapePath.lineTo(getRight() - getLeft(),getTop());
                shapePath.lineTo(getRight() - getLeft(),getBottom());
                shapePath.lineTo(0,getBottom());
            }
            shapePath.close();

        }
        //下面描绘颜色
        if(btnIndex == LEFT){
            if(LeftisSelect){
                shapePaint.setShader(gradient);
            } else {
                shapePaint.setShader(null);
                //白色
                shapePaint.setColor(defaultFillColor);
            }
        }else{
            if(RightSelect){
                shapePaint.setShader(gradient);
            }else{
                shapePaint.setShader(null);
                shapePaint.setColor(defaultFillColor);
            }
        }
        canvas.drawPath(shapePath,shapePaint);
        canvas.restoreToCount(save);

    }

    //描绘文字
    private void drawTxt(Canvas canvas){
        if(staticLayout == null){
            staticLayout = new StaticLayout(btnTxt,textPaint,(int) (getWidth() - thirdAngleDistance / 2), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        }
        int save = canvas.save();
        if(btnIndex == LEFT){
            if(LeftisSelect){
                textPaint.setColor(defaultFillColor);
            }else{
                textPaint.setColor(pressFillColor);
            }
            canvas.translate((float)(thirdAngleDistance / 2),getHeight() / 6);
        }else if(btnIndex == RIGHT){
            if(RightSelect){
                textPaint.setColor(defaultFillColor);
            }else{
                textPaint.setColor(pressFillColor);
            }
            canvas.translate((float) (getWidth() / 2), getHeight() / 6);
        }
        staticLayout.draw(canvas);
        canvas.restoreToCount(save);

    }

    //描绘边框
    private void drawSide(Canvas canvas){
        mBroderLeftPath = new Path();
        mBroderRightPath = new Path();
        mBorderPaint.setColor(pressFillColor);
        if(btnIndex == LEFT){
            //默认或者被点击
            mBroderLeftPath.moveTo(0, 0);
            mBroderLeftPath.lineTo(getRight(), 0);
        }
        if(btnIndex == RIGHT){
            mBroderRightPath.moveTo(getRight(), 0);
            mBroderRightPath.lineTo((int) distance, 0);
        }
        canvas.drawPath(mBroderLeftPath, mBorderPaint);
        canvas.drawPath(mBroderRightPath, mBorderPaint);

    }

    public void isSelect(boolean LeftisSelect, boolean RightSelect) {
        //如果是左
        // this.LeftRight = LEFT;
        this.LeftisSelect = LeftisSelect;
        this.RightSelect = RightSelect;
        invalidate();

    }

}
