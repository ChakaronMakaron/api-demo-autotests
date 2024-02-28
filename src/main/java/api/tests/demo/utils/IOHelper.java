package api.tests.demo.utils;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IOHelper {

    public static String readToString(String path) {
        try (Reader reader = new FileReader(new File(path).getAbsolutePath(), StandardCharsets.UTF_8)) {
            return IOUtils.toString(reader);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static byte[] readToBytes(String path) {
        try {
            return Files.readAllBytes(Paths.get(path).toAbsolutePath());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
