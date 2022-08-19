import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Project2_2 {

    public static void print_TestCase(LinkedList linkedlistNodes) {

        path testCase1 = dijkstra_Linkedlist(linkedlistNodes,197,27);
        System.out.println("From 197 to 27, path is " + Arrays.toString(testCase1.getPath().toArray()));

        path testCase2 = dijkstra_Linkedlist(linkedlistNodes,65,280);
        System.out.println("From 65 to 280, path is " + Arrays.toString(testCase2.getPath().toArray()));

        path testCase3 = dijkstra_Linkedlist(linkedlistNodes,187,68);
        System.out.println("From 187 to 68, path is " + Arrays.toString(testCase3.getPath().toArray()));
        System.out.println();
    }

    public static void dijkstra_Linkedlist_Input(String filePath, String fileFormat) throws FileNotFoundException {
        for(int i = 1; i <= 10; i++) {

            Scanner sc_1 = new Scanner(new BufferedReader(new FileReader(filePath + i + fileFormat)));
            Scanner sc_2 = new Scanner(new BufferedReader(new FileReader(filePath + i + fileFormat)));
            sc_2.nextLine().trim().split(",");
            sc_1.nextLine();

            double memoryStart = Runtime.getRuntime().freeMemory() / 1024 / 1024;
            double timeStart = System.nanoTime();

            int totalNodeID = countNodeID(sc_1);
            LinkedList<LinkedList<node>> linkedlistNodes = new LinkedList<>();
            while (sc_2.hasNextLine()){
                String[] row = sc_2.nextLine().trim().split(",");
                int startNode = Integer.valueOf(row[0]);
                int endNode = Integer.valueOf(row[1]);
                if(startNode > linkedlistNodes.size() -1){
                    LinkedList<node> newList = new LinkedList();
                    newList.add(new node(startNode));
                    linkedlistNodes.add(newList);
                }
                LinkedList current = linkedlistNodes.get(startNode);
                current.add(new node(endNode, Integer.valueOf(row[2])));
            }

            for(int j = 0; j < totalNodeID; j++){
                dijkstra_Linkedlist(linkedlistNodes, j, -1);
            }

            double timeEnd = System.nanoTime();
            double memoryEnd = Runtime.getRuntime().freeMemory() / 1024 / 1024;
            double time = (timeEnd - timeStart) * 1e-9;
            double memory = memoryStart - memoryEnd;

            System.out.println("Running time for input file " + i + " :" + time + "s");
            System.out.println("Memory usage for input file " + i + " :" + memory + "MB" + "\n");

            if(i == 3){
                print_TestCase(linkedlistNodes);
            }
        }
    }

    private static path dijkstra_Linkedlist(LinkedList<LinkedList<node>> linkedlistNodes, int sourceNode, int destinationNode) {
        List<Integer> nodeVistated = new ArrayList<>();
        nodeVistated.add(sourceNode);
        Map<Integer,path> dist = new HashMap<>();
        path statPath = new path(sourceNode);
        dist.put(sourceNode,statPath);
        for(int i = 0; i < linkedlistNodes.size(); i++){
            int nextNode = minDistance(dist, nodeVistated, linkedlistNodes);
            if (nextNode == -1) {
                return null;
            }
            if (nextNode == destinationNode) {
                return dist.get(destinationNode);
            }
            nodeVistated.add(nextNode);
        }

        return null;
    }

    static int minDistance(Map<Integer,path> dist, List<Integer> nodeVisited, LinkedList<LinkedList<node>> linkedlistNodes)
    {
        int min = Integer.MAX_VALUE, min_start = -1, min_idx = -1;
        for(int i = 0; i<nodeVisited.size(); i++){
            for(node curNode : linkedlistNodes.get(nodeVisited.get(i))){
                if(curNode.getDist()!=0 && !nodeVisited.contains(curNode.getIndex()) && dist.get(nodeVisited.get(i)).getDist() + curNode.getDist() < min){
                    min_start = nodeVisited.get(i); min_idx = curNode.getIndex();
                    min = dist.get(nodeVisited.get(i)).getDist() + curNode.getDist();
                }
            }
        }
        if(min_start != -1){
            path current = dist.get(min_start);
            List<Integer> currPath = current.getPath();
            currPath.add(min_idx);
            path newPath = new path(currPath,min);
            dist.put(min_idx, newPath);
        }
        return min_idx;
    }

    public static int countNodeID(Scanner sc_1){
        int totalRow = 0;
        while (sc_1.hasNext()){
            String[] line = sc_1.nextLine().trim().split(",");
            totalRow = totalRow < Integer.valueOf(line[0]) ? Integer.valueOf(line[0]) : totalRow;
        }
        return totalRow + 1;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String filePath = "./Project2_Input_Files/Project2_Input_File";
        String fileFormat = ".csv";

        dijkstra_Linkedlist_Input(filePath, fileFormat);
    }

    private static class node{
        private int idx;
        private int dist;
        private node(int start){
            this.idx = start;
            this.dist = 0;
        }

        private node(int idx, int dist){
            this.idx = idx;
            this.dist = dist;
        }

        public int getDist(){
            return this.dist;
        }
        public int getIndex(){
            return this.idx;
        }
    }

    public static class path {

        private int totalDist;
        private List<Integer> path;

        public path(List<Integer> path, int dist){
            this.totalDist = dist;
            this.path = path;
        }

        public path(int source){
            this.totalDist = 0;
            this.path = new ArrayList<>();
            path.add(source);
        }
        public int getDist(){
            return this.totalDist;
        }
        public List<Integer> getPath(){
            return new ArrayList<>(this.path);
        }
    }
}