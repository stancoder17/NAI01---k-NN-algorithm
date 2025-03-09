import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        int k = 35; // normalnie Integer.parseInt(args[0])
        int correctCount = 0;
        int incorrectCount = 0;

        File testFile = new File("./iris.test.data");
        try {
            BufferedReader in_test = new BufferedReader(new FileReader(testFile));
            String test_record;
            while ((test_record = in_test.readLine()) != null) {
                String[] test_record_split = test_record.split(",");
                String test_attribute = test_record_split[test_record_split.length - 1];

                Map<String, Double> distances = new LinkedHashMap<>();
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

                File trainFile = new File("./iris.train.data");
                try {
                    BufferedReader in_train = new BufferedReader(new FileReader(trainFile));
                    String line_train;
                    while ((line_train = in_train.readLine()) != null) {
                        String[] lineAllElements = line_train.split(",");
                        List<Double> lineDimensions = new ArrayList<>();

                        for (int i = 0; i < dimensionsNumber; i++)
                            lineDimensions.add(Double.parseDouble(lineAllElements[i]));

                        double sum = 0.0;

                        // Getting sum of (x1 - x2)^2
                        for (int i = 0; i < dimensionsNumber; i++)
                            sum += Math.pow(testRecordDimensions.get(i) - lineDimensions.get(i), 2);

                        Double result = Math.sqrt(sum);

                        distances.put(line_train, result);
                    }

                    in_train.close();
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

                if (train_attribute_mostCount.equals(test_attribute))
                    correctCount++;
                else
                    incorrectCount++;

            }

            in_test.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        double accuracy = 100.0 - ((double) incorrectCount / (correctCount + incorrectCount));
        System.out.println(correctCount);
        System.out.println(incorrectCount);
        System.out.println(accuracy + "%");

    }
}