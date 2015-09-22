package com.mindpin.art_demo.samples.models.interfaces;

/**
 * Created by dd on 14-9-18.
 */
public interface ICourse extends IBase {
    public String get_title();
    public String get_state();
    public String get_cover();
    public int get_score();
    public String get_parent_id();
    public ICourse get_parent();
}