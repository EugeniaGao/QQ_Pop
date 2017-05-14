package www.hello.com.qq_pop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Msg> msgsList = new ArrayList<>();
        for(int i=0;i<50;i++){
            msgsList.add(new Msg("标题"+i,i));
        }
        //找到在住布局中存放条目的布局
        RecyclerView rlv = (RecyclerView) findViewById(R.id.rlv);
        //获取线性布局管理器???
        rlv.setLayoutManager(new LinearLayoutManager(this));
        //设置适配器
        rlv.setAdapter(new MsgAdapter(msgsList,this));




    }
}
