package algorithms.tabusearch.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TabuCoords {
    private int row;
    private int col;

    public TabuCoords(int row, int col, NeighborhoodStrategy neighborhoodStrategy) {
        switch (neighborhoodStrategy) {
            case REPLACE_CITIES:
                int lower = Math.min(row, col);
                int higher = Math.max(row, col);
                this.row = lower - 1;
                this.col = higher - 1;
                break;
            case PUT_CITY_TO_ANOTHER_VEHICLE:
                this.row = row;
                this.col = col - 1;
                break;
        }
    }

    public boolean isSameCoords(TabuCoords coords) {
        return this.row == coords.getRow() && this.col == coords.getCol();
    }

    @Override
    public String toString() {
        return "[" + row + ", " + col + "]";
    }
}
