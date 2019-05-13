/*
Just some code to produce the node objects and prove they can take another object as an arguement
Written by Robert De La Cruz II on 5/13/2019 as part of getting myself back onto the coding wagon.
*/

public class NodeDriver<E> {

   public static void main(String[] args) {
      Node<TEST_CODE_TESTOBJECT> node1 = new Node<TEST_CODE_TESTOBJECT>(new TEST_CODE_TESTOBJECT(1));
      Node<TEST_CODE_TESTOBJECT> node2 = new Node<TEST_CODE_TESTOBJECT>(new TEST_CODE_TESTOBJECT(2));
      Node<TEST_CODE_TESTOBJECT> node3 = new Node<TEST_CODE_TESTOBJECT>(new TEST_CODE_TESTOBJECT(3));
      Node<TEST_CODE_TESTOBJECT> node4 = new Node<TEST_CODE_TESTOBJECT>(new TEST_CODE_TESTOBJECT(4));
      Node<TEST_CODE_TESTOBJECT> node5 = new Node<TEST_CODE_TESTOBJECT>(new TEST_CODE_TESTOBJECT(5));

      System.out.println(node1.toString() + "\n");
      System.out.println(node2.toString() + "\n");
      System.out.println(node3.toString() + "\n");
      System.out.println(node4.toString() + "\n");
      System.out.println(node5.toString() + "\n");
   }
}