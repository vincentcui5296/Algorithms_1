import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int size;
    private final int num;
    private int numOpened;
    private boolean [] grid;
    private final WeightedQuickUnionUF wqufBackwash;
    private final WeightedQuickUnionUF wqufFull;

    public Percolation(int n) {               // create n-by-n grid, with all sites blocked
        
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException("n should be > 0");
        }
        
        size = n;
        num = size * size + 2;
        numOpened = 0;
        grid = new boolean[num];
        for (int i = 1; i < num; i++) {
            grid[i] = false;
        }
        grid[0] = true; // union the top
        grid[num - 1] = true; // union the bottom
        wqufBackwash = new WeightedQuickUnionUF(num);
        wqufFull = new WeightedQuickUnionUF(num - 1);
    }

    private void validate(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new java.lang.IllegalArgumentException("row or col should be between [1, " + size + "]");
        }
    }
    
    public void open(int row, int col) {    // open site (row, col) if it is not open already
        validate(row, col);
    	
        row -= 1;
        col -= 1;
        int pos = row * size + col + 1;
        
        if (!grid[pos]) {
            grid[pos] = true;
            numOpened += 1;
            
            int up = pos - size;
            if (up < 0) {
                up = 0;
            }
            if (grid[up]) {
                wqufBackwash.union(pos, up);
                wqufFull.union(pos, up);
            }
            
            int down = pos + size;
            if (down > num - 1) {
                down = num - 1;
            }
            if (grid[down]) {
                wqufBackwash.union(pos, down);
                if (row != (size - 1)) {
                	wqufFull.union(pos, down);
                }
            }
            
            if (col != 0 && grid[pos - 1]) {
                wqufBackwash.union(pos, pos - 1);
                wqufFull.union(pos, pos - 1);
            }
            
            if (col != (size - 1) && grid[pos + 1]) {
                wqufBackwash.union(pos, pos + 1);
                wqufFull.union(pos, pos + 1);
            }
        }
    }
    
    public boolean isOpen(int row, int col) { // is site (row, col) open?
    	validate(row, col);
        row -= 1;
        col -= 1;
        int pos = row * size + col + 1;
        return grid[pos];
    }
    
    public boolean isFull(int row, int col) { // is site (row, col) full?
    	validate(row, col);
        row -= 1;
        col -= 1;
        int pos = row * size + col + 1;
        if (grid[pos]) {
            return wqufFull.connected(0, pos);
        }
        return false;
    }
    public int numberOfOpenSites() {      // number of open sites
        return numOpened;
    }
    public boolean percolates() {             // does the system percolate?
        return wqufBackwash.connected(0, num - 1);
    }
    public static void main(String[] args) {   // test client (optional)
        Percolation p = new Percolation(4);
        p.open(1, 1);
        p.open(1, 3);
        p.open(2, 3);
        p.open(3, 3);
        p.open(3, 1);
        System.out.println(p.isOpen(3, 1));
        System.out.println(p.isFull(3, 1));
        System.out.println(p.numberOfOpenSites());
        System.out.println(p.percolates());
    }
}

