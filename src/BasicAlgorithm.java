public class BasicAlgorithm {
    static final int LENGTH = Generator.LENGTH;
    private final double[] B;
    private final double[][] ME;
    private final double[][] MZ;
    private final double[][] MM;
    private final double[] E;
    public BasicAlgorithm(double[] b, double[][] me, double[][] mz, double[][] mm, double[] e) {

        B = b;
        ME = me;
        MZ = mz;
        MM = mm;
        E = e;
    }

    public double[] calculate() {
        double[] d = calculate_d();
        double[][] ma = calculate_ma();
        return null;
    }

    public double[][] calculate_ma() {

        double[][] me_plus_mz = addMatrix(ME, MZ);
        double[][] me_mult_mm = multiplyMatrix(ME, MM);
        double min_mm = findMinNumberInMatrix(MM);
        double[][] min_mm_mult_me_plus_mz = multiplyMatrixByScalar(me_plus_mz, min_mm);
        double[][] ma = matrixSubstraction(min_mm_mult_me_plus_mz, me_mult_mm);
        return ma;
    }

    public double[] calculate_d() {
        double[][] me_plus_mz = addMatrix(ME, MZ);
        double[][] mm_plus_me = addMatrix(MM, ME);
        double[] b_mult_me_plus_mz = multiplyVectorAndMatrix(B, me_plus_mz);
        double[] e_mult_mm_plus_me = multiplyVectorAndMatrix(E, mm_plus_me);
        double[] d = vectorSubstraction(b_mult_me_plus_mz, e_mult_mm_plus_me);
        return d;
    }

    static double[][] matrixSubstraction(double[][] M1, double[][] M2) {
        double[][] result = new double[LENGTH][LENGTH];
        for (int i = 0; i < LENGTH; i++) {
            for (int j = 0; j < LENGTH; j++) {
                result[i][j] = M1[i][j] - M2[i][j];
            }
        }

        return result;
    }

    static double[][] multiplyMatrix(double[][] M1, double[][] M2) {
        double[][] result = new double[LENGTH][LENGTH];

        for (int i = 0; i < LENGTH; i++) {
            for (int j = 0; j < LENGTH; j++) {
                double[] numToSum = new double[LENGTH];
                for (int k = 0; k < LENGTH; k++) {
                    numToSum[k] = M1[i][k] * M2[k][j];
                }
                result[i][j] = kahanSum(numToSum);
            }
        }
        return result;
    }

    static double findMinNumberInMatrix(double[][] M) {
        double minValue = M[0][0];
        for (int i = 0; i < LENGTH; i++) {
            for (int j = 0; j < LENGTH; j++) {
                if (M[i][j] < minValue) {
                    minValue = M[i][j];
                }
            }
        }
        return minValue;
    }

    static double[][] multiplyMatrixByScalar(double[][] M, double num) {
        double[][] result = new double[LENGTH][LENGTH];

        for (int i = 0; i < LENGTH; i++) {
            for (int j = 0; j < LENGTH; j++) {
                result[i][j] = M[i][j] * num;
            }
        }
        return result;
    }

    public static double[] vectorSubstraction(double[] v1, double[] v2){
        double[] result = new double[LENGTH];
        for(int i = 0; i < LENGTH; i++){
            result[i] = v1[i] - v2[i];
        }
        return result;
    }

    static double[] multiplyVectorAndMatrix(double[] V, double[][] M) {
        double[] result = new double[LENGTH];
        for (int i = 0; i < LENGTH; i ++) {
            double[] numToSum = new double[LENGTH];
            for (int k = 0; k < LENGTH; k++) {
                numToSum[k] = V[k] * M[k][i];
            }
            result[i] = kahanSum(numToSum);
        }
        return result;
    }

    static double[][] addMatrix(double[][] M1, double[][] M2) {
        double[][] result = new double[LENGTH][LENGTH];

        for (int i = 0; i < LENGTH; i++) {
            for (int j = 0; j < LENGTH; j++) {
                result[i][j] = M1[i][j] + M2[i][j];
            }
        }
        return result;
    }

    static double kahanSum(double... fa) {
        double sum = 0.0;
        double c = 0.0;
        for (double f : fa) {
            double y = f - c;
            double t = sum + y;
            c = (t - sum) - y;
            sum = t;
        }
        return sum;
    }
}
