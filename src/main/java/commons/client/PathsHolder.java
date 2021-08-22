package commons.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PathsHolder {
    private static PathsHolder INSTANCE;
    private String sheet;
    private String inputPath;
    private String outputPath;
    private String outputFile;

    public static PathsHolder getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PathsHolder();
        }
        return INSTANCE;
    }

}
