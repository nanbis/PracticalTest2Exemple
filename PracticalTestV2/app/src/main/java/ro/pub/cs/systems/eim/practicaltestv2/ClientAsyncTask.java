package ro.pub.cs.systems.eim.practicaltestv2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientAsyncTask extends AsyncTask<String, String, Bitmap> {
    ImageView image;
    TextView response;
    GoogleMap googleMap;

    public ClientAsyncTask(ImageView image, TextView response, GoogleMap googleMap) {
        this.image = image;
        this.response = response;
        this.googleMap = googleMap;

    }
    @Override
    protected Bitmap doInBackground(String... strings) {
        try {
            String address = strings[0],
                    port = strings[1],
                    ip = strings[2];

            Socket socket = new Socket(address, Integer.valueOf(port));

            PrintWriter writer = Utilities.getWriter(socket);

            writer.println(ip);
            writer.flush();

            BufferedReader reader = Utilities.getReader(socket);

            String name = reader.readLine(),
                    continent = reader.readLine(),
                    latitude = reader.readLine(),
                    longitude = reader.readLine();

            publishProgress(name, continent, latitude, longitude);

            String image_url = reader.readLine();
            InputStream in = new java.net.URL(image_url).openStream();
            Bitmap mIcon11 = BitmapFactory.decodeStream(in);

            socket.close();
            return mIcon11;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onProgressUpdate(String... result) {
        response.setText(result[0]);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(Float.parseFloat(result[2]), Float.parseFloat(result[3])))
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    protected void onPostExecute(Bitmap result) {
        image.setImageBitmap(result);
    }
}
