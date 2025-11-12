import acm.graphics.GCompound;
import acm.graphics.GImage;

public class GSoldier extends GCompound {
    GImage soldierLive;
    int scaleLocal = 0;
    public GSoldier(int scale) {
        scaleLocal = scale;
        soldierLive = new GImage("../pictures/soldier/soldierRight.png");
        soldierLive.setSize(scaleLocal, (scaleLocal*417.0/327.0));
        add(soldierLive, 0, 0);
    }

    public void turnAround(boolean toLeft) {
        if (toLeft)
            soldierLive.setImage("../pictures/soldier/soldierLeft.png");
        else
            soldierLive.setImage("../pictures/soldier/soldierRight.png");
        soldierLive.setSize(scaleLocal, (scaleLocal*417.0/327.0));
    }

    public void kill(int who) {
        soldierLive.setImage("../pictures/soldier/soldierDead.png");
        soldierLive.setSize(1.7*scaleLocal, (scaleLocal*1.7*567.0/1295.0));
        soldierLive.setLocation(0, scaleLocal*0.7);
        if (who == 1)
            PlaneShooter.soldierOne = null;
        else if (who == 2)
            PlaneShooter.soldierTwo = null;
        else if (who == 3)
            PlaneShooter.soldierThree = null;
        pause(3000);
        remove(soldierLive);
    }
}
