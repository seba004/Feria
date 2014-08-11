package frisbit.profit;


import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import test.login.library.Httppostaux;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import frisbit.profit.InterfazPrueba.asyncGetLugares;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;



public class Mapa extends FragmentActivity {

	GoogleMap googleMap;
	String URL_connect = "http://www.frisbit.net/nicolas/getGeofences.php";
	Httppostaux post;
	private ProgressDialog pDialog;
	boolean result_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapa);
		post = new Httppostaux();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
        	 
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        }
        else{
		
		
		
		
		googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.storelocationmap)).getMap();
        
		googleMap.setMyLocationEnabled(true);
		
		new asyncGetLugares().execute();
		
        
		
		
		
        
		
        }
		
		
		
	}

	private void drawMarker(LatLng point, int idGeo, String direccion){
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();
        
        // Setting latitude and longitude for the marker
        markerOptions.position(point);
        
        //Agregar el titulo de la Geocerca
        markerOptions.title("Geofence "+idGeo);
        
        //Agregar Descricpcion
        markerOptions.snippet(direccion);
        
        // Adding marker on the Google Map
        googleMap.addMarker(markerOptions);
        
       
    }
	
	
	
	/*
	 * Funcion encargada de construir el circulo en el mapa Google
	 * solamente es un aspecto visual.
	 */
	private void drawCircle(LatLng point, int radio){
		 
        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();
 
        // Specifying the center of the circle
        circleOptions.center(point);
 
        // Radius of the circle
        circleOptions.radius(radio);
 
        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);
 
        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);
 
        // Border width of the circle
        circleOptions.strokeWidth(2);
 
        // Adding the circle to the GoogleMap
        googleMap.addCircle(circleOptions);
 
    }
	
	public JSONArray getLugares() {

		ArrayList<NameValuePair> postparameters2send = new ArrayList<NameValuePair>();
		String nombre = "l";
		postparameters2send.add(new BasicNameValuePair("nombre", nombre));

		
		// realizamos una peticion y como respuesta obtenes un array JSON
		JSONArray jdata = post.getserverdata(postparameters2send, URL_connect);
		return jdata;
	}
	
	class asyncGetLugares extends AsyncTask<String, JSONArray, JSONArray> {

		protected JSONArray doInBackground(String... params) {
			
			JSONArray misLugares = getLugares();
            
            
            return misLugares;
            
  
			
		} // end... doInBackground.
		
		protected void onPostExecute(JSONArray misLugares) {
			JSONObject json_data;

            try {

            	int total = misLugares.length();
            	for (int i = 0; i < total; i++) {
            		json_data = misLugares.getJSONObject(i);
            		
            		String latitud = json_data.getString("latitud");
            		String longitud = json_data.getString("longitud");
            		String direccion = json_data.getString("direccion");
            		int radio = json_data.getInt("radio");

            		drawMarker(new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud)), i+1 , direccion);
                    drawCircle(new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud)),radio);     

            	}
            	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
		}
		
	} // end class asynctask
	
	
}
