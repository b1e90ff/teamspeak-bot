package systems.tat.teamspeak.loader;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import systems.tat.teamspeak.annotation.Watcher;
import systems.tat.teamspeak.util.FileUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

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
    private List<Class<?>> watcherClasses;

    private WatcherLoader() {
        log.info("WatcherLoader initialized");
        instance = this;
    }

    public void searchForAnyWatcher() {
        log.info("Searching for any Watchers...");
        watcherClasses = FileUtil.findClassesWithAnnotation("systems.tat.teamspeak.watcher", Watcher.class);
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
                Method method = watcherClass.getMethod("start");
                method.invoke(null);
            } catch (Exception e) {
                log.error("Error while stopping watcher: " + watcherClass.getName());
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
