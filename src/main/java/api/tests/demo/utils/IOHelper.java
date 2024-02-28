package api.tests.demo.utils;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.Consumer;

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

    public static void writeToFile(String path, byte[] content) {
        File file = new File(path).getAbsoluteFile();
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file.getAbsolutePath()))) {
            out.write(content);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static String readToStringFromClasspath(String path) {
        try (InputStream reader = IOHelper.class.getClassLoader().getResourceAsStream(path)) {
            return IOUtils.toString(reader, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static byte[] readToBytesFromClasspath(String path) {
        try {
            return Files.readAllBytes(Paths.get(path).toAbsolutePath());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static String streamToString(InputStream in) {
        try {
            return IOUtils.toString(in, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static byte[] streamToBytes(InputStream in) {
        try {
            return IOUtils.toByteArray(in);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @SafeVarargs
    public static <T extends AutoCloseable> void closeAll(Consumer<Exception> exceptionConsumer, T ...resources) {
        for (int i = 0; i < resources.length; i++) {
            try {
                if (Objects.nonNull(resources[i])) {
                    resources[i].close();
                }
            } catch (Exception e) {
                exceptionConsumer.accept(e);
            }
        }
    }

    @SafeVarargs
    public static <T extends AutoCloseable> void closeAll(T ...resources) {
        closeAll(ex -> {}, resources);
    }
}
