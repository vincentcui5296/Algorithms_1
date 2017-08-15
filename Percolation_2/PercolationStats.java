import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double[] percolationThreshold;
    private final double mean;
    private final double stddev;

    public PercolationStats(int n, int trials) {    // perform trials independent experiments on an n-by-n grid
        if (n <= 0 || trials <= 0) {
            throw new java.lang.IllegalArgumentException("n and trials should be > 0");
        }
        
        percolationThreshold = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                p.open(StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
            }
            double num = p.numberOfOpenSites();
            percolationThreshold[i] = num / (n*n);
        }
        mean = StdStats.mean(percolationThreshold);
        stddev = StdStats.stddev(percolationThreshold);
    }
    
    public double mean() {                         // sample mean of percolation threshold
        return mean;
    }
    
    public double stddev() {                       // sample standard deviation of percolation threshold
        return stddev;
    }
    
    public double confidenceLo() {                 // low  endpoint of 95% confidence interval
        return mean - 1.96 * stddev / Math.sqrt(percolationThreshold.length);
    }
    
    public double confidenceHi() {                 // high endpoint of 95% confidence interval
        return mean + 1.96 * stddev / Math.sqrt(percolationThreshold.length);
    }
   
    public static void main(String[] args) {       // test client (described below)
        PercolationStats percSts = new PercolationStats(200, 100);
        System.out.println(percSts.mean());
        System.out.println(percSts.stddev());
    }
}