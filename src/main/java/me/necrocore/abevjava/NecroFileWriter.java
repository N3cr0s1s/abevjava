package me.necrocore.abevjava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;

public class NecroFileWriter extends FileWriter {

    private final static Logger logger = LoggerFactory.getLogger(NecroFileWriter.class);

    public NecroFileWriter(String fileName) throws IOException {
        super(checkWriteAllowed(fileName));
        logger.debug("[{}] Creating writer with path: {}", this, fileName);
    }

    public NecroFileWriter(String fileName, boolean append) throws IOException {
        super(checkWriteAllowed(fileName), append);
        logger.debug("[{}] Creating writer with path: {}, append: {}", this, fileName, append);
    }

    public NecroFileWriter(File file) throws IOException {
        super(checkWriteAllowed(file));
        logger.debug("[{}] Creating writer with file: {}", this, file);
    }

    public NecroFileWriter(File file, boolean append) throws IOException {
        super(checkWriteAllowed(file), append);
        logger.debug("[{}] Creating writer with file: {}, append: {}", this, file, append);
    }

    public NecroFileWriter(String fileName, Charset charset) throws IOException {
        super(checkWriteAllowed(fileName), charset);
        logger.debug("[{}] Creating writer with path: {}, charset: {}", this, fileName, charset);
    }

    public NecroFileWriter(String fileName, Charset charset, boolean append) throws IOException {
        super(checkWriteAllowed(fileName), charset, append);
        logger.debug("[{}] Creating writer with path: {}, charset: {}, append: {}", this, fileName, charset, append);
    }

    public NecroFileWriter(File file, Charset charset) throws IOException {
        super(checkWriteAllowed(file), charset);
        logger.debug("[{}] Creating writer with file: {}, charset: {}", this, file, charset);
    }

    public NecroFileWriter(File file, Charset charset, boolean append) throws IOException {
        super(checkWriteAllowed(file), charset, append);
        logger.debug("[{}] Creating writer with file: {}, charset: {}, append: {}", this, file, charset, append);
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