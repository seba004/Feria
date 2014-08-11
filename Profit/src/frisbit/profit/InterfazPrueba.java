package frisbit.profit;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v7.app.ActionBarActivity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class InterfazPrueba extends ActionBarActivity {
	
	
	
	/*
	 * Variables Geocerca
	 */

	//Variable correspondiente a la locación del usuario en la aplicación
    LocationManager locationManager;
    
    //Variable correspondiente de "enviar" la información a la clase ProximityActivity.
    PendingIntent pendingIntent;
    
    
    /*
    *Variables agregadas para arreglar detalles de la geocerca
    */
	private static final String PROX_ALERT_INTENT ="frisbit.profit.ProximityAlert";
    	ProximityActivity ProximityActivity= new ProximityActivity();
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interfaz_prueba);
		
		
		
		Button bt_it_mapa=(Button)findViewById(R.id.button1);
		bt_it_mapa.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent actividad_iniciar = new Intent(InterfazPrueba.this, Mapa.class);
				startActivity(actividad_iniciar);
				
			}
		});
			
		Button bt_it_out=(Button)findViewById(R.id.button2);
		bt_it_out.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//borrar preferencias para cerrar sesion
				
				SharedPreferences prefs = getSharedPreferences("prefs",Context.MODE_PRIVATE);
				 SharedPreferences.Editor editor = prefs.edit();
				  editor.clear();
				  editor.commit();
				
				Intent volver = new Intent(InterfazPrueba.this, MainActivity.class);
				startActivity(volver);
				unregisterReceiver(ProximityActivity);
			}
		});
		
		// Obtiene el estado de GooglePlayServicesUtil
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        
        
        
        //Showing status
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
 
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        }
        
        
        else { // Google Play Services are available
 
            // Getting LocationManager object from System Service LOCATION_SERVICE
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);           
            /*
             * Funcion que se encarga de crear la geocerca en el Mapa.
             * in: Punto donde se creara geocerca, id de la geocerca.
             */
            CrearGeocerca(new LatLng(-33.47958211050352,-70.73455732315),1,20);
            CrearGeocerca(new LatLng(-33.48968939543297,-70.61871472746),2,20);
            CrearGeocerca(new LatLng(-33.42917070261277,-70.62644317746),3,20);
            
            IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
    	    registerReceiver(ProximityActivity, filter);
          
	  }
        
	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.interfaz_prueba, menu);
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
	
	/*
	 * Funciones Creadas por desarrollador
	 */
	
	
	private void CrearGeocerca(LatLng point, int idGeo, int radio){
		Intent intent = new Intent(PROX_ALERT_INTENT);
		intent.putExtra("idGeo", idGeo);
		PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
		locationManager.addProximityAlert(point.latitude, point.longitude, radio, -1, proximityIntent);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        moveTaskToBack(true); 
	    	
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	
	
}
