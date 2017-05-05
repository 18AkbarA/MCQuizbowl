package com.mcquizbowl.qbwsserv;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * A simple WebSocketServer implementation. Keeps track of a "chatroom".
 */
public class WsServer extends WebSocketServer {

	public int servernumber = 0;
	public WsServer( int port ) throws UnknownHostException {
		super( new InetSocketAddress( port ) );
		
		servernumber = port - 8886;
	}

	public WsServer( InetSocketAddress address ) {
		super( address );
	}

	@Override
	public void onOpen( WebSocket conn, ClientHandshake handshake ) {
	
	}

	@Override
	public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
		
	}

	@Override
	public void onMessage( WebSocket conn, String message ) {
		
		System.out.println("message: " + message );
		
		this.sendToAll(message);
	}

	public void onFragment( WebSocket conn, Framedata fragment ) {
		
	}

	
	@Override
	public void onError( WebSocket conn, Exception ex ) {
		ex.printStackTrace();
		if( conn != null ) {
			// some errors like port binding failed may not be assignable to a specific websocket
		}
	}

	/**
	 * Sends <var>text</var> to all currently connected WebSocket clients.
	 * 
	 * @param text
	 *            The String to send across the network.
	 * @throws InterruptedException
	 *             When socket related I/O errors occur.
	 */
	public void sendToAll( String message ) {
		
		String[] peopleInRoom = getPeopleNamesInRoom(servernumber);
		
		for(String playerName : peopleInRoom){
			Bukkit.getLogger().info("playerName");
			Bukkit.getServer().getPlayer(playerName).sendMessage(message);
		}
		/*
        for(Player p : Bukkit.getOnlinePlayers()){
       	 
            
            p.sendMessage(message);
        

        }
        */
	}
	
	
	public String[] getPeopleNamesInRoom(int roomNumber){
		// The name of the file to open.
        String fileName = "datafiles/usernamesinroom" + roomNumber + ".txt";
        String peopleNames = "";
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
               
                peopleNames += line;
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
        
        
        
        return peopleNames.split(";");
	}
}

