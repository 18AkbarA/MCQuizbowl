package com.mcquizbowl.main;


import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.java_websocket.WebSocketImpl;

import com.mcquizbowl.qbwsserv.*;
import com.mcquizbowl.qbbackend.*;

import java.io.*;
import java.net.UnknownHostException;

public class joinRoom implements CommandExecutor {

	private final mainClass plugin;
	
	public joinRoom(mainClass plugin) {
		this.plugin = plugin; // Store the plugin in situations where you need it.
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// implementation exactly as before...
		
    		
    		if(args.length != 1){
    			sender.sendMessage("Syntax: /joinroom <Room Number (1,2,3,4)>");
    		}
    		else{
			
    		
    		sender.sendMessage("Well hello there!");
    		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "say you are being watched!");
    		
    		/*
    		writeStuff(sender.getName());
    		
    		readStuff(sender.getName());
    		
    		try {
				sayQuestion(sender.getName());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
    		
    		}
    		
    		return true;

			
    		
		
	}
	
	
	public void enterRoom(int roomNumber, String playerName) throws UnknownHostException{
		WebSocketImpl.DEBUG = true;
		int port = 0;
		if(roomNumber == 1){
			port = 8887; // 843 flash policy port
			try {
			//port = Integer.parseInt( args[ 0 ] );
				port = 8887;
			} catch ( Exception ex ) {
			}
		}
		else{
		}
		if(port != 0){
			WsServer roomServer = new WsServer( port );
			roomServer.start();
		}
	}
	
	/*public void sayQuestion(String playerName) throws UnknownHostException{
		WebSocketImpl.DEBUG = true;
		int port = 8887; // 843 flash policy port
		try {
			//port = Integer.parseInt( args[ 0 ] );
			port = 8887;
		} catch ( Exception ex ) {
		}
		WsServer s = new WsServer( port );
		s.start();
		System.out.println( "ChatServer started on port: " + s.getPort() );

		ProtobowlConnect testBot = new ProtobowlConnect(); 
		testBot.connectToRoom("bsadjakdhasf","testBot2");
		
		testBot.nextQuestion();
		testBot.delayTime(200);
		testBot.finishQuestion();
		String thequestion = testBot.returnQuestion();
		
		Bukkit.getServer().getPlayer(playerName).sendMessage("Connected! The question will now begin!");
		testBot.displayQuestion(thequestion, s);
		
		
		
		testBot.disconnect();
	}*/
	
	public void writeStuff(String playerName){
		  String fileName = "datafiles/temp.txt";

	        try {
	            // Assume default encoding.
	            FileWriter fileWriter =
	                new FileWriter(fileName , true);

	            // Always wrap FileWriter in BufferedWriter.
	            BufferedWriter bufferedWriter =
	                new BufferedWriter(fileWriter);

	            // Note that write() does not automatically
	            // append a newline character.
	            bufferedWriter.newLine();
	            bufferedWriter.append(playerName + " triggered a textcmd");
	           

	            // Always close files.
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
	
	public void readStuff(String playerName){
		// The name of the file to open.
        String fileName = "datafiles/temp.txt";

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
               
                Bukkit.getServer().getPlayer(playerName).sendMessage(line);
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
    
	}
	

}
