package Model;


import javafx.scene.image.Image;

public class Hero implements Unit {
    /**
     * The current position of the creep
     */
    private final double x;
    private final double y;

    /**
     * The position of the creep after movement
     */
    private final double final_x;
    private final double final_y;

    private final HeroType type;


    public Hero(double x, double y, double final_x, double final_y, HeroType type) {
        this.x = x;
        this.y = y;
        this.final_x = final_x;
        this.final_y = final_y;
        this.type = type;
    }

    @Override
    public String toString() {
        return x + "," +
                y + "," +
                final_x + "," +
                final_y + "," +
                type;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getFinal_x() {
        return final_x;
    }

    @Override
    public double getFinal_y() {
        return final_y;
    }



    public HeroType getType() {
        return type;
    }

    public static Hero parse(String s) {
        int first = s.indexOf(',');
        int second = s.indexOf(',', first + 1);
        int third = s.indexOf(',', second + 1);
        int fourth = s.indexOf(',', third + 1);

        double x = Double.parseDouble(s.substring(0, first));
        double y = Double.parseDouble(s.substring(first + 1, second));
        double final_x = Double.parseDouble(s.substring(second + 1, third));
        double final_y = Double.parseDouble(s.substring(third + 1, fourth));

        HeroType type = HeroType.valueOf(s.substring(fourth + 1));

        return new Hero(x, y, final_x, final_y, type);
    }

    public Image getImage() {
        return null;
    }
}
