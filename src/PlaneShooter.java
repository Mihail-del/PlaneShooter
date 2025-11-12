/** Task Two
  Auth: Pulov MYkhailo
  написати гру в якій літак має збивати цілі, що розміщені на землі
 * Вимоги: цілі - рухомі і нерухомі,
 * розміщення цілей генерується,
 * літак - зображення літака несиметричне,
 * літак летить в одну сторону, долітає до краю вікна і розвертається - летить в іншу сторону,
 * літак при зіткненні з ціллю або землею взривається;
 * додати візуальні та аудіо ефекти.
 */

import acm.graphics.*;
import acm.program.GraphicsProgram;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.MouseEvent;

import java.applet.*;
import java.io.File;

public class PlaneShooter extends GraphicsProgram {
    /** ===== CONSTANTS ===== */
    // Board settings
    private static final int BOARD_WIDTH = 1000;
    private static final int BOARD_HEIGHT = 700;

    // Plane settings
    private static final int PLANE_SPEED = 3;
    private static final int PLANE_WIDTH = 150;
    private static final int PLANE_HEIGHT = PLANE_WIDTH/2;

    // Target settings
    private static final int SOLDIER_SPEED = 2;
    private static final int SOLDIER_WIDTH = 70;
    private static final int BAR_WIDTH = 350;
    private static final int ARTILLERY_WIDTH = 200;
    private static final int TREE_WIDTH = 300;

    // Size and speed of bombs
    private static final int BOMB_SPEED = 5;
    private static final int BOMB_DIAM = 20;

    // Animation cycle delay
    private static final int DELAY = 10;

    // sounds
    private static final int SYSTEM_VOLUME = 70;        // from 0 to 100
    private static final int EFFECTS_VOLUME = 70;       // from 0 to 100

    /** ===== VARIABLES ====== */
    public static boolean gameOverVar = false;

    public static GPlane plane;
    public static GBomb bomb;
    public static GSoldier soldierOne;
    public static GSoldier soldierTwo;
    public static GSoldier soldierThree;
    public static GBar bar;
    public static GArtillery artillery;
    public static GTree tree;

    private boolean planeToLeft;
    private boolean soldierOneToLeft;
    private boolean soldierTwoToLeft;
    private boolean soldierThreeToLeft;

    double planeLocationX = 0;
    double planeLocationY = 0;

    private boolean running = false;
    private boolean waitingRestart = false;
    long restartCooldownUntil = 0L;


    /** ===== RUN ===== */
    public void run() {
        loadSounds();
        addMouseListeners();
        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        while (true) {
            startGame();
            gameLoop();
            gameOverScreen();
            waitForRestart();
        }
    }

    /** ===== GETTING READY FOR THE GAME ===== */
    private void startGame() {
        running = true;
        gameOverVar = false;
        waitingRestart = false;

        bomb = null;
        soldierOne = null;
        soldierTwo = null;
        soldierThree = null;
        plane = null;
        bar = null;
        artillery = null;
        tree = null;

        soldierOneToLeft = false;
        soldierTwoToLeft = false;
        soldierThreeToLeft = false;

        removeAll();
        configScene();

        restartCooldownUntil = System.currentTimeMillis() + 200;
    }


    /** ===== GAMING PROCESS ===== */
    private void gameLoop() {
        while (running) {
            movePlane();
            moveTargets();
            moveBomb();
            bombOffScreen();
            collideWithTarget();
            gameOver();

            if (gameOverVar) {
                running = false;
            }
            pause(DELAY);
        }
    }


    /** ===== SCENE CONFIGURATION ===== */
    private void configScene() {
        SoundManager.loadFromResource("bgMusic", "/sounds/bgMusic.wav");
        SoundManager.loop("bgMusic");
        SoundManager.setVolumeDb("bgMusic", SYSTEM_VOLUME/2 - 44);

        plane = new GPlane(PLANE_WIDTH, PLANE_HEIGHT);
        add(plane, getHeight(), 0);

        // Background
        setBackground(new Color(36, 76, 121));

        // Ground
        GImage groung = new GImage("../pictures/ground.png");
        groung.setSize(getWidth(), getWidth()*894.0/1536.0);
        add(groung, 0, getHeight()-groung.getHeight()*0.2);

        // Plane starts at top right
        planeToLeft = true;
        addMouseListeners();

        // Soldiers
        soldierOne = new GSoldier(SOLDIER_WIDTH);
        add(soldierOne, 0, getHeight()*0.76);

        soldierTwo = new GSoldier(SOLDIER_WIDTH);
        add(soldierTwo, getWidth()*0.6, getHeight()*0.78);

        soldierThree = new GSoldier(SOLDIER_WIDTH);
        add(soldierThree, getWidth()*0.9, getHeight()*0.8);

        bar = new GBar(BAR_WIDTH);
        add(bar, getWidth()*0.75, getHeight()*0.95);

        artillery = new GArtillery(ARTILLERY_WIDTH);
        add(artillery, getWidth()*0.05, getHeight()*0.95);

        tree = new GTree(TREE_WIDTH);
        add(tree, -getWidth()*0.1, getHeight()*0.99);
    }

    /** ===== REACTION TO CLICKED MOUSE ===== */
    public void mousePressed(MouseEvent e) {
        if (waitingRestart) {
            if (e.getX()>=getWidth()*0.38 && e.getX()<=getWidth()*0.62){
                if (e.getY()>=getHeight()*0.6 && e.getY()<=getHeight()*0.7){
                    removeAll();
                    SoundManager.stop("win");
                    SoundManager.stop("lose");
                    waitingRestart = false;
                    gameOverVar = false;
                    bomb = null;
                    pause(DELAY);
                    restartCooldownUntil = System.currentTimeMillis() + 200;
                    return;
                }
                else if (e.getY()>=getHeight()*0.75 && e.getY()<=getHeight()*0.85){
                    System.out.println("Exiting");
                    exit();
                }
            }
        }

        if (System.currentTimeMillis() < restartCooldownUntil) return;

        if (!gameOverVar) {
            if (bomb == null) {
                bomb = new GBomb(BOMB_DIAM);
                add(bomb, planeLocationX + PLANE_WIDTH / 2, planeLocationY + PLANE_HEIGHT);
                SoundManager.play("drop");
            }
        }
    }

    /** ==== MOVING PLANE WItH PROVIDING DATA */
    private void movePlane() {
        if (planeToLeft) {
            plane.move(-PLANE_SPEED, 0.22);
            if (plane.getX() <= 0) {
                planeToLeft = false;
                plane.turnAround(false);
            }
        } else {
            plane.move(PLANE_SPEED, 0.22);
            if (plane.getX() >= getWidth() - PLANE_WIDTH) {
                planeToLeft = true;
                plane.turnAround(true);
            }
        }

        planeLocationX = plane.getLocation().getX();
        planeLocationY = plane.getLocation().getY();
    }

    /** ===== CHECKS IF PLANE ALIVE OR NO TARGETS LEFT ====== */
    private void gameOver() {
        // plane collisions check
        boolean planeCollides = (planeLocationY >= getHeight()*0.75 - PLANE_HEIGHT*0.2) ||
                (planeLocationY >= getHeight()*0.85 - BAR_WIDTH*793/1155.0 && planeLocationX>=getWidth()*0.75) ||
                (planeLocationY >= getHeight()*0.9 - TREE_WIDTH*84/75.0 && planeLocationX<=TREE_WIDTH*3/5.0-getWidth()*0.05);
        if (bar == null && tree == null)
            planeCollides = (planeLocationY >= getHeight()*0.75 - PLANE_HEIGHT*0.2);
        if (bar == null && tree != null)
            planeCollides = (planeLocationY >= getHeight()*0.75 - PLANE_HEIGHT*0.2) ||
                    (planeLocationY >= getHeight()*0.9 - TREE_WIDTH*84/75.0 && planeLocationX<=TREE_WIDTH*3/5.0-getWidth()*0.05);
        if (bar != null && tree == null)
            planeCollides = (planeLocationY >= getHeight()*0.75 - PLANE_HEIGHT*0.2) ||
                    (planeLocationY >= getHeight()*0.85 - BAR_WIDTH*793/1155.0 && planeLocationX>=getWidth()*0.75);

        // checks enemies
        boolean noEnemy = (soldierOne == null && soldierTwo == null && soldierThree == null
                && bar == null && artillery == null);

        // game over checking
        if (noEnemy || planeCollides) {
            SoundManager.stop("bgMusic");
            SoundManager.play("gameOver");
            if (planeCollides) {
                if (planeLocationX > getWidth())
                    plane.setLocation(getWidth(), planeLocationY);
                if (planeLocationX < 0)
                    plane.setLocation(0, planeLocationY);
                new Thread(() -> plane.kill()).start();
                SoundManager.play("huge");
            }
            System.out.println("Game Over");
            gameOverVar = true;
        }
        else
            gameOverVar = false;
    }

    /** ===== CONTROL ALL TARGET'S MOVING ===== */
    private void moveTargets() {
        moveSoldierOne();
        moveSoldierTwo();
        moveSoldierThree();
    }

    // ===== MOVING SOLDIER ONE =====
    private void moveSoldierOne() {
        if (soldierOne != null) {
            if (soldierOneToLeft) {
                soldierOne.move(-SOLDIER_SPEED, 0);
                if (soldierOne.getX() <= 0) {
                    soldierOneToLeft = false;
                    soldierOne.turnAround(false);
                }
            } else {
                soldierOne.move(SOLDIER_SPEED, 0);
                if (soldierOne.getX() >= getWidth() - SOLDIER_WIDTH) {
                    soldierOneToLeft = true;
                    soldierOne.turnAround(true);
                }
            }
        }
    }

    // ===== MOVING SOLDIER TWO =====
    private void moveSoldierTwo() {
        if (soldierTwo != null) {
            if (soldierTwoToLeft) {
                soldierTwo.move(-SOLDIER_SPEED, 0);
                if (soldierTwo.getX() <= 0) {
                    soldierTwoToLeft = false;
                    soldierTwo.turnAround(false);
                }
            } else {
                soldierTwo.move(SOLDIER_SPEED, 0);
                if (soldierTwo.getX() >= getWidth() - SOLDIER_WIDTH) {
                    soldierTwoToLeft = true;
                    soldierTwo.turnAround(true);
                }
            }
        }
    }

    // ===== MOVING SOLDIER THREE =====
    private void moveSoldierThree() {
        if (soldierThree != null) {
            if (soldierThreeToLeft) {
                soldierThree.move(-SOLDIER_SPEED, 0);
                if (soldierThree.getX() <= 0) {
                    soldierThreeToLeft = false;
                    soldierThree.turnAround(false);
                }
            } else {
                soldierThree.move(SOLDIER_SPEED, 0);
                if (soldierThree.getX() >= getWidth() - SOLDIER_WIDTH) {
                    soldierThreeToLeft = true;
                    soldierThree.turnAround(true);
                }
            }
        }
    }

    /** ===== MOVING THE BOMB ===== */
    private void moveBomb() {
        if (bomb != null) {
            bomb.move(0, BOMB_SPEED);
        }
    }

    /** ===== CHECKING THE BOMB TARGET ===== */
    private void collideWithTarget() {
        if (bomb != null) {
            GRectangle br = bomb.getBounds();

            if (soldierOne != null) {
                GRectangle sr1 = soldierOne.getBounds();
                if (br.intersects(sr1)) {
                    if (bomb != null)
                        new Thread(() -> bomb.burst()).start();
                    new Thread(() -> soldierOne.kill(1)).start();
                    pause(DELAY);
                    bomb = null;
                    SoundManager.stop("drop");
                    SoundManager.play("small");
                    SoundManager.play("kurwa");
                }
            }

            if (soldierTwo != null) {
                GRectangle sr2 = soldierTwo.getBounds();
                if (br.intersects(sr2)) {
                    if (bomb != null)
                        new Thread(() -> bomb.burst()).start();
                    new Thread(() -> soldierTwo.kill(2)).start();
                    pause(DELAY);
                    bomb = null;
                    SoundManager.stop("drop");
                    SoundManager.play("small");
                    SoundManager.play("kurwa");
                }
            }

            if (soldierThree != null) {
                GRectangle sr3 = soldierThree.getBounds();
                if (br.intersects(sr3)) {
                    if (bomb != null)
                        new Thread(() -> bomb.burst()).start();
                    new Thread(() -> soldierThree.kill(3)).start();
                    pause(DELAY);
                    bomb = null;
                    SoundManager.stop("drop");
                    SoundManager.play("small");
                    SoundManager.play("kurwa");
                }
            }

            if (bar != null) {
                GRectangle barBounds = bar.getBounds();
                if (br.intersects(barBounds)) {
                    if (bomb != null)
                        new Thread(() -> bomb.burst()).start();
                    new Thread(() -> bar.kill()).start();
                    pause(DELAY);
                    bomb = null;
                    SoundManager.stop("drop");
                    SoundManager.play("small");
                }
            }

            if (artillery != null) {
                GRectangle artilleryBounds = artillery.getBounds();
                if (br.intersects(artilleryBounds)) {
                    if (bomb != null)
                        new Thread(() -> bomb.burst()).start();
                    new Thread(() -> artillery.kill()).start();
                    pause(DELAY);
                    bomb = null;
                    SoundManager.stop("drop");
                    SoundManager.play("small");
                }
            }

            if (tree != null) {
                GRectangle sr2 = tree.getBounds();
                if (br.intersects(sr2)) {
                    if (bomb != null)
                        new Thread(() -> bomb.burst()).start();
                    new Thread(() -> tree.kill()).start();
                    pause(DELAY);
                    bomb = null;
                    SoundManager.stop("drop");
                    SoundManager.play("small");
                    SoundManager.play("fallingTree");
                }
            }
        }
    }

    /** ====== DELETING BOMB WHEN IT IS OFF THE SCREEN */
    private void bombOffScreen() {
        if (bomb != null) {
            if (bomb.getY() >= getHeight()) {
                remove(bomb);
                bomb = null;
            }
        }
    }


    /** ===== GAME OVER SCREEN ===== */
    private void gameOverScreen() {
        Color fontColor = new Color(255, 255, 255, 255);
        Color BgColor = new Color(87, 18, 18, 255);

        // GAME OVER LABEL
        GImage gameOverImage = new GImage("../pictures/gameOver.png");
        gameOverImage.setSize(getWidth()*0.6, getHeight()*0.6);
        add(gameOverImage, getWidth()*0.2, getHeight()*0.2);
        pause(2000);
        remove(gameOverImage);

        // RESULT:  WIN \ LOSE
        GImage gameResultOverImage;
        if (plane != null) {
            gameResultOverImage = new GImage("../pictures/youWin.png");
            gameResultOverImage.setSize(getWidth()*0.6, getHeight()*0.6);
            add(gameResultOverImage, getWidth()*0.2, getHeight()*0.01);
            SoundManager.play("win");
        }
        else {
            gameResultOverImage = new GImage("../pictures/youLose.png");
            gameResultOverImage.setSize(getWidth()*0.6, getHeight()*0.6);
            add(gameResultOverImage, getWidth()*0.2, getHeight()*0.01);
            SoundManager.play("lose");
        }

        // RESTART BTN
        GRect gameOverRestartBg = new GRect(getWidth()*0.38, getHeight()*0.6, getWidth()*0.24, getHeight()*0.1);
        gameOverRestartBg.setFilled(true);
        gameOverRestartBg.setColor(fontColor);
        gameOverRestartBg.setFillColor(BgColor);
        add(gameOverRestartBg);

        GLabel gameOverRestart = new GLabel("Restart");
        gameOverRestart.setFont("Monospaced-"+(int) Math.round(getWidth()*0.05));
        gameOverRestart.setColor(fontColor);
        add(gameOverRestart, getWidth()*0.395, getHeight()*0.678);

        // EXIT BTN
        GRect gameOverExitBg = new GRect(getWidth()*0.38, getHeight()*0.75, getWidth()*0.24, getHeight()*0.1);
        gameOverExitBg.setFilled(true);
        gameOverExitBg.setColor(fontColor);
        gameOverExitBg.setFillColor(BgColor);
        add(gameOverExitBg);

        GLabel gameOverExit = new GLabel("Exit");
        gameOverExit.setFont("Monospaced-"+(int) Math.round(getWidth()*0.05));
        gameOverExit.setColor(fontColor);
        add(gameOverExit, getWidth()*0.44, getHeight()*0.828);
    }

    /** ===== WAITING FOR THE CLICK TO RESTART ===== */
    private void waitForRestart() {
        waitingRestart = true;
        println("Restarting"); // здесь твоя консольная строка
        while (waitingRestart) {
            pause(20); // yield UI
        }
        // labels will be removed in startGame() via removeAll()
    }



    /** ===== LOAD SOUNDS ===== */
    private void loadSounds(){
        SoundManager.loadFromResource("drop", "/sounds/fallingBomb.wav");
        SoundManager.setVolumeDb("drop", (float) EFFECTS_VOLUME /2 - 44);
        SoundManager.loadFromResource("small", "/sounds/smallBomb.wav");
        SoundManager.setVolumeDb("small", (float) EFFECTS_VOLUME /2 - 44);
        SoundManager.loadFromResource("huge", "/sounds/hugeBomb.wav");
        SoundManager.setVolumeDb("huge", (float) EFFECTS_VOLUME /2 - 44);
        SoundManager.loadFromResource("win", "/sounds/win.wav");
        SoundManager.setVolumeDb("win", (float) SYSTEM_VOLUME /2 - 44);
        SoundManager.loadFromResource("lose", "/sounds/lose.wav");
        SoundManager.setVolumeDb("lose", (float) SYSTEM_VOLUME /2 - 44);
        SoundManager.loadFromResource("fallingTree", "/sounds/fallingTree.wav");
        SoundManager.setVolumeDb("fallingTree", (float) EFFECTS_VOLUME /2 - 44);
        SoundManager.loadFromResource("kurwa", "/sounds/kurwa.wav");
        SoundManager.setVolumeDb("kurwa", (float) EFFECTS_VOLUME /2 - 44);
        SoundManager.loadFromResource("gameOver", "/sounds/gameOver.wav");
        SoundManager.setVolumeDb("gameOver", (float) SYSTEM_VOLUME /2 - 44);
    }
}