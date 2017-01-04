package widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/12/16.
 */

public class ViewPagerIndicator extends ViewGroup implements ViewPager.OnPageChangeListener {
    private static final java.lang.String TAG = "ViewPagerIndicator";
    private int mLineColor = 0XFFECECEC;
    private static final int MAIN_COLOR = 0XFF12B7F5;
    private static final int DEFULT_COLOR = 0XFF5C6273;
    private int mIndicatorWidth = 0; //指针宽度
    private int mIndicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2
            , getResources().getDisplayMetrics());// 指针高度，默认2dp


    private int mIndicatorColor = MAIN_COLOR; //指针颜色
    private int mTextColor = MAIN_COLOR; // tab选中文本颜色
    private int mDefultTextColor = DEFULT_COLOR; //tab未选中文本颜色

    private int mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 13, getResources().getDisplayMetrics());
    private int mRound = 10;
    private boolean isRound;
    private boolean isDrawLine;

    private Paint mPaint = new Paint();
    RectF mRect = new RectF();
    private String[] mTitles;
    private float drawLeft;
    private int maxCount = -1; // 屏幕上最多显示多少个tab -1 = 全部
    private int tabCenter = -1; // 屏幕上最多显示tab数量的中心
    private int value;
    private int layoutLeft;
    private int lastPosition;
    private int viewPagerPosition; // ViewPager当前位置
    private int indicatorPosition; // 指针当前位置
    private boolean isScroll; // 当前ViewPager是否滚动
    private float startX;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint.setAntiAlias(true);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.widthMeasureSpec = widthMeasureSpec;
        this.heightMeasureSpec = heightMeasureSpec;
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode == MeasureSpec.AT_MOST) {
            sizeHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 100, getResources().getDisplayMetrics());
        }
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
        }

        setMeasuredDimension(sizeWidth, sizeHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initTitles();
    }

    private float getTabWidthF() {
        return maxCount == -1 ? getMeasuredWidth() / (mTitles.length + 0.0f) : getMeasuredWidth() / (maxCount + 0.0f);
    }

    private int getTabWidthI() {
        return (int) (maxCount == -1 ? getMeasuredWidth() / (mTitles.length + 0.0f) : getMeasuredWidth() / (maxCount + 0.0f));
    }

    private void initTitles() {
        removeAllViews();
        if (mTitles == null || mTitles.length == 0) {
            mTitles = new String[]{"必须设置一个标题"};
        }
        // 获取到是否设置指针宽度
        mIndicatorWidth = mIndicatorWidth == 0 ? getTabWidthI() : mIndicatorWidth;
        // 如果设置的宽度大于TextView宽度，那就默认等于TextView 宽度 防止挤出屏幕
        mIndicatorWidth = mIndicatorWidth > getTabWidthI() ? getTabWidthI() : mIndicatorWidth;
        for (String mTitle : mTitles) {
            final TextView textView = new TextView(getContext());
            textView.setWidth(getTabWidthI());
            textView.setHeight(getMeasuredHeight());
            textView.setText(mTitle);
            textView.setTextColor(mDefultTextColor);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(mTextSize);
            addView(textView);
        }
        layoutView();
        scroll(0, 0.0f);
    }


    public void scroll(int position, float offset) {
        //为了防止ViewPager 回调最先执行有些变量还没初始化完成，先加一层判断
        if (getVisibility() == View.GONE || getChildAt((position + (int) (offset + 0.5f))) == null) {
            return;
        }
        viewPagerPosition = position;
        startX = (getTabWidthF() - mIndicatorWidth) / 2.0f;
        TextView currentTextView = (TextView) getChildAt((position + (int) (offset + 0.5f)));
        TextView lastTextView = (TextView) getChildAt(lastPosition);
        lastTextView.setTextColor(mDefultTextColor);
        currentTextView.setTextColor(mTextColor);
        lastPosition = (position + (int) (offset + 0.5f));
        if (tabCenter != -1 && position + 1 >= tabCenter) {
            if (mTitles.length - position > tabCenter + value) {
                layoutView((int) (0 - (getTabWidthF() * (position - tabCenter + 1 + offset))));
                layoutLeftTemp = (int) (0 - (getTabWidthF() * (position - tabCenter + 1 + offset)));
                offset = 0f;
                position = position - (position - (tabCenter - 1));
            } else {
                position = (tabCenter - 1 + value) + (tabCenter - (mTitles.length - position));
            }
        }
        indicatorPosition = position;
        drawLeft = startX + getTabWidthF() * (position + offset);
        drawLeftTemp = drawLeft;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (isDrawLine) {
            canvas.save();
            mPaint.setColor(mLineColor);
            canvas.drawRect(0, getMeasuredHeight() - 2,
                    getMeasuredWidth(), getMeasuredHeight(), mPaint);
            canvas.restore();
        }
        canvas.save();
        mPaint.setColor(mIndicatorColor);
        float drawTop = getMeasuredHeight() - mIndicatorHeight;
        float drawRight = drawLeft + mIndicatorWidth;
        float drawBottom = drawTop + mIndicatorHeight;
        mRect.left = drawLeft;
        mRect.top = drawTop;
        mRect.right = drawRight;
        mRect.bottom = drawBottom;
        if (isRound) {
            canvas.drawRoundRect(mRect, mRound, mRound, mPaint);
        } else {
            canvas.drawRect(mRect, mPaint);
        }
        canvas.restore();

    }

    int widthMeasureSpec;
    int heightMeasureSpec;

    private void layoutView() {
        layoutView(0);
    }


    private void layoutView(int left) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
            int lt = left;
            int rt = left + view.getMeasuredWidth();
            int bm = getMeasuredHeight();
            view.layout(lt, 0, rt, bm);
            left += view.getWidth();
        }
        invalidate();
        postInvalidate();
    }

    float downX;
    float layoutLeftTemp;
    float drawLeftTemp;
    private int mode;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = 1;
                downX = event.getX();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = 2;
                break;
            case MotionEvent.ACTION_MOVE:
                if (maxCount != -1 && mTitles.length > maxCount && !isScroll && mode == 1) {
                    float moveX = event.getX();
                    float margin = drawLeftTemp - layoutLeftTemp;
                    layoutLeft = (int) (layoutLeftTemp + (moveX - downX));
                    if (layoutLeft > 0) {
                        layoutLeft = 0;
                    }
                    if (layoutLeft < -getTabWidthI() * (getChildCount() - maxCount)) {
                        layoutLeft = -getTabWidthI() * (getChildCount() - maxCount);
                    }
                    drawLeft = layoutLeft + margin;
                    layoutView(layoutLeft);
                }
                break;
            case MotionEvent.ACTION_UP:
                drawLeftTemp = drawLeft;
                int upX = (int) event.getX();
                if (Math.abs(upX - downX) < 20) {
                    if (mOnTextClickListener != null) {
                        layoutLeft = (int) layoutLeftTemp;
                        layoutView(layoutLeft);
                        mOnTextClickListener.textOnClickListener((TextView) getChildAt(getMovePosition()), getMovePosition());
                    }
                }
                layoutLeftTemp = layoutLeft;
                break;
        }

        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (maxCount == -1) {
            return super.onInterceptTouchEvent(event);
        }
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutView((int) layoutLeftTemp);
    }


    public void setIndicatorWidth(int indicatorWidth) {
        this.mIndicatorWidth = indicatorWidth;
        invalidate();
    }

    public void setIndicatorHeight(int indicatorHeight) {
        this.mIndicatorHeight = indicatorHeight;
        invalidate();
    }

    private int getMovePosition() {
        int position;
        if (maxCount == -1) {
            position = (int) (downX / (getMeasuredWidth() / mTitles.length));
        } else if (layoutLeft == 0) {
            position = (int) (downX / (getMeasuredWidth() / maxCount));
        } else {
            position = (int) (Math.abs(layoutLeft / getTabWidthF()) + (downX / (getMeasuredWidth() / maxCount)));
        }
        return position;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }


    public void setTitle(String[] titles) {
        this.mTitles = titles;

    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
        if (maxCount % 2 == 0) {
            tabCenter = maxCount / 2;
            value = 1;
        } else {
            tabCenter = maxCount / 2 + 1;
            value = 0;
        }
    }

    private OnTextClickListener mOnTextClickListener;

    public void scrollEnd(int state) {
        if (maxCount == -1) return;
        if (state == 0) { // 滚动结束
            isScroll = false;
            if (viewPagerPosition < tabCenter && indicatorPosition < tabCenter && drawLeft > 0) {
                layoutLeftTemp = 0;
                layoutView((int) layoutLeftTemp);
            }
            if (mTitles.length - viewPagerPosition <= tabCenter + value && indicatorPosition >= tabCenter - 1 && drawLeft > 0) {
                layoutLeftTemp = -(getTabWidthF() * (getChildCount() - maxCount));
                layoutView((int) layoutLeftTemp);
            }
        } else if (state == 1) { // 滚动开始
            isScroll = true;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        scroll(position, positionOffset);
        Log.i(TAG, "onPageScrolled");
    }

    @Override
    public void onPageSelected(int position) {
        Log.i(TAG, "onPageSelected");

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        scrollEnd(state);
        Log.i(TAG, "onPageScrollStateChanged");

    }

    public interface OnTextClickListener {
        void textOnClickListener(TextView textView, int position);
    }

    public void setTextOnClickListener(OnTextClickListener onTextClickListener) {
        this.mOnTextClickListener = onTextClickListener;
    }

    public void setTextSize(int textSize) {
        this.mTextSize = textSize;
    }

    public void setIndicatorColor(int indicatorColor) {
        this.mIndicatorColor = indicatorColor;
        mPaint.setColor(mIndicatorColor);
    }

    public void setTextColor(int textColor, int defultTextColor) {
        this.mTextColor = textColor;
        this.mDefultTextColor = defultTextColor;
    }

    public void isDrawLine(boolean isDrawLine) {
        this.isDrawLine = isDrawLine;
    }

    public void setUniteColor(int color) {
        mIndicatorColor = color;
        mTextColor = color;
        mDefultTextColor = color;
        mPaint.setColor(mIndicatorColor);
    }

    public void setRound(int mRound) {
        isRound = true;
        this.mRound = mRound;
    }

    /**
     * 动态设置title文字
     */
    public void upDateTitleText(String[] texts) {
        if (getChildCount() != texts.length)
            return;
        for (int i = 0; i < texts.length; i++) {
            ((TextView) getChildAt(i)).setText(texts[i]);
        }
    }


}

