package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import martian_stuff.*;

public class MartianTester {

	static File martiansIn = new File( "src\\application\\martiansIn.txt" );
	File martiansOut = new File( "src//application//martiansOut.txt" );
	static Scanner feedMe;
	MartianManager mm;

	public static void main(String[] args) throws FileNotFoundException {
		MartianManager mm = new MartianManager();
		mm.addMartian(new GreenMartian(33,99,true,false));

		feedMe = new Scanner(martiansIn);
		while(feedMe.hasNext()){
			String rgb = "";
			int id = -1;
			int volume = 0;
			boolean hasESP = false;
			boolean isVegetarian = false;

			rgb = feedMe.next();
			id = feedMe.nextInt();
			if(feedMe.hasNextInt())
				volume = feedMe.nextInt();
			if(feedMe.hasNext() && feedMe.next().equalsIgnoreCase("T"))
				hasESP = true;
			if(feedMe.hasNext() && feedMe.next().equalsIgnoreCase("T"))
				isVegetarian = true;

			if(rgb.equalsIgnoreCase("R") && id > -1){
				mm.addMartian(new RedMartian(id, volume, hasESP, isVegetarian));
				System.out.print("Red:Successful ");
			}
			if(rgb.equalsIgnoreCase("G") && id > -1){
				mm.addMartian(new GreenMartian(id, volume, hasESP, isVegetarian));
				System.out.print("Green:Successful ");
			}
		}
		feedMe.close();

		System.out.println(mm.groupSpeak());
		/*
		for(int i=0; i<mm.getNumMartians(); i++) {
			System.out.println(mm.getMartianAt(i));
		}
		*/
		System.out.print(mm.groupTeleport("Mars"));
	}

}
