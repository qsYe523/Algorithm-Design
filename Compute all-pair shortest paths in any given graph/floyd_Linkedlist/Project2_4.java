import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Project2_4 {

    public static void print_TestCase(List result) {

        int[][] path = (int[][]) result.get(1);

        System.out.print("From 197 to 27, path is " + "197>>"); printPath(path,27,197); System.out.println("27");
        System.out.print("From 65 to 280, path is " + "65>>"); printPath(path,280,65); System.out.println("280");
        System.out.print("From 187 to 68, path is " + "187>>"); printPath(path,68,187); System.out.println("68");
        System.out.println();

    }

    public static void floyd_Linkedlist_Input(String filePath, String fileFormat) throws FileNotFoundException {
        for(int i = 1; i <= 10; i++) {

            Scanner sc_1 = new Scanner(new BufferedReader(new FileReader(filePath + i + fileFormat)));
            Scanner sc_2 = new Scanner(new BufferedReader(new FileReader(filePath + i + fileFormat)));
            sc_2.nextLine().trim().split(",");
            sc_1.nextLine();

            double memoryStart = Runtime.getRuntime().freeMemory() / 1024 / 1024;
            double timeStart = System.nanoTime();

            int totalNodeID = countLines(sc_1);
            LinkedList<LinkedList> linkedlistNodes = new LinkedList<>();

            while (sc_2.hasNextLine()){
                String[] row = sc_2.nextLine().trim().split(",");
                int startNode = Integer.valueOf(row[0]);
                int endNode = Integer.valueOf(row[1]);
                if(startNode > linkedlistNodes.size() - 1){
                    LinkedList<node> newList = new LinkedList();
                    newList.add(new node(startNode));
                    linkedlistNodes.add(newList);
                }
                LinkedList current = linkedlistNodes.get(startNode);
                current.add(new node(endNode, Integer.valueOf(row[2])));
            }

            for (int j = 0; j < totalNodeID; j++) {
                floyd_Linkedlist(linkedlistNodes, totalNodeID, j);
            }

            if(i == 3){
                print_TestCase(floyd_Linkedlist(linkedlistNodes, totalNodeID, totalNodeID));
            }

            double timeEnd = System.nanoTime();
            double memoryEnd = Runtime.getRuntime().freeMemory() / 1024 / 1024;
            double time = (timeEnd - timeStart) * 1e-9;
            double memory = memoryStart - memoryEnd;

            System.out.println("Running time for input file " + i + " :" + time + "s");
            System.out.println("Memory usage for input file " + i + " :" + memory + "MB" + "\n");

        }

    }

    public static int countLines(Scanner sc_1){
        int totalRow = 0;
        while (sc_1.hasNext()){
            String[] row = sc_1.nextLine().trim().split(",");
            totalRow = totalRow < Integer.valueOf(row[0]) ? Integer.valueOf(row[0]) : totalRow;
        }
        return totalRow + 1;
    }

    public static List<int[][]> floyd_Linkedlist(LinkedList<LinkedList> linkedLists, int totalNodeID, int a)
    {
        int[][] temp = new int[a][totalNodeID];
        int[][] path = new int[a][totalNodeID];

        for(int[] A : temp){
            Arrays.fill(A,Integer.MAX_VALUE);
        }

        for(int[] A : path){
            Arrays.fill(A,-1);
        }

        for (int start = 0; start < a; start++)
        {
            LinkedList<node> startNodesList = linkedLists.get(start);
            for(node nodes : startNodesList){
                int end = nodes.getIndex();
//                System.out.println(totalLines);
//                System.out.println(to);
//                System.out.println(start);
                temp[start][end] = nodes.getDist();
                if (start == end) {
                    path[start][end] = 0;
                }
                else if (temp[start][end] != Integer.MAX_VALUE) {
                    path[start][end] = start;
                } else {
                    path[start][end] = -1;
                }
            }
        }

        for (int m = 0; m < a; m++)
        {
            for (int n = 0; n < a; n++)
            {
                for (int x = 0; x < a; x++)
                {
                    if (temp[n][m] != Integer.MAX_VALUE && temp[m][x] != Integer.MAX_VALUE && (temp[n][m] + temp[m][x] < temp[n][x]))
                    {
                        temp[n][x] = temp[n][m] + temp[m][x];
                        path[n][x] = path[m][x];
                    }
                }
                if (temp[n][n] < 0) return null;
            }
        }
        List<int[][]> result = new ArrayList();
        result.add(temp);
        result.add(path);
        return result;
    }

    private static void printPath(int[][] path, int end, int start)
    {
        if (path[end][start] == end) return;
        System.out.print(path[end][start] + ">>");
        printPath(path, end,  path[end][start]);
    }

    public static void main(String[] args) throws FileNotFoundException {
        String filePath = "./Project2_Input_Files/Project2_Input_File";
        String fileFormat = ".csv";

        floyd_Linkedlist_Input(filePath, fileFormat);
    }

    private static class node{
        private int idx;
        private int dist;
        private node(int start){
            this.idx = start;
            this.dist = 0;
        }
        private node(int index, int dist){
            this.idx = index;
            this.dist = dist;
        }
        public int getDist(){
            return this.dist;
        }
        public int getIndex(){
            return this.idx;
        }
    }
}


