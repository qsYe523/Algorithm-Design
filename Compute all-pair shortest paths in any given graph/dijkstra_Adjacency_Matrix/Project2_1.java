import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Project2_1 {

    public static void print_TestCase(int adjMatrix [][]) {

        path testCase1 = dijkstra_Adjacency_Matrix(adjMatrix, 197, 27);
        System.out.println("From 197 to 27, path is " + Arrays.toString(testCase1.getPath().toArray()));

        path testCase2 = dijkstra_Adjacency_Matrix(adjMatrix, 65, 280);
        System.out.println("From 65 to 280, path is " + Arrays.toString(testCase2.getPath().toArray()));

        path testCase3 = dijkstra_Adjacency_Matrix(adjMatrix, 187, 68);
        System.out.println("From 187 to 68, path is " + Arrays.toString(testCase3.getPath().toArray()));
        System.out.println();
    }

    public static void dijkstra_Adjacency_Matrix_Input(String filePath, String fileFormat) throws FileNotFoundException {
        for (int i = 1; i <= 10; i ++) {

            Scanner sc_1 = new Scanner(new BufferedReader(new FileReader(filePath + i + fileFormat)));
            Scanner sc_2 = new Scanner(new BufferedReader(new FileReader(filePath + i + fileFormat)));
            sc_2.nextLine().trim().split(",");
            sc_1.nextLine();

            double memoryStart = Runtime.getRuntime().freeMemory() / 1024 / 1024;
            double timeStart = System.nanoTime();

            int totalNodeID = countNodeID(sc_1);
            int[][] adjacent_Matrix = new int[totalNodeID][totalNodeID];
            String[] connect = new String[totalNodeID];
            for (int[] temp : adjacent_Matrix) {
                Arrays.fill(temp, 0);
            }
            while (sc_2.hasNextLine()) {
                String[] row = sc_2.nextLine().trim().split(",");
                int startNode = Integer.valueOf(row[0]);
                int endNode = Integer.valueOf(row[1]);
                if (connect[startNode] != null) connect[startNode] = row[3] + ";" + row[4];
                adjacent_Matrix[startNode][endNode] = Integer.valueOf(row[2]);
            }

            for (int j = 0; j < totalNodeID; j++) {
                dijkstra_Adjacency_Matrix(adjacent_Matrix, j, -1);
            }

            double timeEnd = System.nanoTime();
            double memoryEnd = Runtime.getRuntime().freeMemory() / 1024 / 1024;
            double time = (timeEnd - timeStart) * 1e-9;
            double memory = memoryStart - memoryEnd;

            System.out.println("Running time for input file " + i + " :" + time + "s");
            System.out.println("Memory usage for input file " + i + " :" + memory + "MB" + "\n");

            if (i == 3) {
                print_TestCase(adjacent_Matrix);
            }
        }
    }


    public static int countNodeID(Scanner sc_1) {
        int totalRow = 0;
        while (sc_1.hasNext()) {
            String[] row = sc_1.nextLine().trim().split(",");
            totalRow = totalRow < Integer.valueOf(row[0]) ? Integer.valueOf(row[0]) : totalRow;
        }
        return totalRow + 1;
    }

    static int minDist(Map<Integer, path> dist, List<Integer> nodeVisited, int graph[][]) {
        int min = Integer.MAX_VALUE, min_start = -1, min_idx = -1;
        for (int i = 0; i < nodeVisited.size(); i++) {
            for (int j = 0; j < graph.length; j++) {
                if (graph[nodeVisited.get(i)][j] != 0 && !nodeVisited.contains(j) && graph[nodeVisited.get(i)][j] + dist.get(nodeVisited.get(i)).getDist() < min) {
                    min = graph[nodeVisited.get(i)][j] + dist.get(nodeVisited.get(i)).getDist();
                    min_start = nodeVisited.get(i);
                    min_idx = j;
                }
            }
        }
        if (min_start != -1) {
            path current = dist.get(min_start);
            List<Integer> current_Path = current.getPath();
            current_Path.add(min_idx);
            path new_Path = new path(current_Path, min);
            dist.put(min_idx, new_Path);
        }
        return min_idx;
    }

    public static path dijkstra_Adjacency_Matrix(int graph[][], int source, int distNode) {
        List<Integer> nodeVistated = new ArrayList<>();
        nodeVistated.add(source);
        Map<Integer, path> dist = new HashMap<>();
        path statPath = new path(source);
        dist.put(source, statPath);
        for (int i = 0; i < graph.length; i++) {
            int nextNode = minDist(dist, nodeVistated, graph);
            if (nextNode == -1) {
                return null;
            }
            if (nextNode == distNode) {
                return dist.get(distNode);
            }
            nodeVistated.add(nextNode);
        }
        return null;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String filePath = "./Project2_Input_Files/Project2_Input_File";
        String fileFormat = ".csv";

        dijkstra_Adjacency_Matrix_Input(filePath, fileFormat);
    }

    private static class path {

        private int totalDist;
        private List<Integer> path;

        public path(List<Integer> path, int dist) {
            this.totalDist = dist;
            this.path = path;
        }

        public path(int source) {
            this.totalDist = 0;
            this.path = new ArrayList<>();
            path.add(source);
        }

        public int getDist() {
            return this.totalDist;
        }
        public List<Integer> getPath() {
            return new ArrayList<>(this.path);
        }
    }
}