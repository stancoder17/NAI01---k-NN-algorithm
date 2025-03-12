public class Main {
    public static void main(String[] args) {
        DataTraining dataTraining = new DataTraining(5, "iris");
        dataTraining.train();

        // User input fragment of DataTraining class should be commented out before launching the code below
//        ChartData chartData = new ChartData();
//        chartData.createData("wdbc");
    }
}
