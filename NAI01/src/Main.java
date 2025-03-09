import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        int k = 5; // normalnie Integer.parseInt(args[0])
        Map<String, Double> distances = new HashMap<>();
        String testRecord = "5.0,3.2,1.2,0.2,Iris-setosa";
        List<Double> testRecordDimensions = new ArrayList<>();
        int dimensionsNumber;

        for (int i = 0; true; i++) {
            try {
                testRecordDimensions.add(Double.parseDouble(testRecord.split(",")[i]));
            } catch (Exception e) {
                dimensionsNumber = i;
                break;
            }
        }

        File file = new File("./iris.train.data");
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;
            while ((line = in.readLine()) != null) {
                String[] lineAllElements = line.split(",");
                List<Double> lineDimensions = new ArrayList<>();

                for (int i = 0; i < dimensionsNumber; i++)
                    lineDimensions.add(Double.parseDouble(lineAllElements[i]));

                double sum = 0.0;

                // Getting sum of (x1 - x2)^2
                for (int i = 0; i < dimensionsNumber; i++)
                    sum += Math.pow(testRecordDimensions.get(i) - lineDimensions.get(i), 2);

                Double result = Math.sqrt(sum);

                distances.put(line, result);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
