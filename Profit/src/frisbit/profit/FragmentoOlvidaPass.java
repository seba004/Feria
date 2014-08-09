package frisbit.profit;

import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import frisbit.profit.FragmentoCrearCuenta.asyncCreateAccount;
import test.login.library.Httppostaux;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentoOlvidaPass extends Fragment  implements  OnClickListener {
	
	private EditText email;
	private TextView texto;
	private Button btn_ingresar;
	
	String URL_connect = "http://www.frisbit.net/nicolas/send_mail.php";
	Httppostaux post;
	private ProgressDialog pDialog;
	boolean result_back;
	
	Validador validar=new Validador();
	
	@Override
	   public View onCreateView(LayoutInflater inflater,
	      ViewGroup container, Bundle savedInstanceState) {
		
	       //Inflate the layout for this fragment
		View Vista=inflater.inflate(R.layout.fragmento_olvidar_pass, container, false);
		post = new Httppostaux();
		
		texto=(TextView)Vista.findViewById(R.id.textView1);
		email=(EditText)Vista.findViewById(R.id.email_user);
		
		btn_ingresar=(Button)Vista.findViewById(R.id.boton_aceptar);
		btn_ingresar.setOnClickListener(this);
		return Vista;
	   }


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int validador = 0;
		
		String valor_email=email.getText().toString();
		boolean validar_email= validar.validateemail(valor_email);
		
		if (validar_email == false){
			Toast.makeText(this.getActivity(), 
		            "Por favor ingrese e-mail valido", Toast.LENGTH_LONG).show();//validaciones
			validador=1;
		}
		
		if(validador==0){
			Toast.makeText(this.getActivity(), 
		            "Todo ok", Toast.LENGTH_LONG).show();//validaciones
			new asyncRecuperarPass().execute(valor_email);
		}
	}
	
	// vibra y muestra un Toast
				public void err_login() {
					Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
					vibrator.vibrate(200);
					String msg = "Error:Nombre de usuario o password incorrectos";
//					Toast toast1 = Toast.makeText(getApplicationContext(), msg,	Toast.LENGTH_SHORT);
					Toast toast1 = Toast.makeText(getActivity().getApplicationContext(), msg,	Toast.LENGTH_SHORT);
					toast1.show();
				}

			/*
			 * Valida el estado del logueo solamente necesita como parametros el usuario
			 * y passw
			 */
			public boolean recuperarPassword(String email) {
				int passwordStatus = -1;

				/*
				 * Creamos un ArrayList del tipo nombre valor para agregar los datos
				 * recibidos por los parametros anteriores y enviarlo mediante POST a
				 * nuestro sistema para relizar la validacion
				 */
				ArrayList<NameValuePair> postparameters2send = new ArrayList<NameValuePair>();

				postparameters2send.add(new BasicNameValuePair("email", email));
				

				// realizamos una peticion y como respuesta obtenes un array JSON
				JSONArray jdata = post.getserverdata(postparameters2send, URL_connect);


				// si lo que obtuvimos no es null
				if (jdata != null && jdata.length() > 0) {

					JSONObject json_data; // creamos un objeto JSON
					try {
						// leemos el primer segmento en nuestro caso el unico
						json_data = jdata.getJSONObject(0); 
															
						// accedemos al valor
						passwordStatus = json_data.getInt("passwordStatus");
						// muestro por log que obtuvimos
						Log.e("passwordStatus", "passwordStatus= " + passwordStatus);
																		
												
					} catch (JSONException e) {
						e.printStackTrace();
					}

					// validamos el valor obtenido
					if (passwordStatus == 0) {// [{"logstatus":"0"}]
						Log.e("passwordStatus ", "valido");
						return true;
					} else {// [{"logstatus":"1"}]
						Log.e("passwordStatus ", "invalido");
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

			class asyncRecuperarPass extends AsyncTask<String, String, String> {

				String email;

				protected void onPreExecute() {
					// para el progress dialog
					// TEST!!!
					pDialog = new ProgressDialog(getActivity());
					pDialog.setMessage("Enviando contraseña al email registrado....");
					pDialog.setIndeterminate(false);
					pDialog.setCancelable(false);
					pDialog.show();
				}

				protected String doInBackground(String... params) {
					System.out.println("doInBackground");
					// obtnemos usr y pass
					email = params[0];

					// enviamos y recibimos y analizamos los datos en segundo plano.
					if (recuperarPassword(email) == true) {
						System.out.println("True");
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

//						Intent i = new Intent(Login.this, HiScreen.class);
//						Intent i = new Intent(getActivity(), HiScreen.class);
//						i.putExtra("user", user);
//						startActivity(i);
						String msg = "VERY GOOD MAN";
						
						Toast toast1 = Toast.makeText(getActivity().getApplicationContext(), 
								msg, Toast.LENGTH_SHORT);
						
						toast1.show();
						
						Intent actividad_iniciar = new Intent(getActivity(), MainActivity.class);
						startActivity(actividad_iniciar);

					} else {
						err_login();
					}

				}

			}
}
