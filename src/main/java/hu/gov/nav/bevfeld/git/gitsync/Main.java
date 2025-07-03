package hu.gov.nav.bevfeld.git.gitsync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Main implements IGitSync {

    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    @Override
    public void init(Properties properties) {
        logger.warn("\tGit sync is not found");
    }

    @Override
    public String syncWithGit(String var1) {
        logger.warn("\tGit sync is not found");
        return var1;
    }

}
