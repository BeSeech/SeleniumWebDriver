package com.beseech.page;

import com.beseech.context.Context;

public interface BasicPageInterface {
    String getUrl();
    Context getContext();
    void load();
    boolean isLoaded();
}
