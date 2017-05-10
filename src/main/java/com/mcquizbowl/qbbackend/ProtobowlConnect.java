package com.mcquizbowl.qbbackend;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.mcquizbowl.qbwsserv.WsServer;
import com.mcquizbowl.main.FileManipulator;




/*THINGS TO KNOW BEFORE YOU START:
 * 
 * This is a quizbowl bot I made for fun using HtmlUnit that interacts with the site PROTOBOWL 
 * (which I didn't make, but you should nonetheless check out as it is awesome!)
 * site: (protobowl.com) source code: (https://github.com/neotenic/protobowl)
 * 
 * The objective is to eventually turn this into a backend virtual room for a PROTOBOWL gaming server
 * (probably a Minecraft one)
 * 
 * TL;DR: I'm a huge quizbowl nerd who likes spending (wasting?) his time creating various code projects
 * 
 * Also for the record, I'm mainly a web developer (hence HtmlUnit), so I try to write my Java as
 * similar to Javascript (JS) as I can.
 * 
 */

public class ProtobowlConnect {
	
	
	public static ArrayList<String> playersOn = new ArrayList<String>();
	public static int roomNumber = 0;
	public static int portNumber = 0;
	
	public static String correctAnswerer = "";
	
	public static String answerGiven = "";
	
	public boolean correctAnswerReached = false;
	public boolean noonesBuzzed = true;
	
	//Set up a new webClient (basically a gui-less browser)
	public static WebClient webClient = new WebClient(BrowserVersion.CHROME);
	public static HtmlPage page;
	
	public static FileManipulator filer = new FileManipulator();
	
	public ProtobowlConnect(){
		
	}
	
	//Pauses for 1/4 of a second between every word of a question
    public static void betweenWords(){
    	try {
    	    Thread.sleep(250);                 
    	} catch(InterruptedException ex) {
    	    Thread.currentThread().interrupt();
    	}
    }
	
    
    public void setRoomNumber(int roomnumber){
    	roomNumber = roomnumber;
    }
    
    
    

    
    
    //Custom time delays (equivalent to js' setTimeout)
    public static void delayTime(int delayedTime){
    	try {
    	    Thread.sleep(delayedTime);                 
    	} catch(InterruptedException ex) {
    	    Thread.currentThread().interrupt();
    	}
    }
    

		/*
		 * THINGS TO KNOW ABOUT PROTOBOWL INFRASTRUCTURE:
		 * 
		 * Questions are formatted like this:
		 * "This nation's city of Magdalena was the site of the world's largest oil spill ever, and this nation claims the South Georgia and South Sandwich Islands. Along with its western neighbor, this country is home to the Mapuche, and it is also the site of the southern half of the river (*) Parana. This nation also claims the Malvinas Islands and is divided from two of its northern neighbors by the Rio de la Plata. This nation is home to the pampas, which are home to its gauchos. For 10 points, name this nation with capital at Buenos Aires."
		 * 
		 * Answers are formatted like this:
		 * {Argentina} 
		 * where the part in the {} is the answer needed
		 * 
		 * A correct answer given before the (*) signifies what we call a "power" (15 pts)
		 * A correct answer given after the (*) is worths 10 pts
		 * An incorrect answer given while the question is being read costs -5 pts
		 * 
		 * Variables prefixed by wo are room variables (wo.answer and wo.question)
		 * 
		 * functions prefixed by fo are admin commands (fo.next(), fo.name('Alia Bhatt'))
		 * 
		 * 
		 * Current functions I know I'll be using:
		 * wo.answer - gives answer
		 * wo.question - gives question
		 * fo.set_name('name') - sets bot's name
		 * fo.next() - starts next question
		 * fo.finish() - skips to the end of a question
		 * fo.set_category(category) - sets question category to one of the following:  Literature, Trash, Geography, Social Science, Science, History, Mythology, Fine Arts, Current Events, and "" for Everything
		 * fo.set_difficulty(difficulty)
		 * fo.disconnect_all() - disconnects bot from the room
		 * 
		 * 
		 * 
		
		
		

		
	*/
	
	public void connectToRoom(String roomname, String botname){
		//Disable error logging cuz its annoying
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
	    java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

	    java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit.javascript.StrictErrorReporter").setLevel(java.util.logging.Level.OFF);
	    
		//Connect to a protobowl room, store that in page, and set botname
				
				try {
					page = webClient.getPage("http://protobowl.com/" + roomname);
					
					//To clean stuff up, I send this page to another method
					
					
					
				} catch (FailingHttpStatusCodeException e) {
					
					e.printStackTrace();
				} catch (MalformedURLException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				//Waits for all that JS goodness to load
				webClient.waitForBackgroundJavaScript(1000);
				
				//time to run some javascript!
				page.executeJavaScript("fo.set_name('"+botname+"')").getNewPage();
				
				
	}

	
	
	//The name of these functions tell it all
	public static void disconnect(){
		//page.executeJavaScript("fo.disconnect_all()").getNewPage();
		webClient.closeAllWindows();
	}
	public static void nextQuestion(){
		page.executeJavaScript("fo.next()").getNewPage();
		filer.writeFile("datafiles/buzzedinroom" + roomNumber + ".txt","noonesbuzzed");
	}
	
	public void setCorrectAnswerer(String answererName ,String correctAnswerGiven){
		correctAnswerer = answererName;
		answerGiven = correctAnswerGiven;
	}
	public static void finishQuestion(){
		page.executeJavaScript("fo.finish()").getNewPage();
		filer.writeFile("datafiles/buzzedinroom" + roomNumber + ".txt","finishedQuestion");
		
		for(String playerName : playersOn){
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "xp -100L " +playerName);
		}
	}
	
	public static void setCategory(String category){
		page.executeJavaScript("fo.set_category('"+category+"')").getNewPage();
	}
	public static void setDifficulty(String difficulty){
		page.executeJavaScript("fo.set_difficulty('"+difficulty+"')").getNewPage();
	}
	
	
	
	public static String returnQuestion(){
		ScriptResult result = page.executeJavaScript("wo.question");
		result.getNewPage();
		
		
		//HtmlUnit returns variables in a strange format, so I have to refine it to traditional format it.
		String unrefinedQuestion = result.toString().split("page=HtmlPage")[0];
		
		String refinedQuestion = unrefinedQuestion.replace("ScriptResult[result=","");
		
		//return the question
		return refinedQuestion;
	}
	
	//get Answer
	public static String returnAnswer(){
		ScriptResult result = page.executeJavaScript("wo.answer");
		result.getNewPage();
		
		
		//HtmlUnit returns variables in a strange format, so I have to refine it to traditional format it.
		String unrefinedAnswer = result.toString().split("page=HtmlPage")[0];
		
		String refinedAnswer = unrefinedAnswer.replace("ScriptResult[result=","");
		
		//displays the question
		return refinedAnswer;
	}

	
	//This method breaks the question up word by word and displays it, much like protobowl does, simulating a speaking moderator.
	public void displayQuestion(String question){
		
		
		filer.writeFile("datafiles/buzzedinroom" + roomNumber + ".txt", "noonesbuzzed");
		
		
		String[] brokenQuestion = question.split(" ");
	
		int i = 0;
		
		while( i < brokenQuestion.length){
		
			String hasBuzzed = filer.readFile("datafiles/buzzedinroom" + roomNumber + ".txt");
			if(hasBuzzed.equals("noonesbuzzed")){
				this.sendToAll(brokenQuestion[i]);
				if(i != brokenQuestion.length - 1){
				
					
					
					delayTime(450);
					
				}
				else{
					int delaySecs = 0;
					
					while(delaySecs < 3){
						hasBuzzed = filer.readFile("datafiles/buzzedinroom" + roomNumber + ".txt");
						if(hasBuzzed.equals("noonesbuzzed")){
							delayTime(1000);
							delaySecs++;
						}
						else if(hasBuzzed.equals("finishedQuestion")){
							questionEnd();
							break;
						}
					}
					if(delaySecs >= 3){
						questionEnd();
					}
				}
			
				i++;
			}
			else if(hasBuzzed.equals("finishedQuestion")){
				for(int k = i; k < brokenQuestion.length; k++){
					this.sendToAll(brokenQuestion[k]);
					
					if(k == brokenQuestion.length - 1){
		
						questionEnd();
					}
				}
				break;
			}
			else{
				
			}
		
		}
		
	}
	
	
	//Displays the answer at the end
	
	public void questionEnd(){
		sendToAll("-------------------------------");
		filer.writeFile("datafiles/buzzedinroom" + roomNumber + ".txt", "finishedQuestion");
		
		
		if(correctAnswerer.equals("")){
			
		}
		else{
			sendToAll(ChatColor.GREEN + "" + ChatColor.BOLD + correctAnswerer + ChatColor.RESET + ChatColor.GREEN +" got the question correct by answering: " + ChatColor.BOLD + answerGiven + ChatColor.RESET + ChatColor.GREEN +"!");
			correctAnswerer = "";
		}
		
		sendToAll(ChatColor.BLUE+ " The correct answer was: " + ChatColor.BOLD + returnAnswer().replace("{","").replace("}",""));

	}
	//Sends a message to everyone in the room
	public void sendToAll(String message){
		for(String playerName : playersOn){
			Bukkit.getServer().getPlayer(playerName).sendMessage(message);
		}
	}
	
	//adds a player to the room
	public void addPlayer(String playerName){
		playersOn.add(playerName);
	}
	
	//removes a player from the room
	public void removePlayer(String playerName){
		playersOn.remove(playerName);
	}
	
	
	
	

	
	
}
