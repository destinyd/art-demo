package com.mindpin.art_demo.samples.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.mindpin.android.loadingview.LoadingView;
import com.mindpin.art_demo.samples.R;
import com.mindpin.art_demo.samples.utils.DemoAsyncTask;
import com.nostra13.universalimageloader.core.ImageLoader;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class ResultActivity extends RoboActivity implements View.OnClickListener, View.OnTouchListener {
    @InjectView(R.id.loading_view)
    LoadingView loading_view;
    @InjectView(R.id.iv_result)
    ImageView iv_result;

    @InjectView(R.id.btn_compare)
    Button btn_compare;
    @InjectView(R.id.btn_retry)
    Button btn_retry;
    @InjectView(R.id.btn_next)
    Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        bind();
        get_data();
    }

    private void bind() {
        btn_compare.setOnTouchListener(this);
        btn_retry.setOnClickListener(this);
        btn_next.setOnClickListener(this);
    }


    protected void get_data() {
        new DemoAsyncTask<Void>(this) {

            @Override
            protected void onPreExecute() throws Exception {
                loading_view.show();
            }

            @Override
            public Void call() throws Exception {
                Thread.sleep(1000);
                return null;
            }

            @Override
            protected void onSuccess(Void aVoid) throws Exception {
                build_views();
            }

            @Override
            protected void onFinally() throws RuntimeException {
                super.onFinally();
                loading_view.hide();
            }
        }.execute();
    }

    private void build_views() {
        ImageLoader.getInstance().displayImage("assets://result.png", iv_result);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_retry:
                startActivity(new Intent(this, StepActivity.class));
                break;
            case R.id.btn_next:
                startActivity(new Intent(this, CoursesActivity.class));
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(view.getId() == R.id.btn_compare) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {  //按下的時候
                ImageLoader.getInstance().displayImage("assets://chapter_1_1.png", iv_result);
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {  //起來的時候
                ImageLoader.getInstance().displayImage("assets://result.png", iv_result);
            }
        }
        return false;
    }

//    @SuppressWarnings("unchecked")
//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        Map<String, Object> map = (Map<String, Object>) l.getItemAtPosition(position);
//        Log.d("title", (String) map.get("title"));
//        Log.d("id", (String) map.get("id"));
////        Intent intent = new Intent(this, (Class<? extends Activity>) map.get("activity"));
////        startActivity(intent);
//    }

//    private List<? extends Map<String, ?>> getData() {
//        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
//        addItem(data, "用几何体画身边小物品", "1");
//        addItem(data, "素描简单的小物品", "2");
//        addItem(data, "充满活力的动物和植物", "3");
//        addItem(data, "可以装进画框的素描", "4");
////        addItem(data, "other", OtherActivity.class);
//
//        return data;
//    }
//
//    private void addItem(List<Map<String, Object>> data, String title,
//                         String id) {
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("title", data.size() + ". " + title);
//        map.put("id", id);
//        data.add(map);
//    }
}