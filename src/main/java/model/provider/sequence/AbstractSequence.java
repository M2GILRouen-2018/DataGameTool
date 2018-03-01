package model.provider.sequence;

import model.provider.AbstractProvider;

/**
 * Abstract implementation for generic mechanisms which should be
 * common to all sequences.
 *
 * @version 1.0
 */
public abstract class AbstractSequence<T> extends AbstractProvider<T> implements Sequence<T> {
    // ATTRIBUTES
    private final T first;


    // CONSTRUCTORS
    protected AbstractSequence(T first) {
        super(first);
        this.first = first;
    }
    protected AbstractSequence() {
        this(null);
    }


    // REQUESTS
    @Override
    public T first() {
        return first;
    }
}
