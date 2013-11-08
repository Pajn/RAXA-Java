package se.raxa.server.plugins;

import java.io.File;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author Rasmus Eneman
 */
public class PluginLoader {
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
        System.out.println(file.getAbsolutePath());
        return ClasspathUtils.addDirToClasspath(file);
    }

    /**
     * @return A singleton instance of PluginLoader
     */
    private static PluginLoader getInstance() {
        if(pluginLoader == null) {
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
            System.out.println("No plugins were found!");
        }
        while(iterator.hasNext()) {
            Plugin plugin = iterator.next();
            System.out.println("Initializing the plugin " + plugin.getName());
            plugin.init();
        }
    }
}
