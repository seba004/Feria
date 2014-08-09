package frisbit.profit;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;



public class Mapa extends FragmentActivity {

	GoogleMap googleMap;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapa);
		
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
        	 
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        }
        else{
		
		
		
		
		googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.storelocationmap)).getMap();
        
		googleMap.setMyLocationEnabled(true);
		
        drawMarker(new LatLng(-33.47958211050352,-70.73455732315), "1");
        drawCircle(new LatLng(-33.47958211050352,-70.73455732315));     
        
        drawMarker(new LatLng(-33.48968939543297,-70.61871472746), "2");
        drawCircle(new LatLng(-33.48968939543297,-70.61871472746));
        
        
        drawMarker(new LatLng(-33.42917070261277,-70.62644317746), "3");
        drawCircle(new LatLng(-33.42917070261277,-70.62644317746));
        
		
        }
		
		
		
	}

	private void drawMarker(LatLng point, String idGeo){
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();
        
        // Setting latitude and longitude for the marker
        markerOptions.position(point);
        
        //Agregar el titulo de la Geocerca
        markerOptions.title("Geofence "+idGeo);
        
        //Agregar Descricpcion
        markerOptions.snippet("Su relleno");
        
        // Adding marker on the Google Map
        googleMap.addMarker(markerOptions);
        
       
    }
	
	
	
	/*
	 * Funcion encargada de construir el circulo en el mapa Google
	 * solamente es un aspecto visual.
	 */
	private void drawCircle(LatLng point){
		 
        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();
 
        // Specifying the center of the circle
        circleOptions.center(point);
 
        // Radius of the circle
        circleOptions.radius(30);
 
        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);
 
        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);
 
        // Border width of the circle
        circleOptions.strokeWidth(2);
 
        // Adding the circle to the GoogleMap
        googleMap.addCircle(circleOptions);
 
    }
	
	
	
}
