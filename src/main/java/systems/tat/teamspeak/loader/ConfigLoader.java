package systems.tat.teamspeak.loader;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import systems.tat.teamspeak.annotation.Configuration;
import systems.tat.teamspeak.config.BotConfiguration;
import systems.tat.teamspeak.util.FileUtil;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 09.10.2022
 */
@Slf4j
public class ConfigLoader {

    private static ConfigLoader instance;

    private ConfigLoader() {
        instance = this;
        log.info("ConfigLoader initialized");
    }

    public static ConfigLoader getInstance() {
        if (instance == null) {
            return new ConfigLoader();
        }

        return instance;
    }

    public void loadConfig() {
        log.info("Loading config...");

        AtomicBoolean newConfigFileCreated = new AtomicBoolean(false);
        Reflections reflections = new Reflections("systems.tat.teamspeak.model");

        reflections.get(Scanners.SubTypes.of(Scanners.TypesAnnotated.with(Configuration.class)).asClass()).forEach(configClazz -> {
            // Get Annotation variables
            Configuration annotation = configClazz.getAnnotation(Configuration.class);
            String path = FileUtil.getJarPath() + annotation.path();

            // Check if the config file exits
            if (!FileUtil.jsonFileExists(path)) {
                // Create new config file
                try {
                    FileUtil.writeJsonFile(path, configClazz.getDeclaredConstructor().newInstance());
                    newConfigFileCreated.set(true);
                    log.warn("Created new Config file for {} in path {}", configClazz.getSimpleName(), path);
                    log.warn("The Bot will shutdown after checking the other configs");
                } catch (Exception e) {
                    log.error("Could not create new config file for " + configClazz.getSimpleName());
                    log.error("Exception: ", e);
                    log.error("Please create the file manually and restart the bot");
                    log.error("The bot will now exit");
                    System.exit(1);
                }
            }

            // Read the config file
            if (!newConfigFileCreated.get()) {
                try {
                    Object config = FileUtil.readJsonFile(path, configClazz);
                    Class<?> botConfigurationClass = BotConfiguration.class;

                    // Searching for the config field in the BotConfiguration class
                    for (Field field : botConfigurationClass.getDeclaredFields()) {
                        if (field.getType().equals(configClazz)) {
                            field.setAccessible(true);
                            field.set(null, config);
                            log.info("Loaded {} from path {} successfully", configClazz.getSimpleName(), path);
                        }
                    }
                } catch (Exception e) {
                    log.error("Could not read config file for " + configClazz.getSimpleName());
                    log.error("Exception: ", e);
                    log.error("Please check the file manually and restart the bot");
                    log.error("The bot will now exit");
                    System.exit(1);
                }
            }
        });

        // If a config file was created, the bot will exit
        if (newConfigFileCreated.get()) {
            log.warn("Created new config files!");
            log.warn("Please fill out the config files and restart the bot!");
            System.exit(0);
        }
    }
}
