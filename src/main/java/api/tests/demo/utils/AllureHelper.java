package api.tests.demo.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;
import java.util.Objects;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;

public class AllureHelper {

    public static void updateTestName(String newName) {
        Allure.getLifecycle().updateTestCase(test -> test.setName(newName));
    }

    public static void updateDescription(String description) {
        Allure.getLifecycle().updateTestCase(test -> test.setDescription(description));
    }

    public static void appendToStepName(Object obj) {
        Allure.getLifecycle().updateStep(step -> step.setName("%s %s".formatted(step.getName(), obj)));
    }

    public static void formatStepName(Object ...args) {
        Allure.getLifecycle().updateStep(step -> step.setName(step.getName().formatted(args)));
    }

    public static void updateStepName(String newName) {
        Allure.getLifecycle().updateStep(step -> step.setName(newName));
    }

    public static void attachBase64Data(String attachmentName, String type, String data, String extension) {
        if (Objects.isNull(data) || data.isEmpty()) {
            Allure.addAttachment(attachmentName, type, "Empty");
            return;
        }
        Allure.addAttachment(attachmentName, type, new ByteArrayInputStream(Base64.getDecoder().decode(data)), extension);
    }

    public static void attachBinaryData(String attachmentName, String type, byte[] data, String extension) {
        Allure.addAttachment(attachmentName, type, new ByteArrayInputStream(data), extension);
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

    @Step
    public static void wrapInStep(String stepName, Runnable runnable) {
        updateStepName(stepName);
        runnable.run();
    }
}
