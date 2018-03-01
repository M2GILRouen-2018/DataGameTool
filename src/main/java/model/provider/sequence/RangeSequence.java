package model.provider.sequence;

import model.provider.RandomProvider;

/**
 * This providers returns value which are included between two limits min and max.
 *
 * All values are determined through a law probability which will define
 * their evolution :
 *  > 33% chance of keeping the value
 *  > 67% of modifying (the probability given to determine the direction will depend of
 *  the value's proximity with the closest boundary)
 */
public class RangeSequence extends AbstractSequence<Double> {
    // CONSTANTE
    /**
     * The portion of the total quantity used for evolution (%)
     */
    public static final double EVOLUTION_STEP = 0.05;

    /**
     * The probability that the value is stable between 2 value generations.
     */
    public static final double PROBABILITY_STAY = 0.33;


    // ATTRIBUTES
    private double min;
    private double max;

    /**
     * The portion added or substracted during an evolution
     */
    private double step;

    /**
     * The probability generator used to determine the choice.
     */
    private final RandomProvider probabilityProvider;


    // CONSTRUCTORS
    /**
     * Defines a new range sequence base on a given range, and with a begin value.
     *
     * @param first The start value of this sequence
     * @param a one boundary of the range
     * @param b the other boundary of the range
     */
    public RangeSequence(double first, double a, double b) {
        super(checkAssertions(first, a, b));

        min = a < b ? a : b;
        max = a < b ? b : a;
        probabilityProvider = new RandomProvider(0, 1);
        step = (max - min) * EVOLUTION_STEP;
    }
    public RangeSequence(double a, double b) {
        this((a + b) / 2, a, b);
    }
    public RangeSequence() {
        this(0.5, 0, 1);
    }


    // COMMAND
    @Override
    protected Double generate() {
        final double PROBABILITY_REDUCE = (1 - PROBABILITY_STAY) * ((last() - min) / (max - min));
        double p = probabilityProvider.next();

        // [0, PROB_REDUCE[ : We'll return a lower value than the last
        if (p < PROBABILITY_REDUCE) {
            return last() - step;
        }

        // ]1 - PROBABILITY_STAY; 1] : We keep the value
        if (p > (1 - PROBABILITY_STAY)) {
            return last();
        }

        // [PROB_REDUCE; 1 - PROBABILITY_STAY] : We'll return a higher value
        return last() + step;
    }


    // TOOLS
    /**
     * Just checks the validity of the provided arguments.
     * (first value of this sequence included in the range [a, b])
     *
     * @param first The start value of this sequence
     * @param a one boundary of the range
     * @param b the other boundary of the range
     *
     * @return the first value, used in the call for super constructor
     */
    private static double checkAssertions(double first, double a, double b) {
        if (first < (a < b ? a : b) || (a < b ? b : a) < first) {
            throw new AssertionError();
        }

        return first;
    }

    // TEST
    public static void main(String[] args) throws Exception {
        RangeSequence rs = new RangeSequence(10, 0, 20);
        while (rs.hasNext()) {
            System.out.println(rs.next());
            Thread.sleep(100);
        }
    }
}
