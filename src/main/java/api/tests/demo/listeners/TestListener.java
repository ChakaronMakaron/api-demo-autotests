package api.tests.demo.listeners;

import api.tests.demo.config.environment.EnvironmentContainer;
import api.tests.demo.constants.ProjectConstants;
import api.tests.demo.utils.AllureHelper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class TestListener implements ITestListener {

    private static final String TEST_LOG_ATTACHMENT = "Test Log";
    private static final String DISCRIMINATOR = "threadName";
    private static final AtomicInteger testCounter = new AtomicInteger(1);

    @Override
    public void onTestStart(ITestResult result) {
        Thread.currentThread().setName("Test-%s".formatted(testCounter.getAndIncrement()));
        MDC.put(DISCRIMINATOR, Thread.currentThread().getName());
        log.info("========== Test Started: \"{}\" ==========", getTestDescription(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("========== Test Passed: \"{}\" ==========", getTestDescription(result));
        AllureHelper.attach(
                TEST_LOG_ATTACHMENT,
                "text/log",
                testLogFilePath(),
                "log"
        );
        MDC.remove(DISCRIMINATOR);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.info("========== Test Skipped: \"{}\" ==========", getTestDescription(result));
        AllureHelper.attach(
                TEST_LOG_ATTACHMENT,
                "text/log",
                testLogFilePath(),
                "log"
        );
        MDC.remove(DISCRIMINATOR);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.info("========== Test Failed: \"{}\" ==========", getTestDescription(result));
        log.info("Failure reason: {}", result.getThrowable().toString());
        AllureHelper.attach(
                TEST_LOG_ATTACHMENT,
                "text/log",
                testLogFilePath(),
                "log"
        );
        MDC.remove(DISCRIMINATOR);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}

    @Override
    public void onStart(ITestContext context) {}

    @Override
    public void onFinish(ITestContext context) {
        log.info("Writing environment");
        EnvironmentContainer.writeEnvironment(ProjectConstants.ALLURE_ENVIRONMENT_FILE_PATH);
        log.info("Environment successfully written");
    }

    private String getTestDescription(ITestResult result) {
        Method testMethod = result.getMethod().getConstructorOrMethod().getMethod();
        String testDescription = testMethod.getAnnotation(Test.class).description();
        return Objects.nonNull(testDescription) && !testDescription.isEmpty()
                ? testDescription
                : testMethod.getName();
    }

    private String testLogFilePath() {
        return ProjectConstants.LOG_DIR + Thread.currentThread().getName() + ProjectConstants.LOG_EXTENSION;
    }
}
