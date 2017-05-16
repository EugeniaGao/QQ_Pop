# QQ_Pop
未读消息的可拖拉的自定义View----红色圆点
 > 测试行内代码
```
private Handler mHandler = new Handler();
//动画执行后调用
    @Override
    public void onAnimationEnd(Animation animation) {
        //延时2s进入向导界面GuideActivity  该方法在主线程   发送一个延时的消息
        mHandler.postDelayed(new MyTask(),2000);
    }
private class MyTask implements  Runnable{

        @Override
        public void run() {
           boolean has_guide =  SPUtils.getBoolean(getApplicationContext(), Constant.KEY_HAS_GUIDE,false);
            if(has_guide){
                //进入主界面
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }else{
                //进入向导界面
                Intent intent = new Intent(getApplicationContext(),GuideActivity.class);
                startActivity(intent);
            }
            finish();
        }
    }
```
