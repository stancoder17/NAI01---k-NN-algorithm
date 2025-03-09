import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        int k = 5; // normalnie Integer.parseInt(args[0])
        Map<String, Double> distances = new LinkedHashMap<>();
        String test_record = "5.0,3.2,1.2,0.2,Iris-setosa";
        String[] test_record_split = test_record.split(",");
        String test_attribute = test_record_split[test_record_split.length - 1];
        List<Double> testRecordDimensions = new ArrayList<>();
        int dimensionsNumber;

        for (int i = 0; true; i++) {
            try {
                testRecordDimensions.add(Double.parseDouble(test_record.split(",")[i]));
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
        distances = distances.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(k)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new
                ));

        Map<String, Integer> counts = new HashMap<>();

        for (Map.Entry<String, Double> entry : distances.entrySet()) {
            if (counts.containsKey(entry.getKey()))
                counts.put(entry.getKey(), counts.get(entry.getKey()) + 1);
            else
                counts.put(entry.getKey(), 1);
        }

        String[] train_record_mostCount = Collections.max(counts.entrySet(), Map.Entry.comparingByValue()).getKey().split(",");
        String train_attribute_mostCount = train_record_mostCount[train_record_mostCount.length - 1];
    }
}
