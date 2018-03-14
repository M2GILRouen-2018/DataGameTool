package model.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Provides items furnished by a given collection in a randomly way.
 *
 * @param <T> The type of items distributed by this provider
 */
class ItemProvider<T> extends AbstractProvider<T> {
    // ATTRIBUTES
    /**
     * All element in which the provider will choose its value.
     */
    private final List<T> elements;

    /**
     * The index generator, used for item's selection
     * @param elements
     */
    private final Provider<Integer> indexProvider;


    // CONSTRUCTOR
    ItemProvider(Collection<T> elements) {
        this.elements = new ArrayList<>(elements);
        this.indexProvider = ProviderBuilder.getRandomIntProvider(0, elements.size() - 1);
    }
    ItemProvider(T... elements) {
        this(Arrays.asList(elements));
    }


    // METHOD
    @Override
    protected T generate() {
        return elements.get(indexProvider.next());
    }
}
