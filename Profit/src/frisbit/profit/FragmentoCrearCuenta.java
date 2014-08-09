package frisbit.profit;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import test.login.library.Httppostaux;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FragmentoCrearCuenta extends Fragment  implements  OnClickListener {
	private EditText nombre;
	private EditText email;
	private EditText nickname;
	private EditText pass1;
	private EditText pass2;
	private EditText fecha_nacimiento;
	private Button btn_ingresar;
	
	String URL_connect = "http://www.frisbit.net/nicolas/adduser.php";
	Httppostaux post;
	private ProgressDialog pDialog;
	boolean result_back;
	
	Validador validar=new Validador();
	
	@Override
	   public View onCreateView(LayoutInflater inflater,
	      ViewGroup container, Bundle savedInstanceState) {
		
	       //Inflate the layout for this fragment
		View Vista=inflater.inflate(R.layout.fragmento_cuenta, container, false);
		post = new Httppostaux();
		
		nombre=(EditText)Vista.findViewById(R.id.nombre_cuenta);
		email=(EditText)Vista.findViewById(R.id.email_cuenta);
		nickname=(EditText)Vista.findViewById(R.id.nickname_cuenta);
		pass1=(EditText)Vista.findViewById(R.id.pass_cuenta);
		pass2=(EditText)Vista.findViewById(R.id.check_pass_cuenta);
		fecha_nacimiento=(EditText)Vista.findViewById(R.id.nacimiento_cuenta);
		btn_ingresar=(Button)Vista.findViewById(R.id.crear_cuenta);
		btn_ingresar.setOnClickListener(this);
		return Vista;
	   }


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int validador=0;
		String valor_nombre=nombre.getText().toString();
		String valor_email=email.getText().toString();
		String valor_nickname=nickname.getText().toString();
		String valor_pass1=pass1.getText().toString();
		String valor_pass2=pass2.getText().toString();
		String valor_fecha_nacimiento=fecha_nacimiento.getText().toString();
		
		boolean validar_nombre = validar.validatename(valor_nombre);
		boolean validar_email= validar.validateemail(valor_email);
		boolean validar_pass= validar.validatepass(valor_pass1);
		boolean validar_fecha=validar.validatedate(valor_fecha_nacimiento);
		
		
		if (validar_pass == false){
			Toast.makeText(this.getActivity(), 
					"contraseña invalida ingrese contraseña alfanumerica ", Toast.LENGTH_LONG).show();//validaciones
			validador=1;
		}
		
		
		if(!valor_pass1.equals(valor_pass2)){
			Toast.makeText(this.getActivity(), 
					 "Las contraseas no coinciden", Toast.LENGTH_LONG).show();
			validador=1;
		}
		
		if (validar_email == false){
			Toast.makeText(this.getActivity(), 
		            "Por favor ingrese e-mail valido", Toast.LENGTH_LONG).show();//validaciones
			validador=1;
		}
		
		if (validar_nombre == false){
			Toast.makeText(this.getActivity(), 
		            "Por favor ingrese nombre valido", Toast.LENGTH_LONG).show();//validaciones
			validador=1;
		}
		
		if (valor_nickname.length()<1){
			Toast.makeText(this.getActivity(), 
		            "Por favor ingrese nickname valido", Toast.LENGTH_LONG).show();//validaciones
			validador=1;
		}
		
		if ( validar_fecha == false){
			Toast.makeText(this.getActivity(), 
		            "Por favor ingrese fecha valida en formato dd/mm/aa", Toast.LENGTH_LONG).show();//validaciones
			validador=1;
		}
		if(validador==0){
			Toast.makeText(this.getActivity(), 
		            "Todo ok", Toast.LENGTH_LONG).show();//validaciones
			new asyncCreateAccount().execute(valor_nombre, valor_fecha_nacimiento, valor_email, valor_nickname, valor_pass1);	
		}
	}
	
	public boolean createAccount(String nombre, String fecha, String email, String username, String password ) {
		int accountStatus = -1;

		/*
		 * Creamos un ArrayList del tipo nombre valor para agregar los datos
		 * recibidos por los parametros anteriores y enviarlo mediante POST a
		 * nuestro sistema para relizar la validacion
		 */
		ArrayList<NameValuePair> postparameters2send = new ArrayList<NameValuePair>();
		/* INSERT INTO `persona`(`ID_persona`, `Nombre`, `fecha_nacimiento`, `correo`, `user`,
		 *  `pass`, `puntos`)
		 * VALUES ([value-1],[value-2],[value-3],[value-4],[value-5],[value-6],[value-7])
		 * valor_nombre valor_email valor_nickname valor_pass1 valor_fecha_nacimiento 
		 */
		postparameters2send.add(new BasicNameValuePair("usuario", username));
		postparameters2send.add(new BasicNameValuePair("password", password));
		postparameters2send.add(new BasicNameValuePair("nombre", nombre));
		postparameters2send.add(new BasicNameValuePair("email", email));
		postparameters2send.add(new BasicNameValuePair("fecha", fecha));
//		System.out.println(postparameters2send);
		// realizamos una peticion y como respuesta obtenes un array JSON
		System.out.println("POST PRE");
		JSONArray jdata = post.getserverdata(postparameters2send, URL_connect);
		System.out.println("POST SEND");

		// si lo que obtuvimos no es null
		if (jdata != null && jdata.length() > 0) {
			JSONObject json_data; // creamos un objeto JSON
			try {
				// leemos el primer segmento en nuestro caso el unico
				json_data = jdata.getJSONObject(0); 
													
				// accedemos al valor
				accountStatus = json_data.getInt("logstatus");
				// muestro por log que obtuvimos
				Log.e("loginstatus", "logstatus= " + accountStatus);
																
										
			} catch (JSONException e) {
				e.printStackTrace();
			}

			// validamos el valor obtenido
			if (accountStatus == 0) {// [{"logstatus":"0"}]
				Log.e("accountStatus ", "valido");
				return true;
			} else {// [{"logstatus":"1"}]
				Log.e("accountStatus ", "invalido");
				return false;
			}

		} else { // json obtenido invalido verificar parte WEB.
			Log.e("JSON  ", "ERROR");
			return false;
		}
	}
	
	class asyncCreateAccount extends AsyncTask<String, String, String> {
//		asyncCreateAccount().execute(valor_nombre, valor_fecha_nacimiento, valor_email, valor_nickname, valor_pass1);
		
		String nombre, fecha, email, user, pass;

		protected void onPreExecute() {
			// para el progress dialog
			// TEST!!!
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Creando cuenta....");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... params) {
			// obtnemos usr y pass
			nombre = params[0];
			fecha = params[1];
			email = params[2];
			user = params[3];
			pass = params[4];
			System.out.println("doInBackground"+nombre+fecha+email+user+pass);
			// enviamos y recibimos y analizamos los datos en segundo plano.
			if (createAccount(nombre, fecha, email, user, pass) == true) {
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

//				Intent i = new Intent(Login.this, HiScreen.class);
//				Intent i = new Intent(getActivity(), HiScreen.class);
//				i.putExtra("user", user);
//				startActivity(i);

				String msg = "VERY GOOD MAN - Cuenta Creada";
				Context cont = getActivity().getApplicationContext();
				Toast toast1 = Toast.makeText(cont, msg, Toast.LENGTH_SHORT);
				toast1.show();
				
				Intent actividad_iniciar = new Intent(getActivity(), InterfazPrueba.class);
				startActivity(actividad_iniciar);

			} else {
//				err_login();
				System.out.println("ERROR LOGIN!!");
			}
		}
	}
	
}
