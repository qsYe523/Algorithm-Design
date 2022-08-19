import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

class Project3_2_centroid {
    private double x1;
    private double x2;

    private List<Project3_2_dataNodes> nodes;
    private int clusterN;
    private boolean end;

    public Project3_2_centroid(double x1, double x2, int clusterN) {
        this.x1 = x1;
        this.x2 = x2;
        this.clusterN = clusterN;
        this.nodes = new ArrayList<>();
        this.end = false;
    }

    public void clean() {
        this.nodes = new ArrayList<>();
        this.end = false;
    }

    public void assignNodes(Project3_2_dataNodes node) {
        this.nodes.add(node);
        node.assignCentroid(this);
    }

    public List<Project3_2_dataNodes> getNodes() {
        return this.nodes;
    }

    public double getX1() {
        return this.x1;
    }

    public double getX2() {
        return this.x2;
    }


    public void moveTo(double nx1, double nx2) {
        this.x1 = nx1;
        this.x2 = nx2;
        //this.clean();
    }

    public int getClusterID() {
        return this.clusterN;
    }

    public void moveToNewPos() {
        double sumX1 = 0.0, subX2 = 0.0;
        for (Project3_2_dataNodes node : nodes) {
            sumX1 += node.getX1();
            subX2 += node.getX2();
        }
        double nx1 = sumX1 / this.nodes.size();
        double nx2 = subX2 / this.nodes.size();
        if (nx1 == this.x1 && nx2 == this.x2) {
            this.end = true;
        } else {
            moveTo(nx1, nx2);
        }
    }

    public boolean isEnd(){
        return this.end;
    }

    public double getDistance(Project3_2_dataNodes node, Project3_2_centroid cenNode) {
        return Math.abs(Math.sqrt(Math.pow(node.getX1() - cenNode.getX1(), 2) + Math.pow(node.getX2() - cenNode.getX2(), 2)));
    }

    public double getLost(){
        double totalLost = 0.0;
        for(Project3_2_dataNodes node : nodes){
            totalLost += getDistance(node, this);
        }
        return totalLost/nodes.size();
    }

}

class Project3_2_dataNodes {
    private double x1;
    private double x2;
    private int y;
    private Project3_2_centroid centroidNode;

    public Project3_2_dataNodes(double x1,double x2, int y){
        this.x1 = x1;
        this.x2 = x2;
        this.y = y;
        this.centroidNode = null;
    }

    public void assignCentroid(Project3_2_centroid node){
        this.centroidNode = node;
    }

    public int getClusterID(){
        if(centroidNode == null){
            System.out.println("THIS NODE HAVE NOT BEEN ASSIGNED");
            return -1;
        }
        return centroidNode.getClusterID();
    }

    public double getX1(){
        return this.x1;
    }

    public double getX2(){
        return this.x2;
    }

}

public class Project3_2_k_means_test {
    private static List<Project3_2_dataNodes> allNodes;
    private static List<Project3_2_centroid> allCentroids;

    public static void main(String[] args) throws FileNotFoundException {
        allNodes = new ArrayList<>();
        Scanner sc = new Scanner(new BufferedReader(new FileReader("./Project3_Input_Files/Project3_Test_Case.csv")));
        sc.nextLine();
        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().trim().split(",");
            allNodes.add(new Project3_2_dataNodes(Double.parseDouble(line[0]), Double.parseDouble(line[1]), Integer.parseInt(line[2])));
        }
        kMeans(2);

    }

    public static void kMeans(int k) {
        allCentroids = new ArrayList<>();
        int nIteration = 1;
        initializeClusterCentroids(k);
        while (!isEnd()) {
            assignCentroid();
            moveCentroids();
            printReport(nIteration);
            nIteration++;
        }
    }

    public static void initializeClusterCentroids(int k) {
        if (k > allNodes.size()) {
            System.out.println("Not Possible to do with more k then size");
            return;
        }
        Set<Integer> picked = new HashSet<>();
        Random rand = new Random();
        for (int i = 0; i < k; i++) {
            int nextCent = rand.nextInt(allNodes.size() - 1);
            while (picked.contains(nextCent)) {
                nextCent = rand.nextInt(allNodes.size() - 1);
            }
            picked.add(nextCent);
            Project3_2_dataNodes nodePicked = allNodes.get(nextCent);
            allCentroids.add(new Project3_2_centroid(nodePicked.getX1(), nodePicked.getX2(), i));
        }
    }

    public static double getDistance(Project3_2_dataNodes node, Project3_2_centroid cenNode) {
        return Math.abs(Math.sqrt(Math.pow(node.getX1() - cenNode.getX1(), 2) + Math.pow(node.getX2() - cenNode.getX2(), 2)));
    }

    public static void assignCentroid() {
        for (Project3_2_centroid centroids : allCentroids) {
            centroids.clean();
        }
        for (Project3_2_dataNodes nodes : allNodes) {
            Project3_2_centroid closestCentroid = allCentroids.get(0);
            double distance = Double.MAX_VALUE;
            for (Project3_2_centroid centroids : allCentroids) {
                if (getDistance(nodes, centroids) < distance) {
                    distance = getDistance(nodes, centroids);
                    closestCentroid = centroids;
                }
            }
            closestCentroid.assignNodes(nodes);
        }
    }

    public static void moveCentroids() {
        for (Project3_2_centroid centroid : allCentroids) {
            centroid.moveToNewPos();
        }
    }

    public static boolean isEnd() {
        for (Project3_2_centroid centroid : allCentroids) {
            if (!centroid.isEnd()) return false;
        }
        return true;
    }

    public static void printReport(int n) {
        if (isEnd()) return;
        double totalLost = 0.0;
        System.out.println("Iteration " + n + ":");
        for (Project3_2_centroid node : allCentroids) {
            System.out.println("[" + node.getX1() + " " + node.getX2() + "]" + "  nodes number for cluster: " + node.getNodes().size());
            totalLost += node.getLost();
        }
        System.out.println("After " + n + " times iteration, the loss is: " + (totalLost/allCentroids.size()));
        System.out.println(" ");
    }

}