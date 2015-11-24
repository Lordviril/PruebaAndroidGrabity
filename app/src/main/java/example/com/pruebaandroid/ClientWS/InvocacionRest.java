package example.com.pruebaandroid.ClientWS;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.net.URI;

/**
 * Implementa la invocacion de una operacion HTTP 
 * y notifica a la actividad invocadora, 
 * mediante una instancia de handler
 * acerca de los resultados de la invocación
 * @author Humberto Quijano y  Andres Serrano vivas
 *
 */
public class InvocacionRest implements Runnable {
	
	// peticion HTTP 
	private HttpUriRequest request;
	
	// handler para notificación
	private Handler notificador = null;
	
	/**
	 * Constructor parametrizado
	 * @param url	url destino de la invocacion
	 * @param json	cadena json con datos de invocaci�n puede ser nula o vac�a
	 * @param tipoOperacion	tipo de operacion http a ejecutar en la petici�n rest
	 * @param notificador	handler usado para informar resultados
	 */
	public InvocacionRest(String url, String json, TipoOperacionRest tipoOperacion, Handler notificador,String token) {
		this.notificador = notificador;
		try	{
			switch(tipoOperacion){
				case GET:
					HttpGet get =  new HttpGet( new URI(url) );
					get.setHeader("Authorization","token "+token);
					this.request = get;
					break;
				case POST:
					HttpPost post = new HttpPost( new URI(url) );
					String datos = json;
					StringEntity entity = new StringEntity(datos, HTTP.UTF_8);
					entity.setContentType("application/json");
					post.setHeader("Authorization","token "+token);
					entity.setContentEncoding("UTF-8");
					post.setEntity(entity);
                    this.request = post;
					break;
					
				case PUT:
					HttpPut put = new HttpPut(new URI(url));
					String datosput = json;
					StringEntity entityput = new StringEntity(datosput, HTTP.UTF_8);
					entityput.setContentType("application/json");
                    put.setHeader("Authorization","token "+token);
					entityput.setContentEncoding("UTF-8");
					put.setEntity(entityput);
					this.request = put;
					break;	
					
				case DELETE:
					HttpDelete delete = new HttpDelete(new URI(url));
                    delete.setHeader("Authorization","token "+token);
					this.request = delete;
					break;
                case FAC:
                    HttpPost fac = new HttpPost( new URI(url) );
                    String datosfac = json;
                    StringEntity entityfac = new StringEntity(datosfac, HTTP.UTF_8);
                    entityfac.setContentType("text/xml");
                    entityfac.setContentEncoding("UTF-8");
                    fac.setEntity(entityfac);
					this.request = fac;
                    break;
				case FOTO:
					throw new Exception ("Invocacion no válida, utiliza el constructor apropiado");
			}
		} catch(Exception e) {
			InformarError(e.getMessage());
		}
		
	}

	/**
	 * Constructor parametrizado para env�o de fotos
	 * @param url	url destino de la invocacion
	 * @param id identificador del recurso asociado 
	 * @param foto fotgraf�a a transmitir
	 * @param notificador	handler usado para informar resultados
	 */
	public InvocacionRest(String url, int id, Bitmap foto, Handler notificador) {
		try	{
			this.notificador =notificador;
			HttpPost post = new HttpPost( new URI(url) );
			post.addHeader("id", String.valueOf(id));
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			foto.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] btArr = stream.toByteArray();
			
			ByteArrayEntity entity = new ByteArrayEntity(btArr); 
			post.setEntity(entity);
			this.request = post;
		} catch(Exception e) {
			InformarError(e.getMessage());
		}
	}

	@Override
	public void run() {
		try{
			HttpClient client = new DefaultHttpClient();
			HttpResponse serverResponse = client.execute(this.request);
			
			// validar el c�digo de retorno del servicio rest
			int statusCode = serverResponse.getStatusLine().getStatusCode();
			if ( statusCode < 200 || statusCode >= 300 ){
				this.InformarRespuesta(String.valueOf(statusCode));				
			}
			else {
				BasicResponseHandler handler = new BasicResponseHandler();
				String response = handler.handleResponse(serverResponse);
				this.InformarRespuesta(response);				
			}
		} catch (Exception e) {
			InformarError(e.getMessage());
		}
	}
	
	/**
	 * envia una notificaci�n de un mensaje de error
	 * @param mensajeError	mensaje de error a enviar
	 */
	private void InformarError(String mensajeError){
		Bundle b = Toolkit.getStringAsABundle(String.format("Error=%s", mensajeError));
		this.EnviarMensaje(b);
	}

	/**
	 * env�a una notificaci�n con los adtos de respuesta de una invocaci�n
	 * @param respuesta	informaci�n a enviar
	 */
	private void InformarRespuesta(String respuesta){
		Bundle b = Toolkit.getStringAsABundle(respuesta);
		this.EnviarMensaje(b);	
	}
	
	/**
	 * env�a un mensaje usando la instancia de Handler
	 * @param b	Instancia de bundle conteniendo el texto del mensaje a enviar
	 */
	private void EnviarMensaje(Bundle b) {

		Message msg = this.notificador.obtainMessage();
		msg.setData(b);
		this.notificador.sendMessage(msg);			
	}

}

