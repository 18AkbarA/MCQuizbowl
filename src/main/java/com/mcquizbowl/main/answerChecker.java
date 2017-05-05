/*
 * Class used to check if the answer given is correct
 * 
 * The way quizbowl works is that you dont have to give a full answer in some instances
 * for example, you only have to give the last name of people
 * 
 * protobowl accounts for this by putting {} around the necessary parts
 * 
 * like this:
 * 
 * answer: Emma {Watson}
 * 
 * but if there are more than one correct answers, there can be more than one set of {}
 * 
 * also, you have to account for someone typing the full name, not just the {} part
 */

package com.mcquizbowl.main;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import java.util.*;




public class answerChecker {
	
	
	public static ArrayList<Integer> usedIndices = new ArrayList<Integer>();
	public String answer;
	public String answerGiven;
	
	public answerChecker(String theAnswer , String theAnswerGiven){
		if (theAnswer.indexOf("{") > -1){
			answer = theAnswer.toLowerCase();
		}
		else{
			answer = "";
			String[] answerArray = answer.split(" ");
			for (String answerFragment: answerArray){
				answer += "{" + answerFragment + "}";
			}
		}
		answerGiven = theAnswerGiven.toLowerCase();
	}
	
	
	//Checks three possibilities: all acceptable answers are present, one acceptable answer is present, or the whole answer is present
	
	public boolean checkAllPossibilities(){
		
		boolean testOne = false;
		boolean testTwo = false;
		boolean testThree = false;
		
		String answerPartsTogether = "";
		String fullAnswer = answer.replace("{", "").replace("}", "");
		
		String firstAnswer = answer.replace("{", "EndOne").replace("}", "EndTwo");
		
		String[] answerArray = firstAnswer.replaceAll("^.*?EndOne", "").split("EndTwo.*?(EndOne|$)");
		
		
		for(String answerPart : answerArray){
			testOne = compareStrings(answerGiven, answerPart);
				if(testOne){
					break;
				}
				else{
					answerPartsTogether += answerPart;
				}
			
			
		}
		
		testTwo = compareStrings(answerGiven, answerPartsTogether);
		testThree = compareStrings(answerGiven,fullAnswer);
		
		
		return testOne || testTwo || testThree;
		
	}
	
	
	//This method goes through the correct answer, and then compares how closely the words in the answer given
	//match up with the actual correct answer.
	public static boolean compareStrings(String answerGiven, String answer){
		
		answer = answer.replace(" ", "");
		
		
		
		answerGiven = answerGiven.replace(" ", "");
		
		boolean returnBool = false;
		int compareScore = 0;
		int lastIndex = 0;
		
		int givenLength = answerGiven.length();
		int answerLength = answer.length();
		
		String resultString = answer;
		
		
		for(int i = 0; i < answerLength; i++){
			char answerChar = answer.charAt(i);
			
			
			
			
			int answerCharIndex = getIndex(answerGiven,answerChar,lastIndex);
			
			
			int backDistance = lastIndex - answerCharIndex;
			int distance = answerCharIndex - lastIndex;
			
			
		
			if((distance <= 2) && (distance >= -1)){
				
				resultString = StringUtils.replaceOnce(resultString, "" + answerChar, "");
				
			}
			
			
			lastIndex++;
		}
		
		
	        

		compareScore = resultString.length();
		
		
		usedIndices.clear();
		
		
		//The tolerance for the compare score is less than or equal to the length of the answer divided by three
		
		if(compareScore <= (answerLength / 3)){
			returnBool = true;
		}
		else{
			returnBool = false;
		}
		
		return returnBool;
		
	}
	
	
	public static int getIndex(String stringGiven, char charGiven, int lastIndex){
		
		
		for (int i = 0 ; i< stringGiven.length() ; i++){
	        if (stringGiven.charAt(i) == charGiven){
	        	
	        	
	        	
	        	if(!usedIndices.contains(i)){
	        		
	        		int backDistance = lastIndex - i;
	    			int distance = i - lastIndex;
	    			
	    			if((distance <= 2) && (distance >= -1)){
	    				usedIndices.add(i);
		        		return i;
	    			}
	        		
	        	}
	        }
		}
		return -100;
	}
}
