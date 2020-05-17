package ro.pub.cs.systems.eim.practicaltestv2;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class CommunicationThread extends Thread {
    Socket socket;

    public CommunicationThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = Utilities.getReader(socket);
            PrintWriter writer = Utilities.getWriter(socket);

            String ip = reader.readLine();

            HttpClient client = new DefaultHttpClient();

            HttpGet request = new HttpGet(Constants.SITE + ip);
            ResponseHandler<String> handler = new BasicResponseHandler();

            String page_source = client.execute(request, handler);

            JSONObject content = new JSONObject(page_source);

            Log.d(Constants.DEBUG, page_source);

            JSONObject geo = new JSONObject(content.getString("geo"));

            String name = content.getString("name"),
                    continent = content.getString("continent"),
                    latitute = geo.getString("latitude"),
                    longitute = geo.getString("longitude"),
                    code = content.getString("alpha2");

            writer.println(name);
            writer.flush();
            writer.println(continent);
            writer.flush();
            writer.println(latitute);
            writer.flush();
            writer.println(longitute);
            writer.flush();
            writer.println("https://www.countryflags.io/" + code + "/flat/64.png");
            writer.flush();

            socket.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
