package com.ytempest.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.ytempest.chart.util.ViewUtils;

import java.util.List;

/**
 * @author heqidu
 * @since 2019/11/30
 */
public class SimpleBarGraph extends View {
    public SimpleBarGraph(Context context) {
        this(context, null);
    }

    public SimpleBarGraph(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public static final String TAG = "SimpleBarGraph";

    private static final int DEFAULT_HEIGHT = 270;
    private static final int DEFAULT_WIDTH = 370;

    // x轴刻度名称颜色
    private static final int NAME_TEXT_COLOR = 0xFF7F7D7E;
    // y轴刻度文字颜色
    private static final int SCALE_TEXT_COLOR = 0xFF7F7D7E;
    // 图表线颜色
    private static final int GRAPH_LINE_COLOR = 0xFFDADADA;

    private int mMarkHeight;
    private int mNameHeight;
    private int mScaleTextWidth;
    private int mRightMargin;
    private int mSpace;

    private int mScaleTextSize;
    private int mNameTextSize;

    private Paint mLinePaint = new Paint();
    private TextPaint mTextPaint = new TextPaint();
    private Paint mPillarPaint = new Paint();


    private RectF mGraphRect = new RectF();

    public SimpleBarGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMarkHeight = ViewUtils.dp2px(context, 50);
        mNameHeight = ViewUtils.dp2px(context, 28);
        mScaleTextWidth = ViewUtils.dp2px(context, 45);
        mRightMargin = ViewUtils.dp2px(context, 16);
        mSpace = ViewUtils.dp2px(context, 2);

        mScaleTextSize = ViewUtils.sp2px(context, 13);
        mNameTextSize = ViewUtils.sp2px(context, 14);

        mLinePaint.setStrokeWidth(ViewUtils.dp2px(context, 1F));
        mLinePaint.setColor(GRAPH_LINE_COLOR);

        mPillarPaint.setStyle(Paint.Style.FILL);

        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setAntiAlias(true);
    }

    private float mPillarWidth;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST) {
            width = ViewUtils.dp2px(getContext(), DEFAULT_WIDTH) + getPaddingLeft() + getPaddingRight();
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            height = ViewUtils.dp2px(getContext(), DEFAULT_HEIGHT) + getPaddingTop() + getPaddingBottom();
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mGraphRect.set(getPaddingLeft() + mScaleTextWidth,
                getPaddingTop() + mMarkHeight,
                w - getPaddingRight() - mRightMargin,
                h - getPaddingBottom() - mNameHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawPillarTypeMark(canvas);

        drawGraphScale(canvas, mScaleConfigs);

        drawPillar(canvas);
    }

    private RectF mTmpRectF = new RectF();

    private void drawPillar(Canvas canvas) {
        ensurePillarWidth();
        mTextPaint.setTextSize(mNameTextSize);
        mTextPaint.setColor(NAME_TEXT_COLOR);
        int count = mAdapter.getPillarCount();
        for (int i = 0; i < count; i++) {
            // 第n个柱形的开始位置为：n*间隔 + (n-1)*柱形类型数
            float startX = mGraphRect.left + +(i + 1) * mPillarWidth + (i * mAdapter.getPillarTypeCount()) * mPillarWidth;
            Pillar pillar = mAdapter.getPillar(i);

            // 1、绘制柱体不同类型的实体
            List<Float> peakList = pillar.getPeakList();
            drawPillarType(canvas, startX, peakList);

            // 2、绘制柱体的名称
            String name = pillar.getName();
            drawPillarName(canvas, startX, name);
        }
    }

    private void drawPillarType(Canvas canvas, float startX, List<Float> peakList) {
        for (int i = 0, size = peakList.size(); i < size; i++) {
            calculateRect(peakList.get(i), startX + i * mPillarWidth, mTmpRectF);
            // 获取每一个柱体对应的颜色
            int color = mPillarConfigs.get(i).getColor();
            mPillarPaint.setColor(color);
            canvas.drawRect(mTmpRectF.left, mTmpRectF.top, mTmpRectF.right, mTmpRectF.bottom, mPillarPaint);
        }
    }

    private void calculateRect(Float val, float startX, RectF rect) {
        float graphHeight = mGraphRect.bottom - mGraphRect.top;
        float len = mScaleConfigs.getEnd() - mScaleConfigs.getStart();
        float percent = (val - mScaleConfigs.getStart()) / len;
        rect.set(startX,
                mGraphRect.bottom - percent * graphHeight,
                startX + mPillarWidth,
                mGraphRect.bottom);
    }

    private void drawPillarName(Canvas canvas, float startX, String name) {
        if (TextUtils.isEmpty(name)) {
            return;
        }
        float textCenterX = startX + mAdapter.getPillarTypeCount() * mPillarWidth / 2;
        float offset = (mTextPaint.descent() + mTextPaint.ascent()) / 2;
        float baseline = mGraphRect.bottom + mNameHeight / 2 - offset;
        canvas.drawText(name, textCenterX, baseline, mTextPaint);
    }

    private void ensurePillarWidth() {
        if (mPillarWidth != 0) {
            return;
        }
        int spaceCount = mAdapter.getPillarCount() + 1;
        int pillarCount = mAdapter.getPillarCount() * mAdapter.getPillarTypeCount();
        mPillarWidth = (mGraphRect.right - mGraphRect.left) / (spaceCount + pillarCount);
    }

    /**
     * 绘制顶部柱体类型的标识
     */
    private void drawPillarTypeMark(Canvas canvas) {
        ensurePillarWidth();
        mTextPaint.setTextSize(mNameTextSize);
        mTextPaint.setColor(NAME_TEXT_COLOR);
        float centerY = mGraphRect.top - mMarkHeight / 2;
        float baseline = centerY - (mTextPaint.ascent() + mTextPaint.descent()) / 2;
        // 顶部柱体块大小
        float blockHeight = (mTextPaint.descent() - mTextPaint.ascent()) * 0.6F;
        float blockWidth = blockHeight * 2F;
        float curX = mGraphRect.right;
        // 从右往左绘制
        for (int i = mPillarConfigs.size() - 1; i >= 0; i--) {
            PillarConfigs config = mPillarConfigs.get(i);
            String name = config.getPillarName();
            if (TextUtils.isEmpty(name)) {
                continue;
            }

            // 1、先绘制柱体块的名称
            float len = mTextPaint.measureText(name);
            // 因为TextPaint设置了居中绘制，所以只需要需要减去len/2
            canvas.drawText(name, curX - len / 2, baseline, mTextPaint);
            curX -= len;

            // 2、绘制柱体块
            mPillarPaint.setColor(config.getColor());
            curX = curX - mSpace - blockWidth;
            canvas.drawRect(curX, centerY - blockHeight / 2, curX + blockWidth, centerY + blockHeight / 2, mPillarPaint);

            // 3、两个item之间的间距
            curX = curX - mSpace * 5;
        }
    }

    /**
     * 绘制刻度线和刻度值
     */
    private void drawGraphScale(Canvas canvas, ScaleConfigs scaleConfigs) {
        mTextPaint.setTextSize(mScaleTextSize);
        mTextPaint.setColor(SCALE_TEXT_COLOR);
        int count = scaleConfigs.getCount();
        float textOffset = (mTextPaint.ascent() + mTextPaint.descent()) / 2;

        float perScaleHeight = (mGraphRect.bottom - mGraphRect.top) / (count - 1);
        for (int i = 0; i < count; i++) {
            float y = mGraphRect.bottom - i * perScaleHeight;
            // 绘制每一个刻度的x线
            canvas.drawLine(mGraphRect.left, y, mGraphRect.right, y, mLinePaint);

            // 绘制刻度文字
            String text = scaleConfigs.getScaleText(i);
            if (!TextUtils.isEmpty(text)) {
                float baseline = y - textOffset;
                canvas.drawText(text, mGraphRect.left - mScaleTextWidth / 2, baseline, mTextPaint);
            }
        }
        // 绘制y轴
        canvas.drawLine(mGraphRect.left, mGraphRect.top, mGraphRect.left, mGraphRect.bottom, mLinePaint);
    }

    /*adapter*/

    private Adapter mAdapter;
    private ScaleConfigs mScaleConfigs;
    private List<PillarConfigs> mPillarConfigs;

    public void setAdapter(Adapter adapter) {
        if (adapter == null) {
            return;
        }
        checkAdapter(adapter);
        mAdapter = adapter;
        mScaleConfigs = mAdapter.getScaleConfigs();
        mPillarConfigs = mAdapter.getPillarConfigs();
        invalidate();
    }

    private void checkAdapter(Adapter adapter) {
        ScaleConfigs scaleConfigs = adapter.getScaleConfigs();
        if (scaleConfigs == null) {
            throw new NullPointerException("Please set the " + ScaleConfigs.class.getCanonicalName() + " for adapter");
        }

        List<PillarConfigs> pillarConfigs = adapter.getPillarConfigs();
        if (pillarConfigs == null || pillarConfigs.size() <= 0) {
            throw new NullPointerException("Please set the " + PillarConfigs.class.getCanonicalName() + " for adapter");
        }
    }

    public interface Adapter {

        ScaleConfigs getScaleConfigs();

        List<PillarConfigs> getPillarConfigs();

        int getPillarCount();

        int getPillarTypeCount();

        Pillar getPillar(int position);
    }

    public interface ScaleConfigs {
        float getStart();

        float getEnd();

        int getCount();

        String getScaleText(int position);
    }

    public interface PillarConfigs {
        int getColor();

        String getPillarName();
    }


    public interface Pillar {
        String getName();

        List<Float> getPeakList();
    }

}
