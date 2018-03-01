package model.provider.sequence;

import model.provider.Provider;

/**
 * Define a special kind of providers known as sequences.
 *
 * A sequence is a provider which uses the last generated value in order to
 * produce the next. It will depend on a first value (acting as an intializer),
 * which will determine the way of producing all values.
 *
 * @version 1.0
 */
public interface Sequence<T> extends Provider<T> {
    // REQUESTS
    /**
     * Returns the first value of this sequence
     */
    T first();
}
