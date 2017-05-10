/* The biggest class of them all-- contains all the code to handle commands players issue
 * 
 */


package com.mcquizbowl.main;


import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.*;
import org.bukkit.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.drafts.Draft_10;







import java.util.ArrayList;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.mcquizbowl.qbwsserv.*;
import com.mcquizbowl.qbbackend.*;

import java.awt.Color;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.UnknownHostException;

import org.bukkit.scoreboard.*;

public class roomCmds implements CommandExecutor {

	
	/*public ScoreboardManager manager = Bukkit.getScoreboardManager();
	public Scoreboard board = manager.getNewScoreboard();
	 
	public Objective score = board.registerNewObjective("Score", "dummy");*/
	
	public ProtobowlConnect questioner = new ProtobowlConnect();

	public int maxRooms = 4;
	
	public String roomList = "";
	
	public FileManipulator filer = new FileManipulator();
	
	private final mainClass plugin;
	
	
	public boolean readingQuestion = false;
	
	//initializes the class
	public roomCmds(mainClass plugin) {
		

		/*score.setDisplaySlot(DisplaySlot.SIDEBAR);
		score.setDisplayName("Score");*/
		
		
		this.plugin = plugin; // Store the plugin in situations where you need it.
	}
	
	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
		// implementation exactly as before...
		for (int i = 1; i <= maxRooms; i++){
			if (i != maxRooms){
				roomList += "" + i + ", ";
			}
			else{
				roomList += "" + i;
			}
		}
		String playerName = sender.getName();
		
		
		//Joins a player to a room
		if (cmd.getName().equalsIgnoreCase("joinroom")) {	
    		if(args.length != 1){
    			sender.sendMessage("Syntax: /joinroom <Room Number " +roomList + ">");
    		}
    		else{
			
    			//checks if player entered a valid room number
    			int roomNumber = Integer.parseInt(args[0]);
    			if(roomNumber != 1 && roomNumber != 2 && roomNumber != 3 && roomNumber != 4){
    				sender.sendMessage("Error: number must one of the following: " +roomList);
    			}
    			else{
    				
    				
    				boolean stopCheckingRooms = false;
    				boolean continueOn = true;
    				int i = 0;
    				while(!stopCheckingRooms){
    					
    					i++;
    					int playerIndex = filer.readFile("datafiles/usernamesinroom" + i + ".txt").indexOf(playerName);
    					
    					
    					if(playerIndex > -1){
    						stopCheckingRooms = true;
    						continueOn = false;
    						sender.sendMessage("You are already in room "+i +"!");
    						
    					}

    					
    					if(i > maxRooms){
    						
    						stopCheckingRooms = true;
    					}
    				}
    				
    				if(continueOn){
    					
    					//Connects a player to a room
    					
    					int roomCount = filer.getPeopleInRoom(roomNumber);
    					/*if (roomCount == 2){
    						sender.sendMessage("That room is full!");
    					}
    					else{
    					*/
    				
    						
    						
    						filer.addPeopleToRoom(roomNumber, 1, playerName);
    						
    						questioner.setRoomNumber(roomNumber);
    						
    						questioner.addPlayer(playerName);
    						
    						questioner.sendToAll(ChatColor.BLUE + playerName + " joined the room!");
    						//random protobowl urls for each room
    						String[] roomNames = {"bsadjakdhasfroom", "asdkjkfdaffad","zoxicpasd","ioiusdfa"};
    						String roomName = roomNames[roomNumber - 1];
    						
    						
    						//Starts the HTMLunit client if there is only one player currently on
    						if(questioner.playersOn.size() == 1){
    						questioner.connectToRoom(roomName,"testBot");
    						}
    							

    	    		    	Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard objectives add Score dummy");
    	    		    	Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard objectives setdisplay sidebar Score");

    							
    							
    							

    						
    					
    				}
    				
    			}
    		
    		}
    		
    		return true;

		}
		
		//makes a player leave a room
		else if(cmd.getName().equalsIgnoreCase("leaveroom")){
			
			boolean stopCheckingRooms = false;
			int i = 1;
			while(!stopCheckingRooms){
				
				
				int playerIndex = filer.readFile("datafiles/usernamesinroom" + i + ".txt").indexOf(playerName);
				
				
				if(playerIndex > -1){
					stopCheckingRooms = true;
					
					filer.addPeopleToRoom(i,-1,playerName);
					sender.sendMessage("You left room " + i +"!");
					questioner.removePlayer(playerName);
				}

				
				if(i > maxRooms){
					sender.sendMessage("You are not in a room!");
					stopCheckingRooms = true;
				}
				i++;
			}
			
			return true;
		}
    	
		//Starts a question
		else if(cmd.getName().equalsIgnoreCase("startquestion")){
			//makes sure a question is not already being read
			if(!readingQuestion){
			
				int playerRoomNumber = roomPlayerIsIn(playerName);
				if(playerRoomNumber != 0){

				
				
					questioner.nextQuestion();
					questioner.delayTime(300);
					questioner.finishQuestion();
					
					//Starts a thread- this allows a player to buzz while the question is being read
					Thread t = new Thread(){ 
						public void run(){
							readingQuestion = true;
							questioner.displayQuestion(questioner.returnQuestion());
							readingQuestion = false;
							Thread.currentThread().interrupt();
							return;
						}
					};
					t.start();
				
				
				//questioner.disconnect();
				
				
				
				}
			}
			else{
				sender.sendMessage("A question is already being read!");
			}
			
			return true;
		}
		
		
		
		//sets category
		else if(cmd.getName().equalsIgnoreCase("setcategory")){
			
			int roomPlayersIn = roomPlayerIsIn(playerName);
			if(args.length != 1){
				sender.sendMessage("You must type a category to set!");
			}
			else{
				questioner.setCategory(args[0]);
				sender.sendMessage("Set category to '" + args[0]+"'");
			}
			return true;
		}
		
		//sets difficulty
		else if(cmd.getName().equalsIgnoreCase("setdifficulty")){
			if(args.length != 1){
				sender.sendMessage("You must type a difficulty to set!");
			}
			else{
				questioner.setDifficulty(args[0]);
				sender.sendMessage("Set difficulty to '" + args[0]+"'");
			}
			return true;
		}
		
		
		//Opposite of buzzing in
		else if(cmd.getName().equalsIgnoreCase("unbuzz")){
			int playerRoomNumber = roomPlayerIsIn(playerName);
			if(playerRoomNumber != 0){
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "say continuing on!");
				filer.writeFile("datafiles/buzzedinroom" + playerRoomNumber + ".txt", "noonesbuzzed");
			}
			else{
				sender.sendMessage("You are not in a room!");
			}
			return true;
			
			
		}
		
		//buzzes a player in. After they buzz in, they must type /ans <answer>
		else if(cmd.getName().equalsIgnoreCase("buzz")){
			final int playerRoomNumber = roomPlayerIsIn(playerName);
			if(playerRoomNumber != 0){
				String playerBuzzed = filer.readFile("datafiles/buzzedinroom" + playerRoomNumber + ".txt");
				if(playerBuzzed.equals("noonesbuzzed")){
					Bukkit.getServer().dispatchCommand(sender, "me buzzed in!");
					filer.writeFile("datafiles/buzzedinroom" + playerRoomNumber + ".txt", playerName);
				
				
					
					
					//Starts a thread to lock the player out after 10 seconds have elapsed
					Thread t = new Thread(){ 
						public void run(){
							
							
							//questioner.delayTime(10000);
							
							//modifies the xp bar so that its like a timer
							Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "xp 10L " +sender.getName());
							
							for (int i = 0; i < 10; i++){
								questioner.delayTime(1000);
								Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "xp -1L " +sender.getName());
								
								String playerBuzzed = filer.readFile("datafiles/buzzedinroom" + playerRoomNumber + ".txt");
								if(playerBuzzed.equals("finishedQuestion")){
									return;
								}
								
								
							}
							
							String playerBuzzed = filer.readFile("datafiles/buzzedinroom" + playerRoomNumber + ".txt");
								
							if(!playerBuzzed.equals("noonesbuzzed") && !playerBuzzed.equals("finishedQuestion")){
								Bukkit.getServer().dispatchCommand(sender, "unbuzz");
							}
							
						
							return;
						}
					};
					t.start();
				
				}
				else{
					sender.sendMessage(playerBuzzed + "has already buzzed!");
				}
			}
			else{
				sender.sendMessage("You are not in a room!");
			}
			return true;
		}
		
		
		//Answers a question
		else if(cmd.getName().equalsIgnoreCase("ans")){
			if(args.length <= 0){
				sender.sendMessage("Correct usage: /ans <answer>");
				return true;
			}
			
			final int playerRoomNumber = roomPlayerIsIn(playerName);
			if(playerRoomNumber != 0){
				String playerBuzzed = filer.readFile("datafiles/buzzedinroom" + playerRoomNumber + ".txt");
				
				if(playerBuzzed.equals("finishedQuestion")){
					sender.sendMessage(ChatColor.RED + "The question has already finished!");
				}
				
				if(playerName.equals(playerBuzzed)){
					
					
					String answerGiven = "";
					for(String answerPart : args){
						answerGiven += answerPart + " ";
					}
					
					questioner.sendToAll(ChatColor.BLUE + "["+playerName+"] answered: " + answerGiven);
					
					
					answerChecker checkAns = new answerChecker(questioner.returnAnswer(),answerGiven);
					
					
					
					if(checkAns.checkAllPossibilities()){
						//questioner.correctAnswerer = playerName;
						
						questioner.setCorrectAnswerer(playerName, answerGiven);
						questioner.finishQuestion();
						
						
						Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players add " + playerName + " Score 10");
						//questioner.sendToAll(ChatColor.GREEN + "" + ChatColor.BOLD + playerName + ChatColor.RESET + ChatColor.GREEN +" got the question correct by answering: " + ChatColor.BOLD + answerGiven + ChatColor.RESET + ChatColor.GREEN +"!");

						
						/*int playerScore = score.getScore(Bukkit.getPlayer(playerName)).getScore();
						score.getScore(Bukkit.getPlayer(playerName)).setScore(playerScore + 10);*/
						//questioner.sendToAll(ChatColor.GREEN );
						
					}
					
					else{
						questioner.sendToAll(ChatColor.RED + " that is incorrect!");
						Bukkit.getServer().dispatchCommand(sender, "unbuzz");
					}
					
					
					
					
					
				}
				else{
					sender.sendMessage("You must buzz first!");
				}
			}
			else{
				sender.sendMessage("You are not in a room!");
			}
			
			return true;
		}
		
		
		
		
		else{
			return false;
		}
		
	}
	
	//Gets the room the player is in
	
	public int roomPlayerIsIn(String playerName){
		boolean stopCheckingRooms = false;
		int roomNumber = 0;
		int i = 0;
		while(!stopCheckingRooms){
			
			i++;
			int playerIndex = filer.readFile("datafiles/usernamesinroom" + i + ".txt").indexOf(playerName);
			
			
			if(playerIndex > -1){
				stopCheckingRooms = true;
				roomNumber = i;
			}

			
			if(i > maxRooms){
				
				stopCheckingRooms = true;
			}
		}
		return roomNumber;
	}
	
	
	
	
	

}
