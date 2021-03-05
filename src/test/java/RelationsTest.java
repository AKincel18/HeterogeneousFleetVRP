import algorithms.genetic.Relation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RelationsTest {
    public void test1() {
        //        int leftRange = 3;
//        int rightRange = 6;
//        Integer[] i1 = new Integer[] {3, 4, 8, 2, 7, 1, 6, 5};
//        Integer[] i2 = new Integer[] {4, 2, 5, 1, 6, 8, 3, 7};

        int leftRange = 1;
        int rightRange = 4;
        Integer[] i1 = new Integer[] {1, 2, 4, 6, 0};
        Integer[] i2 = new Integer[] {2, 4, 6, 1, 0};

        System.out.println("Before");
        System.out.println(Arrays.toString(i1));
        System.out.println(Arrays.toString(i2));
        List<Relation> relations = new ArrayList<>();
        for (int i = leftRange; i < rightRange; i++) {
            int buf1 = i1[i];
            int buf2 = i2[i];
            i1[i] = buf2;
            i2[i] = buf1;
            addRelation(relations, buf1, buf2);
        }

        for (int i = 0; i < leftRange; i++) {
            Relation relation = findRelation(relations, i1[i]);
            if (relation != null)
                i1[i] = i1[i] == relation.getA() ? relation.getB() : relation.getA();
            relation = findRelation(relations, i2[i]);
            if (relation != null)
                i2[i] = i2[i] == relation.getA() ? relation.getB() : relation.getA();
        }
        for (int i = rightRange; i < i1.length; i++) {
            Relation relation = findRelation(relations, i1[i]);
            if (relation != null)
                i1[i] = i1[i] == relation.getA() ? relation.getB() : relation.getA();
            relation = findRelation(relations, i2[i]);
            if (relation != null)
                i2[i] = i2[i] == relation.getA() ? relation.getB() : relation.getA();
        }
        System.out.println("After");
        System.out.println(Arrays.toString(i1));
        System.out.println(Arrays.toString(i2));
    }

    private static Relation findRelation(List<Relation> relations, Integer num) {
        for (Relation relation : relations) {
            if (relation.getA() == num || relation.getB() == num) {
                return relation;
            }
        }
        return null;
    }

    private static void addRelation(List<Relation> relations, int buf1, int buf2) {
        for (Relation r : relations) {
            boolean isSet = r.setRelation(buf1, buf2);
            if (isSet) {
                return;
            }
        }
        relations.add(new Relation(buf1, buf2));

    }
}
