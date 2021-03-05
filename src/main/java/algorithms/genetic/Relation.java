package algorithms.genetic;

public class Relation {
    private int a, b;
    private boolean isVisited1, isVisited2;

    public Relation(int a, int b) {
        this.a = a;
        this.b = b;
        this.isVisited1 = false;
        this.isVisited2 = false;
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    private void setter(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public boolean setRelation(int buf1, int buf2) {
        if (buf1 == a) {
            setter(buf2, b);
            //r.setB(b);
            //r.setA(buf2);
            return true;
        }
        if (buf1 == b) {
            setter(buf2, a);
//            r.setB(a);
//            r.setA(buf2);
            return true;
        }
        if (buf2 == a) {
            setter(buf1, b);
//            r.setB(b);
//            r.setA(buf1);
            return true;
        }
        if (buf2 == b) {
            setter(buf1, a);
//            r.setB(a);
//            r.setA(buf1);
            return true;
        }
        return false;
    }

    public boolean isVisited1() {
        return this.isVisited1;
    }

    public boolean isVisited2() {
        return this.isVisited2;
    }

    public void setVisited1() {
        this.isVisited1 = true;
    }

    public void setVisited2() {
        this.isVisited2 = true;
    }
}
