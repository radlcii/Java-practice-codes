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
    public void setData(E data) {
        this.data = data;
    }

    public void setLink0(Node<E> n) {
        link0 = n;
    }

    public void setLink1(Node<E> n) {
        link1 = n;
    }

    public void setLink2(Node<E> n) {
        link2 = n;
    }

    /* Special setters for trees */

    public void setLeftChild(Node<E> n) {
        link0 = n;
    }

    public void setRightChild(Node<E> n) {
        link2 = n;
    }

    @Override
    public String toString() {
        /* To prevent recursive printing and null pointer exceptions, the links returns had to be checked individually and amalgamated for the returned string */
        String result = "";
        String link_0_Filler;
        String link_1_Filler;
        String link_2_Filler;
        
        if (link0 == null) { link_0_Filler = "null"; }
        else { link_0_Filler = this.getLink0().getData().toString(); }
        
        if (link1 == null) { link_1_Filler = "null"; }
        else { link_1_Filler = this.getLink1().getData().toString(); }
        
        if (link2 == null) { link_2_Filler = "null"; }
        else { link_2_Filler = this.getLink2().getData().toString(); }

        return "Data = " + this.getData().toString() + ";\n" + 
                  "Link0 AKA LeftChild = " + link_0_Filler + ";\n" + 
                  "Link1 = " + link_1_Filler + ";\n" +
                  "Link2 AKA RightChild = " + link_2_Filler;
    }
}