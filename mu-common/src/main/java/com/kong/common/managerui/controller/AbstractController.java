package com.kong.common.managerui.controller;

import com.kong.common.domain.SearchField;
import com.kong.common.domain.SearchFilter;
import com.kong.common.enumration.SearchEnum;
import com.kong.common.service.IDataPermAware;
import com.kong.common.service.IDataPermService;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
/**
 * Created by kong on 2016/1/3.
 */
public abstract class AbstractController implements IDataPermAware {
    @Autowired
    protected IDataPermService dataPermService;

    public AbstractController() {
    }

    protected Map<String, Object> makeQueryCondition(HttpServletRequest request, HttpServletResponse response, String uri) {
        HashMap conditions = Maps.newHashMap();
        String sidx = request.getParameter("sidx");
        String sord = request.getParameter("sord");
        conditions.put("sort", sidx + " " + sord);
        String searchField = request.getParameter("searchField");
        String searchOper = request.getParameter("searchOper");
        String searchString = request.getParameter("searchString");
        conditions.put(searchField, searchString);
        String filters;
        if(this.getEnableDataPerm()) {
            filters = this.dataPermService.makeDataPermSql(uri);
            if(filters != null) {
                conditions.put("whereSql", filters);
            }
        }

        filters = request.getParameter("filters");
        if(StringUtils.isNotBlank(filters)) {
            SearchFilter searchFilter = (SearchFilter)JSON.parseObject(filters, SearchFilter.class);
            this.enhanceSearchFilter(searchFilter);
            if(!CollectionUtils.isEmpty(searchFilter.getRules())) {
                conditions.put("groupOp", searchFilter.getGroupOp());
                Iterator i$ = searchFilter.getRules().iterator();

                while(i$.hasNext()) {
                    SearchField field = (SearchField)i$.next();
                    if(field.getOp().equals(SearchEnum.lk.getCode())) {
                        field.setData("%" + String.valueOf(field.getData()).trim() + "%");
                    }

                    field.setOp(SearchEnum.codeOf(field.getOp()).getDes());
                    conditions.put(this.getField(field.getField()), field);
                }
            }
        }

        return conditions;
    }

    private String getField(String field) {
        return field.replace(".", "");
    }

    protected void enhanceSearchFilter(SearchFilter searchFilter) {
    }
}
