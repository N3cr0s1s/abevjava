package me.necrocore.abevjava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class NecroFile extends File {

    private final static Logger logger = LoggerFactory.getLogger(NecroFile.class);

    public NecroFile(String pathname) {
        super(pathname);
        logger.debug("[{}] Using file {}",this, pathname);
    }

    public NecroFile(String parent, String child) {
        super(parent, child);
        logger.debug("[{}] Using file parent: {} child: {}",this, parent, child);
    }

    public NecroFile(File parent, String child) {
        super(parent, child);
        logger.debug("[{}] Using file parent: {} child: {}", this ,parent, child);
    }

    public NecroFile(URI uri) {
        super(uri);
        logger.debug("[{}] Using file {}", this, uri);
    }

    @Override
    public boolean renameTo(File dest) {
        logger.debug("[{}] Renaming to {}", this, dest);
        WriteSecurity.ensureWriteAllowed(dest.getAbsolutePath());
        return super.renameTo(dest);
    }

    @Override
    public boolean mkdir() {
        logger.debug("[{}] Making directory", this);
        WriteSecurity.ensureWriteAllowed(this.getAbsolutePath());
        return super.mkdir();
    }

    @Override
    public boolean mkdirs() {
        logger.debug("[{}] Making directories", this);
        WriteSecurity.ensureWriteAllowed(this.getAbsolutePath());
        return super.mkdirs();
    }

    @Override
    public void deleteOnExit() {
        logger.debug("[{}] Deleting on exit", this);
        WriteSecurity.ensureWriteAllowed(this.getAbsolutePath());
        super.deleteOnExit();
    }

    @Override
    public boolean delete() {
        logger.debug("[{}] Deleting", this);
        WriteSecurity.ensureWriteAllowed(this.getAbsolutePath());
        return super.delete();
    }

    @Override
    public boolean createNewFile() throws IOException {
        logger.debug("[{}] Creating new file", this);
        WriteSecurity.ensureWriteAllowed(this.getAbsolutePath());
        return super.createNewFile();
    }

    @Override
    public boolean setReadOnly() {
        return super.setReadOnly();
    }
}

