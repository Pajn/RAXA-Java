package se.raxa.server.plugins;

import java.io.File;
import java.io.IOException;
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
     * @throws IOException
     */
    public static URLClassLoader addDirToClasspath(File directory) throws IOException {
        List<URL> urls = new ArrayList<>();
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".jar")) {
                        urls.add(file.toURI().toURL());
                    }
                }
            }
        }
        return addURLs(urls.toArray(new URL[urls.size()]));
    }

    /**
     * @param urls Array of urls to add to the classpath
     * @throws IOException
     */
    private static URLClassLoader addURLs(URL[] urls) throws IOException {
        return new URLClassLoader(urls, ClasspathUtils.class.getClassLoader());
    }
}
