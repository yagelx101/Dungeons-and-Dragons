package Game.Utils;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double range(Position other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    public Position up() {
        return new Position(getX(), getY() - 1);
    }

    public Position down() {
        return new Position(getX(), getY() + 1);
    }

    public Position left() {
        return new Position(getX() - 1, getY());
    }

    public Position right() {
        return new Position(getX() + 1, getY());
    }

    public Position stay() {
        return this;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position position = (Position) obj;
            return position.getX() == this.getX() && position.getY() == this.getY();
        }
        return false;
    }
}
