package systems.tat.teamspeak.loader;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import systems.tat.teamspeak.annotation.Watcher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 09.10.2022
 */
@Data
@Slf4j
public class WatcherLoader {

    private static WatcherLoader instance;
    private Set<Class<?>> watcherClasses;

    private WatcherLoader() {
        log.info("WatcherLoader initialized");
        instance = this;
    }

    public void searchForAnyWatcher() {
        log.info("Searching for any Watchers...");
        Reflections reflections = new Reflections("systems.tat.teamspeak.watcher");

        watcherClasses = reflections.get(Scanners.SubTypes.of(Scanners.TypesAnnotated.with(Watcher.class)).asClass());
        log.info("Found {} Watchers", watcherClasses.size());
    }

    public static WatcherLoader getInstance() {
        if (instance == null) {
            return new WatcherLoader();
        }

        return instance;
    }

    public void startWatchers() {
        watcherClasses.forEach(watcherClass -> {
            try {
                if (watcherClass.getSuperclass() == Thread.class) {
                    Thread thread = (Thread) watcherClass.getDeclaredConstructor().newInstance();
                    thread.setName(watcherClass.getSimpleName());
                    thread.start();
                    return;
                }
                log.warn("Watcher {} is not a Thread", watcherClass.getSimpleName());
                log.warn("Please make sure that the Watcher extends Thread");
            } catch (Exception e) {
                log.error("Error while starting watcher: " + watcherClass.getName());
                log.error("Exception: ", e);
            }
        });
    }

    public void stopWatchers() {
        watcherClasses.forEach(watcherClass -> {
            try {
                Method method = watcherClass.getMethod("stop");
                method.invoke(null);
            } catch (Exception e) {
                log.error("Error while stopping watcher: " + watcherClass.getName());
            }
        });
    }
}
