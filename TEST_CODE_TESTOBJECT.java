/*
 * This is a simple object with a few properties meant to test my data structures ability to handle comparable objects.
 * It is meant to be created in batches with for loops when needed
 * I forget when I wrote this but It's going to my github repo on 5/13/2019
 */

public class TEST_CODE_TESTOBJECT implements Comparable<TEST_CODE_TESTOBJECT> {

	private int me;
	private int myVal;
	private String myName;
	
	public TEST_CODE_TESTOBJECT(int me){
		this.me = me;
		myVal = me+100;
		myName = ("I am # " + me);
	}
	
	public int getMe() { return me; }
	public int getMyVal() { return myVal; }
	public String getMyName() { return myName; } 
	
	public int compareTo(TEST_CODE_TESTOBJECT them) {
		if (this.me > them.getMe()) { return 1; }
		if (this.me < them.getMe()) { return -1; }
		else { return 0; }
	}
	
	@Override
	public String toString() {
		String result = (myName);
		return result;
	}
}