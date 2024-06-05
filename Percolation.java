import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][][] grids; 
    private int gridSize;
    private int gridSquared;
    private WeightedQuickUnionUF[] wquFinds; // Use an array to store multiple WeightedQuickUnionUF instances
    private int virtualTop;
    private int virtualBottom;
    private boolean[] percolates; // Use an array to store percolation status for each model
    
    
    // Constructor to initialize the Percolation object with a grid of given size and number of models
    public Percolation(int n, int numModels) {
        gridSize = n;
        gridSquared = n * n;
        grids = new boolean[numModels][n][n];
        wquFinds = new WeightedQuickUnionUF[numModels];
        virtualTop = gridSquared;
        virtualBottom = gridSquared + 1;
        percolates = new boolean[numModels];
     
        
        // Initialize WeightedQuickUnionUF instances for each model
        for (int i = 0; i < numModels; i++) {
            wquFinds[i] = new WeightedQuickUnionUF(gridSquared + 2);
        }
    }
 
    // Opens a site at the specified row and column for the given model
    public void openSite(int model, int row, int col) {
        if (!isValid(row, col)) return;

        if (!grids[model][row][col]) {
            grids[model][row][col] = true;
            
            
            // Connect to virtual top and bottom if on the edges
            if (row == 0) wquFinds[model].union(getIndex(row, col), virtualTop);
            if (row == gridSize - 1) wquFinds[model].union(getIndex(row, col), virtualBottom);
            
            // Connect to adjacent open sites
            connectToAdjacent(model, row, col);

            percolates[model] = wquFinds[model].connected(virtualTop, virtualBottom);
        }
    }
    
    // Opens sites randomly based on the probability for the given model
    public void openAllSites(int model, double p) {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (StdRandom.bernoulli(p)) {
                    openSite(model, i, j);
                }
            }
        }
    }
    
    // Returns the percolation status for the specified model
    public boolean percolationCheck(int model) {
        return percolates[model];
    }

    

    // Checks if the given row and column are valid indices within the grid
    private boolean isValid(int row, int col) {
        return row >= 0 && row < gridSize && col >= 0 && col < gridSize;
    }
    
    // Converts 2D grid indices to a 1D index
    private int getIndex(int row, int col) {
        return row * gridSize + col;
    }
 
    // Connects the current site to its adjacent open sites
    private void connectToAdjacent(int model, int row, int col) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (isValid(newRow, newCol) && grids[model][newRow][newCol]) {
                wquFinds[model].union(getIndex(row, col), getIndex(newRow, newCol));
            }
        }
    }
    // main method
    public static void main(String[] args) {
        int gridSize = 6; // Change the grid size as needed
        int numModels = 10; // determine the number of grids

        // grid size settings
        StdDraw.setCanvasSize(gridSize * 25 * numModels, gridSize * 25);
        StdDraw.setXscale(0, gridSize * 25 * numModels);
        StdDraw.setYscale(0, gridSize * 25);
        
        
        // Create a Percolation object with the specified grid size and number of models
        Percolation percolation = new Percolation(gridSize, numModels);
        
        
        // Open specific sites and randomly open additional sites for each model
        for (int model = 0; model < numModels; model++) {
            percolation.openSite(model, 1, 1);
            percolation.openSite(model, 2, 1);
            percolation.openAllSites(model, 0.6);
        }
        
        // Create a PercolationVisualizer object for visualization
        PercolationVisualizer visualizer = new PercolationVisualizer(percolation.grids, percolation.percolates, gridSize);

        for (int model = 0; model < numModels; model++) {
            System.out.println("Model " + (model + 1) + ": Percolates - " + percolation.percolationCheck(model));
        }
        // Draw the visual representation of the grids and percolation status
        visualizer.draw();
    }

}