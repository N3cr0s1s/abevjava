package me.necrocore.abevjava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class NecroFileOutputStream extends FileOutputStream {

    private final static Logger logger = LoggerFactory.getLogger(NecroFileOutputStream.class);

    public NecroFileOutputStream(String name) throws FileNotFoundException {
        super(checkWriteAllowed(name));
        logger.debug("[{}] Creating output stream with path: {}", this, name);
    }

    public NecroFileOutputStream(String name, boolean append) throws FileNotFoundException {
        super(checkWriteAllowed(name), append);
        logger.debug("[{}] Creating output stream with path: {}, append: {}", this, name, append);
    }

    public NecroFileOutputStream(File file) throws FileNotFoundException {
        super(checkWriteAllowed(file));
        logger.debug("[{}] Creating output stream with file: {}", this, file);
    }

    public NecroFileOutputStream(File file, boolean append) throws FileNotFoundException {
        super(checkWriteAllowed(file), append);
        logger.debug("[{}] Creating output stream with file: {}, append: {}", this, file, append);
    }

    public NecroFileOutputStream(FileDescriptor fdObj) {
        super(fdObj);
        logger.debug("[{}] Creating output stream with FileDescriptor: {}", this, fdObj);
        logger.warn("FileDescriptor used without path security check.");
    }

    private static String checkWriteAllowed(String path) {
        WriteSecurity.ensureWriteAllowed(path);
        return path;
    }

    private static File checkWriteAllowed(File file) {
        WriteSecurity.ensureWriteAllowed(file.getAbsolutePath());
        return file;
    }

}