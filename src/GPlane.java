import acm.graphics.*;

import java.awt.*;


public class GPlane extends GCompound {
    // ===== COLOR PALETTE ======
    Color bodyColor = new Color(74, 76, 59);
    Color wingColor = new Color(64, 66, 50);
    Color headColor = new Color(28, 169, 174);

    GPolygon bodyRight;
    GPolygon wingRight;
    GPolygon wingBackRight;
    GPolygon headRight;

    GPolygon bodyLeft;
    GPolygon wingLeft;
    GPolygon wingBackLeft;
    GPolygon headLeft;

    GImage image;

    int scaleLocalX = 0;
    int scaleLocalY = 0;

    public GPlane(int scaleX, int scaleY) {
        scaleLocalX = scaleX;
        scaleLocalY = scaleY;


        /** ===== RIGHT FORWARDING ===== */
        wingBackRight = new GPolygon();
        wingBackRight.addVertex(scaleLocalX*0.4, scaleLocalY*0.6);
        wingBackRight.addVertex(scaleLocalX*0.2, scaleLocalY*0.0);
        wingBackRight.addVertex(scaleLocalX*0.7, scaleLocalY*0.6);
        wingBackRight.setFilled(true);
        wingBackRight.setColor(Color.black);
        wingBackRight.setFillColor(wingColor);
        add(wingBackRight, scaleX*0.0, scaleY*0.0);

        bodyRight = new GPolygon();
        bodyRight.addVertex(scaleLocalX*0.05, scaleLocalY*0.1);
        bodyRight.addVertex(scaleLocalX*0.05, scaleLocalY*0.5);
        bodyRight.addVertex(scaleLocalX*0.0, scaleLocalY*0.65);
        bodyRight.addVertex(scaleLocalX*0.2, scaleLocalY*0.8);
        bodyRight.addVertex(scaleLocalX*0.9, scaleLocalY*0.8);
        bodyRight.addVertex(scaleLocalX*0.95, scaleLocalY*0.75);
        bodyRight.addVertex(scaleLocalX*1.00, scaleLocalY*0.7);
        bodyRight.addVertex(scaleLocalX*1.00, scaleLocalY*0.6);
        bodyRight.addVertex(scaleLocalX*0.95, scaleLocalY*0.55);
        bodyRight.addVertex(scaleLocalX*0.85, scaleLocalY*0.4);
        bodyRight.addVertex(scaleLocalX*0.3, scaleLocalY*0.4);
        bodyRight.setFilled(true);
        bodyRight.setColor(Color.black);
        bodyRight.setFillColor(bodyColor);
        add(bodyRight, scaleX*0.0, scaleY*0.0);

        wingRight = new GPolygon();
        wingRight.addVertex(scaleLocalX*0.4, scaleLocalY*0.6);
        wingRight.addVertex(scaleLocalX*0.2, scaleLocalY*1.0);
        wingRight.addVertex(scaleLocalX*0.7, scaleLocalY*0.6);
        wingRight.setFilled(true);
        wingRight.setColor(Color.black);
        wingRight.setFillColor(wingColor);
        add(wingRight, scaleX*0.0, scaleY*0.0);

        headRight = new GPolygon();
        headRight.addVertex(scaleLocalX*0.78, scaleLocalY*0.45);
        headRight.addVertex(scaleLocalX*0.85, scaleLocalY*0.45);
        headRight.addVertex(scaleLocalX*0.95, scaleLocalY*0.6);
        headRight.addVertex(scaleLocalX*0.95, scaleLocalY*0.7);
        headRight.addVertex(scaleLocalX*0.78, scaleLocalY*0.7);
        headRight.setFilled(true);
        headRight.setColor(Color.black);
        headRight.setFillColor(headColor);
        add(headRight, scaleX*0.0, scaleY*0.0);


        /** ===== LEFT FORWARDING ===== */
        wingBackLeft = new GPolygon();
        wingBackLeft.addVertex(scaleLocalX-scaleLocalX*0.4, scaleLocalY*0.6);
        wingBackLeft.addVertex(scaleLocalX-scaleLocalX*0.2, scaleLocalY*0.0);
        wingBackLeft.addVertex(scaleLocalX-scaleLocalX*0.7, scaleLocalY*0.6);
        wingBackLeft.setFilled(true);
        wingBackLeft.setColor(Color.black);
        wingBackLeft.setFillColor(wingColor);
        add(wingBackLeft, scaleX*0.0, scaleY*0.0);

        bodyLeft = new GPolygon();
        bodyLeft.addVertex(scaleLocalX-scaleLocalX*0.05, scaleLocalY*0.1);
        bodyLeft.addVertex(scaleLocalX-scaleLocalX*0.05, scaleLocalY*0.5);
        bodyLeft.addVertex(scaleLocalX-scaleLocalX*0.0, scaleLocalY*0.65);
        bodyLeft.addVertex(scaleLocalX-scaleLocalX*0.2, scaleLocalY*0.8);
        bodyLeft.addVertex(scaleLocalX-scaleLocalX*0.9, scaleLocalY*0.8);
        bodyLeft.addVertex(scaleLocalX-scaleLocalX*0.95, scaleLocalY*0.75);
        bodyLeft.addVertex(scaleLocalX-scaleLocalX*1.00, scaleLocalY*0.7);
        bodyLeft.addVertex(scaleLocalX-scaleLocalX*1.00, scaleLocalY*0.6);
        bodyLeft.addVertex(scaleLocalX-scaleLocalX*0.95, scaleLocalY*0.55);
        bodyLeft.addVertex(scaleLocalX-scaleLocalX*0.85, scaleLocalY*0.4);
        bodyLeft.addVertex(scaleLocalX-scaleLocalX*0.3, scaleLocalY*0.4);
        bodyLeft.setFilled(true);
        bodyLeft.setColor(Color.black);
        bodyLeft.setFillColor(bodyColor);
        add(bodyLeft, scaleX*0.0, scaleY*0.0);

        wingLeft = new GPolygon();
        wingLeft.addVertex(scaleLocalX-scaleLocalX*0.4, scaleLocalY*0.6);
        wingLeft.addVertex(scaleLocalX-scaleLocalX*0.2, scaleLocalY*1.0);
        wingLeft.addVertex(scaleLocalX-scaleLocalX*0.7, scaleLocalY*0.6);
        wingLeft.setFilled(true);
        wingLeft.setColor(Color.black);
        wingLeft.setFillColor(wingColor);
        add(wingLeft, scaleX*0.0, scaleY*0.0);

        headLeft = new GPolygon();
        headLeft.addVertex(scaleLocalX-scaleLocalX*0.78, scaleLocalY*0.45);
        headLeft.addVertex(scaleLocalX-scaleLocalX*0.85, scaleLocalY*0.45);
        headLeft.addVertex(scaleLocalX-scaleLocalX*0.95, scaleLocalY*0.6);
        headLeft.addVertex(scaleLocalX-scaleLocalX*0.95, scaleLocalY*0.7);
        headLeft.addVertex(scaleLocalX-scaleLocalX*0.78, scaleLocalY*0.7);
        headLeft.setFilled(true);
        headLeft.setColor(Color.black);
        headLeft.setFillColor(headColor);
        add(headLeft, scaleX*0.0, scaleY*0.0);

        // turn visibility of right forwarding off
        bodyRight.setVisible(false);
        wingRight.setVisible(false);
        wingBackRight.setVisible(false);
        headRight.setVisible(false);
    }

    public void turnAround(boolean toLeft) {
        if (toLeft) {
            bodyLeft.setVisible(true);
            wingLeft.setVisible(true);
            wingBackLeft.setVisible(true);
            headLeft.setVisible(true);

            bodyRight.setVisible(false);
            wingRight.setVisible(false);
            wingBackRight.setVisible(false);
            headRight.setVisible(false);
        }
        else{
            bodyRight.setVisible(true);
            wingRight.setVisible(true);
            wingBackRight.setVisible(true);
            headRight.setVisible(true);

            bodyLeft.setVisible(false);
            wingLeft.setVisible(false);
            wingBackLeft.setVisible(false);
            headLeft.setVisible(false);
        }
    }

    public void kill(){
        removeAll();
        image = new GImage("../pictures/plane/explosion.png");
        image.setSize(14*scaleLocalX, 14*scaleLocalX);
        add(image, -7*scaleLocalX, -7*scaleLocalX);
        pause(1000);
        PlaneShooter.plane = null;
    }
}
