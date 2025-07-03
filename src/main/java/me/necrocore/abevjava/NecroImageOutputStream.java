package me.necrocore.abevjava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.IOException;

public class NecroImageOutputStream extends FileImageOutputStream {

    private final static Logger logger = LoggerFactory.getLogger(NecroImageOutputStream.class);

    public NecroImageOutputStream(File f) throws IOException {
        super(checkWriteAllowed(f));
        logger.debug("[{}] Creating output stream to file {}", this, f);
    }

    private static File checkWriteAllowed(File f) {
        WriteSecurity.ensureWriteAllowed(f.getAbsolutePath());
        return f;
    }
}