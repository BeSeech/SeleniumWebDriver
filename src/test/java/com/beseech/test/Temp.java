package com.beseech.test;

import com.beseech.tools.Config;
import org.junit.Test;

public class Temp {
    @Test
    public void dosomething() throws  Exception {
        com.beseech.model.TestConfig testConfig = Config.load();
        System.out.println("s: " + testConfig.apiBasicUrl);
    }
}
