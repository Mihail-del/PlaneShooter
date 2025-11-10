import acm.graphics.GCompound;
import acm.graphics.GImage;

public class GBomb extends GCompound {
    GImage bomb;
    int scaleLocal = 0;
    public GBomb(int scale) {
        scaleLocal = scale;
        bomb = new GImage("../pictures/bomb/bomb.png");
        bomb.setSize(scaleLocal, (scaleLocal*862.0/704.0));
        add(bomb, 0, 0);
    }

    public void burst() {
        if (PlaneShooter.bomb != null) {
            bomb.setImage("../pictures/bomb/bombBurst.png");
            bomb.setSize(7 * scaleLocal, 7 * scaleLocal);
            pause(150);
            remove(bomb);
        }
    }
}
