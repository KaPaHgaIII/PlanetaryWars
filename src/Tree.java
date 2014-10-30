import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Tree<T> {

    private Node<T> root;
    private Node<T> iterator;
    private Node<T> memory;

    private List<Node<T>> allNodes;

    public Tree(T rootObj) {
        allNodes = new LinkedList<Node<T>>();

        root = new Node<T>();
        root.obj = rootObj;
        root.children = new ArrayList<Node<T>>();

        allNodes.add(root);

        startIteration();
    }

    public void startIteration() {
        iterator = root;
        for (Node<T> node : allNodes) {
            node.iterated = false;
        }

    }

    public class Node<T> {
        private T obj;
        private Node<T> parent;
        private List<Node<T>> children;
        private boolean iterated;


        public Node() {
            children = new ArrayList<Node<T>>();
        }
    }


    public void add(T obj) {
        Node<T> child = new Node<T>();
        child.obj = obj;
        child.parent = iterator;
        iterator.children.add(child);
        allNodes.add(child);
    }

    public T get() {
        return iterator.obj;
    }

    public boolean haveChildren() {
        return !iterator.children.isEmpty();
    }

    public void goToFirstChild() {
        iterator.iterated = true;
        iterator = iterator.children.get(0);
    }

    public boolean isNext() {
        if (iterator != root) {
            List<Node<T>> brothers = iterator.parent.children;
            return brothers.indexOf(iterator) < brothers.size() - 1;
        } else {
            return false;
        }
    }

    public void goToNext() {
        iterator.iterated = true;
        List<Node<T>> brothers = iterator.parent.children;
        iterator = brothers.get(brothers.indexOf(iterator) + 1);
    }

    public void goToParent() {
        iterator.iterated = true;
        iterator = iterator.parent;
    }

    public boolean wasIterated() {
        return iterator.iterated;
    }

    public boolean isRoot() {
        return iterator == root;
    }

    public int size() {
        return allNodes.size();
    }

    public void rememberElement(){
        memory = iterator;
    }

    public void goToMemory(){
        iterator.iterated = true;
        iterator = memory;
    }
}