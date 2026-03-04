import java.io.*;
import java.util.*;

public class ForestFire {
    public static void main(String[] args) throws IOException{
        ArrayList<Forest> forests = new ArrayList<Forest>();
        File f = new File("forests.csv");
        Scanner scanner = new Scanner(f);
        scanner.nextLine();
        while(scanner.hasNextLine()){
            
            String[] data = scanner.nextLine().split(",");
            Forest forest = new Forest(data[0], data[1], data[2], Double.parseDouble(data[7]), Integer.parseInt(data[9]), Integer.parseInt(data[8]), Integer.parseInt(data[5]), Integer.parseInt(data[6]));
            forests.add(forest);
        }
        scanner.close();
        Forest sim = forests.get(0);
        Tree[][] grid = sim.getGrid();
        grid[(int)(Math.random()*grid.length)][(int)(Math.random()*grid[0].length)].setState(2);
        sim.setGrid(grid);

        for(int i = 0; i < 1; i ++){
            sim.spreadFire();
            sim.saveGridSnapshotToFile();
        }
        System.out.println(sim.percentBurned());
        // Step 1: Read in the data file (forests.csv) and create Forest objects.
        //   - Open the CSV file.
        //   - Skip/read the header row.
        //   - Parse each line into fields and construct a Forest.
        //   - Store forests in an ArrayList: ArrayList<Forest> forests = ...

//name,type,vegetation,area_sq_mi,cell_area_sq_mi,grid_rows,grid_cols,burn_rate,initial_tree_count,burn_duration
//String name, String type, String vegetation, double burnRate, int burnDuration, int initialTreeCount, int gridRows, int gridCols

        // Step 2: Pick one forest to run the simulation.
        //   - Choose by index.
        //   - Start at least one burning tree to begin the fire.

        // Step 3: Run the simulation.
        //   - Repeat spreadFire() for a fixed number of steps (or until fire ends).
        //   - At the end, print percentBurned() and summary stats.
        //   - At the end of each simulation step, you should write the current state of the Tree[][] grid to a file

        // Step 4: We will vibe code our way to visualization

    }

}
