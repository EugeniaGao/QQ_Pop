package www.hello.com.qq_pop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * Created by Administrator on 2017/1/16.
 */

public class GooView extends View {

    private Paint paint;
    private Path path;
    private int statusBarHeight;
    private boolean isDisapear;
    private Rect textRect;

    public GooView(Context context) {
        this(context,null);
    }

    public GooView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GooView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        path = new Path();
        //定义文本矩形
        textRect = new Rect();
    }
    //定义内部文本
    private String text ="6";
    public  void  setText(String text){
        this.text = text;
    }

    public void initGooViewPosition(float x,float y){
       stablePoint.x = x;
        stablePoint.y=y;
        //重绘
        invalidate();
    }

    //定义拖拽圆圆心半径,和固定圆圆心半径
    private PointF dragPoint = new PointF(200f,200f);
    private  float dragRadious = 20f;
    private PointF stablePoint = new PointF(100f,100f);
    private  float stableRadious = 20f;
    //固定圆和拖拽圆的两个附着点
    private PointF [] dragPoints= new PointF[]{new PointF(200f, 300f),new PointF(200f, 350f)};
    private PointF[] stablePoints = new PointF[]{new PointF(100f, 300f),new PointF(100f, 350f)};
    //固定圆变化的最小半径
    private float minRadius = 5f;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(0,-statusBarHeight);
        float distance  = GeometryUtil.getDistance2Point(dragPoint,stablePoint);
        float percent = distance/maxDragDistance;
        //定义临时变量

        float tempRadius= GeometryUtil.value(stableRadious, minRadius, percent);
        if(tempRadius < minRadius){
            tempRadius =minRadius;
        }

        if (!isDisapear) {
        /*不超过指定的范围才会绘制中间和静态圆,超过后只需要绘制动态圆*/
            if (!isOutOfRange) {
                //获取附着点和贝塞尔曲线的控制点
                //(工具类:附着点所需参数:斜率,圆心,半径;控制点所需参数,两个圆的圆
                //附着点
                float dx = dragPoint.x - stablePoint.x;
                float dy = dragPoint.y - stablePoint.y;
                double k = 0;
                if (dx != 0) {
                    k = dy / dx;
                }
                //获取附着点
                dragPoints = GeometryUtil.getIntersectionPoints(dragPoint, dragRadious, k);
                stablePoints = GeometryUtil.getIntersectionPoints(stablePoint, tempRadius, k);
                //获取控制点
                PointF contralPoint = GeometryUtil.getMiddlePoint(dragPoint, stablePoint);

                //坐标系的平移
                //canvas.save();
                //canvas.translate();

        /*根据附着点来绘制贝塞尔曲线*/
        /*path.moveTo(dragPoints[0].x,dragPoints[0].y);
        //绘制
        path.quadTo(contralPoint.x,contralPoint.y,stablePoints[0].x,stablePoints[0].y);
        path.lineTo(stablePoints[1].x,stablePoints[1].y);
        path.quadTo(contralPoint.x,contralPoint.y,dragPoints[1].x,dragPoints[1].y);*/

                //绘制中间图形的步骤:
                //1,移动到固定圆的附着点1
                path.moveTo(stablePoints[0].x, stablePoints[0].y);
                //2,向拖拽圆的附着点1绘制贝塞尔曲线
                path.quadTo(contralPoint.x, contralPoint.y, dragPoints[0].x, dragPoints[0].y);
                //3,向拖拽圆的附着点2绘制直线
                path.lineTo(dragPoints[1].x, dragPoints[1].y);
                //4,向固定圆的附着点2绘制贝塞尔曲线
                path.quadTo(contralPoint.x, contralPoint.y, stablePoints[1].x, stablePoints[1].y);
                path.close();
                canvas.drawPath(path, paint);
                path.reset();
                //绘制固定圆
                canvas.drawCircle(stablePoint.x, stablePoint.y, tempRadius, paint);
            }
            //绘制拖动圆
            canvas.drawCircle(dragPoint.x, dragPoint.y, dragRadious, paint);
            drawText(canvas);
        }
        canvas.restore();
    }
    /**文本的绘制*/
private void drawText (Canvas canvas){
    paint.setColor(Color.WHITE);
    paint.setTextSize(12f);
    paint.getTextBounds(text,0,text.length(),textRect);
    //文本坐标系在左下角
    float x = dragPoint.x -textRect.width()*0.5f;
    float y =dragPoint.y+textRect.height()*0.5f;
    canvas.drawText(text,x,y,paint);
    paint.setColor(Color.RED);
}
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.statusBarHeight =getStatusBarHeight(this);
    }

    /**获取状态栏的高度*/
    private int getStatusBarHeight(View view) {

        Rect rect =new Rect();
        //会把可视范围的左上右下传入矩形
        view.getWindowVisibleDisplayFrame(rect);
       // 我只想要他的高
        return rect.top;
    }

    //指定两圆之间的最大距离
    private float maxDragDistance = 200f;
    private boolean isOutOfRange =false;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
         switch(event.getAction()){
             case MotionEvent.ACTION_DOWN:
                 //getRawX:获取到屏幕的距离.拖动时以手机屏幕为参考点
                 //getx:获取到控件的距离
                 isDisapear =false;
                 isOutOfRange =false;

                 float downRawX = event.getRawX();
                 float downRawY = event.getRawY();
                 dragPoint.set(downRawX,downRawY);
                 invalidate();
                 break;
             case MotionEvent.ACTION_MOVE:

                 float moveRawX = event.getRawX();
                 float moveRawY = event.getRawY();
                 dragPoint.set(moveRawX,moveRawY);
                 //获取两个圆之间的距离,使用工具类
                 float distance = GeometryUtil.getDistance2Point(dragPoint,stablePoint);
                 //拖动的时候,超过最大距离,中间图形和固定圆消失,即不再绘制,设定一个boolean值即可
                 if(distance > maxDragDistance ){
                   isOutOfRange =true; //是超出了范围
                 }
                 invalidate();
                 break;
             case MotionEvent.ACTION_UP:
                 //松开手后,判断距离,
                 // 在范围内,原路返回
                 //范围外,拖动圆消失,即重新绘制的时候,不再绘制
                 //需要再重新确定一下距离,因为此方法又一次重新被调用的,优质代码
                 distance = GeometryUtil.getDistance2Point(dragPoint,stablePoint);

               if(isOutOfRange){ //判断的是移动的时候超出

                   if(distance > maxDragDistance){  //松开时在范围外,消失
                       isDisapear = true;
                       //调用接口回调的方法
                       if(listener != null){
                           listener.onDisappear();

                       }
                   }else{ //松开时在范围内,不会按原路返回,直接跳到固定圆
                       dragPoint.set(stablePoint.x,stablePoint.y);
                      // isDisapear =false;
                       if(listener != null){
                           listener.onReset();

                       }

                   }

               }else{ //一直在范围内
                   //判断的是移动的时候没有超出,松开时也在范围内,按原路返回并添加shoot动画
                   //原路返回,只是指定了
                   ValueAnimator va = ValueAnimator.ofFloat(distance,0);
                   //添加数据变化的监听, //
                   final PointF tempPointF =new PointF(dragPoint.x,dragPoint.y);

                  //动画中数据变化的监ting,动画显示的效果
                   va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                       @Override
                       public void onAnimationUpdate(ValueAnimator animation) {
                           //获取变化后拖拽圆的圆心,从初始的拖拽圆心开始

                          float percent =animation.getAnimatedFraction();
                           //根据两圆之间的距离,确定中间的某一个点
                          dragPoint = GeometryUtil.getPointByPercent(tempPointF, stablePoint, percent);
                         invalidate();
                       }
                   });
                   //动画结束做重置操作,此方法中有4个要重写的
                 va.addListener(new AnimatorListenerAdapter() {
                     @Override
                     public void onAnimationEnd(Animator animation) {
                         super.onAnimationEnd(animation);
//做重置操作
                         if(listener!=null){
                             Log.i("test","onReset");
                             listener.onReset();
                         }
                     }


                 });
                   va.setInterpolator(new OvershootInterpolator());//动画差值器,只是声明了路线,松开时没有任何动作
                   va.setDuration(300);
                   va.start();
               }
                 invalidate();
                 break;
         }

        return true;
    }

    public interface OnGooViewChangeListener{
        void onDisappear();
        void onReset();
    }
    private OnGooViewChangeListener listener;

    public void setonGooViewChangeListener(OnGooViewTouchListener listener) {
    this.listener= listener;
    }
}
