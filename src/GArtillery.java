import acm.graphics.GCompound;
import acm.graphics.GImage;

public class GArtillery extends GCompound {
    GImage artillery;
    int scaleLocal = 0;
    public GArtillery(int scale) {
        scaleLocal = scale;
        artillery = new GImage("../pictures/artillery/artillery.png");
        artillery.setSize(scaleLocal, (scaleLocal*8/10.0));
        add(artillery, 0, -scaleLocal*8/10.0);
    }


    public void kill() {
        artillery.setImage("../pictures/artillery/artilleryDestroyed.png");
        artillery.setSize(scaleLocal, (scaleLocal*8/10.0));
        PlaneShooter.artillery = null;
    }
}
