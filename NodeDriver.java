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
      Node<TEST_CODE_TESTOBJECT> node6 = new Node<TEST_CODE_TESTOBJECT>(new TEST_CODE_TESTOBJECT(6));
      Node<TEST_CODE_TESTOBJECT> node7 = new Node<TEST_CODE_TESTOBJECT>(new TEST_CODE_TESTOBJECT(7));
            
      node1.setLeftChild(node2);
      node1.setRightChild(node3);
      
      node2.setLeftChild(node4);
      node2.setRightChild(node5);
      
      node3.setLeftChild(node6);
      node3.setRightChild(node7);
      
      node4.setLink0(node1);
      node4.setLink1(node2);
      node4.setLink2(node3);
      
      node5.setLink0(node4);
      node5.setLink2(node6);
      
      node6.setLink1(node1);
      
      node7.setRightChild(node4);
      
      System.out.println(node1.toString() + "\n");
      System.out.println(node2.toString() + "\n");
      System.out.println(node3.toString() + "\n");
      System.out.println(node4.toString() + "\n");
      System.out.println(node5.toString() + "\n");
      System.out.println(node6.toString() + "\n");
      System.out.println(node7.toString() + "\n");
   }
}