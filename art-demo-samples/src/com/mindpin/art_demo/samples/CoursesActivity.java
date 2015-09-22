package com.mindpin.art_demo.samples;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.mindpin.android.loadingview.LoadingView;
import com.mindpin.art_demo.samples.utils.DemoAsyncTask;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoursesActivity extends RoboActivity {
    @InjectView(R.id.lv_courses)
    ListView lv_courses;
    @InjectView(R.id.loading_view)
    LoadingView loading_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courses);
        get_data();
//        setListAdapter(
//                new SimpleAdapter(
//                        this, getData(), android.R.layout.simple_list_item_1, new String[]{"title"},
//                        new int[]{android.R.id.text1}
//                )
//        );
//        getListView().setScrollbarFadingEnabled(false);
    }


    protected void get_data() {
        new DemoAsyncTask<Void>(this) {

            @Override
            protected void onPreExecute() throws Exception {
                loading_view.show();
            }

            @Override
            public Void call() throws Exception {
//                order = DataProvider.deliveryman_order(order_id);
                Thread.sleep(2000);
                return null;
            }

            @Override
            protected void onSuccess(Void aVoid) throws Exception {
//                build_views();
//                if (need_show_scan)
//                    show_scan();
            }

            @Override
            protected void onFinally() throws RuntimeException {
                super.onFinally();
                loading_view.hide();
            }
        }.execute();
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