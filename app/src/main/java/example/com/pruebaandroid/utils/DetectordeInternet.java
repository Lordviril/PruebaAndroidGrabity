package example.com.pruebaandroid.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class DetectordeInternet {

private Context contexto;

 public DetectordeInternet(Context contexto)
 {
 this.contexto = contexto;

 }
 public boolean estasConectado()
 {
 ConnectivityManager conectividad = (ConnectivityManager)
 contexto.getSystemService(Context.CONNECTIVITY_SERVICE);

 if (conectividad != null)
 {
 NetworkInfo[] informacion = conectividad.getAllNetworkInfo();

 if(informacion != null)
 for (int i= 0; i< informacion.length; i++)
 if (informacion[i].getState() == NetworkInfo.State.CONNECTED)
 return true;

 }// fin de if conectividad
 return false;
 }
}
