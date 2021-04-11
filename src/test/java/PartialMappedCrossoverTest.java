import algorithms.genetic.geneticoperations.PartialMappedCrossover;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PartialMappedCrossoverTest {

    private PartialMappedCrossover crossover;

    @Before
    public void setUp() {
        crossover = new PartialMappedCrossover();
    }

    @Test
    public void test1() {
        int leftCutPoint = 1;
        int rightCutPoint = 7;
        Integer[] i1 = new Integer[]{5, 3, 8, 7, 9, 6, 2, 1, 4};
        Integer[] i2 = new Integer[]{1, 6, 2, 3, 5, 7, 9, 8, 4};

        crossover.start(leftCutPoint, rightCutPoint, i1, i2);

        Assert.assertArrayEquals(new Integer[]{8, 6, 2, 3, 5, 7, 9, 1, 4}, i1);
        Assert.assertArrayEquals(new Integer[]{1, 3, 8, 7, 9, 6, 2, 5, 4}, i2);
    }

    @Test
    public void test2() {
        int leftCutPoint = 3;
        int rightCutPoint = 6;
        Integer[] i1 = new Integer[]{3, 4, 8, 2, 7, 1, 6, 5};
        Integer[] i2 = new Integer[]{4, 2, 5, 1, 6, 8, 3, 7};

        crossover.start(leftCutPoint, rightCutPoint, i1, i2);

        Assert.assertArrayEquals(new Integer[]{3, 4, 2, 1, 6, 8, 7, 5}, i1);
        Assert.assertArrayEquals(new Integer[]{4, 8, 5, 2, 7, 1, 3, 6}, i2);
    }

    @Test
    public void test3() {
        int leftCutPoint = 2;
        int rightCutPoint = 5;
        Integer[] i1 = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        Integer[] i2 = new Integer[]{5, 4, 6, 9, 2, 1, 7, 8, 3};

        crossover.start(leftCutPoint, rightCutPoint, i1, i2);

        Assert.assertArrayEquals(new Integer[]{1, 5, 6, 9, 2, 3, 7, 8, 4}, i1);
        Assert.assertArrayEquals(new Integer[]{2, 9, 3, 4, 5, 1, 7, 8, 6}, i2);
    }
}
