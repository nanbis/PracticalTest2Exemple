package ro.pub.cs.systems.eim.practicaltestv2;

import androidx.appcompat.app.AppCompatActivity;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    EditText server_port, client_address, client_port, client_ip;
    TextView response;
    ImageView image;
    Button server_button, client_button;
    GoogleMap googleMap = null;

    ServerThread server_thread;

    ButtonListener listener = new ButtonListener();

    protected class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_client:
                    ClientAsyncTask task = new ClientAsyncTask(image, response, googleMap);
                    task.execute(client_address.getText().toString(), client_port.getText().toString(), client_ip.getText().toString());
                    break;
                case R.id.button_server:
                    if (server_thread == null) {
                        server_thread = new ServerThread(server_port);
                        server_thread.startServer();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        server_button = (Button) findViewById(R.id.button_server);
        client_button = (Button) findViewById(R.id.button_client);

        server_button.setOnClickListener(listener);
        client_button.setOnClickListener(listener);

        server_port = (EditText) findViewById(R.id.editText_server_port);
        client_port = (EditText) findViewById(R.id.editText_client_port);
        client_address = (EditText) findViewById(R.id.editText_client_address);
        client_ip = (EditText) findViewById(R.id.editText_client_ip);

        response = (TextView) findViewById(R.id.textView_response);
        image = (ImageView) findViewById(R.id.imageView);

        if (googleMap == null) {
            ((MapFragment) getFragmentManager().findFragmentById(R.id.google_map)).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap readyGoogleMap) {
                    googleMap = readyGoogleMap;
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        if (server_thread != null) {
            try {
                server_thread.stopServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }
}
