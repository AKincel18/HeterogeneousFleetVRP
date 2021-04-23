package commons;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PathsHolder {
    private static PathsHolder INSTANCE;
    private String inputPath;
    private String outputPath;
    private ExampleInputName exampleInputName;

    public static PathsHolder getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PathsHolder();
        }
        return INSTANCE;
    }

}
