package Model;

/**
 * The Building class is used to store information of building and used to show on  map
 */
public class Building {
    private final int number;
    private final double hp;

    public Building(int number, double hp) {
        this.number = number;
        this.hp = hp;
    }

    public int getNumber() {
        return number;
    }

    public double getHp() {
        return hp;
    }

    @Override
    public String toString() {
        return number + "," + hp;
    }

    /**
     *
     * @param s Info of the building received form server
     * @return An instance of Building with info received from server
     */
    public static Building parse(String s) {
        int div = s.indexOf(',');
        int number = Integer.parseInt(s.substring(0,div));
        double hp = Double.parseDouble(s.substring(div + 1));
        return new Building(number, hp);
    }
}
