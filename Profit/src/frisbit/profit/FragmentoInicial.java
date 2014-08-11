package frisbit.profit;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import test.login.library.Httppostaux;
import android.support.v4.app.FragmentActivity;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;




public class FragmentoInicial extends Fragment implements  OnClickListener {
	private TextView registro;
	private EditText email;
	private EditText pass;
	private Button iniciar_sesion;//se defiene variables a usar
	
	Httppostaux post;
	boolean result_back;
	
	// pDialog usado en el momento del login.
	private ProgressDialog pDialog;
	//URL_connect direccion del php que realiza el login.
	String URL_connect="http://www.frisbit.net/nicolas/acces.php";
	
	
	Validador validar=new Validador();
	
   @Override
   public View onCreateView(LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	   View Vista= inflater.inflate(R.layout.fragmento_inicial, container, false); //se crea fragment 
	   post = new Httppostaux();
	   iniciar_sesion=(Button)Vista.findViewById(R.id.boton_inicio_session);//le asigno a un boton su correspondiente en el xml
	    iniciar_sesion.setOnClickListener(this);//le doy la capacidad de  esperar evento

      return Vista;
   }
   

	@Override
	public void onClick(View v) {
		int validador=0;
		email =(EditText)getView().findViewById(R.id.email_usuario);//le asigno a la variable  lo que esta puesto en  el xml
		pass =(EditText)getView().findViewById(R.id.pass_usuario);//le asigno a la variable  lo que esta puesto en  el xml
		// TODO Auto-generated method stub
		String valor_email=email.getText().toString();//se obtiene el valor
		String valor_pass=pass.getText().toString();
		
		boolean validar_email= validar.validateemail(valor_email);
		boolean validar_pass= validar.validatepass(valor_pass);
		
		
		if (validar_email == false){
			Toast.makeText(this.getActivity(), 
		            "Por favor ingrese e-mail valido", Toast.LENGTH_LONG).show();//validaciones
			validador=1;
		}
		if (validar_pass==false){
			Toast.makeText(this.getActivity(), 
		            "Por favor ingrese una password valida", Toast.LENGTH_LONG).show();
			validador=1;
		}
		
		if(validador==0){
			
			new asynclogin().execute(valor_email, valor_pass);
			
			
			/*
			 * Luego de hacer Login se dirigir� a la pantasha principal.
			 */
			
			
		}
	}

	// vibra y muestra un Toast
			public void err_login() {
				Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(200);
				String msg = "Error:Nombre de usuario o password incorrectos";
//				Toast toast1 = Toast.makeText(getApplicationContext(), msg,	Toast.LENGTH_SHORT);
				Toast toast1 = Toast.makeText(getActivity().getApplicationContext(), msg,	Toast.LENGTH_SHORT);
				toast1.show();
			}

		/*
		 * Valida el estado del logueo solamente necesita como parametros el usuario
		 * y passw
		 */
		public boolean loginstatus(String email, String password) {
			int logstatus = -1;

			/*
			 * Creamos un ArrayList del tipo nombre valor para agregar los datos
			 * recibidos por los parametros anteriores y enviarlo mediante POST a
			 * nuestro sistema para relizar la validacion
			 */
			ArrayList<NameValuePair> postparameters2send = new ArrayList<NameValuePair>();

			postparameters2send.add(new BasicNameValuePair("email", email));
			postparameters2send.add(new BasicNameValuePair("password", password));

			// realizamos una peticion y como respuesta obtenes un array JSON
			JSONArray jdata = post.getserverdata(postparameters2send, URL_connect);


			// si lo que obtuvimos no es null
			if (jdata != null && jdata.length() > 0) {

				JSONObject json_data; // creamos un objeto JSON
				try {
					// leemos el primer segmento en nuestro caso el unico
					json_data = jdata.getJSONObject(0); 
														
					// accedemos al valor
					logstatus = json_data.getInt("logstatus");
					// muestro por log que obtuvimos
					Log.e("loginstatus", "logstatus= " + logstatus);
																	
											
				} catch (JSONException e) {
					e.printStackTrace();
				}

				// validamos el valor obtenido
				if (logstatus == 0) {// [{"logstatus":"0"}]
					Log.e("loginstatus ", "valido");
					return true;
				} else {// [{"logstatus":"1"}]
					Log.e("loginstatus ", "invalido");
					
					return false;
				}

			} else { // json obtenido invalido verificar parte WEB.
				Log.e("JSON  ", "ERROR");
				return false;
			}

		}
		
		/*
		 * CLASE ASYNCTASK
		 * 
		 * usaremos esta para poder mostrar el dialogo de progreso mientras enviamos
		 * y obtenemos los datos podria hacerse lo mismo sin usar esto pero si el
		 * tiempo de respuesta es demasiado lo que podria ocurrir si la conexion es
		 * lenta o el servidor tarda en responder la aplicacion sera inestable.
		 * ademas observariamos el mensaje de que la app no responde.
		 */

		class asynclogin extends AsyncTask<String, String, String> {

			String email, pass;

			protected void onPreExecute() {
				// para el progress dialog
				// TEST!!!
				pDialog = new ProgressDialog(getActivity());
				pDialog.setMessage("Autenticando....");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
			}

			protected String doInBackground(String... params) {
				System.out.println("doInBackground");
				// obtnemos usr y pass
				email = params[0];
				pass = params[1];
				
				//se crean las preferncias
				SharedPreferences preferences = getActivity().getSharedPreferences("prefs",Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = preferences.edit();
				

			    //se guarda el mail, la pass y un string por si a caso
				 editor.putString("email", email);
				 editor.putString("pass", pass);
				 editor.putString("logged", "logged");
				 editor.commit();
				 
				// enviamos y recibimos y analizamos los datos en segundo plano.
				if (loginstatus(email, pass) == true) {
					return "ok"; // login valido
				} else {
					System.out.println("False");
					return "err"; // login invalido
				}

			}

			/*
			 * Una vez terminado doInBackground segun lo que halla ocurrido pasamos
			 * a la sig. activity o mostramos error
			 */
			protected void onPostExecute(String result) {

				pDialog.dismiss();// ocultamos progess dialog.
				Log.e("onPostExecute=", "" + result);

				if (result.equals("ok")) {
					Intent actividad_iniciar = new Intent(getActivity(), InterfazPrueba.class);
					startActivity(actividad_iniciar);
					
					String msg = "Ha iniciado sesión";
					Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

				} else {
					err_login();
				}

			}

		}
	
	


}



