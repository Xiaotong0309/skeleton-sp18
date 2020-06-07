package hw2;

import java.util.Random;

public class PercolationStats {
    private double mean;
    private double std;
    private double confLow;
    private double confHigh;

    public PercolationStats(int N, int T, PercolationFactory pf){ // perform T independent experiments on an N-by-N grid
        this.mean = 0;
        this.std = 0;
        double[] sample = new double[T];
        for(int i = 0; i < T; i++){
            Percolation p = pf.make(N);
            int count = 0;
            while(!p.percolates()){
                //randomly open a site
                Random rand = new Random();
                int rand1 = rand.nextInt(N*N);
                if(!p.isOpen(rand1 / N, rand1 % N)) {
                    p.open(rand1 / N, rand1 % N);
                    count++;
                }
            }
            double f = (double)count / (N * N);
            this.mean += f;
            sample[i] = f;
        }
        this.mean /= T;
        for(int i = 0; i < T; i++){
            this.std += Math.pow(sample[i] - this.mean, 2);
        }
        this.std = Math.sqrt(this.std / (T-1));
        this.confLow = this.mean - (1.96 * this.std / Math.sqrt(T));
        this.confHigh = this.mean + (1.96 * this.std / Math.sqrt(T));
    }
    public double mean(){                                       // sample mean of percolation threshold
        return this.mean;
    }
    public double stddev(){                                     // sample standard deviation of percolation threshold
        return this.std;
    }
    public double confidenceLow(){                              // low endpoint of 95% confidence interval
        return this.confLow;
    }
    public double confidenceHigh(){                             // high endpoint of 95% confidence interval
        return this.confHigh;
    }

    public static void main(String[] args){
        PercolationStats s = new PercolationStats(100, 10000, new PercolationFactory());
        System.out.println("Mean: " + s.mean() +
                " Stddev: " + s.stddev() +
                " ConfLow: " + s.confidenceLow() +
                " ConfHigh: " + s.confidenceHigh());
    }
}
