import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final int OPENED = 1;
    private static final int CONNECTEDTOTOP = 2;
    private static final int CONNECTEDTOBOTTOM = 4;
    
    private final int size;
    private final int num;
    private int numOpened;
    private boolean percolated;
    private int [] grid;
    private final WeightedQuickUnionUF wquf;

    public Percolation(int n) {               // create n-by-n grid, with all sites blocked
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException("n should be > 0");
        }
        size = n;
        num = size * size;
        numOpened = 0;
        percolated = false;
        grid = new int[num];
        for (int i = 0; i < num; i++) {
            grid[i] = 0;
        }
        wquf = new WeightedQuickUnionUF(num);
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
        int stats = 0;
        int pos = row * size + col;
        int root = 0;
        if (grid[pos] == 0) {
            if (row != 0) {
                int up = pos - size;
                root = wquf.find(up);
                if (grid[up] != 0) {
                    stats |= grid[root];
                    wquf.union(pos, up);
                }
            } else {
                stats |= CONNECTEDTOTOP;
            }

            if (row != size - 1) {
                int down = pos + size;
                root = wquf.find(down);
                if (grid[down] != 0) {
                    stats |= grid[root];
                    wquf.union(pos, down);
                }
            } else {
                    stats |= CONNECTEDTOBOTTOM;
            }

            if (col != 0) {
                root = wquf.find(pos - 1);
                if (grid[pos - 1] != 0) {
                    stats |= grid[root];
                    wquf.union(pos, pos - 1);
                }
            }
            
            if (col != (size - 1)) {
                root = wquf.find(pos + 1);
                if (grid[pos + 1] != 0) {
                    stats |= grid[root];
                    wquf.union(pos, pos + 1);
                }
            }

            stats |= OPENED;
            grid[wquf.find(pos)] = stats;
            grid[pos] |= stats;
            numOpened += 1;
            if (stats == (OPENED | CONNECTEDTOTOP | CONNECTEDTOBOTTOM)) {
                percolated = true;
            }
        }
    }
    
    public boolean isOpen(int row, int col) { // is site (row, col) open?
        validate(row, col);
        row -= 1;
        col -= 1;
        int pos = row * size + col;
        return grid[pos] != 0;
    }

    public boolean isFull(int row, int col) { // is site (row, col) full?
        validate(row, col);
        row -= 1;
        col -= 1;
        int pos = row * size + col;
        if (grid[pos] != 0) {
            int full = OPENED | CONNECTEDTOTOP;
            return (grid[pos] & full) == full ? true : (grid[wquf.find(pos)] & full) == full;
        }
        return false;
    }

    public int numberOfOpenSites() {      // number of open sites
        return numOpened;
    }

    public boolean percolates() {             // does the system percolate?
        return percolated;
    }

    public static void main(String[] args) {   // test client (optional)
        Percolation p = new Percolation(1);
        p.open(1, 1);
        // p.open(1, 3);
        // p.open(2, 3);
        // p.open(3, 3);
        // p.open(3, 1);
        System.out.println(p.isOpen(1, 1));
        System.out.println(p.isFull(1, 1));
        System.out.println(p.numberOfOpenSites());
        System.out.println(p.percolates());
    }
}

