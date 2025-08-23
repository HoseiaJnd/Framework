package jnd.upload;

import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class UploadedFile {
    private final Part filePart;
    private String fileName;
    private String contentType;
    private long size;
    private InputStream inputStream;
    private String savedPath;

    public UploadedFile(Part filePart) {
        this.filePart = filePart;
        this.fileName = null;
        this.contentType = null;
        this.size = -1;
        this.inputStream = null;
    }

    public UploadedFile(String fileName, String contentType, long size, InputStream inputStream) {
        this.filePart = null;
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
        this.inputStream = inputStream;
    }

    public String getFileName() {
        if (filePart != null) {
            String submittedFileName = filePart.getSubmittedFileName();
            return submittedFileName != null ? submittedFileName : "";
        }
        return fileName != null ? fileName : "";
    }

    public String getContentType() {
        return filePart != null ? filePart.getContentType() : contentType;
    }

    public long getSize() {
        return filePart != null ? filePart.getSize() : size;
    }

    public InputStream getInputStream() throws IOException {
        return filePart != null ? filePart.getInputStream() : inputStream;
    }

    public String getSavedPath() {
        return savedPath;
    }

    public void setSavedPath(String savedPath) {
        this.savedPath = savedPath;
    }

    public String getExtension() {
        String fileName = getFileName();
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(lastDot + 1) : "";
    }

    public boolean save(String directory) {
        try {
            Path dirPath = Paths.get(directory);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            Path filePath = dirPath.resolve(getFileName());
            try (InputStream input = getInputStream()) {
                Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
            savedPath = filePath.toString();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}