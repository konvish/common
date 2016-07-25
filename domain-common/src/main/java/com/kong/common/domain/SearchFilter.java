package com.kong.common.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索条件集合
 * Created by kong on 2016/1/3.
 */
public class SearchFilter extends BaseWrapper {
    /**
     * 多个条件的并列关系 and or
     */
    private String groupOp;
    /**
     * 搜索条件集合
     */
    private List<SearchField> rules = new ArrayList<>();
    /** 排序规则集合 */
    private List<OrderField> orders = new ArrayList<>();
    /** 当前页 */
    private Integer curPage;
    /** 每页显示条数 */
    private Integer num;

    public String getGroupOp() {
        return groupOp;
    }

    public void setGroupOp(String groupOp) {
        this.groupOp = groupOp;
    }

    public List<SearchField> getRules() {
        return rules;
    }

    public void setRules(List<SearchField> rules) {
        this.rules = rules;
    }

    public List<OrderField> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderField> orders) {
        this.orders = orders;
    }

    public Integer getCurPage() {
        return curPage;
    }

    public void setCurPage(Integer curPage) {
        this.curPage = curPage;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
