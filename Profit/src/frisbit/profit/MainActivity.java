package frisbit.profit;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private Button btn_olvidar_pass;
	private Button btn_nueva_cuenta;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
			
		// se obtienen las preferencias
				SharedPreferences prefs = getSharedPreferences("prefs",Context.MODE_PRIVATE); 
				String email = prefs.getString("email", null);
				String pass = prefs.getString("pass", null);
				

				// AQUI SEBOO agregué todo este if(email!= null && pass != null) , y a lo que estaba antes lo encerré en el else
				
				//si hay preferencias...
				if (email!= null && pass != null) 
				{
					
					Intent actividad_iniciar = new Intent(MainActivity.this, InterfazPrueba.class);
					startActivity(actividad_iniciar);
					// si hay sesion abierta se envia a "la pagina de inicio"(InterfazPrueba)
					// esta actividad deberia abrirse para cada
					//usuario osea me sirve guardar los nombres  estos solo existen una vez que están validados
					
					//System.out.println(email); en estas variables quedan guardados
					//System.out.println(pass);
				}
				
				//si no hay preferencias... (son null)
				else{
					
				 btn_olvidar_pass=(Button)findViewById(R.id.olvidar_pass);
				 btn_nueva_cuenta=(Button)findViewById(R.id.crear_cuenta);
				 
				 btn_olvidar_pass.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							/*Toast.makeText(getApplicationContext(), 
						            "boton pass", Toast.LENGTH_LONG).show();*/
							
							Intent actividad_cuenta = new Intent(MainActivity.this, ActivityPass.class);
							actividad_cuenta.putExtra("valor_fragmento",1);
							startActivity(actividad_cuenta);
							
							
						}
					});
				 
				 btn_nueva_cuenta.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							/*Toast.makeText(getApplicationContext(), 
						            "boton cuenta", Toast.LENGTH_LONG).show();*/
							
							Intent actividad_cuenta = new Intent(MainActivity.this, ActivityCuentas.class);
							actividad_cuenta.putExtra("valor_fragmento",2);
							startActivity(actividad_cuenta);
						}
					});
			}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	//boton de atras
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        moveTaskToBack(true); 
	    	
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
}
