package ro.pub.cs.systems.pdsd.practicaltest02var05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import android.widget.TextView;

public class CommunicationThread extends Thread {
	
	private ServerThread serverThread;
	private Socket       socket;
	
	public CommunicationThread(ServerThread serverThread, Socket socket) {
		this.serverThread = serverThread;
		this.socket       = socket;
	}
	public TextView results;
	public String timestamp;
	public String message;
	@Override
	public void run() {
		if (socket != null) {
			try {
				BufferedReader bufferedReader = Utilities.getReader(socket);
				PrintWriter    printWriter    = Utilities.getWriter(socket);
				String url = Constants.WEB_SERVICE_ADDRESS;
				
				Valoare valoare = null;
				if (bufferedReader != null && printWriter != null) {
					Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client!");
					String operatia = bufferedReader.readLine();
					HashMap<String, Valoare> data = serverThread.getData();
					Valoare valoare_information = null;

					if (operatia != null && !operatia.isEmpty()) {
						
							String pageContentInformation= null;
							String [] parts = operatia.split(",");
							String parsare = parts[0];
							String cheie = parts[1];
							String valoare_1 = parts[2];
							
							if (parsare.equals("put")) {
								
								HttpClient httpClient = new DefaultHttpClient();
								HttpGet httpGet = new HttpGet(url);
								ResponseHandler<String> responseHandler = new BasicResponseHandler();
								
								timestamp = httpClient.execute(httpGet, responseHandler);
								pageContentInformation = timestamp;
								serverThread.setData(cheie, new Valoare(valoare_1, timestamp));
								
								if (pageContentInformation != null) {
									printWriter.println(pageContentInformation);
									message = "modified";
									printWriter.flush();
								} else {
									Log.e(Constants.TAG, "[COMMUNICATION THREAD] Server information is null!");
									message = "inserted";
								}
								
								results.post(new Runnable() {
									
									final String final_line = message;
									@Override
									public void run() {
										// TODO Auto-generated method stub
										results.append(final_line + "\n");
									}
								});
								
							}
							
							if (parsare.equals("get")) {
								if (data.containsKey(cheie)) {
									String current_timestamp = bufferedReader.readLine();
									int time = Integer.parseInt(current_timestamp);
									int time1 = Integer.parseInt(timestamp);
									 if (time1 < time - 60) {
										 timestamp = current_timestamp;
									 }
								}
							}
							
							while ((timestamp = bufferedReader.readLine()) != null) {
							
							results.post(new Runnable() {
								
								final String final_line = timestamp;
								@Override
								public void run() {
									// TODO Auto-generated method stub
									results.append(final_line + "\n");
								}
							});
							}
							
					}
							Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
										
						}
						
						
					
				
				socket.close();
			} catch (IOException ioException) {
				Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
				if (Constants.DEBUG) {
					ioException.printStackTrace();
				}
			} 
		} else {
			Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
		}
	}
}
