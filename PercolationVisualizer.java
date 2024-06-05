import edu.princeton.cs.algs4.StdDraw;

public class PercolationVisualizer {
    private boolean[][][] grids; // 3D array to store multiple grids
    private boolean[] percolates; // Array to store percolation status for each model
    private int gridSize;

    public PercolationVisualizer(boolean[][][] grids, boolean[] percolates, int gridSize) {
        this.grids = grids;
        this.percolates = percolates;
        this.gridSize = gridSize;
    }

    public void draw() {
        int cellSize = 20; // Adjust cell size as needed
        int gapSize = 1; // Adjust gap size between grids

        StdDraw.clear();

        for (int model = 0; model < grids.length; model++) {
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    if (grids[model][i][j]) {
                        StdDraw.setPenColor(StdDraw.BLUE); // Set color to blue for open sites
                    } else {
                        StdDraw.setPenColor(StdDraw.BLACK); // Set color to black for closed sites
                    }

                    double x = j * cellSize + cellSize / 2 + model * (gridSize * cellSize + gapSize * cellSize);
                    double y = gridSize * cellSize - i * cellSize - cellSize / 2;
                    StdDraw.filledSquare(x, y, cellSize / 2);
                }
            }

            // Write "Percolate "True" or "False" text for each model
            StdDraw.setPenColor(StdDraw.RED);
            double modelX = gridSize * cellSize / 2 + model * (gridSize * cellSize + gapSize * cellSize);
            StdDraw.text(modelX, 5, "Percolate " + (percolates[model] ? "True" : "False"));
        }

        // Pause to separate grids
        StdDraw.pause(100);
    }
}

