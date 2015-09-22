package com.mindpin.art_demo.samples.models.demo;

import com.mindpin.art_demo.samples.models.interfaces.ICourse;

import java.util.Random;

/**
 * Created by dd on 14-9-23.
 */
public class Course implements ICourse {
    public static Integer i = 0;
    public String id, title, state, parent_id, cover;
    public Integer score;
    public ICourse parent;

    public Course() {
        i++;
        id = i.toString();
        score = new Random().nextInt(101);
    }

    @Override
    public String get_title() {
        return title;
    }

    @Override
    public String get_state() {
        return state;
    }

    @Override
    public String get_cover() {
        return cover;
    }

    @Override
    public int get_score() {
        return 0;
    }

    @Override
    public String get_parent_id() {
        return parent_id;
    }

    @Override
    public ICourse get_parent() {
        return parent;
    }

    @Override
    public String get_id() {
        return id;
    }
}
