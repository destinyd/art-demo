package com.mindpin.art_demo.samples.views;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.mindpin.android.loadingview.LoadingView;
import com.mindpin.art_demo.samples.R;
import com.mindpin.art_demo.samples.utils.DemoAsyncTask;
import com.nostra13.universalimageloader.core.ImageLoader;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class StepActivity extends RoboActivity {
    @InjectView(R.id.loading_view)
    LoadingView loading_view;
    @InjectView(R.id.iv_step_1)
    ImageView iv_step_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step);
        get_data();
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
        ImageLoader.getInstance().displayImage("assets://step_1_1.png", iv_step_1);
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