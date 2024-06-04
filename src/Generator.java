import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Generator {
    public static final int LENGTH = 20;

    public static void generate() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("input.txt", false));
        Random random = new Random();
        for (int k = 1; k <= 3; k++) {
            StringBuilder toSave = new StringBuilder();
            for (int i = 0; i < LENGTH * 3; i++) {
                for (int l = 0; l < LENGTH * 3; l++) {
                    toSave.append(Math.pow(random.nextDouble(500), k * 2)).append(",");
                }
                toSave.deleteCharAt(toSave.length() - 1);
                toSave.append(";");
            }
            toSave.deleteCharAt(toSave.length() - 1);
            bufferedWriter.write(toSave.toString());
            bufferedWriter.write(System.lineSeparator());
        }

        for (int k = 1; k <= 2; k++) {
            StringBuilder toSave = new StringBuilder();
            for (int i = 0; i < LENGTH * 10; i++) {
                toSave.append(Math.pow(random.nextDouble(500), k)).append(",");
            }
            toSave.deleteCharAt(toSave.length() - 1);
            bufferedWriter.write(toSave.toString());
            bufferedWriter.write(System.lineSeparator());
        }
        bufferedWriter.flush();
    }
}
