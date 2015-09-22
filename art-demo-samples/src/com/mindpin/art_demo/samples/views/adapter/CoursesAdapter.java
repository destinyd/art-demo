package com.mindpin.art_demo.samples.views.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.mindpin.art_demo.samples.R;
import com.mindpin.art_demo.samples.models.interfaces.ICourse;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by dd on 14-9-18.
 */
public class CoursesAdapter extends SingleTypeAdapter<ICourse> implements View.OnClickListener {

    private final List<ICourse> courses;
    private final Activity activity;

    public CoursesAdapter(Activity activity,
                          final List<ICourse> items) {
        super(activity, R.layout.courses_list_item);
        this.activity = activity;
        courses = items;
        setItems(courses);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{
                R.id.iv_cover, R.id.course_name, R.id.course_state //, R.id.order_desc, R.id.btn_action, R.id.rl_order
        };
    }

    @Override
    protected void update(int position, ICourse item) {
        ImageLoader.getInstance().displayImage(item.get_cover(), imageView(0));
        setText(1, item.get_title());
        setText(2, item.get_state());
//        setText(2, String.format(Constants.Format.COURSE_DESC, item.get_total()));
//        update_btn_action(item);
    }

//    private void update_btn_action(ICourse item) {
//        if (Order.coursestatus.pending == item.get_status()) {
//            setText(3, "支付");
//            getView(3, Button.class).setVisibility(View.VISIBLE);
//        } else if (Order.coursestatus.took_away == item.get_status()) {
//            setText(3, "收货");
//            getView(3, Button.class).setVisibility(View.VISIBLE);
//        } else {
//            getView(3, Button.class).setVisibility(View.INVISIBLE);
//        }
//    }

    @Override
    public void onClick(View v) {
        Integer position = (Integer) v.getTag();
        ICourse order = getItem(position);
        switch (v.getId()) {
//            case R.id.btn_action:
//                action_for_order_status(order);
//                break;
            case R.id.rl_course:
                go_to_course(order);
                break;
        }
    }

    private void go_to_course(ICourse order) {
        Log.d("CoursesAdapter", "go to course");
//        Intent intent = new Intent(activity, OrderActivity.class);
//        intent.putExtra(Constants.Extra.COURSE_ID, order.get_id());
//        activity.startActivityForResult(intent, Constants.Request.COURSE);
    }

//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View view = super.getView(position, convertView, parent);
//        bind_views(position);
//        return view;
//    }

//    private void bind_views(int position) {
//        Button button = getView(3, Button.class);
//        button.setTag(position);
//        button.setOnClickListener(this);
//
//        RelativeLayout relativeLayout = getView(4, RelativeLayout.class);
//        relativeLayout.setTag(position);
//        relativeLayout.setOnClickListener(this);
//    }
}
