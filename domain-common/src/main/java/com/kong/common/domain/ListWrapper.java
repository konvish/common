package com.kong.common.domain;

import java.util.List;

/**
 * Created by Administrator on 2016/1/3.
 */
public class ListWrapper<T> extends BaseWrapper {
    private List<T> lists;

    public List<T> getLists() {
        return lists;
    }

    public void setLists(List<T> lists) {
        this.lists = lists;
    }
}
