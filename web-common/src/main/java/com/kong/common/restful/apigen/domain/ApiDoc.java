package com.kong.common.restful.apigen.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kong on 2016/2/2.
 */
public class ApiDoc implements Serializable {
    private List<ApiSummary> apiSummaryList = new ArrayList();
    private List<ApiDetail> apiDetailList = new ArrayList();

    public ApiDoc() {
    }

    public void addApiSummary(ApiSummary apiSummary) {
        this.apiSummaryList.add(apiSummary);
    }

    public void addApiDetail(ApiDetail apiDetail) {
        this.apiDetailList.add(apiDetail);
    }

    public List<ApiSummary> getApiSummaryList() {
        return this.apiSummaryList;
    }

    public List<ApiDetail> getApiDetailList() {
        return this.apiDetailList;
    }
}
