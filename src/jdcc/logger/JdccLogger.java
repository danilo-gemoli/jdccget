package jdcc.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JdccLogger {

    public static Logger logger = null;

    static {
        logger = LoggerFactory.getLogger("jdcc.app");
    }

}
