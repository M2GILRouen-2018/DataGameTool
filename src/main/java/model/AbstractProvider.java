package model;

/**
 * Abstract implementation for generic mechanisms which should be
 * common to all providers.
 */
public abstract class AbstractProvider<T> implements Provider<T> {
    // ATTRIBUTES
    /**
     * The value generations' count.
     */
    private long count;

    /**
     * The last produced value
     */
    private T last;


    // REQUESTS
    @Override
    public long count() {
        return count;
    }

    @Override
    public T last() {
        return last;
    }

    @Override
    public boolean hasNext() {
        return true;
    }


    // COMMANDS
    @Override
    public T next() {
        last = generate();
        ++count;
        return last;
    }

    // TOOLS
    /**
     * Defines the way a new value is produced within a provider.
     */
    protected abstract T generate();
}
