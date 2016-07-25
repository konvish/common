package com.kong.common.managerui.iauth.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/3.
 */
@Deprecated
public abstract class BundledAuthentication implements Authentication, Bundled<BundledAuthentication> {
    private boolean bundled = false;
    private BundledAuthentication bundledTo;
    private List<BundledAuthentication> bundles = new ArrayList();

    public BundledAuthentication() {
    }

    public boolean isBundled() {
        return this.bundled;
    }

    public void setBundled(boolean bundled) {
        this.bundled = bundled;
    }

    public BundledAuthentication getBundledTo() {
        return this.bundledTo;
    }

    public void setBundledTo(BundledAuthentication bundledTo) {
        this.bundledTo = bundledTo;
        bundledTo.getOthers().add(this);
        bundledTo.setBundled(true);
    }

    public List<BundledAuthentication> getOthers() {
        return this.bundles;
    }
}
