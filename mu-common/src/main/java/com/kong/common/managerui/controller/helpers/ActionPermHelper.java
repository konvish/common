package com.kong.common.managerui.controller.helpers;

import com.kong.common.managerui.domain.Resource;
import com.kong.common.managerui.service.IActionPermService;
import com.kong.common.managerui.service.IResourceService;
import com.kong.common.utils.UserContext;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * Created by kong on 2016/1/3.
 */
@Service("actionPermHelper")
public class ActionPermHelper {
    @Autowired
    private IResourceService resourceService;
    @Autowired
    private IActionPermService actionPermService;

    public ActionPermHelper() {
    }

    public final Set<String> getActionPerm(String mainObj) {
        Resource resource = (Resource)this.resourceService.findOne("bizModelName", mainObj);
        return resource == null?null:this.actionPermService.getActionPermsByRes(UserContext.getCurrentUser().getId(), resource.getId());
    }

    public final Set<String> getActionPerm(String mainObj, String product) {
        HashMap map = new HashMap();
        map.put("bizModelName", mainObj);
        map.put("product", product);
        Resource resource = (Resource)this.resourceService.queryOne(map);
        return resource == null?null:this.actionPermService.getActionPermsByRes(UserContext.getCurrentUser().getId(), resource.getId());
    }

    public final List<Resource> getResourcePerm(String product) {
        return this.getResourcePerm(UserContext.getCurrentUser().getId(), product, "0");
    }

    public final List<Resource> getResourcePerm(Object uid, String product) {
        return this.actionPermService.getResourcePerms(uid, product);
    }

    public final List<Resource> getResourcePerm(Object uid, String product, String hide) {
        return this.actionPermService.getResourcePerms(uid, product, hide);
    }
}
