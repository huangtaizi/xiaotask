package wang.tai.sun.xiaotask.model;

public class Point {
    public float x;
    public float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point point = (Point) obj;
            if (point.x == x && point.y == y) {
                return true;
            }
        }
        return false;
    }
}
