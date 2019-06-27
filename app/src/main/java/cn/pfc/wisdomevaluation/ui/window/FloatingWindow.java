package cn.pfc.wisdomevaluation.ui.window;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.pfc.wisdomevaluation.R;
import cn.pfc.wisdomevaluation.api.BaseApi;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.WINDOW_SERVICE;
/**
 * @author fyh
 * @date 2019.6.20
 * */
public class FloatingWindow extends BaseApi {
    private WindowManager.LayoutParams layoutParams;
    private WindowManager windowManager;
    private LinearLayout mFloatingWindowLayout;
    private CircleImageView mFloatingWindowImageView;
    private TextView mFloatingWindowTextView;
    private View view;
    OnActionListener onActionListener;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public FloatingWindow(Context context) {
        super(context);
        initView();
        view.setVisibility(View.GONE);
        initlayoutPararm();
        addView();
        setListener();
    }

    public TextView getmFloatingWindowTextView() {
        return mFloatingWindowTextView;
    }

    private void setListener() {
        mFloatingWindowImageView.setOnClickListener(v ->
        {
            if(onActionListener!=null) onActionListener.onButtonClick();
        });
        if(onActionListener!=null)
        {
            onActionListener.onTextViewChanged(mFloatingWindowTextView);
        }
    }

    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    @Override
    public void init() {

    }
    public void addView()
    {
        windowManager.addView(view,layoutParams);
    }
    public void show()
    {
        view.setVisibility(View.VISIBLE);
    }
    public void removeView()
    {
        view.setVisibility(View.GONE);
    }
    private void initView() {
        view = LayoutInflater.from(context).inflate(R.layout.layout_floating_window,null);
        view.setBackgroundResource(R.drawable.login_btn_bg);
        mFloatingWindowLayout = (LinearLayout) view.findViewById(R.id.mFloatingWindowLayout);
        mFloatingWindowImageView = (CircleImageView) view.findViewById(R.id.mFloatingWindowImageView);
        mFloatingWindowTextView = (TextView) view.findViewById(R.id.mFloatingWindowTextView);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initlayoutPararm()
    {
        if (Settings.canDrawOverlays(context)) {
            // 获取WindowManager服务
            windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);

            // 设置LayoutParam
            layoutParams = new WindowManager.LayoutParams();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
            layoutParams.format = PixelFormat.RGBA_8888;
            layoutParams.width = 500;
            layoutParams.height = 100;
            layoutParams.x = 300;
            layoutParams.y = 300;
            //如果不设置flags会导致屏幕事件被拦截
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            // 将悬浮窗控件添加到WindowManager
//            windowManager.addView(button, layoutParams);
            view.setOnTouchListener(new FloatingOnTouchListener());
        }
    }
    public interface OnActionListener
    {
        void onButtonClick();
        void onTextViewChanged(TextView textView);
    }
    /**
     * @Description 悬浮窗移动手势
     * */
    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    // 更新悬浮窗控件布局
                    windowManager.updateViewLayout(view, layoutParams);
                    break;
                default:
                    break;
            }
            return false;
        }
    }

}
