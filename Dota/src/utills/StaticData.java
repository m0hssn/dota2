package utills;

import connection.Handler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;

/**
 * The variables in StaticData class are common variables in this app
 */
public class StaticData {
    public static AnchorPane map;
    public static GraphicsContext gc;
    public static Handler handler;
    public static final double SCALE = 3;
    public static HeroManagement management;
    public static ProgressBar hpHero;
    public static ProgressBar manaHero;

    public static ToggleButton power1;
    public static ToggleButton power2;
    public static ToggleButton power3;

    public static Button levelup;

    public static Label label;
    public static Label nameTag;

    public static boolean gameOver;
    public static String winner;
}
