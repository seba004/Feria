package frisbit.profit;



import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ProximityActivity extends BroadcastReceiver {

	String notificationTitle;
    String notificationContent;
    String tickerMessage;
    
    
    
    @Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
    	Toast.makeText(context,"Probando"  ,Toast.LENGTH_LONG).show();
    	String key = LocationManager.KEY_PROXIMITY_ENTERING;
    	Boolean entering = intent.getBooleanExtra(key, false);
    	
    	
    	if (entering){
    		notificationTitle="Has entrado a la Geocerca" + intent.getStringExtra("idGeo");
            notificationContent="¿Quieres entrenar acá?";
            tickerMessage = "Profit ha encontrado un lugar para entrenar!";
    	}
    	else{
    		notificationTitle="Has salido de la Geocerca";
            notificationContent="Saliendo del lugar de entrenamiento";
            tickerMessage = "Saliendo del luugar de entrenamiento";
    		
    	}
    	
    	
    	 Intent notificationIntent = new Intent(context,NotificationView.class);
         notificationIntent.putExtra("content", notificationContent );
         
         // This is needed to make this intent different from its previous intents 
         notificationIntent.setData(Uri.parse("tel:/"+ (int)System.currentTimeMillis()));
  
         //Creating different tasks for each notification. See the flag Intent.FLAG_ACTIVITY_NEW_TASK 
         PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
  
         // Getting the System service NotificationManager 
         NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
  
         // Configuring notification builder to create a notification 
         NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                 .setWhen(System.currentTimeMillis())
                 .setContentText(notificationContent)
                 .setContentTitle(notificationTitle)
                 .setSmallIcon(R.drawable.ic_launcher)
                 .setAutoCancel(true)
                 .setTicker(tickerMessage)
                 .setContentIntent(pendingIntent);
  
         // Creating a notification from the notification builder 
         Notification notification = notificationBuilder.build();
  
         // Sending the notification to system.
         //* The first argument ensures that each notification is having a unique id
         //* If two notifications share same notification id, then the last notification replaces the first notification
         //* 
         nManager.notify((int)System.currentTimeMillis(), notification);
	}
	
}

