import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    static final int LENGTH = Generator.LENGTH;
    static final int NUM_THREADS = Math.min(LENGTH, 16);

    static double[][] MM;
    static double[][] ME;
    static double[][] MZ;
    static double[] B;
    static double[] E;
    public static void main(String[] args) throws IOException {
        Generator.generate();
        MM = initializeMatrix(1);
        ME = initializeMatrix(2);
        MZ = initializeMatrix(3);
        B = initializeVector(4);
        E = initializeVector(5);
        BasicAlgorithm basicAlgorithm = new BasicAlgorithm(B, ME, MZ, MM, E);
        long currTime = System.currentTimeMillis();
        double[] basic_d = basicAlgorithm.calculate_d();
        currTime = System.currentTimeMillis() - currTime;
        System.out.println("Base algorithm B: " + currTime);
        ParallelAlgorithm parallelAlgorithm = new ParallelAlgorithm(B, ME, MZ, MM, E);
        currTime = System.currentTimeMillis();
        double[] parallel_d = parallelAlgorithm.calculate_d();
        currTime = System.currentTimeMillis() - currTime;
        System.out.println("Parallel algorithm B: " + currTime);
        boolean areEqual = areEqual(basic_d, parallel_d);
        System.out.println("B are " + (areEqual ? "equal" : "not equal"));

        currTime = System.currentTimeMillis();
        double[][] basic_ma = basicAlgorithm.calculate_ma();
        currTime = System.currentTimeMillis() - currTime;
        System.out.println("Basic algorithm MA: " + currTime);
        currTime = System.currentTimeMillis();
        double[][] parallel_ma = parallelAlgorithm.calculate_ma();
        currTime = System.currentTimeMillis() - currTime;
        System.out.println("Parallel algorithm MA: " + currTime);
        areEqual = areEqual(basic_ma, parallel_ma);
        System.out.println("MA are " + (areEqual ? "equal" : "not equal"));
    }

    static double[][] initializeMatrix(int line) throws IOException {
        double[][] result = new double[LENGTH][LENGTH];
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
            int count = 1;
            String resultStr;
            while ((resultStr = bufferedReader.readLine()) != null) {
                if (count++ == line) {
                    break;
                }
            }
            if (resultStr != null) {
                String[] rows = resultStr.split(";");
                for (int i = 0; i < LENGTH; i++) {
                    String[] items = rows[i].split(",");
                    for (int j = 0; j < LENGTH; j++) {
                        result[i][j] = Double.parseDouble(items[j]);
                    }
                }
            }
        }
        return result;
    }

    static double[] initializeVector(int line) throws IOException {
        double[] result = new double[LENGTH];
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
            int count = 1;
            String resultStr;
            while ((resultStr = bufferedReader.readLine()) != null) {
                if (count++ == line) {
                    break;
                }
            }
            if (resultStr != null) {
                String[] items = resultStr.split(",");
                for (int i = 0; i < LENGTH; i++) {
                    result[i] = Double.parseDouble(items[i]);
                }
            }
        }
        return result;
    }

    static boolean areEqual(double[][] M1, double[][] M2) {
        for (int i = 0; i < LENGTH; i++) {
            for (int j = 0; j < LENGTH; j++) {
                if (!areEqualWithPrecision(M1[i][j], M2[i][j], 5))
                    return false;
            }
        }
        return true;
    }

    static boolean areEqual(double[] V1, double[] V2) {
        for (int i = 0; i < LENGTH; i++) {
            if (!areEqualWithPrecision(V1[i], V2[i], 5))
                return false;
        }
        return true;
    }

    static boolean areEqualWithPrecision(double num1, double num2, int precision) {
        return Math.abs(num1 - num2) < Math.pow(10, -precision);
    }
}