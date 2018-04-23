package com.example.guoliang.floatsphere;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by guoliang on 2018/4/23.
 */
public class Main1Activity extends AppCompatActivity implements View.OnTouchListener {

    private View inflate;
    private WindowManager windowManager;
    private MySuspendButtonLayout suspendButtonLayout;
    private int screenWidth;
    private int screenHeight;
    private static int xinX;
    private static int xinY;
    public static boolean suspendedInLeft = true;
    private int mStatus=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main1);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        createFloatWindow(this);
    }

    /**
     * 悬浮窗
     */
    private static SuspendButtonLayout mFloatLayout;
    private static WindowManager.LayoutParams wmParams;
    private static boolean mHasShown;

    /**
     * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
     *
     * @param context 必须为应用程序的Context.
     */
    public void createFloatWindow(final Context context) {
        wmParams = new WindowManager.LayoutParams();
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        inflate = LinearLayout.inflate(context, R.layout.activity_main1, null);
        suspendButtonLayout = (MySuspendButtonLayout) inflate.findViewById(R.id.layout);
        if (Build.VERSION.SDK_INT >= 24) { /*android7.0不能用TYPE_TOAST*/
            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        } else { /*以下代码块使得android6.0之后的用户不必再去手动开启悬浮窗权限*/
            String packname = context.getPackageName();
            PackageManager pm = context.getPackageManager();
            boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.SYSTEM_ALERT_WINDOW", packname));
            if (permission) {
                wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            } else {
                wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            }
        }

        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.START | Gravity.TOP;

        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        windowManager.getDefaultDisplay().getMetrics(dm);
        //窗口的宽度
        screenWidth = dm.widthPixels;
        //窗口高度
        screenHeight = dm.heightPixels;
        //以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 0;
//        wmParams.horizontalWeight = 0;
//        wmParams.verticalWeight = 0;
        //设置悬浮窗口长宽数据
        wmParams.width = 150;
        wmParams.height = 150;
        inflate.setOnTouchListener(this);
        inflate.setLayoutParams(wmParams);
        windowManager.addView(inflate, wmParams);
        suspendButtonLayout.setOnSuspendListener(new MySuspendButtonLayout.OnSuspendListener() {
            @Override
            public void onButtonStatusChanged(int status, ImageView imageView) {

            }

            @Override
            public void onButtonStatusChanged(int status, final ImageView imageMain, final MotionEvent event) {
                RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(imageMain.getLayoutParams());
                mStatus = status;
                if (status == 3) {
                    int[] ints=new int[2];
                    int[] ints2=new int[2];
                    imageMain.getLocationOnScreen(ints);
                    imageMain.getLocationInWindow(ints2);
//                    layoutParams.setMargins(0,(int) (ints[1]/2)-getStatusBarHeight(Main1Activity.this)/2,0,0);
                    layoutParams.setMargins(0,(int) ints[1]-getStatusBarHeight(Main1Activity.this),0,0);
                    imageMain.setLayoutParams(layoutParams);

                    imageMain.invalidate();
                    imageMain.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            imageMain.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    });
                    wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    wmParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                    windowManager.updateViewLayout(inflate, wmParams);
                } else if (status == 0) {
//                    RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                    layoutParams.setMargins(0,0,0,0);
//                    imageMain.setPadding(0,0,0,0);
                    imageMain.setLayoutParams(layoutParams);
                    imageMain.invalidate();
                    wmParams.width = 150;
                    wmParams.height = 150;
                    windowManager.updateViewLayout(inflate, wmParams);
//                    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
//                    imageMain.setLayoutParams(layoutParams);
//                    imageMain.requestLayout();
                }

            }

            @Override
            public void onChildButtonClick(int index) {

            }

        });
    }
    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private long startTime;
    private float mTouchStartX;
    private float mTouchStartY;
    private boolean isclick;
    private long endTime;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        // 获取相对屏幕的坐标，即以屏幕左上角为原点
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        //下面的这些事件，跟图标的移动无关，为了区分开拖动和点击事件
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //图标移动的逻辑在这里
                float mMoveStartX = event.getX();
                float mMoveStartY = event.getY();
                // 如果移动量大于3才移动
                if (Math.abs(mTouchStartX - mMoveStartX) > 3
                        && Math.abs(mTouchStartY - mMoveStartY) > 3) {
                    // 更新浮动窗口位置参数

                    wmParams.x = (int) (x - mTouchStartX) > screenWidth ? screenWidth : (int) (x - mTouchStartX);
                    wmParams.y = (int) (y - mTouchStartY) > screenHeight ? screenWidth : (int) (y - mTouchStartY);
                    xinX = wmParams.x;
                    xinY = wmParams.y;
//                    wmParams.width=WindowManager.LayoutParams.MATCH_PARENT;
//                    wmParams.height=WindowManager.LayoutParams.MATCH_PARENT;
                    windowManager.updateViewLayout(inflate, wmParams);
                }
                break;
            case MotionEvent.ACTION_UP:
                endTime = System.currentTimeMillis();
                //当从点击到弹起小于半秒的时候,则判断为点击,如果超过则不响应点击事件
                if ((endTime - startTime) > 0.1 * 1000L) {
                    isclick = false;
                } else {
                    isclick = true;
                }
                int mScreenWidth = getResources().getDisplayMetrics().widthPixels;
                if (mStatus!=1&&mStatus!=2) {
                    // 判断左右
                    if ((wmParams.x + (view.getMeasuredWidth() / 2)) < (mScreenWidth / 2)) { // 左
                        wmParams.x = 0;
                        ObjectAnimator animatorX = ObjectAnimator.ofFloat(inflate, "translationX", view.getX(), 0);
                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(animatorX);
                        set.setDuration(0);
                        set.start();
                        windowManager.updateViewLayout(inflate, wmParams);
                        suspendedInLeft = true;
                    } else { // 右

                        wmParams.x = screenWidth;
                        ObjectAnimator animatorX = ObjectAnimator.ofFloat(inflate, "translationX", view.getX(), mScreenWidth - view.getWidth());
                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(animatorX);
                        set.setDuration(0);
                        set.start();
                        windowManager.updateViewLayout(inflate, wmParams);
                        suspendedInLeft = false;
                    }
                }
                if (mTouchStartX==event.getX()&&mTouchStartY==event.getY()){
                    suspendButtonLayout.openSuspendButton(event);
                    suspendButtonLayout.closeSuspendButton(event);
                }
                break;
        }
        //响应点击事件
        if (isclick) {
//            Toast.makeText(mContext, "我是大傻叼", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public static WindowManager.LayoutParams getWmParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.x = xinX;
        params.y = xinY;
        return wmParams;
    }
}
