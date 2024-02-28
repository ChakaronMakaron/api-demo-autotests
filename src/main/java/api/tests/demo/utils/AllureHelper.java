package api.tests.demo.utils;

import io.qameta.allure.Allure;

import java.io.File;
import java.io.FileInputStream;

public class AllureHelper {

    public static void updateTestName(String newName) {
        Allure.getLifecycle().updateTestCase(test -> test.setName(newName));
    }

    public static void attachIfExists(String attachmentName, String type, String path, String extension) {
        if (new File(path).getAbsoluteFile().exists()) {
            String content = IOHelper.readToString(path);
            Allure.addAttachment(attachmentName, type, content, extension);
        }
    }
    
    public static void attach(String attachmentName, String type, String path, String extension) {
        try (FileInputStream in = new FileInputStream(new File(path).getAbsolutePath())) {
            Allure.addAttachment(attachmentName, type, in, extension);
        } catch (Exception e) {
            Allure.addAttachment(attachmentName, type, "File '%s' not found".formatted(path));
        }
    }
}
