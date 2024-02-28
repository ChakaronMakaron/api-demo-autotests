package api.tests.demo.listeners;

import api.tests.demo.constants.ProjectConstants;
import api.tests.demo.utils.AllureHelper;
import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import org.slf4j.MDC;

import java.io.File;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.UUID;

public class StepListener implements StepLifecycleListener {

    private final String DISCRIMINATOR = "stepName";
    private final ThreadLocal<Deque<String>> stepIdStack;

    public StepListener() {
        this.stepIdStack = ThreadLocal.withInitial(LinkedList::new);
    }

    @Override
    public void beforeStepStart(StepResult result) {
        String stepId = "step-%s".formatted(UUID.randomUUID().toString());
        stepIdStack.get().push(stepId);
        MDC.put(DISCRIMINATOR, stepId);
    }

    @Override
    public void beforeStepStop(StepResult result) {
        String currentStepId = stepIdStack.get().pollFirst();
        AllureHelper.attachIfExists(
            "Step Log",
            "text/plain",
            getLogFilePath(currentStepId),
            "txt"
        );
        if (result.getStatus().equals(Status.PASSED)) {
            onSuccess();
        } else {
            onFailure();
        }
    }

    private void onSuccess() {
        String previousStepId = stepIdStack.get().peek();
        if (Objects.nonNull(previousStepId)) {
            MDC.put(DISCRIMINATOR, previousStepId);
        } else {
            MDC.remove(DISCRIMINATOR);
        }
    }

    private void onFailure() {
        stepIdStack.get().clear();
        MDC.remove(DISCRIMINATOR);
    }

    private String getLogFilePath(String stepId) {
        return new File(ProjectConstants.LOG_DIR + stepId + ProjectConstants.LOG_EXTENSION)
                .getAbsolutePath();
    }
}
