package model.provider;

import io.univ.rouen.m2gil.smartclass.core.data.Data;
import io.univ.rouen.m2gil.smartclass.core.datagenerator.DataGenerator;

import java.util.Collection;
import java.util.List;

/**
 * Aims at producing simple providers
 */
public abstract class ProviderBuilder {
    // METHODS
    /**
     * Create a new provider to generate random values which acts
     * as a probability sample (values between 0 and 1).
     */
    public static Provider<Double> getProbabilityProvider() {
        return new RandomProvider(0, 1);
    }

    /**
     * Create a new provider to generate random integer
     * between two values.
     */
    public static Provider<Integer> getRandomIntProvider(int a, int b) {
        return new AbstractProvider<Integer>() {
            @Override
            protected Integer generate() {
                return a + (int) ((b - a + 1) * Math.random());
            }
        };
    }

    /**
     * Create a new provider to generate random integer
     * between two values.
     */
    public static Provider<Double> getRandomProvider(int a, int b) {
        return new RandomProvider(a, b);
    }

    /**
     * Create a new provider which aims at producing data entities for the demo.
     */
    public static Provider<Data> getDataProvider(DataGenerator dataGenerator, double a, double b, int limit) {
        return new DataProvider(dataGenerator, a, b, limit);
    }

    /**
     * Create a new random provider based on a given collection
     */
    public static <T> Provider<T> getItemProvider(T... elements) {
        return new ItemProvider<T>(elements);
    }
    public static <T> Provider<T> getItemProvider(Collection<T> elements) {
        return new ItemProvider<T>(elements);
    }
}
