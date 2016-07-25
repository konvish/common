package com.kong.common.managerui.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.kong.common.domain.BaseDomain;
import com.kong.common.domain.BizStatusEnum;
import com.kong.common.exception.BizException;
import com.kong.common.exception.BizExceptionEnum;
import com.kong.common.managerui.controller.helpers.ActionPermHelper;
import com.kong.common.managerui.controller.helpers.BaseServiceMaps;
import com.kong.common.managerui.domain.ResourceGrid;
import com.kong.common.managerui.service.IResourceGridService;
import com.kong.common.service.IBaseService;
import com.kong.common.utils.ActionEnum;
import com.kong.common.utils.SqlOrderEnum;
import com.kong.common.utils.UserContext;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
/**
 * Created by Administrator on 2016/1/3.
 */
public abstract class AbstractCommonController<T> extends AbstractController {
    public static final Logger logger = LoggerFactory.getLogger(AbstractCommonController.class);
    private static final String OPER = "oper";
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    @Autowired
    private ActionPermHelper actionPermHelper;
    @Autowired
    protected IResourceGridService resourceGridService;

    public AbstractCommonController() {
    }

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @RequestMapping({"/commonsave/{mainObj}"})
    @ResponseBody
    public String doSave(@PathVariable String mainObj) {
        HashMap dataMap = Maps.newHashMap();
        String prop = null;
        Enumeration names = this.request.getParameterNames();

        while(names.hasMoreElements()) {
            prop = (String)names.nextElement();
            dataMap.put(prop, this.request.getParameter(prop));
        }

        String operValue = this.request.getParameter("oper");
        Set actionSet = this.actionPermHelper.getActionPerm(mainObj);
        this.enhanceDataMap(dataMap);
        if(ActionEnum.EDIT.getAction().equals(operValue)) {
            if(!actionSet.contains(ActionEnum.EDIT.getAction())) {
                throw new BizException(BizExceptionEnum.NOTEXISTS.getCode(), "没有修改权限");
            }

            this.verifyData(dataMap, mainObj, true);
            dataMap.put("lastModifier", UserContext.getCurrentUser().getId());
            dataMap.put("lastModDate", Long.valueOf(System.currentTimeMillis()));
            this.innerHandleUpdate(mainObj, dataMap);
        } else if(ActionEnum.ADD.getAction().equals(operValue)) {
            if(!actionSet.contains(ActionEnum.ADD.getAction())) {
                throw new BizException(BizExceptionEnum.NOTEXISTS.getCode(), "没有新增权限");
            }

            this.verifyData(dataMap, mainObj, false);
            dataMap.put("creator", UserContext.getCurrentUser().getId());
            dataMap.put("createDate", Long.valueOf(System.currentTimeMillis()));
            dataMap.put("lastModifier", UserContext.getCurrentUser().getId());
            dataMap.put("lastModDate", Long.valueOf(System.currentTimeMillis()));
            if(dataMap.get("status") == null || ((String)dataMap.get("status")).trim().length() == 0) {
                dataMap.put("status", Integer.valueOf(BizStatusEnum.N.getCode()));
            }

            this.innerHandleAdd(mainObj, dataMap);
        } else {
            if(!ActionEnum.DEL.getAction().equals(operValue)) {
                return "false";
            }

            if(!actionSet.contains(ActionEnum.DEL.getAction())) {
                throw new BizException(BizExceptionEnum.NOTEXISTS.getCode(), "没有删除权限");
            }

            dataMap.put("lastModifier", UserContext.getCurrentUser().getId());
            dataMap.put("lastModDate", Long.valueOf(System.currentTimeMillis()));
            this.innerHandleDel(mainObj, dataMap);
        }

        return "true";
    }

    private void verifyData(Map<String, Object> dataMap, String mainObj, boolean isEdit) {
        HashMap condition = Maps.newHashMap();
        condition.put("moduleName", mainObj);
        List resourceGridList = this.resourceGridService.queryList(condition, "orderNum", SqlOrderEnum.ASC.getAction());
        String editRules = null;
        Map rules = null;
        String existRules = null;
        Iterator fields = resourceGridList.iterator();

        while(fields.hasNext()) {
            ResourceGrid var16 = (ResourceGrid)fields.next();
            editRules = var16.getEditrules();
            rules = (Map)JSON.parseObject(editRules, Map.class);
            if(rules.get("exists") != null && String.valueOf(rules.get("exists")).trim().length() > 0) {
                existRules = (String)rules.get("exists");
            }

            if(rules.get("required") != null && ((Boolean)rules.get("required")).booleanValue() && dataMap.containsKey(var16.getColId()) && (dataMap.get(var16.getColId()) == null || ((String)dataMap.get(var16.getColId())).trim().length() == 0)) {
                throw new BizException(BizExceptionEnum.REQUIRED.getCode(), var16.getDisplayName() + BizExceptionEnum.REQUIRED.getDesc());
            }

            if(rules.get("maxLength") != null && String.valueOf(rules.get("maxLength")).trim().length() > 0) {
                if(dataMap.containsKey(var16.getColId()) && (dataMap.get(var16.getColId()) == null || ((String)dataMap.get(var16.getColId())).trim().length() == 0)) {
                    throw new BizException(BizExceptionEnum.REQUIRED.getCode(), var16.getDisplayName() + BizExceptionEnum.REQUIRED.getDesc());
                }

                if(dataMap.containsKey(var16.getColId()) && ((String)dataMap.get(var16.getColId())).trim().length() > ((Integer)rules.get("maxLength")).intValue()) {
                    throw new BizException(BizExceptionEnum.MAXLENGTH.getCode(), var16.getDisplayName() + BizExceptionEnum.MAXLENGTH.getDesc() + rules.get("maxLength"));
                }
            }

            if(rules.get("length") != null && String.valueOf(rules.get("length")).trim().length() > 0) {
                if(dataMap.containsKey(var16.getColId()) && (dataMap.get(var16.getColId()) == null || ((String)dataMap.get(var16.getColId())).trim().length() == 0)) {
                    throw new BizException(BizExceptionEnum.REQUIRED.getCode(), var16.getDisplayName() + BizExceptionEnum.REQUIRED.getDesc());
                }

                if(dataMap.containsKey(var16.getColId()) && ((String)dataMap.get(var16.getColId())).trim().length() != ((Integer)rules.get("length")).intValue()) {
                    throw new BizException(BizExceptionEnum.LENGTH.getCode(), var16.getDisplayName() + BizExceptionEnum.LENGTH.getDesc() + rules.get("length"));
                }
            }
        }

        if(!Strings.isNullOrEmpty(existRules)) {
            String[] var15 = existRules.split(",");
            HashMap conditions = new HashMap();
            String[] existObj = var15;
            int len$ = var15.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String field = existObj[i$];
                conditions.put(field, dataMap.get(field));
            }

            if(isEdit) {
                conditions.put("id", dataMap.get("id"));
                List var17 = this.getServiceMaps().get(mainObj).queryList(conditions, (String)null, (String)null);
                if(var17 != null && var17.size() > 1) {
                    throw new BizException(BizExceptionEnum.EXISTS.getCode(), existRules + BizExceptionEnum.EXISTS.getDesc());
                }
            } else {
                BaseDomain var18 = this.getServiceMaps().get(mainObj).queryOne(conditions);
                if(var18 != null) {
                    throw new BizException(BizExceptionEnum.EXISTS.getCode(), existRules + BizExceptionEnum.EXISTS.getDesc());
                }
            }
        }

    }

    protected void enhanceDataMap(Map<String, Object> dataMap) {
    }

    protected void innerHandleAdd(String mainObj, Map<String, Object> dataMap) {
        this.getServiceMaps().get(mainObj).insertMap(dataMap);
    }

    protected void innerHandleUpdate(String mainObj, Map<String, Object> dataMap) {
        this.getServiceMaps().get(mainObj).updateMap(dataMap);
    }

    protected void innerHandleDel(String mainObj, Map<String, Object> dataMap) {
        dataMap.put("status", Integer.valueOf(BizStatusEnum.D.getCode()));
        this.getServiceMaps().get(mainObj).updateMap(dataMap);
    }

    @RequestMapping({"/import/{mainObj}"})
    @ResponseBody
    public String doImport(@RequestParam("file") MultipartFile file, @PathVariable String mainObj) {
        HashMap dataMap = Maps.newHashMap();
        String prop = null;
        Enumeration names = this.request.getParameterNames();

        while(names.hasMoreElements()) {
            prop = (String)names.nextElement();
            dataMap.put(prop, this.request.getParameter(prop));
        }

        if(!file.isEmpty()) {
            try {
                String e = file.getOriginalFilename();
                String lists = this.request.getSession().getServletContext().getRealPath("/") + "/upload/" + e;
                file.transferTo(new File(lists));
                if(e.endsWith("xls")) {
                    ;
                }
            } catch (Exception var10) {
                var10.printStackTrace();
            }

            try {
                ImportParams e1 = new ImportParams();
                e1.setTitleRows(2);
                e1.setHeadRows(2);
                e1.setNeedSave(true);
                List lists1 = ExcelImportUtil.importExcelByIs(file.getInputStream(), this.getGenericType(0), e1);
                Iterator i$ = lists1.iterator();

                while(i$.hasNext()) {
                    Object t = i$.next();
                    this.getServiceMaps().get(mainObj).insert((BaseDomain)t);
                }
            } catch (Exception var11) {
                var11.printStackTrace();
            }
        }

        return "true";
    }

    protected abstract BaseServiceMaps getServiceMaps();

    @RequestMapping({"/export/{mainObj}"})
    @ResponseBody
    public String doExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String uri = request.getRequestURI().substring(0, request.getRequestURI().length() - 1);
        String title = request.getParameter("fileName");
        title = new String(title.getBytes("ISO-8859-1"), "utf-8");
        Map conditions = this.makeQueryCondition(request, response, uri);
        List expertUserDetails = this.getExportService().queryPage(conditions, 0, 2147483647);
        BufferedOutputStream out = null;

        try {
            response.setContentType("application/x-msdownload");
            out = new BufferedOutputStream(response.getOutputStream());
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((title + ".xls").getBytes("utf-8"), "ISO-8859-1"));
            response.setContentType("application/octet-stream");
            Workbook e = ExcelExportUtil.exportExcel(new ExportParams((String)null, title), this.getGenericType(0), expertUserDetails);
            e.write(out);
        } catch (Exception var12) {
            logger.error("导出数据失败", var12);
        } finally {
            if(out != null) {
                out.flush();
                out.close();
            }

        }

        return null;
    }

    protected abstract IBaseService getExportService();

    private final Class getGenericType(int index) {
        Type genType = this.getClass().getGenericSuperclass();
        if(!(genType instanceof ParameterizedType)) {
            return Object.class;
        } else {
            Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
            if(index < params.length && index >= 0) {
                return !(params[index] instanceof Class)?Object.class:(Class)params[index];
            } else {
                throw new RuntimeException("Index outof bounds");
            }
        }
    }
}
