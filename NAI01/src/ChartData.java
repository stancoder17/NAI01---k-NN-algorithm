import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ChartData {
    public void createData(String dataType) {
        long linesCount;

        try {
            linesCount = Files.lines(Path.of("./" + dataType + ".test.data")).count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            BufferedWriter writer = Files.newBufferedWriter(Path.of("./" + dataType + ".chartData.txt"), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
            for (int k = 1; k < linesCount; k++) {
                DataTraining instance = new DataTraining(k, dataType);
                instance.train();
                writer.write(String.format("%.2f%%", instance.getAccuracy() * 100.0));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
