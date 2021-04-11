package algorithms.genetic.geneticoperations;

import algorithms.genetic.model.Relation;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@NoArgsConstructor
public class PartialMappedCrossover {

    public void start(int leftCutPoints, int rightCutPoints, Integer[] i1, Integer[] i2) {

        List<Relation> relations = new ArrayList<>();
        for (int i = leftCutPoints; i < rightCutPoints; i++) {
            int buf1 = i1[i];
            int buf2 = i2[i];
            i1[i] = buf2;
            i2[i] = buf1;

            relations.add(new Relation(buf1, buf2));
        }

        analyzeRelation(relations);
        for (int i = 0; i < leftCutPoints; i++) {
            makeRotation(i1, i2, relations, i);
        }
        for (int i = rightCutPoints; i < i1.length; i++) {
            makeRotation(i1, i2, relations, i);
        }
    }

    private void analyzeRelation(List<Relation> relations) {
        Iterator<Relation> iterator = relations.listIterator();
        while (iterator.hasNext()) {
            Relation analyzeRelation = iterator.next();
            Relation oppositeRelation = findOppositeRelation(analyzeRelation, relations);
            if (oppositeRelation != null) {
                if (analyzeRelation.getA() == oppositeRelation.getB()) {
                    oppositeRelation.setB(analyzeRelation.getB());
                }
                if (analyzeRelation.getB() == oppositeRelation.getA()) {
                    oppositeRelation.setA(analyzeRelation.getA());
                }
                iterator.remove();
            }
        }
    }

    private Relation findOppositeRelation(Relation analyzeRelation, List<Relation> relations) {
        for (Relation relation : relations) {
            if (analyzeRelation.getA() == relation.getB() || analyzeRelation.getB() == relation.getA())
                return relation;
        }
        return null;
    }

    private void makeRotation(Integer[] i1, Integer[] i2, List<Relation> relations, int i) {
        Relation relation = findRelation(relations, i1[i]);
        if (relation != null)
            i1[i] = i1[i] == relation.getA() ? relation.getB() : relation.getA();
        relation = findRelation(relations, i2[i]);
        if (relation != null)
            i2[i] = i2[i] == relation.getA() ? relation.getB() : relation.getA();
    }


    private Relation findRelation(List<Relation> relations, Integer num) {
        for (Relation relation : relations) {
            if (relation.getA() == num || relation.getB() == num) {
                return relation;
            }
        }
        return null;
    }
}
