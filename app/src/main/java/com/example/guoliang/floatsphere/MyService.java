package com.example.guoliang.floatsphere;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.WindowManager;

/**
 * Created by guoliang on 2018/4/23.
 */
public class MyService extends Service {

    private WindowManager windowManager;
    private WindowManager.LayoutParams wmParams;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        wmParams = new WindowManager.LayoutParams();
        SuspendButtonLayout mFloatLayout=new SuspendButtonLayout(this,null);
//        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        if (Build.VERSION.SDK_INT >= 24) { /*android7.0不能用TYPE_TOAST*/
//            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
//        } else { /*以下代码块使得android6.0之后的用户不必再去手动开启悬浮窗权限*/
//            String packname = getPackageName();
//            PackageManager pm = getPackageManager();
//            boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.SYSTEM_ALERT_WINDOW", packname));
//            if (permission) {
//                wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
//            } else {
//                wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
//            }
//        }
//        //设置图片格式，效果为背景透明
//        wmParams.format = PixelFormat.RGBA_8888;
//        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
//        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//        //调整悬浮窗显示的停靠位置为左侧置顶
//        wmParams.gravity = Gravity.START | Gravity.TOP;
//
//        DisplayMetrics dm = new DisplayMetrics();
//        //取得窗口属性
//        windowManager.getDefaultDisplay().getMetrics(dm);
//        //窗口的宽度
//        int screenWidth = dm.widthPixels;
//        //窗口高度
//        int screenHeight = dm.heightPixels;
//        //以屏幕左上角为原点，设置x、y初始值，相对于gravity
//        wmParams.x = screenWidth;
//        wmParams.y = screenHeight;
//
//        //设置悬浮窗口长宽数据
//        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        mFloatLayout.setLayoutParams(wmParams);
//        windowManager.addView(mFloatLayout, wmParams);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}
