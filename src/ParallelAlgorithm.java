public class ParallelAlgorithm {
    static final int LENGTH = Generator.LENGTH;
    private final double[] B;
    private final double[][] ME;
    private final double[][] MZ;
    private final double[][] MM;
    private final double[] E;
    private static final int NUM_THREADS = Main.NUM_THREADS;
    public ParallelAlgorithm(double[] b, double[][] me, double[][] mz, double[][] mm, double[] e) {

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
        double[][] me_plus_mz = addMatrixParallel(ME, MZ);
        double[][] me_mult_mm = multiplyMatrixParallel(ME, MM);
        double min_mm = BasicAlgorithm.findMinNumberInMatrix(MM);
        double[][] min_mm_mult_me_plus_mz = multiplyMatrixByScalarParallel(me_plus_mz, min_mm);
        double[][] ma = subtractMatrixParallel(min_mm_mult_me_plus_mz, me_mult_mm);
        return ma;
    }

    public double[] calculate_d() {
        double[][] me_plus_mz = addMatrixParallel(ME, MZ);
        double[][] mm_plus_me = addMatrixParallel(MM, ME);
        double[] b_mult_me_plus_mz = multiplyVectorAndMatrixParallel(B, me_plus_mz);
        double[] e_mult_mm_plus_me = multiplyVectorAndMatrixParallel(E, mm_plus_me);
        double[] d = BasicAlgorithm.vectorSubstraction(b_mult_me_plus_mz, e_mult_mm_plus_me);
        return d;
    }

    static double[][] subtractMatrixParallel(double[][] M1, double[][] M2) {
        double[][] result = new double[LENGTH][LENGTH];
        Thread[] threads = new Thread[NUM_THREADS];

        for (int t = 0; t < NUM_THREADS; t++) {
            int finalT = t;
            threads[t] = new Thread(() -> {
                for (int i = finalT; i < LENGTH; i += NUM_THREADS) {
                    for (int j = 0; j < LENGTH; j++) {
                        result[i][j] = M1[i][j] - M2[i][j];
                    }
                }
            });
            threads[t].start();
        }

        for (int t = 0; t < NUM_THREADS; t++) {
            try {
                threads[t].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    static double[] multiplyVectorAndMatrixParallel(double[] V, double[][] M) {
        Thread[] threads = new Thread[NUM_THREADS];
        double[] result = new double[LENGTH];
        for (int t = 0; t < NUM_THREADS; t++) {
            int finalT = t;
            threads[t] = new Thread(() -> {
                for (int i = finalT; i < LENGTH; i += NUM_THREADS) {
                    double[] numToSum = new double[LENGTH];
                    for (int k = 0; k < LENGTH; k++) {
                        numToSum[k] = V[k] * M[k][i];
                    }
                    result[i] = BasicAlgorithm.kahanSum(numToSum);
                }
            });

            threads[t].start();
        }
        for (int t = 0; t < NUM_THREADS; t++) {
            try {
                threads[t].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    static double[][] multiplyMatrixByScalarParallel(double[][] M, double num) {
        Thread[] threads = new Thread[NUM_THREADS];
        double[][] result = new double[LENGTH][LENGTH];

        for (int t = 0; t < NUM_THREADS; t++) {
            int finalT = t;
            threads[t] = new Thread(() -> {
                for (int i = finalT; i < LENGTH; i += NUM_THREADS) {
                    for (int j = 0; j < LENGTH; j++) {
                        result[i][j] = M[i][j] * num;
                    }
                }
            });
            threads[t].start();
        }

        for (int t = 0; t < NUM_THREADS; t++) {
            try {
                threads[t].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    static double[][] addMatrixParallel(double[][] M1, double[][] M2) {
        Thread[] threads = new Thread[NUM_THREADS];
        double[][] result = new double[LENGTH][LENGTH];

        for (int t = 0; t < NUM_THREADS; t++) {
            int finalT = t;
            threads[t] = new Thread(() -> {
                for (int i = finalT; i < LENGTH; i += NUM_THREADS) {
                    for (int j = 0; j < LENGTH; j++) {
                        result[i][j] = M1[i][j] + M2[i][j];
                    }
                }
            });
            threads[t].start();
        }

        for (int t = 0; t < NUM_THREADS; t++) {
            try {
                threads[t].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    static double[][] multiplyMatrixParallel(double[][] M1, double[][] M2) {
        Thread[] threads = new Thread[NUM_THREADS];
        double[][] result = new double[LENGTH][LENGTH];
        for (int t = 0; t < NUM_THREADS; t++) {
            int finalT = t;
            threads[t] = new Thread(() -> {
                for (int i = finalT; i < LENGTH; i += NUM_THREADS) {
                    int count = 0;
                    int j = i;
                    while (count++ < LENGTH) {
                        double[] numToSum = new double[LENGTH];
                        for (int k = 0; k < LENGTH; k++) {
                            numToSum[k] = M1[i][k] * M2[k][j];
                        }
                        result[i][j] = BasicAlgorithm.kahanSum(numToSum);
                        if (--j < 0)
                            j = LENGTH - 1;
                    }
                }
            });

            threads[t].start();
        }
        for (int t = 0; t < NUM_THREADS; t++) {
            try {
                threads[t].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}
