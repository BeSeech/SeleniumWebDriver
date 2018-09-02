package com.beseech.tools;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;

public class Tools {
    public static void takeSnapShot(WebDriver webDriver, String fileWithPath) {
        try {
            TakesScreenshot scrShot = ((TakesScreenshot) webDriver);
            File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
            File DestFile = new File(fileWithPath);
            FileUtils.copyFile(SrcFile, DestFile);

            System.out.println("The screenshot was saved at: " + DestFile.getAbsolutePath());

        } catch (Exception e) {
            System.err.println("Try to save screen shot: " + e.getMessage());
        }
    }

    public static String getSpace(int level){
        String result = "";
        for (int i = 0; i < level; i++)
        {
            result += "   ";
        }
        return result;
    }


}
