package comp;

public class Int {
    private int i = 0;

    public synchronized void add() {
        i++;
    }

    @Override
    public String toString() {
        return i + "";
    }
}
