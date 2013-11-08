package se.raxa.server.plugins;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rasmus Eneman
 */
class ClasspathUtils {

    /**
     * @param directory File object of a directory that contain jars
     */
    public static URLClassLoader addDirToClasspath(File directory) {
        List<URL> urls = new ArrayList<>();
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".jar")) {
                        try {
                            urls.add(file.toURI().toURL());
                        } catch (MalformedURLException e) {
                            // Silently consume and live on
                        }
                    }
                }
            }
        }
        return addURLs(urls.toArray(new URL[urls.size()]));
    }

    /**
     * @param urls Array of urls to add to the classpath
     */
    private static URLClassLoader addURLs(URL[] urls) {
        return new URLClassLoader(urls, ClasspathUtils.class.getClassLoader());
    }
}
