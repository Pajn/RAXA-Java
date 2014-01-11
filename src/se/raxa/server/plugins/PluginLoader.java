package se.raxa.server.plugins;

import java.io.File;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Rasmus Eneman
 */
public class PluginLoader {
    private static final Logger LOGGER = Logger.getLogger(PluginLoader.class.getName());

    private static PluginLoader pluginLoader;
    private final ServiceLoader<Plugin> serviceLoader;

    private PluginLoader() {
        serviceLoader = ServiceLoader.load(Plugin.class, addPluginJarsToClasspath());
    }

    /**
     * Adds all plugins in the plugin dir to the classpath
     *
     * @return a ClassLoader containing all plugin classes
     */
    private static ClassLoader addPluginJarsToClasspath() {
        File file = new File("plugins");
        LOGGER.log(Level.INFO, "Reading JAR: \"{0}\"", file.getAbsolutePath());
        return ClasspathUtils.addDirToClasspath(file);
    }

    /**
     * @return A singleton instance of PluginLoader
     */
    private static PluginLoader getInstance() {
        if (pluginLoader == null) {
            pluginLoader = new PluginLoader();
        }
        return pluginLoader;
    }

    /**
     * Loads all plugins
     *
     * @return a Iterator containing all plugins
     */
    private static Iterator<Plugin> getPlugins() {
        return getInstance().serviceLoader.iterator();
    }

    /**
     * Loads all plugins and initializes them
     */
    public static void initPlugins() {
        Iterator<Plugin> iterator = getPlugins();
        if(!iterator.hasNext()) {
            LOGGER.log(Level.INFO, "No plugins were found!");
        }
        while(iterator.hasNext()) {
            Plugin plugin = iterator.next();
            LOGGER.log(Level.INFO, "Initializing plugin \"{0}\"", plugin.getName());
            plugin.init();
        }
    }
}
