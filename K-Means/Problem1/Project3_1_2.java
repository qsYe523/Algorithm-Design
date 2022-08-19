import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Project3_1_2 {

    static int totalLine = 1993;
    static int[] parent = new int[totalLine];
    static int INF = Integer.MAX_VALUE;

    static void kruskalMST_Adjacency_Matrix(int adjacency_Matrix[][]) {
        int minDistance = 0;
        for (int i = 0; i < totalLine; i++)
            parent[i] = i;

        int count = 0;
        while (count < totalLine - 1) {
            int minCost = INF, a = -1, b = -1;
            for (int i = 0; i < totalLine; i++) {
                for (int j = 0; j < totalLine; j++) {
                    if (find(i) != find(j) && adjacency_Matrix[i][j] < minCost) {
                        minCost = adjacency_Matrix[i][j];
                        a = i;
                        b = j;
                    }
                }
            }
            unions(a, b);
            System.out.println( a +" " + b +" " + minCost);
            count++;
            minDistance = minDistance + minCost;
        }
        System.out.println("The total distance of the minimum-spanning tree is: " + minDistance);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new BufferedReader(new FileReader("./Project3_Input_Files/Project3_Merged_Data.csv")));
        int [][] adjacency_Matrix = new int[totalLine][totalLine];
        while (sc.hasNextLine()) {
            for (int i = 0; i < totalLine; i++) {
                String[] column = sc.nextLine().trim().split(",");
                for (int j = 0; j < totalLine; j++) {
                    adjacency_Matrix[i][j] = column[j].equals("0") ? INF: Integer.parseInt(column[j]);
                }
            }
        }
        System.out.println("Node" + " " + "Node" + " " + "Distance");
        kruskalMST_Adjacency_Matrix(adjacency_Matrix);
    }

    static int find(int i) {
        while (parent[i] != i) i = parent[i];
        return i;
    }

    static void unions(int i, int j) {
        int a = find(i);
        int b = find(j);
        parent[a] = b;
    }

}
