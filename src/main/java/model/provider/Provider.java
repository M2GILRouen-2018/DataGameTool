package model.provider;

import java.util.Iterator;

/**
 * Defines a provider which will be able to build new values from
 * a specific generation logic.
 *
 * @version 1.0
 */
public interface Provider<T> extends Iterator<T> {
    // REQUESTS
    /**
     * Returns the number of generations made with this provider.
     */
    long count();

    /**
     * Returns the last value produced by this provider.
     */
    T last();

    // COMMANDS
    /**
     * Reset the internal state of a provider with a specific "last" value
     */
    void reset(T last);

    /**
     * Reset the internal state of a provider
     */
    void reset();

    /**
     * Generate a new value, based on the provider's generation logic
     */
    @Override
    T next();
}
