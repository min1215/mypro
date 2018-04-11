package com.example.demo1;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bwie.xlistviewlibrary.View.XListView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements XListView.IXListViewListener{
String string = "http://api.tianapi.com/huabian/?key=71e58b5b2f930eaf1f937407acde08fe&num=";
    List<DatteBean.NewslistBean> list=new ArrayList();
    int pager=0;

Handler handler=new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg .what== 1) {
            Gson gson = new Gson();
            DatteBean datteBean = gson.fromJson((String) msg.obj, DatteBean.class);
            List<DatteBean.NewslistBean> newslist = datteBean.getNewslist();
            list.addAll(newslist);
            myadapter.notifyDataSetChanged();
            Log.e("--------++",list.get(0).getTitle());
        }

    }
};
    private Myadapter myadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XListView xlisyt = (XListView) findViewById(R.id.xlist);
        xlisyt.setPullLoadEnable(true);
        myadapter = new Myadapter();
        xlisyt.setAdapter(myadapter);

        getData(0);
        xlisyt.setXListViewListener(this);
//        xlisyt.setXListViewListener(new XListView.IXListViewListener() {
//            @Override
//            public void onRefresh() {
//                list.clear();
//                getData(0);
//            }
//
//            @Override
//            public void onLoadMore() {
//                pager++;
//getData(pager);
//            }
//        });
    }

    private void getData(final  int pager) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    URL url = new URL(string+pager);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer stringBuffer = new StringBuffer();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

//           byte[]  by =   new byte[1024];
//                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream();
//
//                    int len=0;
                    String temp="";
                    while ((temp=bufferedReader.readLine())!=null){
                        stringBuffer.append(temp);
}
                    Log.e("--------",stringBuffer.toString());
                    Message message = handler.obtainMessage();message.what=1;
                    message.obj=stringBuffer.toString();

                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onRefresh() {
        list.clear();
        getData(0);
    }

    @Override
    public void onLoadMore() {
        pager++;
        getData(pager);
    }

    private class Myadapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View inflate = View.inflate(MainActivity.this, R.layout.list, null);
            ImageView viewById = inflate.findViewById(R.id.img);
            TextView viewById1 = inflate.findViewById(R.id.tit);
            viewById1.setText(list.get(i).getTitle());
            ImageLoader.getInstance().displayImage(list.get(i).getPicUrl(),viewById);
            return inflate;
        }
    }
}
