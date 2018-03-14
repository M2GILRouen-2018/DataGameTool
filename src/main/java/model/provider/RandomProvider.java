package model.provider;

/**
 * Produces random values in a given double interval.
 *
 * @version 1.0
 */
class RandomProvider extends AbstractProvider<Double> {
    // ATTRIBUTES
    private double min;
    private double max;

    // CONSTRUCTOR
    public RandomProvider(double a, double b) {
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
