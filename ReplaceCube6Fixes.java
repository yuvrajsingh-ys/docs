import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReplaceConvertalkFixes {
    public static void main(String[] args) {
        Path folderPath = Paths.get(".");

        try (Stream<Path> pathStream = Files.walk(folderPath)) {
            List<Path> allTextFiles = pathStream
                    .filter(Files::isRegularFile)
                    .filter(ReplaceConvertalkFixes::isTextFile)
                    .collect(Collectors.toList());

            int totalFiles = allTextFiles.size();
            int modifiedFiles = 0;

            for (int i = 0; i < totalFiles; i++) {
                Path file = allTextFiles.get(i);
                boolean wasModified = processFile(file);

                if (wasModified) {
                    modifiedFiles++;
                    System.out.println("Modified: " + file);
                }

                double percent = ((i + 1) / (double) totalFiles) * 100;
                System.out.printf("Progress: %d/%d files (%.0f%%)%n", i + 1, totalFiles, percent);
            }

            System.out.println("\nSummary:");
            System.out.println("Total files checked: " + totalFiles);
            System.out.println("Files modified     : " + modifiedFiles);

        } catch (IOException e) {
            System.err.println("Error walking through files: " + e.getMessage());
        }
    }

    private static boolean isTextFile(Path path) {
        String fileName = path.getFileName().toString().toLowerCase();
        return fileName.endsWith(".mdx")
                || fileName.endsWith(".md")
                || fileName.endsWith(".txt")
                || fileName.endsWith(".html")
                || fileName.endsWith(".js")
                || fileName.endsWith(".ts")
                || fileName.endsWith(".jsx")
                || fileName.endsWith(".tsx");
    }

    private static boolean processFile(Path filePath) {
        try {
            String content = Files.readString(filePath);
            String updated = content
                    .replace("_ Convertalk", "_Convertalk")
                    .replace("how- Convertalk-works", "how-botpenguin-works");

            if (!content.equals(updated)) {
                Files.writeString(filePath, updated);
                return true;
            }
        } catch (IOException e) {
            System.err.println("Failed to process: " + filePath + " - " + e.getMessage());
        }
        return false;
    }
}
