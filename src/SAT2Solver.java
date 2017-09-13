import java.util.*;

/**
 * Created by younes on 13/09/2017.
 */

public class SAT2Solver {

    /**
     * @param set : clauses list, each element is 2-dimension array holding the two litterals of a clause
     * @return : true if the set is satisfiable, false otherwise
     */
    public static boolean solve(ArrayList<Literal[]> set){

        //construct the graph
        HashMap<String, Node> graph= new HashMap<>();
        for (Literal[] clause : set) {
            //each clause gives two implications, for example : a -b gives (-a -> b) , (b -> a)
            addEdgeToGraphe(clause[0], clause[1], graph);
            addEdgeToGraphe(clause[1], clause[0], graph);
        }

        //finding SCCs (Strongly Connected Component) using Kosaraju's algorithm
        //the algorithm is described here : https://en.wikipedia.org/wiki/Kosaraju%27s_algorithm
        //Kosaraju's algorithm: first step
        LinkedList<Node> nodeLinkedList =  new LinkedList<>();
        for (Node node : graph.values()) {
            visit(node, nodeLinkedList);
        }
        //Kosaraju's algorithm: last step
        for (Node node : nodeLinkedList) {
            if(node.SCC==-1)
                assign(node, Node.currentSCC++);
        }

        //last step of Aspvall, Plass & Tarjan algorithm
        //checking if a literal and its compliment belong to the same SCC
        LinkedList<Node> nodes = new LinkedList<>(graph.values());
        while (!nodes.isEmpty()){
            Node literal = nodes.removeFirst();
            if(literal.SCC==literal.compliment.SCC)
                return false;
            nodes.remove(literal.compliment);
        }

        return true;
    }


    /**
     * check Kosaraju's algorithm
     * @param node the node to affect to a SCC
     * @param SCC the Strongly Connected Component
     */
    private static void assign(Node node, int SCC) {
        if(node.SCC==-1) {
            node.SCC= SCC;
            for (Node inNode : node.inNodes) {
                assign(inNode, SCC);
            }
        }
    }

    /**
     * @param node the node to visit (check Kosaraju's algorithm)
     * @param nodeLinkedList the nodes list (check Kosaraju's algorithm)
     */
    private static void visit(Node node, LinkedList<Node> nodeLinkedList) {
        if(!node.visited){
            node.visited= true;
            for (Node outNode : node.outNodes) {
                visit(outNode, nodeLinkedList);
            }
            nodeLinkedList.addFirst(node);
        }
    }


    /**
     * add an edge (and the rwo vertex to the graph)
     * @param lit_1 first literal of a clause
     * @param lit_2 second literal of a clause
     * @param graph the graph variable
     */
    private static void addEdgeToGraphe(Literal lit_1, Literal lit_2, HashMap<String, Node> graph){
        //getting the compliment of the first literal ( to convert a clause to an implication we use the compliment of the first literal, obvious?)
        String literal1Compliment= lit_1.toString();
        if(literal1Compliment.charAt(0)=='-')
            literal1Compliment=  literal1Compliment.substring(1);
        else
            literal1Compliment= "-"+literal1Compliment;

        //check existence of node1 in the graph, if it doesn't exist so add it, we add each literal once
        Node node_1;
        if(!graph.containsKey(literal1Compliment)) {
            node_1 = new Node(literal1Compliment);
            graph.put(literal1Compliment, node_1);
        } else
            node_1= graph.get(literal1Compliment);

        //check existence of node2 in the graph, if it doesn't exist so add it, we add each literal once
        Node node_2;
        String lit2name = lit_2.toString();
        if(!graph.containsKey(lit2name)) {
            node_2 = new Node(lit2name);
            graph.put(lit2name, node_2);
        } else
            node_2= graph.get(lit2name);

        //linking nodes, as it as an implication, one node is a successor of the other, and the other is a predecessor of the first
        node_1.addOutNode(node_2);
        node_2.addInNode(node_1);

        // try to link literals to their compliments. if the graph contains the compliment of this literal so add it to the compliment field of the node
        if(node_1.compliment==null)
            linkNodeToCompliment(node_1, graph);
        if(node_2.compliment==null)
            linkNodeToCompliment(node_2, graph);
    }

    /**
     * connect a literal to its compliment by affecting each one to the "compliment" field of the other
     * @param node the node (literal) to add as a compliment
     * @param graph the graph object
     */
    private static void linkNodeToCompliment(Node node, HashMap<String, Node> graph) {
        String complimentName= node.name.charAt(0)=='-' ? node.name.substring(1) : "-"+node.name;
        if(graph.containsKey(complimentName)){
            Node compliment = graph.get(complimentName);
            compliment.compliment= node;
            node.compliment= compliment;
        }
    }

    /**
     * Represents a graph node
     * static int currentSCC : tell the SCC index affected, this field is static, it is used to affect unique labels to SCCs
     * Node compliment : the compliment of a literal, this is a cross field, the other node's compliment point to the current
     * int SCC : SCC to which this literal belongs
     * boolean visited : tell if the node is visited, used in Kosaraju's algorithm
     * LinkedList<Node> outNodes : successors of the node, used in Kosaraju's algorithm
     * LinkedList<Node> inNodes : predecessors of the node, used in Kosaraju's algorithm
     * String name : node name; the literal
     *
     * methods are simple
     */
    static class Node{
        static int currentSCC= 0;
        Node compliment= null;
        int SCC= -1;
        boolean visited= false;
        LinkedList<Node> outNodes= new LinkedList<>();
        LinkedList<Node> inNodes= new LinkedList<>();

        String name;

        public Node(String name) {
            this.name = name;
        }

        public void addInNode(Node node){
            inNodes.add(node);
        }

        public void addOutNode(Node node){
            outNodes.add(node);
        }

        @Override
        public boolean equals(Object o) {
            return name.equals(((Node) o).name);
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
