package reporting;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Logs {
    private static final Logger logger = LogManager.getLogger(Logs.class);

    public static void info(String message) {
        logger.info(message);
    }

    public static void error(String message) {
        logger.error(message);
    }

    public static void debug(String message) {
        logger.debug(message);
    }

    public static void warn(String message) {
        logger.warn(message);
    }
    public static void logTestResult(String testName, boolean isSuccess) {
        if (isSuccess) {
            logger.info("Test passed: " + testName);
        } else {
            logger.error("Test failed: " + testName);
        }
    }

}