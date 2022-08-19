import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class Project3_1_3 {

    static int totalLine = 1993;
    static int[] parent = new int[totalLine];
    static int INF = Integer.MAX_VALUE;

    static void kruskalMST_LinkedList(LinkedList<Integer> list) {
        int minDistance = 0;
        for (int i = 0; i < totalLine; i++)
            parent[i] = i;

        int count = 0;
        while (count < totalLine - 1) {
            int minCost = INF, a = -1, b = -1;
            int i = 2, j = 1;
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                int cur = (int) iterator.next();
                if (find(i-1) != find(j-1) && cur < minCost) {
                    minCost = cur;
                    a = i-1;
                    b = j-1;
                }
                if (i == j + 1) {
                    i++;
                    j = 1;
                } else {
                    j++;
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
        LinkedList<Integer> list = new LinkedList<>();
        while (sc.hasNextLine()) {
            for (int i = 0; i < totalLine; i++) {
                String[] column = sc.nextLine().trim().split(",");
                for (int j = 0; j < i; j++) {
                    list.add(column[j].equals("0") ? INF : Integer.parseInt(column[j]));
                }
            }
        }
        System.out.println("Node" + " " + "Node" + " " + "Distance");
        kruskalMST_LinkedList(list);
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
