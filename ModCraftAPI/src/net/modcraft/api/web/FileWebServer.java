package net.modcraft.api.web;

import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.bukkit.Bukkit;

public class FileWebServer extends Thread{
		
	private byte[] file;
	private ServerSocket server;
	private String filename;
	private int port;
	
	public FileWebServer(byte[] file, int port, String filename) {
		this.file=file;
		this.port=port;
		this.filename=filename;
		setDaemon(true);
		setName("FileServer # " + file.hashCode());
		try {
			this.server = new ServerSocket(port);
			start();
		}catch(Exception ex) {
			throw new NullPointerException("A server on this port is already running");
		}
	}
	
	@Override
	public void run() {
		while(isAlive()) {
			try {
				Socket client = server.accept();
				Bukkit.broadcastMessage("§9" + client.getInetAddress().getHostAddress());
				
				OutputStream out = client.getOutputStream();
				
				out.write(("HTTP/1.1 200 OK\nContent-Type: file\nContent-Length: " + file.length + "\nContent-Disposition: attachment; filename=\"" + filename + "\"\nAccept-Ranges: bytes\nConnection: close\n\n").getBytes());
				out.write(file);
				
				out.flush();

				Thread.sleep(100);
				
				out.close();
				client.close();
			}catch(Exception ex) {}
		}
	}
	
	public ServerSocket getServer() {
		return server;
	}
	
	public byte[] getFile() {
		return file;
	}
	
	public String getFileName() {
		return filename;
	}
	
	public int getPort() {
		return port;
	}

}
