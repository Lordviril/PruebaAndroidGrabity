package example.com.pruebaandroid.ClientWS;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * se encarga de realizar los procesos de comunicacion
 * entre las clases que implementan IRecepcionMensaje
 * y aquellas que la que la usan como mecanismo de emisi�n de mensajes
 * @author humberto.quijano
 *
 */
public class NotificadorInvocacion extends Handler {
	
	// receptor de los mensajes transferidos
	private IRecepcionMensaje receptorMensaje = null;
	
	/**
	 * constructor parametrizado
	 * @param receptorMensaje	instancia que implementa IRecepcionMensaje
	 */
	public NotificadorInvocacion(IRecepcionMensaje receptorMensaje) {
		this.receptorMensaje = receptorMensaje;
	}
	
	@Override
	public void handleMessage(Message msg)
	{
		// recuperar el texto del mensaje enviado
		Bundle b = msg.getData();
		String texto  = Toolkit.getStringFromABundle(b);
		
		// notificar al receptor
		this.enviarMensaje(texto);
	}
	
	/**
	 * enviar el mensaje al receptor
	 * @param texto	texto a notificar
	 */
	private void enviarMensaje(String texto) {
		// verificar si el mensaje es de error
		// y llamar a la rutina apropiada en el receptor
		if (texto==null)
		{
			this.receptorMensaje.recibirError("Put");
		}
		else if (texto.startsWith("Error=")) {
			this.receptorMensaje.recibirError(texto.substring(6));
		} 
		 
		else {
			this.receptorMensaje.recibirTexto(texto);
		}
	}
	
}
