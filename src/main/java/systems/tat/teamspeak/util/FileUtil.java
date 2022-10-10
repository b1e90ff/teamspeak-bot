package systems.tat.teamspeak.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import systems.tat.teamspeak.TeamSpeakBotApplication;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 02.10.2022
 */
@Slf4j
public class FileUtil {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static String getJarPathWithJar() {
        try {
            return new File(TeamSpeakBotApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).toString();
        } catch (Exception e) {
            log.error("Could not get jar path", e);
            return null;
        }
    }

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

    public static boolean jsonFileExists(String path) {
        return Files.exists(Paths.get(path));
    }
}
