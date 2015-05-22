package ro.pub.cs.systems.pdsd.practicaltest02var05;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;

import android.provider.SyncStateContract.Constants;
import android.util.Log;

public class ServerThread extends Thread {
	
	private int port = 0;
	private ServerSocket serverSocket = null;
	
	private HashMap<String, Valoare> data = null;
	
	public ServerThread(int port) {
		this.port = port;
		try {
			this.serverSocket = new ServerSocket(port);
		} catch (IOException ioException) {
			Log.e(ro.pub.cs.systems.pdsd.practicaltest02var05.Constants.TAG, "An exception has occurred: " + ioException.getMessage());
			if (ro.pub.cs.systems.pdsd.practicaltest02var05.Constants.DEBUG) {
				ioException.printStackTrace();
			}
		}
		this.data = new HashMap<String, Valoare>();
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setServerSocker(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	public ServerSocket getServerSocket() {
		return serverSocket;
	}
	
	public synchronized void setData(String cheie, Valoare valoare_information) {
		this.data.put(cheie, valoare_information);
	}
	
	public synchronized HashMap<String, Valoare> getData() {
		return data;
	}
	
	@Override
	public void run() {
		try {		
			while (!Thread.currentThread().isInterrupted()) {
				Log.i(ro.pub.cs.systems.pdsd.practicaltest02var05.Constants.TAG, "[SERVER] Waiting for a connection...");
				Socket socket = serverSocket.accept();
				Log.i(ro.pub.cs.systems.pdsd.practicaltest02var05.Constants.TAG, "[SERVER] A connection request was received from " + socket.getInetAddress() + ":" + socket.getLocalPort());
				CommunicationThread communicationThread = new CommunicationThread(this, socket);
				communicationThread.start();
			}			
		} catch (ClientProtocolException clientProtocolException) {
			Log.e(ro.pub.cs.systems.pdsd.practicaltest02var05.Constants.TAG, "An exception has occurred: " + clientProtocolException.getMessage());
			if (ro.pub.cs.systems.pdsd.practicaltest02var05.Constants.DEBUG) {
				clientProtocolException.printStackTrace();
			}			
		} catch (IOException ioException) {
			Log.e(ro.pub.cs.systems.pdsd.practicaltest02var05.Constants.TAG, "An exception has occurred: " + ioException.getMessage());
			if (ro.pub.cs.systems.pdsd.practicaltest02var05.Constants.DEBUG) {
				ioException.printStackTrace();
			}
		}
	}
	
	public void stopThread() {
		if (serverSocket != null) {
			interrupt();
			try {
				serverSocket.close();
			} catch (IOException ioException) {
				Log.e(ro.pub.cs.systems.pdsd.practicaltest02var05.Constants.TAG, "An exception has occurred: " + ioException.getMessage());
				if (ro.pub.cs.systems.pdsd.practicaltest02var05.Constants.DEBUG) {
					ioException.printStackTrace();
				}				
			}
		}
	}
}
