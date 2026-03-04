// Forest.java
// Stores metadata and grid for a forest

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Forest {
    private String name;
    private String type;
    private String vegetation;
    private double burnRate;
    private int initialTreeCount;
    private int burnDuration;
    private Tree[][] grid;

    public Forest(String name, String type, String vegetation, double burnRate, int burnDuration, int initialTreeCount, int gridRows, int gridCols) {
        this.grid = new Tree[gridRows][gridCols]; //TODO; initialize grid to using gridRows and gridCols
        this.name = name;
        this.type = type;
        this.vegetation = vegetation;
        this.burnRate = burnRate;
        this.initialTreeCount = initialTreeCount;
        this.burnDuration = burnDuration;
        this.initializeForest(); //This is going to populate the grid with trees.
    }
    
    public void initializeForest(){
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                grid[i][j] = new Tree(0);
            }
        }
        int count = 0;
        while(count < initialTreeCount){
            int randRow = (int)(Math.random()*grid.length);
            int randCol = (int)(Math.random()*grid[0].length);
            if(grid[randRow][randCol].getState() == 0){
                grid[randRow][randCol].setState(1);
                count++;
            }
        }
        System.out.println("woo");
        burnDuration = 0;
        burnRate = 0;
        // Step 1: Determine how many cells in the grid should start as TREE
        //         based on initialTreeCount and the grid dimensions.
        // Step 2: Fill the grid with EMPTY trees first so every cell has a Tree object.
        // Step 3: Randomly place TREE states until the target initial tree count is reached.
        // Step 4: Reset any burn-time/state tracking needed for a fresh simulation start.
    }

    public Tree[][] deepCopy(){
        Tree[][] copy = new Tree[grid.length][grid[0].length];
        for(int i = 0; i < copy.length; i++){
            for(int j = 0; j < copy[0].length; j++){
                copy[i][j] = new Tree(grid[i][j].getState());
            }
        }
        // Step 1: Create a new Tree[][] with the same dimensions as grid.
        // Step 2: Loop through every cell in grid.
        // Step 3: Copy each Tree by value (state and burnTime), not by reference.
        // Step 4: Return the copied Tree[][].
        return copy;
    }

    public void spreadFire() {
        Tree[][] updated = deepCopy();
        for(int i = 0; i < updated.length; i ++){
            for(int j = 0; j < updated[0].length; j++){
                if(updated[i][j].getState() == 2){
                    if(i+1 >= 0 && i+1 < updated.length){
                        if(updated[i+1][j].getState() == 1){
                            if(Math.random() <= burnRate)
                                updated[i+1][j].setState(2);
                        }
                }
                if(i-1 >= 0 && i-1 < updated.length){
                        if(updated[i-1][j].getState() == 1){
                            if(Math.random() <= burnRate)
                                updated[i-1][j].setState(2);
                        }
                }
                if(j+1 >= 0 && j+1 < updated[0].length){
                        if(updated[i][j+1].getState() == 1){
                            if(Math.random() <= burnRate)
                                updated[i][j+1].setState(2);
                        }
                }
                if(j-1 >= 0 && j-1 < updated[0].length){
                        if(updated[i][j-1].getState() == 1){
                            if(Math.random() <= burnRate)
                                updated[i][j-1].setState(2);
                        }
                }
                    
                }
            }
        }
        for(int i = 0; i < updated.length; i ++){
            for(int j = 0; j < updated[0].length; j++){
                if(updated[i][j].getBurnTime() >= burnDuration)
                    updated[i][j].setState(0);
                else if(updated[i][j].getState() == 2)
                    updated[i][j].setBurnTime(updated[i][j].getBurnTime()+1);
            }
        }
        grid = updated;
        // Step 1: Call deepCopy() to create a separate "next step" grid so updates happen simultaneously.
        // Step 2: For each BURNING tree, check valid neighbors (up/down/left/right).
        // Step 3: For each neighboring TREE, ignite it with probability burnRate.
        // Step 4: Increase burn time of currently burning trees.
        // Step 5: Turn trees to EMPTY once burn time reaches burnDuration.
        // Step 6: Replace the current grid with the updated next-step grid.
    }

    public double percentBurned() {
        int burned = 0;
        for(int i = 0; i < grid.length; i ++){
            for(int j = 0; j < grid[0].length; j ++){
                if(grid[i][j].getState() == 2)
                    burned ++;
            }
        }
        burned -= (grid.length*grid[0].length - initialTreeCount);
        if(initialTreeCount == 0)
            return 0;
        // Step 1: Count how many trees have burned out (commonly represented as EMPTY after burning).
        // Step 2: Compute and return (burnedCount * 100.0) / initialTreeCount as a percentage.
        // Step 3: Guard against divide-by-zero if the initialTreeCount is 0.
        return (burned*100.0) / initialTreeCount;
    }

    public void saveGridSnapshotToFile() {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return;
        }

        String safeName = name == null ? "forest" : name.trim().replaceAll("[^a-zA-Z0-9._-]", "_");
        if (safeName.isEmpty()) {
            safeName = "forest";
        }
        String fileName = safeName + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write("=== GRID SNAPSHOT ===");
            writer.newLine();
            for (Tree[] row : grid) {
                StringBuilder line = new StringBuilder();
                for (Tree tree : row) {
                    char cell;
                    if (tree == null) {
                        cell = '?';
                    } else if (tree.getState() == Tree.EMPTY) {
                        cell = '.';
                    } else if (tree.getState() == Tree.TREE) {
                        cell = 'T';
                    } else if (tree.getState() == Tree.BURNING) {
                        cell = 'F';
                    } else {
                        cell = '?';
                    }
                    line.append(cell);
                }
                writer.write(line.toString());
                writer.newLine();
            }
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write grid snapshot to file: " + fileName, e);
        }
    }

    public void setGrid(Tree[][] grid) {
        this.grid = grid;
    }
    public Tree[][] getGrid() {
        return grid;
    }
    public double getBurnRate() { return burnRate; }
    public int getBurnDuration() { return burnDuration; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getVegetation() { return vegetation; }
    public int getInitialTreeCount() { return initialTreeCount; }
}
