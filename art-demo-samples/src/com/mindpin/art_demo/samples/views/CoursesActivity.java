package com.mindpin.art_demo.samples.views;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.mindpin.android.loadingview.LoadingView;
import com.mindpin.art_demo.samples.R;
import com.mindpin.art_demo.samples.models.demo.Course;
import com.mindpin.art_demo.samples.models.interfaces.ICourse;
import com.mindpin.art_demo.samples.utils.DemoAsyncTask;
import com.mindpin.art_demo.samples.views.adapter.CoursesAdapter;
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
    private List<ICourse> courses = new ArrayList<ICourse>();

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
                Course course = new Course();
                course.title = "1. 用几何体画身边小物品";
                course.cover = "http://www.mindpin.com/mockups/kc/art-camp_20150920/images/%E8%AF%BE%E7%A8%8B%E5%88%97%E8%A1%A8/u37.png";
                course.state = "状态1";
                courses.add(course);

                course = new Course();
                course.title = "2. 素描简单的小物品";
                course.cover = "http://www.mindpin.com/mockups/kc/art-camp_20150920/images/%E8%AF%BE%E7%A8%8B%E5%88%97%E8%A1%A8/u83.png";
                course.state = "状态2";
                courses.add(course);

                course = new Course();
                course.title = "3. 充满活力的动物和植物";
                course.cover = "http://www.mindpin.com/mockups/kc/art-camp_20150920/images/%E8%AF%BE%E7%A8%8B%E5%88%97%E8%A1%A8/u85.png";
                course.state = "状态3";
                courses.add(course);

                course = new Course();
                course.title = "4. 可以装进画框的素描";
                course.cover = "http://www.mindpin.com/mockups/kc/art-camp_20150920/images/%E8%AF%BE%E7%A8%8B%E5%88%97%E8%A1%A8/u87.png";
                course.state = "状态4";
                courses.add(course);
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
        final CoursesAdapter adapter =
                new CoursesAdapter(this, courses);
        lv_courses.setAdapter(adapter);
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