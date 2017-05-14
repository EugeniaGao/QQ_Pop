package www.hello.com.qq_pop;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */
/**适配数据*/
public class MsgAdapter extends Adapter<MsgAdapter.MyViewHolder> {
   private List<Msg> msgsList;
    private final OnGooViewTouchListener listener;


    public MsgAdapter(List<Msg> msgsList,Context context) {
        listener = new OnGooViewTouchListener(context);
    this.msgsList = msgsList;

    }

    @Override
    /**1.创建ViewHolder*/
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //使用打气筒填充数据并制定list条目布局
        //recyle直接继承ViewGroup

        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.rlv_item,parent,false);
         //将条目布局传给ViewG=Holder
        return new MyViewHolder(view);
    }

    @Override
    /**2.给ViewHolder赋值*/
    public void onBindViewHolder(MyViewHolder holder, int position) {
         holder.tv_title.setText(msgsList.get(position).title);
        int unReadMsgCount = msgsList.get(position).unReadMsgCount;
        if (unReadMsgCount ==0){
            //没有消息就隐藏
            holder.tv_unReadMsgCount.setVisibility(View.INVISIBLE);
        }else {
            holder.tv_unReadMsgCount.setVisibility(View.VISIBLE);
            holder.tv_unReadMsgCount.setText(unReadMsgCount+"");
        }

        //给小圆点设置监听,在motionEvent中设置触摸监听
        holder.tv_unReadMsgCount.setOnTouchListener(listener);

    }

    @Override
    /**3.获取总条目*/
    public int getItemCount() {

        return msgsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public  TextView tv_unReadMsgCount;
        public TextView tv_title;

        public MyViewHolder(View itemView) {
            super(itemView);
             //在ViewHolder中找控件
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_unReadMsgCount = (TextView) itemView.findViewById(R.id.tv_unReadMsgCount);

        }

    }
}

