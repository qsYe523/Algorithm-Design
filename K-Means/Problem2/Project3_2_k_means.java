import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

class centroid {
    private List<Double> features;
    private List<dataNodes> nodes;
    private int clusterN;
    private boolean end;

    public centroid(List<Double> features, int clusterN) {
        this.features = features;
        this.clusterN = clusterN;
        this.nodes = new ArrayList<>();
        this.end = false;
    }

    public void clean() {
        this.nodes = new ArrayList<>();
        this.end = false;
    }

    public void assignNodes(dataNodes node) {
        this.nodes.add(node);
        node.assignCentroid(this);
    }

    public List<dataNodes> getNodes() {
        return this.nodes;
    }

    public List<Double> getFeatures() {
        return this.features;
    }


    public void moveTo(List<Double> features) {
        this.features = features;
    }

    public int getClusterID() {
        return this.clusterN;
    }

    public void moveToNewPos() {
        double[] featureArray = new double[this.features.size()];
        Arrays.fill(featureArray, 0.0);
        for (dataNodes node : this.nodes) {
            List<Double> nodeFeature = node.getFeatures();
            for (int i = 0; i < nodeFeature.size(); i++) {
                featureArray[i] += nodeFeature.get(i);
            }
        }
        for (int i = 0; i < featureArray.length; i++) {
            featureArray[i] /= this.nodes.size();
        }
        if (testIsEnd(featureArray)) {
            this.end = true;
        } else {
            List<Double> temp = new ArrayList<>();
            for (int i = 0; i < featureArray.length; i++) {
                temp.add(featureArray[i]);
            }
            moveTo(temp);
        }
    }

    public boolean testIsEnd(double[] featureArray) {
        for (int i = 0; i < featureArray.length; i++) {
            if (featureArray[i] != this.features.get(i)) return false;
        }
        return true;
    }

    public boolean isEnd() {
        return this.end;
    }

    public double getDistance(dataNodes node) {
        double dis = 0;
        List<Double> nodeFeatures = node.getFeatures(), cenFeatures = this.getFeatures();
        for (int i = 0; i < nodeFeatures.size(); i++) {
            dis += Math.pow(nodeFeatures.get(i) - cenFeatures.get(i), 2);
        }
        return Math.abs(Math.sqrt(dis));
    }

    public double getLost() {
        double totalLost = 0.0;
        for (dataNodes node : nodes) {
            totalLost += getDistance(node);
        }
        return totalLost / nodes.size();
    }
}

class dataNodes {
    private List<Double> features;
    private centroid centroidNode;

    public dataNodes(List<Double> features) {
        this.features = features;
        this.centroidNode = null;
    }

    public void assignCentroid(centroid node) {
        this.centroidNode = node;
    }

    public int getClusterID() {
        if (centroidNode == null) {
            System.out.println("THIS NODE HAVE NOT BEEN ASSIGNED");
            return -1;
        }
        return centroidNode.getClusterID();
    }

    public List<Double> getFeatures() {
        return this.features;
    }
}
public class Project3_2_k_means {

    private static List<dataNodes> allNodes;
    private static List<centroid> allCentroids;

    public static void main(String[] args) throws FileNotFoundException {
        allNodes = new ArrayList<>();
        Scanner sc = new Scanner(new BufferedReader(new FileReader("./Project3_Input_Files/Project3_Power_Consumption.csv")));
        sc.nextLine();
        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().trim().split(",");
            List<Double> features = new ArrayList<>();
            for (String temp : line) {
                features.add(Double.parseDouble(temp));
            }
            allNodes.add(new dataNodes(features));
        }
        for (int i = 3; i <= 20; i++) {
            kMeans(i);
        }
    }

    public static void kMeans(int k) {
        allCentroids = new ArrayList<>();
        int nIteration = 1;
        initializeClusterCentroids(k);
        while (!isEnd()) {
            assignCentroid();
            moveCentroids();
            printLastReport(nIteration);
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
            dataNodes nodePicked = allNodes.get(nextCent);
            allCentroids.add(new centroid(nodePicked.getFeatures(), i));
        }
    }

    public static void assignCentroid() {
        for (centroid centroids : allCentroids) {
            centroids.clean();
        }
        for (dataNodes nodes : allNodes) {
            centroid closestCentroid = allCentroids.get(0);
            double distance = Double.MAX_VALUE;
            for (centroid centroids : allCentroids) {
                if (centroids.getDistance(nodes) < distance) {
                    distance = centroids.getDistance(nodes);
                    closestCentroid = centroids;
                }
            }
            closestCentroid.assignNodes(nodes);
        }
    }

    public static void moveCentroids() {
        for (centroid centroid : allCentroids) {
            centroid.moveToNewPos();
        }
    }

    public static boolean isEnd() {
        for (centroid centroid : allCentroids) {
            if (!centroid.isEnd()) return false;
        }
        return true;
    }

    public static void printLastReport(int n) {
        if (!isEnd()) return;
        double totalLost = 0.0;
        System.out.println("_________k = " + allCentroids.size() + "__________");
        System.out.print("[ ");
        for (centroid node : allCentroids) {
            System.out.print("[ ");
            for (int i = 0; i < node.getFeatures().size(); i++) {
                System.out.print(node.getFeatures().get(i) + " ");
            }
            System.out.print("]");
            totalLost += node.getLost();
        }
        System.out.println(" ]");
        System.out.print("[ ");
        for (centroid node : allCentroids) {
            System.out.print(node.getNodes().size() + " ");
            totalLost += node.getLost();
        }
        System.out.println("]");
        System.out.println("After " + (n - 1) + " times iteration, the loss is: " + (totalLost / allCentroids.size()));
        System.out.println(" ");
    }
}