package project;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Logger {
    private final Path logFilePath;
    
    public Logger(String folderName, String fileName) {
        try {
            // Resolve the folder and ensure it exists
            Path folderPath = Paths.get(folderName);
            if (!Files.exists(folderPath)) {
                throw new IOException("Folder '" + folderName + "' doesn't exists.");
            }
            
            // Resolve the file path and ensure the file exists
            this.logFilePath = folderPath.resolve(fileName);
            if (!Files.exists(logFilePath)) {
                throw new IOException("File '" + fileName + "' doesn't exists.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize logger", e);
        }
    }
    
    public void write(String data) {
        try (BufferedWriter writer = Files.newBufferedWriter(logFilePath, StandardOpenOption.APPEND)) {
            writer.write(data);
            writer.newLine();
            //System.out.println("Logged: " + data);
        } catch (IOException e) {
            System.out.println("Error writing to log file: " + e.getMessage());
        }
    }
}