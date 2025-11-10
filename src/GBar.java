import acm.graphics.GCompound;
import acm.graphics.GImage;

public class GBar extends GCompound {
    GImage bar;
    int scaleLocal = 0;
    public GBar(int scale) {
        scaleLocal = scale;
        bar = new GImage("../pictures/bar/bar.png");
        bar.setSize(scaleLocal, (scaleLocal*793/1155.0));
        add(bar, 0, -scaleLocal*793/1155.0);
    }


    public void kill() {
        bar.setImage("../pictures/bar/barDestroyed.png");
        bar.setSize(scaleLocal, (scaleLocal*793/1155.0));
        PlaneShooter.bar = null;
    }
}
