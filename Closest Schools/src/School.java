

/*
* Author: Shelby Kimmel
* School.java creates School objects which store the name and position of each school.
* 

*/

import java.io.File;
import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.lang.Math;
import java.io.File;
import java.util.Comparator;


public class School {

	//xpos and ypos provide the x-coordinate and y-coordinate of the school location, respectively.
	//name is the name of the school
	//side is the side that the School was sorted to when dividing the data set 
	double xpos;
	double ypos;
	String name;
	char side;


	public School(String name, double xpos, double ypos) {
		this.name=name;
		this.xpos=xpos;
		this.ypos=ypos;
	}

	public void setSide(char side) {
		this.side = side; 
	}
	
	public String getName(){
		return name;
	}

	public double getX(){
		return xpos;
	}

	public double getY(){
		return ypos;
	}
	
	public char getSide() {
		return side; 
	}

	@Override
	public String toString(){
		return this.name + " is at position (" + this.xpos+ ","+this.ypos+")";
	}
}

//The following are based on https://www.geeksforgeeks.org/comparator-interface-java/

class SortbyX implements Comparator<School> {
	// Used to sort Schools by X-position
	public int compare(School a, School b){
		return Double.compare(a.getX(),b.getX());
	}
}

class SortbyY implements Comparator<School> {
	// Used to sort Schools by Y-position
	public int compare(School a, School b){
		return Double.compare(a.getY(),b.getY());
	}
}