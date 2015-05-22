package ro.pub.cs.systems.pdsd.practicaltest02var05;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class PracticalTest02Var05MainActivity extends Activity {

		//server
		private EditText server_port = null;
		private Button server_start= null;
		//client
		private EditText client_address = null;
		private EditText command = null;
		private Button launch_command = null;
		private EditText client_port = null;
		private TextView results = null;
		
		//thread-uri
		private ServerThread serverthread = null;
		private ClientThread clientthread = null;
		
		
		private ServerButtonListener ServerConnectButton = new ServerButtonListener();
		private class ServerButtonListener implements Button.OnClickListener {
			
			@Override
			public void onClick (View view) {
				String serverPort = server_port.getText().toString();
				if (serverPort == null || serverPort.isEmpty()) {
					Toast.makeText(getApplicationContext(),"Server port should be filled!",Toast.LENGTH_SHORT).show();
					return;
				}
				serverthread = new ServerThread(Integer.parseInt(serverPort));
				if (serverthread.getServerSocket() != null) {
					serverthread.start();
				} else {
					Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not creat server thread!");
				}
			}
		}
		
		private ClientButtonListener ClientButton = new ClientButtonListener();
		private class ClientButtonListener implements Button.OnClickListener {
			
			@Override
			public void onClick (View view) {
				String clientAddress = client_address.getText().toString();
				String clientPort    = client_port.getText().toString();
				if (clientAddress == null || clientAddress.isEmpty() || clientPort == null || clientPort.isEmpty()) {
					Toast.makeText(getApplicationContext(),"Client connection parameters should be filled!",Toast.LENGTH_SHORT).show();
					return;
				}
				
				String final_command = command.getText().toString();
				if (final_command == null || final_command.isEmpty()) {
					Toast.makeText(getApplicationContext(),"Command should be filled!",Toast.LENGTH_SHORT).show();
					return;
				}
				
				if (serverthread == null || !serverthread.isAlive()) {
					Log.e(ro.pub.cs.systems.pdsd.practicaltest02var05.Constants.TAG, "[MAIN ACTIVITY] There is no server to connect to!");
					return;
				}
				

				
				results.setText("Empty");
				
				clientthread = new ClientThread(clientAddress,Integer.parseInt(clientPort),command,results);
				clientthread.start();

			}
		}
		
		
		
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_var05_main);
        
        server_port = (EditText)findViewById(R.id.server_port);
        server_start = (Button)findViewById(R.id.server_start);
        server_start.setOnClickListener(ServerConnectButton);
		
		client_address = (EditText)findViewById(R.id.client_address);
		client_port = (EditText)findViewById(R.id.client_port);
		command = (EditText)findViewById(R.id.command);
		launch_command = (Button)findViewById(R.id.button_launch);
		launch_command.setOnClickListener(ClientButton);
		
		results = (TextView)findViewById(R.id.result);
        
    }

    @Override
	protected void onDestroy() {
		if (serverthread != null) {
			serverthread.stopThread();
		}
		super.onDestroy();
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.practical_test02_var05_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
