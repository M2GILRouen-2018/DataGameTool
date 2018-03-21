package model.provider;

import io.univ.rouen.m2gil.smartclass.core.data.Data;
import io.univ.rouen.m2gil.smartclass.core.datagenerator.DataGenerator;
import model.Values;
import model.provider.sequence.RangeSequence;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Aims at producing simple providers
 */
public abstract class ProviderBuilder {
    // METHODS

    /**
     * Create a new provider to generate random decimal value
     * between two given values.
     */
    public static Provider<Double> getRandomProvider(int a, int b) {
        return new AbstractProvider<Double>() {
            @Override
            protected Double generate() {
                return a + Math.random() * (b - a);
            }
        };
    }

    /**
     * Create a new ranged sequence. A range sequence will create values,
     * which are included between two limits min and max.
     */
    public static Provider<Double> getRangeSequence(double a, double b) {
        return new RangeSequence(a, b);
    }

    public static Provider<Double> getRangeSequence(double first, double a, double b) {
        return new RangeSequence(first, a, b);
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
     * Create a new provider to generate random values which acts
     * as a probability sample (values between 0 and 1).
     */
    public static Provider<Double> getProbabilityProvider() {
        return getRandomProvider(0, 1);
    }

    /**
     * Create a new provider which aims at producing data entities for the demo.
     */
    public static Provider<Data> getDataProvider(DataGenerator dataGenerator, double a, double b) {
        return new DataProvider(dataGenerator, a, b);
    }

    public static Provider<Data> getDataProvider(DataGenerator dataGenerator, Provider<Double> dataProvider) {
        return new DataProvider(dataGenerator, dataProvider);
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

    /**
     * Create a new random provider based on a given collection
     */
    public static Provider<Double> getLinearSegmentProvider(int steps, double a, double b) {
        return new LimitedProvider<Double>(steps) {
            @Override
            protected Double generate() {
                return a + ((double) count() / (double) steps) * (b - a);
            }
        };
    }

    /**
     * Limits the amount of values produce by a given provider
     */
    public static <T> Provider<T> limit(final Provider<T> provider, int limit) {
        return new LimitedProvider<T>(limit) {
            @Override
            public T last() {
                return provider.last();
            }

            @Override
            public void reset(T last) {
                super.reset(last);
                provider.reset(last);
            }

            @Override
            protected T generate() {
                return provider.next();
            }
        };
    }

    /**
     * Aims at returning lists of values, instead of simple samples one by one.
     */
    public static <T> Provider<List<T>> collector(final Provider<T> provider, int collectorSize) {
        return new AbstractProvider<List<T>>() {
            @Override
            protected List<T> generate() {
                List<T> list = new ArrayList<>();
                int k = 0;
                while (provider.hasNext() && k < collectorSize) {
                    list.add(provider.next());
                    ++k;
                }

                return list;
            }
        };
    }

    /**
     * Aims at building a new provider based on a collection of providers.
     */
    public static <T> Provider<T> compose(Provider<T>... providers) {
        return new CompositeProvider<T>(providers);
    }

    public static <T> Provider<T> compose(List<Provider<T>> providers) {
        return new CompositeProvider<T>(providers);
    }

    /**
     * Aims at producing a data provider to reproduce the behavior of a light sensor.
     */
    public static Provider<Data> getLightProvider(DataGenerator dataGenerator, LocalTime lightUp, LocalTime lightDown) {
        // Parameters
        int sunrise = lightUp.getHour() * 60 + lightUp.getMinute();
        int sunset = lightDown.getHour() * 60 + lightDown.getMinute();
        int step = Values.LIGHT_STEP;

        // Returning provider
        return getDataProvider(dataGenerator, compose(
                ProviderBuilder.limit(
                        ProviderBuilder.getRangeSequence(0.95*Values.NIGHT_LIGHT, 1.05*Values.NIGHT_LIGHT),
                        sunrise - step
                ),
                ProviderBuilder.limit(
                        ProviderBuilder.getLinearSegmentProvider(2*step,Values.NIGHT_LIGHT,Values.DAY_LIGHT),
                        2 * step
                ),
                ProviderBuilder.limit(
                        ProviderBuilder.getRangeSequence(0.95*Values.DAY_LIGHT,1.05*Values.DAY_LIGHT),
                        sunset -(sunrise + step)-step
                ),
                ProviderBuilder.limit(
                        ProviderBuilder.getLinearSegmentProvider(2*step,Values.DAY_LIGHT,Values.NIGHT_LIGHT),
                        2*step
                ),
                ProviderBuilder.limit(
                        ProviderBuilder.getRangeSequence(0.95*Values.NIGHT_LIGHT,1.05*Values.NIGHT_LIGHT),
                        60*24-(sunset +step)
                )
        ));
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        List<Data> datas = new LinkedList<>();

        // MAIN
        Provider<Data> dataProvider = ProviderBuilder.getLightProvider(null, LocalTime.of(8, 0), LocalTime.of(19, 0));
        Provider<List<Data>> dataCollector = ProviderBuilder.collector(dataProvider,60 * 24);

        for (int k = 0; k < 365; ++k) {
            dataCollector.next();
            dataProvider.reset();

            System.out.println(String.format(
                    "%d : %dms (%d éléments)", k, System.currentTimeMillis() - start, datas.size()
            ));
        }
    }
}
