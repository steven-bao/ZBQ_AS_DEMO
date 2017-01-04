package widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.main.R;
import Utils.Utils;

/**
 * Created by Administrator on 2016/12/16.
 */

public class EmptyLayout extends LinearLayout implements
        View.OnClickListener {

    public static final int HIDE_LAYOUT = 4;
    public static final int NETWORK_ERROR = 1;
    public static final int NETWORK_LOADING = 2;
    public static final int NODATA = 3;
    public static final int NODATA_ENABLE_CLICK = 5;
    public static final int NO_LOGIN = 6;
    public static final int SERVER_ERROR = 7;

    private ImageView animProgress;
    private boolean clickEnable = true;
    protected final Context context;
    public ImageView img;
    private OnClickListener listener;
    private int mErrorState;
    private RelativeLayout mLayout;
    private String strNoDataContent = "";
    private String strLoadingContent = "";
    private int imgNoData = 0;
    private TextView tv;
    private ImageButton btn;

    public EmptyLayout(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private View errorView;

    public View getLayoutId() {
        return View.inflate(context, R.layout.view_error_layout, null);
    }

    private void init() {
        View view = getLayoutId();
        img = (ImageView) view.findViewById(R.id.img_error_layout);
        tv = (TextView) view.findViewById(R.id.tv_error_layout);
        btn = (ImageButton) view.findViewById(R.id.btn_reload);
        mLayout = (RelativeLayout) view.findViewById(R.id.pageerrLayout);
        animProgress = (ImageView) view.findViewById(R.id.animProgress);
        AnimationDrawable drawable = (AnimationDrawable) animProgress.getDrawable();
        drawable.start();
        // setBackgroundColor(-1);
        // setOnClickListener(this);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (clickEnable) {
                    if (listener != null && !Utils.isFastDoubleClick())
                        listener.onClick(v);
                }
            }
        });
        errorView = view;
        addView(view);
    }

    public void setBackgound(int colorId) {
        errorView.setBackgroundColor(getResources().getColor(colorId));
    }

    public void dismiss() {
        mErrorState = HIDE_LAYOUT;
        setVisibility(View.GONE);
    }

    public int getErrorState() {
        return mErrorState;
    }

    public boolean isLoadError() {
        return mErrorState == NETWORK_ERROR;
    }

    public boolean isLoading() {
        return mErrorState == NETWORK_LOADING;
    }

    @Override
    public void onClick(View v) {
        if (clickEnable) {
            if (listener != null)
                listener.onClick(v);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void setErrorType(int errorType) {
        setVisibility(View.VISIBLE);
        switch (errorType) {
            case NETWORK_ERROR:
                mErrorState = NETWORK_ERROR;
                if (!Utils.hasInternet(getContext())) {
                    tv.setText(R.string.network_error);
                    img.setBackgroundResource(R.drawable.page_network_error_icon);
                } else {
                    tv.setText(R.string.loading_failed);
                    img.setBackgroundResource(R.drawable.page_load_failed_icon);
                }
                btn.setVisibility(View.VISIBLE);
                img.setVisibility(View.VISIBLE);
                animProgress.setVisibility(View.GONE);
                clickEnable = true;
                break;
            case NETWORK_LOADING:
                mErrorState = NETWORK_LOADING;
                animProgress.setVisibility(View.VISIBLE);
                btn.setVisibility(View.GONE);
                img.setVisibility(View.GONE);
                setTvLoadingContent();
                clickEnable = false;
                break;
            case NODATA:
                mErrorState = NODATA;
//              img.setBackgroundResource(R.drawable.page_no_data_icon);
                setNoDataImage();
                img.setVisibility(View.VISIBLE);
                btn.setVisibility(View.GONE);
                animProgress.setVisibility(View.GONE);
                setTvNoDataContent();
                break;
            case HIDE_LAYOUT:
                mErrorState = HIDE_LAYOUT;
                setVisibility(View.GONE);
                break;
            case NODATA_ENABLE_CLICK:
                mErrorState = NODATA_ENABLE_CLICK;
//              img.setBackgroundResource(R.drawable.page_no_data_icon);
                setNoDataImage();
                img.setVisibility(View.VISIBLE);
                btn.setVisibility(View.VISIBLE);
                animProgress.setVisibility(View.GONE);
                setTvNoDataContent();
                clickEnable = true;
                break;
            case SERVER_ERROR:
                mErrorState = SERVER_ERROR;
//              img.setBackgroundResource(R.drawable.page_no_data_icon);
                setNoDataImage();
                img.setVisibility(View.VISIBLE);
                btn.setVisibility(View.GONE);
                animProgress.setVisibility(View.GONE);
                strNoDataContent = context.getString(R.string.servicer_error_title);
                setTvNoDataContent();
                break;
            default:
                break;
        }
    }

    public void setErrorImag(int imgResource) {
        try {
            img.setBackgroundResource(imgResource);
        } catch (Exception e) {
        }
    }

    public void setNoDataImag(int imgResource) {
        imgNoData = imgResource;
    }

    public void setNoDataContent(String noDataContent) {
        strNoDataContent = noDataContent;
    }

    public void setLoadingContent(String loadingContent) {
        strLoadingContent = loadingContent;
    }

    public void setOnLayoutClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    private void setNoDataImage() {
        if (0 == imgNoData) {
            img.setBackgroundResource(R.drawable.page_no_data_icon);
        } else {
            img.setBackgroundResource(imgNoData);
        }
    }

    public void setTvNoDataContent() {
        if (!strNoDataContent.equals(""))
            tv.setText(strNoDataContent);
        else
            tv.setText(R.string.no_data);
    }

    public void setTvLoadingContent() {
        if (!strLoadingContent.equals(""))
            tv.setText(strLoadingContent);
        else
            tv.setText(R.string.loading);
    }


    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.GONE)
            mErrorState = HIDE_LAYOUT;
        super.setVisibility(visibility);
    }
}

