package www.hello.com.qq_pop;

import android.graphics.Point;
import android.graphics.PointF;

/**
 * Created by Administrator on 2017/1/16.
 */

public class GeometryUtil {


    /**
     * 获取附着点,得到的是与圆相交的两个交点,即point的数组
     */
    public static PointF[] getIntersectionPoints(PointF center, float radious, Double k) {
        float angle;
        float x;
        float y;
        PointF[] pointFs = new PointF[2];
        if (k != null) {
            angle = (float) Math.atan(k);
            //获取的是x,y方向的长度值
            x = (float) (radious * Math.sin(angle));
            y = (float) (radious * Math.cos(angle));

        } else {
            //k不存在时的长度值
            x = radious;
            y = 0;

        }
        pointFs[0] = new PointF(center.x + x, center.y - y);
        pointFs[1] = new PointF(center.x - x, center.y + y);


        return pointFs;
    }

    /**
     * 获取控制点
     */
    public static PointF getMiddlePoint(PointF dragCenter, PointF stableCenter) {

        float dx = (dragCenter.x + stableCenter.x) / 2.0f;
        float dy = (dragCenter.y + stableCenter.y) / 2.0f;

        return new PointF(dx, dy);
    }

    public static float getDistance2Point(PointF dragPoint, PointF stablePoint) {
        float dx = (dragPoint.x-stablePoint.x)*(dragPoint.x-stablePoint.x);
        float dy = (dragPoint.y-stablePoint.y)*(dragPoint.y-stablePoint.y);
        float distance = (float) Math.sqrt(dx+dy);
        return distance;

    }

    public static PointF getPointByPercent(PointF p1, PointF p2, float percent) {

        return new PointF(value(p1.x,p2.x,percent),value(p1.y,p2.y,percent));
    }

    public static float value(float y, float y1, float percent) {
        return y+(y1-y)*percent;
    }
}
