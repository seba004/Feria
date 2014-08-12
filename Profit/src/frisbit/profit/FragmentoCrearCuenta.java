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
	private EditText sexo;
	private EditText altura;
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
		sexo=(EditText)Vista.findViewById(R.id.sexo);
		altura=(EditText)Vista.findViewById(R.id.altura);
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
		String valor_altura=altura.getText().toString();
		String valor_sexo=sexo.getText().toString();
		String valor_fecha_nacimiento=fecha_nacimiento.getText().toString();
		
		boolean validar_nombre = validar.validatename(valor_nombre);
		boolean validar_email= validar.validateemail(valor_email);
		boolean validar_pass= validar.validatepass(valor_pass1);
		boolean validar_fecha=validar.validatedate(valor_fecha_nacimiento);
		boolean  validar_altura =validar.validatealtura(valor_altura);
		boolean validar_sexo =validar.validatesexo(valor_sexo);
		
		if (validar_pass == false){
			Toast.makeText(this.getActivity(), 
					"contrase�a invalida ingrese contrase�a alfanumerica ", Toast.LENGTH_LONG).show();//validaciones
			validador=1;
		}
		
		if (validar_altura==false){
			Toast.makeText(this.getActivity(), 
					"Altura invalida ", Toast.LENGTH_LONG).show();//validaciones
			validador=1;
		}
		
		if (validar_sexo==false){
			Toast.makeText(this.getActivity(), 
					"Sexo invalida ", Toast.LENGTH_LONG).show();//validaciones
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
//			Toast.makeText(this.getActivity(), "Todo ok", Toast.LENGTH_LONG).show();//validaciones
			new asyncCreateAccount().execute(valor_nombre, valor_fecha_nacimiento, valor_email, valor_nickname, valor_pass1, valor_altura, valor_sexo);	
		}
	}
	
	public int createAccount(String nombre, String fecha, String email, String username, String password, String genero, String altura) {
		int accountStatus = -1;

		/*
		 * Creamos un ArrayList del tipo nombre valor para agregar los datos
		 * recibidos por los parametros anteriores y enviarlo mediante POST a
		 * nuestro sistema para relizar la validacion
		 */
		ArrayList<NameValuePair> postparameters2send = new ArrayList<NameValuePair>();
	
		postparameters2send.add(new BasicNameValuePair("nombre", nombre));
		postparameters2send.add(new BasicNameValuePair("fecha", fecha));
		postparameters2send.add(new BasicNameValuePair("email", email));
		postparameters2send.add(new BasicNameValuePair("usuario", username));
		postparameters2send.add(new BasicNameValuePair("password", password));
		postparameters2send.add(new BasicNameValuePair("genero", genero));
		postparameters2send.add(new BasicNameValuePair("altura", altura));
		
		// realizamos una peticion y como respuesta obtenes un array JSON
		JSONArray jdata = post.getserverdata(postparameters2send, URL_connect);

		// si lo que obtuvimos no es null
		if (jdata != null && jdata.length() > 0) {
			JSONObject json_data; // creamos un objeto JSON
			try {
				// leemos el primer segmento en nuestro caso el unico
				json_data = jdata.getJSONObject(0); 
				Log.e("nasa",json_data.toString());									
				// accedemos al valor
				accountStatus = json_data.getInt("accountStatus");
				// muestro por log que obtuvimos
				Log.e("accountStatus", "accountStatus= " + accountStatus);
																
										
			} catch (JSONException e) {
				e.printStackTrace();
			}

			// validamos el valor obtenido
			if (accountStatus == 0) {// [{"logstatus":"0"}]
				Log.e("accountStatus ", "valido");
				return 0;
			} else if (accountStatus == 2) {// [{"logstatus":"1"}]
				return 2;
			} else {
				Log.e("accountStatus ", "invalido");
				return 1;
			}

		} else { // json obtenido invalido verificar parte WEB.
			Log.e("JSON  ", "ERROR");
			return 1;
		}
	}
	
	class asyncCreateAccount extends AsyncTask<String, String, String> {
//		asyncCreateAccount().execute(valor_nombre, valor_fecha_nacimiento, valor_email, valor_nickname, valor_pass1);
		
		String nombre, fecha, email, user, pass, genero, altura;

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
			genero = params[5];
			altura = params[6];

			// enviamos y recibimos y analizamos los datos en segundo plano.
			int result = createAccount(nombre, fecha, email, user, pass, genero, altura);
			if (result == 0) {
				System.out.println("True");
				return "ok"; // login valido
			} else if (result == 2){
				return "2"; // login invalido
			}
			else {
				return "err";
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
				String msg = "Cuenta creada con éxito";
				Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
				
				Intent actividad_iniciar = new Intent(getActivity(), InterfazPrueba.class);
				startActivity(actividad_iniciar);

			} else if (result.equals("2")) {
				Toast.makeText(getActivity().getApplicationContext(), "El email ya está en uso", Toast.LENGTH_SHORT).show();
			} else {
//				err_login();
				String msg = "Error al procesar la solicitud";
				Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

			}
		}
	}
	
}
