/*
This is a generic java node class, written by Robert De La Cruz II on 5/13/2019
Nothing special about it, it will be the one used to create multiple well known data structures as practice code.

The design schema currently being that the Node will be a wrapper for the generic object (it will hold anything), and the data structures will then hold Nodes.
*/

public class Node<E>{
    private E data;
    private Node<E> link0;
    private Node<E> link1;
    private Node<E> link2;

    public Node (E data) {
        this.data = data;
        link0 = null;
        link1 = null;      //Not all data structures will use this
        link2 = null;      //Not all data structures will use this
    }


    /**************/
    /*   Getters  */
    /**************/
    public E getData() {
        return data;
    }

    public Node<E> getLink0() {
        return link0;
    }

    public Node<E> getLink1() {
        return link1;
    }

    public Node<E> getLink2() {
        return link2;
    }

    /* Special getters for trees */
    public Node<E> getLeftChild() {
        return link0;
    }

    public Node<E> getRightChild() {
        return link2;
    }
    
    /**************/
    /*   Setters  */
    /**************/
    void setData(E data) {
        this.data = data;
    }

    void setLink0(Node<E> n) {
        link0 = n;
    }

    void setLink1(Node<E> n) {
        link1 = n;
    }

    void setLink2(Node<E> n) {
        link2 = n;
    }

    /* Special setters for trees */

    void setleftChild(Node<E> n) {
        link0 = n;
    }

    void setRightChild(Node<E> n) {
        link2 = n;
    }

    @Override
    public String toString() {
        String result = "Data = " + data + ";\nLink0 AKA LeftChild = " + link0 + ";\nLink1 = " + link1 + ";\nLink2 AKA RightChild = " + link2;
        return result;
    }
}