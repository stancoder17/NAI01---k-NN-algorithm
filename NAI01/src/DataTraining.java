import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DataTraining {
    private int dimensionsNumber = 0;
    private boolean dimensionsFound = false;
    private final int k;
    private final String dataChosen;
    private double accuracy;


    public DataTraining(int k, String dataChosen) {
        this.k = k;
        this.dataChosen = dataChosen;
    }

    public void train() {
        int correctCount = 0;
        int incorrectCount = 0;

        Scanner scanner = new Scanner(System.in);

        // Store in a list for a future user input use
        List<String> trainingData = new ArrayList<>();

        File testFile = new File("./" + dataChosen + ".test.data");
        try {
            BufferedReader in_test = new BufferedReader(new FileReader(testFile));
            String test_record;
            while ((test_record = in_test.readLine()) != null) {
                String[] test_record_split = test_record.split(",");
                String test_decisionAttribute = test_record_split[test_record_split.length - 1];

                if (!dimensionsFound) {
                    dimensionsNumber = test_record_split.length - 1;
                    dimensionsFound = true;
                }

                Map<String, Double> distances = new LinkedHashMap<>();

                File trainFile = new File("./" + dataChosen + ".train.data");
                BufferedReader in_train = new BufferedReader(new FileReader(trainFile));
                String line_train;
                while ((line_train = in_train.readLine()) != null) {
                    trainingData.add(line_train);
                    Double distance = calculateDistance(line_train, test_record);
                    distances.put(line_train, distance);
                }

                in_train.close();

                distances = sortAndLimitMap(distances, k);
                String train_attribute_mostCount = extractMostCountedAttribute(distances);

                if (train_attribute_mostCount.equals(test_decisionAttribute))
                    correctCount++;
                else
                    incorrectCount++;
            }

            in_test.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        accuracy = ((double) correctCount / (correctCount + incorrectCount));

        // Display statistics
        System.out.println("Statistics for " + dataChosen + ".test.data: ");
        System.out.println("Correct count: " + correctCount);
        System.out.println("Incorrect count: " + incorrectCount);
        System.out.printf("Accuracy: %.2f%%\n", accuracy * 100.0);


        System.out.println("==================================================================");
        // User input
        System.out.print("Input a " + dimensionsNumber + "-dimensional vector to test (x,y,z...,n) or say \"quit\" to exit the program: ");
        String userInput = scanner.nextLine();

        while (!userInput.equals("quit")) {
            Map<String, Double> distances = new LinkedHashMap<>();
            try {
                if (userInput.split(",").length != dimensionsNumber)
                    throw new IllegalArgumentException();

                for (String trainingRecord : trainingData) {
                    Double distance = calculateDistance(trainingRecord, userInput);
                    distances.put(trainingRecord, distance);
                }
            } catch (IllegalArgumentException e) {
                System.out.print("Incorrect input. Please provide a " + dimensionsNumber + "-dimensional vector (x,y,z...,n) or say \"quit\" to exit the program: ");
                userInput = scanner.nextLine();
                continue;
            }
            distances = sortAndLimitMap(distances, k);
            String train_attribute_mostCount = extractMostCountedAttribute(distances);

            System.out.println("Etiquette for inputted vector: " + train_attribute_mostCount);

            System.out.print("Input another vector or say \"quit\" to exit the program: ");
            userInput = scanner.nextLine();
        }
    }

    private Double calculateDistance(String object1, String object2) throws IllegalArgumentException {
        String[] object1_allElements = object1.split(",");
        List<Double> object1_dimensions = new ArrayList<>();
        for (int i = 0; i < dimensionsNumber; i++)
            object1_dimensions.add(Double.parseDouble(object1_allElements[i]));

        String[] object2_allElements = object2.split(",");
        List<Double> object2_dimensions = new ArrayList<>();
        for (int i = 0; i < dimensionsNumber ; i++) {
            object2_dimensions.add(Double.parseDouble(object2_allElements[i]));
        }

        // Sum of (x1 - x2)^2
        double sum = 0.0;
        for (int i = 0; i < dimensionsNumber; i++)
            sum += Math.pow(object2_dimensions.get(i) - object1_dimensions.get(i), 2);

        return Math.sqrt(sum);
    }

    private Map<String, Double> sortAndLimitMap(Map<String, Double> map, int limit) {
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, _) -> e1, LinkedHashMap::new
                ));
    }

    private String extractMostCountedAttribute(Map<String, Double> distances) {
        Map<String, Integer> counts = new HashMap<>();

        for (Map.Entry<String, Double> entry : distances.entrySet()) {
            if (counts.containsKey(entry.getKey()))
                counts.put(entry.getKey(), counts.get(entry.getKey()) + 1);
            else
                counts.put(entry.getKey(), 1);
        }

        String[] record_mostCount = Collections.max(counts.entrySet(), Map.Entry.comparingByValue()).getKey().split(",");

        return record_mostCount[record_mostCount.length - 1];
    }

    public double getAccuracy() {
        return accuracy;
    }
}