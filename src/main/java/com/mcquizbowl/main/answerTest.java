package com.mcquizbowl.main;

import org.apache.commons.lang3.StringUtils;
import java.util.*;
public class answerTest {
	public static String answer = "Walt {Whitman} is a {bro}";
	
	public static ArrayList<Integer> usedIndices = new ArrayList<Integer>();	
	public static void main( String[] args ){
		
	
		String givenanswer = "Walt Whitman is a bro";
		
		//System.out.println(checkAnswer(givenanswer));
		char testchar = 'n';
			//System.out.println(getIndex(givenanswer, testchar));
		
		
		
		System.out.println(compareStrings("brollioao","brollo"));
		
		System.out.println(compareStrings("swagman","swargogman"));
		
		
	}
	
	/*public static int numberMatches(String mainword, String wordtomatch){
		
	}*/
	
	public static boolean isAccepted(String givenAnswer){
		
		String loweredAnswer = answer.toLowerCase().replace("{", "Emma").replace("}", "Watson");
		givenAnswer = givenAnswer.toLowerCase();
		boolean returnVal = false;
		String testo = StringUtils.substringBetween(answer, "{", "}");
		String[] answerArray = loweredAnswer.replaceAll("^.*?Emma", "").split("Watson.*?(Emma|$)");
		
		String[] splitAnswer = givenAnswer.split("");
		
		String otherParts = loweredAnswer;
		
		for(int i = 0; i < answerArray.length; i++){
			otherParts += otherParts.replace("Emma" + answerArray[i] + "Watson", "");
			
			
		}
		
		
		
		int letterMatches = 0;
		for (int i = 0; i < answerArray.length; i++){
			String testAgainst = answerArray[i];
			int testLength = testAgainst.length();
			
			
			ArrayList<Integer> indices = new ArrayList<Integer>();
			
			for (int k = 1; k < splitAnswer.length; k++){
				char testChar = splitAnswer[k].charAt(0);
				//System.out.println(splitAnswer[k] + " with " + testAgainst.indexOf(testChar));

				int theIndex = testAgainst.indexOf(testChar);
				if (indices.contains(theIndex)){
					
				}
				else if (theIndex > -1){
					letterMatches++;
					indices.add(theIndex);
				}
				else{
					
				}
			}
			
			//System.out.println(testAgainst + " with length: " + testLength + " and " + letterMatches + "matches");
			int factorsOfThree = (int)testLength / 3;
			if((Math.abs(testLength - letterMatches) < (testLength - factorsOfThree)) && Math.abs(testAgainst.length() - givenAnswer.length()) < 6){
				
				System.out.println("testlength: " + testLength + " and letterMatches: " + letterMatches);
				
				returnVal = true;
				i = answerArray.length;
			}
			

			
			
		}

		
		return returnVal;
		//return letterMatches;
	}
	
	
	public static int checkAnswer(String givenAnswer){
		
		boolean returnVal = false;
		String loweredAnswer = answer.toLowerCase().replace("{", "bookEnd").replace("}", "Endbook");
		givenAnswer = givenAnswer.toLowerCase();
		
		String testo = StringUtils.substringBetween(answer, "{", "}");
		String[] answerArray = loweredAnswer.replaceAll("^.*?bookEnd", "").split("Endbook.*?(bookEnd|$)");
		
		
		int totalAnswerLength = 0;
		
		for(String answerPart: answerArray){
			totalAnswerLength += answerPart.length();
		}
		
		return totalAnswerLength;
		
		//return returnVal;
		
		
	}
	
	
	
	
	public static boolean compareStrings(String answerGiven, String answer){
		answer = answer.toLowerCase();
		answerGiven = answerGiven.toLowerCase();
		
		boolean returnBool = false;
		int compareScore = 0;
		int lastIndex = 0;
		
		int givenLength = answerGiven.length();
		int answerLength = answer.length();
		
		String resultString = answer;
		
		
		for(int i = 0; i < answerLength; i++){
			char answerChar = answer.charAt(i);
			
			
			
			
			int answerCharIndex = getIndex(answerGiven,answerChar,lastIndex);
			
			//System.out.println("Char " + answerChar + " at answer index: " + answerCharIndex + "and last index: "+ lastIndex );
			
			int backDistance = lastIndex - answerCharIndex;
			int distance = answerCharIndex - lastIndex;
			
			
			//System.out.println("back distance: " + backDistance + " and distance: " + distance);
			if((distance <= 2) && (distance >= -1)){
				
				resultString = StringUtils.replaceOnce(resultString, "" + answerChar, "");
				
			}
			
			
			lastIndex++;
		}
		
		
	        
		//StringUtils.replaceOnce("aba", "a", "")
		
		System.out.println("result string: " + resultString);
		compareScore = resultString.length();
		
		
		usedIndices.clear();
		
		
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
