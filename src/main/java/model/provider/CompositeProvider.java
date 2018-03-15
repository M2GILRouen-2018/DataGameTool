package model.provider;

import model.provider.sequence.RangeSequence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Create a provider composed of several sub-providers.
 * These sub-providers are linked by their datas (the last value of the previous
 * sub-provider will define the next's first value).
 */
public class CompositeProvider<T> extends AbstractProvider<T> {
    // ATTRIBUTES
    /**
     * The list of all sub-providers which defines this provider
     */
    private final List<Provider<T>> subProviders;

    /**
     * The currently used subprovider.
     */
    private Provider<T> current;


    // CONSTRUCTORS
    public CompositeProvider(T first, Collection<Provider<T>> subProviders) {
        super(first);
        this.subProviders = new ArrayList<>(subProviders);
        this.current = this.subProviders.get(0);
        this.current.reset(first);
    }
    public CompositeProvider(T first, Provider<T>... subProviders) {
        this(first, Arrays.asList(subProviders));
    }

    public CompositeProvider(List<Provider<T>> subProviders) {
        this(subProviders.get(0).last(), subProviders);
    }
    public CompositeProvider(Provider<T>... subProviders) {
        this(subProviders[0].last(), subProviders);
    }


    // REQUEST
    @Override
    public boolean hasNext() {
        if (current == null) return false;

        int index = subProviders.indexOf(current);
        for (int k = index; k < subProviders.size(); ++k) {
            if (subProviders.get(k).hasNext()) return true;
        }
        return false;
    }


    // METHODS
    @Override
    public void reset(T last) {
        for (Provider<T> provider : subProviders) provider.reset();

        this.current = this.subProviders.get(0);
        this.current.reset(last);
    }

    @Override
    public T next() {
        T value = super.next();

        if (!current.hasNext()) takeNextSubSequence();
        return value;
    }

    @Override
    protected T generate() {
        return current.next();
    }

    // TOOL
    /**
     * Modify the current used subsequence to take the next usable one.
     */
    private void takeNextSubSequence() {
        int index = subProviders.indexOf(current) + 1;

        // Searching next usable subsequence
        while (index < subProviders.size() && !subProviders.get(index).hasNext()) {
            ++index;
        }

        // If none has been found : return null
        if (index >= subProviders.size()) {
            current = null;
            return;
        }

        // Defining current by restting it...
        current = subProviders.get(index);
        current.reset(last());
    }


    // TEST ENTRY POINT
    public static void main(String[] args) {
        Provider<Double> provider = new CompositeProvider<Double>(2.,
                ProviderBuilder.limit(new RangeSequence(0, 20), 10),
                ProviderBuilder.limit(new RangeSequence(5, 100), 90)
        );

        while (provider.hasNext()) {
            System.out.println(provider.next());
        }

        System.out.println("----");

        provider.reset();
        while (provider.hasNext()) {
            System.out.println(provider.next());
        }
    }
}
