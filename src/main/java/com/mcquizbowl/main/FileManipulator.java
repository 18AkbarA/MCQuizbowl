package com.mcquizbowl.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManipulator {

	public String readFile(String fileName){
		// The name of the file to open.
        

        // This will reference one line at a time
        String data = "";
        String line = null;
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
               data += line;
                
            }   

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
        
        return data;
    
	}
	
	public void addPeopleToRoom(int roomNumber ,int numbToAdd, String playerName){
		String fileName = "datafiles/peopleinroom" + roomNumber + ".txt";
		int peopleCount = getPeopleInRoom(roomNumber);
		int newCount = peopleCount + numbToAdd;
	        try {
	            
	            FileWriter fileWriter =
	                new FileWriter(fileName);

	            
	            BufferedWriter bufferedWriter =
	                new BufferedWriter(fileWriter);


	            bufferedWriter.write("" + newCount);
	           

	            
	            bufferedWriter.close();
	        }
	        catch(IOException ex) {
	            System.out.println(
	                "Error writing to file '"
	                + fileName + "'");
	            // Or we could just do this:
	            // ex.printStackTrace();
	        }
	       
	    String peopleFile = "datafiles/usernamesinroom" + roomNumber + ".txt";
	    if(numbToAdd == -1){
	    	String newFileData = this.readFile(peopleFile).replace(";" + playerName, "");
	    	writeFile(peopleFile, newFileData);
	    }
	    else{
	    	appendFile(peopleFile,";"+playerName);
	    }
	    
	    
	}
	
	
	public int getPeopleInRoom(int roomNumber){
		// The name of the file to open.
        String fileName = "datafiles/peopleinroom" + roomNumber + ".txt";
        String peoplePresent = "";
        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName);
            
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
               
                peoplePresent += line;
            }   

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
        
        return Integer.parseInt(peoplePresent);
    
	}
	
	
	public void writeFile(String fileName, String data){
		try {
            
            FileWriter fileWriter =
                new FileWriter(fileName);

            
            BufferedWriter bufferedWriter =
                new BufferedWriter(fileWriter);


            bufferedWriter.write(data);
           

            
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println(
                "Error writing to file '"
                + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
	}
	public void appendFile(String fileName, String data){
		try {
            
            FileWriter fileWriter =
                new FileWriter(fileName,true);

            
            BufferedWriter bufferedWriter =
                new BufferedWriter(fileWriter);


            bufferedWriter.append(data);
           

            
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println(
                "Error writing to file '"
                + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
	}
	
	
}
