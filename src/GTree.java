import acm.graphics.GCompound;
import acm.graphics.GImage;

public class GTree extends GCompound {
    GImage tree;
    int scaleLocal = 0;
    public GTree(int scale) {
        scaleLocal = scale;
        tree = new GImage("../pictures/tree/tree.png");
        tree.setSize(scaleLocal, (scaleLocal*84/75.0));
        add(tree, 0, -scaleLocal*84/75.0);
    }


    public void kill() {
        tree.setImage("../pictures/tree/treeDestroyed.png");
        tree.setSize(scaleLocal, (scaleLocal*93/102.0));
        tree.setLocation(-scaleLocal*0.05, -scaleLocal*75/84.0);
        PlaneShooter.tree = null;
    }
}
