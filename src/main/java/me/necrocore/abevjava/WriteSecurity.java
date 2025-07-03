package me.necrocore.abevjava;

import hu.piller.enykp.util.oshandler.OsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WriteSecurity {

    private final static Logger logger = LoggerFactory.getLogger(WriteSecurity.class);
    private final static String WRITE_DIRECTORY = OsFactory.getOsHandler().getEnvironmentVariable("WRITE_DIRECTORY");

    public static void ensureWriteAllowed(String path) {
        if (WRITE_DIRECTORY == null || WRITE_DIRECTORY.isBlank()) {
        } else {
            if (!path.startsWith(WRITE_DIRECTORY)) {
                logger.warn("Writing to {} is not allowed.", path);
                throw new SecurityException("Writing to " + path + " is not allowed.");
            }
        }
    }
}
