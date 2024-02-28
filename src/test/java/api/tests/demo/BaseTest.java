package api.tests.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import api.tests.demo.config.ConfigContainer;
import api.tests.demo.config.environment.EnvConstants;
import api.tests.demo.listeners.TestListener;

@Listeners(TestListener.class)
public abstract class BaseTest {

    protected final Logger log = LoggerFactory.getLogger(BaseTest.class);

    @BeforeSuite(alwaysRun = true)
    public void setUpBeforeSuite() {
        /* To pick up local.properties */
        System.setProperty(
            EnvConstants.TEST_CONFIG_ENV,
            EnvConstants.TEST_CONFIG_LOCAL
        );
        ConfigContainer.getConfig();
    }
}
