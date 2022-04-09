
/*
* Author: Katie Macalintal w/ starter code from Professor Kimmel 
* Implements the closest pair of points recursive algorithm
* on locations of K-12 schools in Vermont obtained from http://geodata.vermont.gov/datasets/vt-school-locations-k-12

*/

import java.io.File;
import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;
import java.io.File;


public class Main {


	public static void main(String[] args) throws IOException{
		
		
		//Creates an ArrayList containing School objects from the .csv file
		// Based on https://stackoverflow.com/questions/49599194/reading-csv-file-into-an-arrayliststudent-java
		String line = null;
		ArrayList<School> schoolList = new ArrayList <School>();
		// You may have to adjust the file address in the following line to your computer
		BufferedReader br = new BufferedReader(new FileReader("./src/Data/VT_School_Locations__K12(1).csv"));
		if ((line=br.readLine())==null){
			return;
		}
		while ((line = br.readLine())!=null) {
			String[] temp = line.split(",");
			schoolList.add(new School(temp[4],Double.parseDouble(temp[0]),Double.parseDouble(temp[1])));
		}
		


		//Preprocess the data to create two sorted arrayLists (one by X-coordinate and one by Y-coordinate):
		ArrayList<School> Xsorted = new ArrayList <School>();
		ArrayList<School> Ysorted = new ArrayList <School>();
		Collections.sort(schoolList, new SortbyX());
		Xsorted.addAll(schoolList);
		Collections.sort(schoolList, new SortbyY());
		Ysorted.addAll(schoolList);


		
		//Run the Recursive Algorithm
		School[] cp = new School[2];
		cp = ClosestPoints(Xsorted,Ysorted);
		if(cp[0]!=null)
			System.out.println("The two closest schools are "+ cp[0].name + " and " + cp[1].name +".");
		
	}

	/**
	 * ClosestPoints ()
	 * Recursive divide and conquer algorithm for closest points
	 * sLx and sLy are ArrayLists containing the same School objects, but sorted differently 
	 * 
	 * @param sLx - ArrayList of School objects sorted by x coordinates 
	 * @param sLy - ArrayList of School objects sorted by y coordinates 
	 * @return closestPair - Array of size two containing the two School objects that are closest to each other
	 */
	public static School[] ClosestPoints(ArrayList<School> sLx, ArrayList<School> sLy){
		
		
		School[] closestPair = new School[2];

		
		
		// ------------------------------BASE CASE----------------------------------
		if ((sLx.size() <= 3) && (sLy.size() <= 3)) {
			return bruteForce(sLx); 
		}
		
		
		
		// -------------------------DIVIDE INTO L AND R------------------------------
		
		// Divide according to x coordinates first 
		ArrayList<School> Lx = new ArrayList<School>(); // L points sorted by x coordinates
		ArrayList<School> Rx = new ArrayList<School>(); // R points sorted by y coordinates 
	
		// Want to divide input into equally sized halves 
		int midIndex = sLx.size() / 2; 
		
		// Dividing sLx into equally sized ArrayLists L and R by x-coordinates 
		for(int i = 0; i < sLx.size(); i++) {
			if (i < midIndex){
				sLx.get(i).setSide('L'); // Want to keep track of which side it was sorted to 
				Lx.add(sLx.get(i)); 
			} else {
				sLx.get(i).setSide('R'); // Want to keep track of which side it was sorted to 
				Rx.add(sLx.get(i)); 
			} 
		}
		
		// Value of the xMidline that divides the input into equal parts 
		double xMidline = (Lx.get(midIndex - 1).getX() + Rx.get(0).getX()) / 2 ; 		
		
		
		// Divide the y sorted array accordingly 
		ArrayList<School> Ly = new ArrayList<School>(); 	
		ArrayList<School> Ry = new ArrayList<School>(); 
		
		
		// Dividing sLy into equal ArrayLists L and R based on their x-coordinates 
		// need to base it off their x-coordinates to ensure that Ly has the same School objects as Lx 
		for(int i = 0; i < sLy.size(); i++) {
			// Get the attribute of the School object that tracks which side it was sorted to 
			if (sLy.get(i).getSide() == 'L') {
				Ly.add(sLy.get(i)); 
			} else {
				Ry.add(sLy.get(i)); 
			}
		}
		
		
		
		// Recursively call the ClosestPoints
		School[] LPoints = ClosestPoints(Lx, Ly); 
		double LMinDistance = get2SchoolsDistance(LPoints); 
		 
		School[] RPoints = ClosestPoints(Rx, Ry); 
		double RMinDistance = get2SchoolsDistance(RPoints); 
		
		double delta; // Smallest distance found in either the left or right side exclusively
		
		// Want to find which half had the smaller distance and update the closestPair 
		if (LMinDistance < RMinDistance) {
			delta = LMinDistance; 
			closestPair = LPoints; 
		} else {
			delta = RMinDistance;
			closestPair = RPoints; 
		}


		
		
		
		//-----------------------------COMBINE--------------------------------------
		
		
		
		// Array of points w/in delta of xMidline sorted by y coordinate
		ArrayList<School> yDelta = getYDelta(sLy, xMidline, delta); 

		
		// Check the next 7 points 
		for(int i = 0; i < yDelta.size(); i++) {
			// Want to account for when yDelta may have less than 7 points left 
			if (yDelta.size() - i <= 7) {
				// If the array has less than 7 points left in the array to check, want to check the rest of the array 
				for(int j = i + 1; j < yDelta.size(); j++) {
					double deltaPrime = get2SchoolsDistance(new School[]{yDelta.get(i), yDelta.get(j)}); 
					// If smaller distance found, update variables 
					if (deltaPrime < delta) {
						delta = deltaPrime; 
						closestPair[0] = yDelta.get(i); 
						closestPair[1] = yDelta.get(j); 
					}
				}
			} else { // yDelta has more than 7 points left to check 
				// Want to check the next 7 points 
				for(int j = 1; j < 8; j++) {
					double deltaPrime = get2SchoolsDistance(new School[]{yDelta.get(i), yDelta.get(i + j)}); 
					// If smaller distance found, update variables 
					if (deltaPrime < delta) {
						delta = deltaPrime; 
						closestPair[0] = yDelta.get(i); 
						closestPair[1] = yDelta.get(i + j); 
					}
				}
			}
		}
		
		
		return closestPair;

		
	}
	
	/**
	 * get2SchoolsDistance() 
	 * Calculates the distance between two School objects 
	 * 
	 * @param points - Array of size two containing two School objects 
	 * @return distance - Distance between the two schools (double) 
	 */
	public static double get2SchoolsDistance(School[] points) {
		
		double xDifference = points[0].getX() - points[1].getX(); 
		double yDifference = points[0].getY() - points[1].getY(); 
		double distance = Math.sqrt(Math.pow(xDifference, 2) + Math.pow(yDifference, 2)); 
		return distance; 
		
	}
	
	
	/**
	 * bruteForce() 
	 * Calculates the distance between every pair of points given through the parameter schools 
	 * and keeps track of which schools have the shortest distance from each other 
	 * 
	 * @param schools - small ArrayList of School objects 
	 * @return closestPair - Array of size two containing the two School objects that are closest to each other 
	 */
	public static School[] bruteForce(ArrayList<School> schools) {
		School[] closestPair = new School[2];

		double delta = Double.POSITIVE_INFINITY;
		for(int i = 0; i < schools.size() - 1; i++) {
			for(int j = i + 1; j < schools.size(); j++) {
	
				double distance = get2SchoolsDistance(new School[] {schools.get(i), schools.get(j)});  
				if (distance < delta) {
					delta = distance; 
					closestPair[0] = schools.get(i); 
					closestPair[1] = schools.get(j); 
				}
				
			}
		}
		
		return closestPair; 
	}
	
	
	/**
	 * 
	 * getYDelta() 
	 * Create an array of points that are less than delta away from the XMidline 
	 * and are sorted according to their Y coordinates 
	 * 
	 * @param sLy - ArrayList of School objects sorted by their y coordinate 
	 * @param xMidline - x value of the midline that splits the original input into two equal sizes 
	 * @param delta - the smallest distance found in the recursive calls
	 * @return yDelta - ArrayList of School objects that are less than delta away from the XMidline and sorted 
	 */
	public static ArrayList<School> getYDelta(ArrayList<School> sLy, double xMidline, double delta) {
		ArrayList<School> yDelta = new ArrayList<School>(); 
		
		for(int i = 0; i < sLy.size(); i++) {
			 double xDifference = sLy.get(i).getX() - xMidline; 
			 double distance = Math.sqrt(Math.pow(xDifference, 2)); 
			 if (distance < delta) {
				 yDelta.add(sLy.get(i)); 
			 }
		}
		
		return yDelta; 
		
	}

	

}