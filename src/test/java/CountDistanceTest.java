import model.Coords;
import org.junit.Assert;
import org.junit.Test;
import utils.Utils;

public class CountDistanceTest {

    @Test
    public void CountDistanceTest_Should_PassedForTheGivenInput_1() {

        //input
        Coords coordsGdynia = new Coords(54.520, 18.530);
        Coords coordsKatowice = new Coords(50.260, 19.020);
        double realDistance = 474.20; //by google maps

        //call method
        double countedDistance = Utils.countDistance(coordsGdynia, coordsKatowice);

        //check result
        Assert.assertEquals(countedDistance, realDistance, 1.0);

    }

    @Test
    public void CountDistanceTest_Should_PassedForTheGivenInput_2() {

        //input
        Coords coordsWarszawa = new Coords(52.260, 21.020);
        Coords coordsKielce = new Coords(50.890, 20.650);
        double realDistance = 153.92; //by google maps

        //call method
        double countedDistance = Utils.countDistance(coordsWarszawa, coordsKielce);

        //check result
        Assert.assertEquals(countedDistance, realDistance, 1.0);

    }

    @Test
    public void CountDistanceTest_Should_PassedForTheGivenInput_3() {

        //input
        Coords coordsSlupsk = new Coords(54.470, 17.020);
        Coords coordsBielskoBiala = new Coords(49.820, 19.050);
        double realDistance = 534.38; //by google maps

        //call method
        double countedDistance = Utils.countDistance(coordsSlupsk, coordsBielskoBiala);

        //check result
        Assert.assertEquals(countedDistance, realDistance, 1.0);

    }
}
