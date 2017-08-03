package com.cnpeng.cnpeng_demos2017_01.b_18_customSwitchButton;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 作者：CnPeng
 * <p>
 * 时间：2017/8/3:上午9:01
 * <p>
 * 说明：自定义开关按钮
 * <p>
 * 因为是继承自View，所以没有 onLayout 方法，只能测量和绘制
 */

public class CustomSwitchButton extends View {

    private int viewWidth   = 100;   //背景的默认宽
    private int viewHeight  = 50;    //背景的默认高
    private int xCooidinate = viewHeight / 2;   //圆圈的 x 坐标，默认值
    private int yCoordinate = viewHeight / 2;   //圆圈的 y 坐标，默认值
    private int radius      = viewHeight / 2 - 2;   //圆圈的 半径（高度除2-2是为了显出不让圆圈挨边）

    private RectF   rect;                //view所在的矩形区域
    private Paint   paint;
    private boolean isChecked;           //开启状态

    public CustomSwitchButton(Context context) {
        this(context, null);
    }

    public CustomSwitchButton(Context context,
                              @Nullable
                                      AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public CustomSwitchButton(Context context,
                              @Nullable
                                      AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rect = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //继承自View时必须处理 SpecMode ,如果父布局的mode 是 AtMost,并且view中使用了 wrapContent,那么，该view最终会占满屏幕，
        // 所以，测量时需要判断处理下，如果父类的 mode 是 AtMost ，那么就指定父类的宽或高的size
        //测量宽高
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);    //获取宽的布局模式
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);    //获取宽度

        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);    //高的布局模式
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);    //高度

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(viewWidth, viewHeight);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {      //宽度包裹，则指定宽度
            setMeasuredDimension(viewWidth, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {     //高度暴多，则指定高度
            setMeasuredDimension(widthSpecSize, viewHeight);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();

        //绘制背景
        if (isChecked) {
            paint.setColor(Color.GREEN);     //选中状态背景为蓝色
        } else {
            paint.setColor(Color.GRAY);     //未选中为灰色
        }


        if (measuredWidth != 0) {
            viewWidth = measuredWidth;
        }

        if (measuredHeight != 0) {
            viewHeight = measuredHeight;
        }

        rect.set(0, 0, viewWidth, viewHeight);
        canvas.drawRoundRect(rect, viewHeight / 2, viewHeight / 2, paint);  //画背景

        //绘制上层圆圈
        xCooidinate = viewHeight / 2;
        yCoordinate = viewHeight / 2;
        radius = viewHeight / 2 - 4;
        paint.setColor(Color.WHITE);
        canvas.drawCircle(xCooidinate, yCoordinate, radius, paint);
    }
}
