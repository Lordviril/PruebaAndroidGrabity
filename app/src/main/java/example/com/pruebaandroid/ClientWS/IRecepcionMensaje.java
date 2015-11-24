package example.com.pruebaandroid.ClientWS;

/**
 * Interface con las definciciones de los m�todos
 * usados para intercambiar informaci�n entre
 * instancias de Activity e instancias de Handler
 * @author humberto.quijano
 *
 */
public interface IRecepcionMensaje {
	void recibirTexto(String texto);
	
	void recibirError(String textoError);
}
