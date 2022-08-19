import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Project3_1_1 {

    public static void mergeGraph(String filePath1, String filePath2) throws IOException {

        Scanner sc = new Scanner(new BufferedReader(new FileReader(filePath1)));
        sc.nextLine();
        Scanner sc2 = new Scanner(new BufferedReader(new FileReader(filePath2)));
        sc2.nextLine();

        HashMap<String, String> merged_Nodes = new HashMap<>();
        HashMap<String, String> nodes_1 = new HashMap<>();
        HashMap<String, String> nodes_2 = new HashMap<>();

        while (sc.hasNext()) {
            String[] column = sc.nextLine().trim().split(",");
            String coordinate_Name = column[3] + "," + column[4];
            if(!nodes_1.containsKey(column[0])) nodes_1.put(column[0], coordinate_Name);
            if (!merged_Nodes.containsKey(coordinate_Name)) {
                String size = merged_Nodes.size() + "";
                merged_Nodes.put(coordinate_Name, size);
            }
        }

        while (sc2.hasNext()) {
            String[] column = sc2.nextLine().trim().split(",");
            String coordinate_Name = column[3] + "," + column[4];
            if(!nodes_2.containsKey(column[0])) nodes_2.put(column[0], coordinate_Name);
            if (!merged_Nodes.containsKey(coordinate_Name)) {
                String size = merged_Nodes.size() + "";
                merged_Nodes.put(coordinate_Name, size);
            }
        }
        int lineNum = merged_Nodes.size();
        String[][] adjacency_Matrix = new String[lineNum][lineNum];
        for (String[] temp : adjacency_Matrix) {
            Arrays.fill(temp, "0");
        }

        sc = new Scanner(new BufferedReader(new FileReader(filePath1)));
        sc.nextLine();
        sc2 = new Scanner(new BufferedReader(new FileReader(filePath2)));
        sc2.nextLine();

        while (sc.hasNextLine()) {
            String[] column = sc.nextLine().trim().split(",");
            String coordinate_Name = column[3] + "," + column[4];
            int nodeID = Integer.valueOf(merged_Nodes.get(coordinate_Name));
            int connected = Integer.valueOf(merged_Nodes.get(nodes_1.get(column[1])));
            adjacency_Matrix[nodeID][connected] = column[2];
        }

        while (sc2.hasNextLine()) {
            String[] column = sc2.nextLine().trim().split(",");
            String coordinate_Name = column[3] + "," + column[4];
            int nodeID = Integer.valueOf(merged_Nodes.get(coordinate_Name));
            int connected = Integer.valueOf(merged_Nodes.get(nodes_2.get(column[1])));
            adjacency_Matrix[nodeID][connected] = column[2];
        }

        writeToFile(adjacency_Matrix);
    }

    public static void writeToFile(String[][] arrays) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("./Project3_Input_Files/Project3_Merged_Data.csv"));
        StringBuilder stringBuilder = new StringBuilder();

        for (String[] temp : arrays) {
            for (String element : temp) {
                stringBuilder.append(element);
                stringBuilder.append(",");
            }
            stringBuilder.append(System.getProperty("line.separator"));
        }
        bufferedWriter.write(stringBuilder.toString());
        bufferedWriter.close();
    }

    public static void main(String[] args) throws IOException {

        String filePath1 = "./Project3_Input_Files/Project3_G1_Data.csv";
        String filePath2 = "./Project3_Input_Files/Project3_G2_Data.csv";

        mergeGraph(filePath1, filePath2);
    }
}