package se.raxa.server.plugins;

import se.raxa.server.exceptions.BadPluginException;

/**
 * A interface all plugins should implement
 *
 * @author Rasmus Eneman
 */
public interface Plugin {
    /**
     * @return A user readable name for the plugin
     */
    String getName();

    /**
     * Called when the plugin is loaded
     */
    void init() throws BadPluginException;
}
