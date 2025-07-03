package hu.piller.enykp.util.gitsync;

import hu.gov.nav.bevfeld.git.gitsync.IGitSync;
import hu.piller.enykp.interfaces.IENYKGitSync;
import hu.piller.enykp.util.base.PropertyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class UbevGitSync implements IENYKGitSync {

    private static final Logger logger = LoggerFactory.getLogger(UbevGitSync.class);

    private IGitSync gs;

    public UbevGitSync() throws Exception {
        try {
            logger.info("GIT_TESZT_MESSAGE : Class.forName(hu.gov.nav.bevfeld.git.gitsync.Main)");
            Class<?> clazz = Class.forName("hu.gov.nav.bevfeld.git.gitsync.Main");
            gs = (IGitSync) clazz.getDeclaredConstructor().newInstance();
            Properties props = (Properties) PropertyList.getInstance().get("prop.usr.git_init_data");
            gs.init(props);
            logger.info("GIT_TESZT_MESSAGE : init completed");
        } catch (ClassNotFoundException e) {
            logger.error("package not found - no GitSync", e);
            throw e;
        } catch (Exception e) {
            logger.error("no GitSync ex", e);
            throw e;
        } catch (Error e) {
            logger.error("no GitSync er", e);
            throw e;
        }
    }

    @Override
    public void init(Properties props) {
        gs.init(props);
    }

    @Override
    public String syncWithGit(String branch) throws Exception {
        return gs.syncWithGit(branch);
    }
}
