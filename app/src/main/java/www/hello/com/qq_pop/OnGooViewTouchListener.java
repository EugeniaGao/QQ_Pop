package www.hello.com.qq_pop;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/1/18.
 */
/**小圆点只要被触摸就会触发该监听,在构造方法中用windowManger添加上去将小圆点添加上去*/
public class OnGooViewTouchListener implements View.OnTouchListener, GooView.OnGooViewChangeListener {

    private  WindowManager manager;
    private final GooView gooView;
    private  WindowManager.LayoutParams params;
    private Context context;
    private TextView  tv_unread_msg_count;

    public OnGooViewTouchListener(Context context) {
        //manager的使用是在触摸的时候去是实现,就是在onTouch中
        manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //创建GooView对象,以便设置圆心
        this.context= context;
        gooView = new GooView(context); //因为gooView只有有参构造,需要传参
        // 设置改变监听
        //TODO
          gooView.setonGooViewChangeListener(this);
        //设置WindowManager的参数//因为在windowmanager中需要传参
        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.format = PixelFormat.TRANSLUCENT;//类型为透明的


    }

    @Override
    //触摸小圆点时候事件会触发
    public boolean onTouch(View v, MotionEvent event) {
        v.getParent().requestDisallowInterceptTouchEvent(true);
          //v是指存放小圆点的textView
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //让textView消失

                tv_unread_msg_count = (TextView) v;
                tv_unread_msg_count.setVisibility(View.INVISIBLE);
                //,重新设置gooView的位置
                float rawX = event.getRawX();
                float rawY = event.getRawY();
                //创建gooView对象,重新设置原点
                 gooView.initGooViewPosition(rawX,rawY); //让GooView暴露接口即可
                gooView.setText(tv_unread_msg_count.getText().toString());
                //添加
                manager.addView(gooView,params);//因为在windowmanager中需要传参
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        gooView.onTouchEvent(event);

        return true ;
    }

    @Override
    public void onDisappear() {
        manager.removeView(gooView);
    }

    @Override
    public void onReset() {
        if (gooView.getParent() != null) {
            Log.i("test", "removeView");
            manager.removeView(gooView);
            //2,让TextView显示出来
            tv_unread_msg_count.setVisibility(View.VISIBLE);
        }
    }
}
