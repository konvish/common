package com.kong.common.managerui.controller;

import com.kong.common.domain.view.BizData4Page;
import com.kong.common.managerui.controller.AbstractController;
import com.kong.common.managerui.controller.helpers.ActionPermHelper;
import com.kong.common.managerui.service.IResourceGridService;
import com.kong.common.service.IDataPermService;
import com.kong.common.service.IPageService;
import com.kong.common.utils.SqlOrderEnum;
import com.kong.common.utils.UserContext;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Administrator on 2016/1/3.
 */
public abstract class AbstractAdminController<T extends IPageService> extends AbstractController {
    protected T mainService;
    @Autowired
    protected IResourceGridService resourceGridService;
    protected String mainObjName;
    protected String pagePath;
    public String absroduct;
    @Autowired
    private ActionPermHelper actionPermHelper;
    @Autowired
    private IDataPermService dataPermService;

    public AbstractAdminController() {
    }

    protected abstract T getMainService();

    protected abstract String getBizSys();

    protected abstract String getMainObjName();

    protected abstract String getViewTitle();

    protected abstract String getParentTitle();

    protected String getPagePath() {
        return "";
    }

    protected ModelAndView doRenderMainView(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("module/" + this.getPagePath() + this.getMainObjName());
        this.enhancePreModelAndView(request, mav);
        List resourceList = this.actionPermHelper.getResourcePerm(this.getAbsroduct());
        mav.addObject("resources", resourceList);
        HashMap condition = Maps.newHashMap();
        condition.put("moduleName", this.getMainObjName());
        List resourceGridList = this.resourceGridService.queryList(condition, "orderNum", SqlOrderEnum.ASC.getAction());
        mav.addObject("cols", resourceGridList);
        mav.addObject("bizSys", this.getBizSys());
        mav.addObject("mainObj", this.getMainObjName());
        mav.addObject("parentTitle", this.getParentTitle());
        mav.addObject("title", this.getViewTitle());
        mav.addObject("current_userName", UserContext.getCurrentUser().getName());
        mav.addObject("actions", this.actionPermHelper.getActionPerm(this.getMainObjName(), this.getAbsroduct()));
        this.enhanceModelAndView(request, mav);
        return mav;
    }

    protected void enhancePreModelAndView(HttpServletRequest request, ModelAndView mav) {
    }

    protected void enhanceModelAndView(HttpServletRequest request, ModelAndView mav) {
    }

    protected BizData4Page doPage(HttpServletRequest request, HttpServletResponse response) {
        Integer page = Integer.valueOf(1);
        if(request.getParameter("page") != null) {
            page = Integer.valueOf(request.getParameter("page"));
        }

        Integer rows = Integer.valueOf(10);
        if(request.getParameter("rows") != null) {
            rows = Integer.valueOf(request.getParameter("rows"));
        }

        String uri = request.getRequestURI().substring(0, request.getRequestURI().length() - 1);
        Map conditions = this.makeQueryCondition(request, response, uri);
        this.enhancePageConditions(conditions);
        return this.getMainService().queryPageByDataPerm(uri, conditions, page.intValue(), (page.intValue() - 1) * rows.intValue(), rows.intValue());
    }

    protected void enhancePageConditions(Map<String, Object> conditions) {
    }

    public String getAbsroduct() {
        return StringUtils.isEmpty(this.absroduct)?null:this.absroduct;
    }

    public void setAbsroduct(String absroduct) {
        this.absroduct = absroduct;
    }
}
