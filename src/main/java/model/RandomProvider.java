package model;

/**
 * Produces random values in a given double interval.
 */
public class RandomProvider extends AbstractProvider<Double> {
    // ATTRIBUTES
    private double min;
    private double max;

    // CONSTRUCTOR
    public RandomProvider(int a, int b) {
        min = a < b ? a : b;
        max = a < b ? b : a;
    }
    public RandomProvider() {
        this(0, 1);
    }

    // COMMAND
    @Override
    protected Double generate() {
        return min + Math.random() * (max - min);
    }
}
