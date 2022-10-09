package systems.tat.teamspeak.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 02.10.2022
 */
@Slf4j
public class FileUtil {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static String getJarPath() {
        try {
            return new File(FileUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath() + File.separator;
        } catch (Exception e) {
            log.error("Could not get jar path", e);
            return null;
        }
    }

    public static JsonObject readJsonFile(String path) {
        return readJsonFile(path, JsonObject.class);
    }

    public static <T> T readJsonFile(String path, Class<T> tClass) {
        try {
            return gson.fromJson(new String(Files.readAllBytes(Paths.get(path))), tClass);
        } catch (Exception e) {
            log.error("Error while reading file: " + path, e);
        }
        return null;
    }

    public static <T> void writeJsonFile(String path, Object object) {
        Path jsonPath = Paths.get(path);

        // check if path exists
        if (!new File(path).exists()) {
            try {
                Files.createDirectories(jsonPath.getParent());
            } catch (Exception e) {
                log.error("Error while creating directories for file: " + path, e);
            }
        }

        try {
            Files.write(jsonPath, gson.toJson(object).getBytes());
        } catch (Exception e) {
            log.error("Error while writing file: " + path, e);
        }
    }

    public static List<Class<?>> findClassesWithAnnotation(String packageName, Class<? extends Annotation> annotation) {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = null;

        try {
            resources = classLoader.getResources(path);
        } catch (Exception e) {
            log.error("Could not find resources", e);
        }

        List<File> dirs = new ArrayList<>();

        while (true) {
            assert resources != null;
            if (!resources.hasMoreElements()) break;

            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }

        List<Class<?>> classes = new ArrayList<>();

        dirs.stream().filter(File::exists).forEach(file -> classes.addAll(findClassesWithAnnotation(file, packageName, annotation)));
        log.info("Found " + classes.size() + " classes with @{} annotation", annotation.getSimpleName());

        return classes;
    }

    private static List<Class<?>> findClassesWithAnnotation(File directory, String packageName, Class<? extends Annotation> annotation) {

        List<Class<?>> classes = new ArrayList<>();

        if (!directory.exists())
            return classes;

        File[] files = directory.listFiles();
        assert files != null;

        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClassesWithAnnotation(file,
                        (!packageName.equals("") ? packageName + "." : packageName) + file.getName(), annotation)
                );
            } else if (file.getName().endsWith(".class"))
                try {
                    Class<?> clazz = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
                    // Check for Watcher annotation
                    if (clazz.isAnnotationPresent(annotation))
                        classes.add(clazz);
                } catch (ClassNotFoundException e) {
                    log.error("Could not find class", e);
                }
        }
        return classes;
    }

    public static boolean jsonFileExists(String path) {
        return Files.exists(Paths.get(path));
    }
}
