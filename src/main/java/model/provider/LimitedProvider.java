package model.provider;

/**
 * Defines a type of provider, which will not produce
 * more generated values than specified.
 */
public abstract class LimitedProvider<T> extends AbstractProvider<T> {
    // ATTRIBUTES
    private final long limit;


    // CONSTRUCTOR
    public LimitedProvider(long limit) {
        this.limit = limit;
    }


    // REQUESTS
    public long limit() {
        return limit;
    }

    @Override
    public boolean hasNext() {
        return count() < limit && super.hasNext();
    }
}
