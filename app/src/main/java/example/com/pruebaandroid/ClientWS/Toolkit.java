package example.com.pruebaandroid.ClientWS;


import android.annotation.SuppressLint;
import android.os.Bundle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class Toolkit {

	/**
	 * verifica si una cadena representa un entero
	 * @param str	cadena a evaluar
	 * @return	boolean
	 */
	public static boolean esEntero(String str) {
		if (str == null) {
			return false;
		}
		int length = str.length();
		if (length == 0) {
			return false;
		}
		int i = 0;
		if (str.charAt(0) == '-') {
			if (length == 1) {
				return false;
			}
			i = 1;
		}
		for (; i < length; i++) {
			char c = str.charAt(i);
			if (c <= '/' || c >= ':') {
				return false;
			}
		}
		return true;
	}


	/**
	 * Permite generar una fecha valida para los webservice aptartit de un String
	 * @param cadena de  Fecha valida 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String GetFormatoFechaWeb(String Fecha) {

		String currentDateandTime="";
		SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
		Date fecha = null;
		try {

			fecha = formatoDelTexto.parse(Fecha);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			currentDateandTime = sdf.format(fecha);
		} catch (ParseException ex) {

			ex.printStackTrace();

		}
		return currentDateandTime;
	}
	/**
	 * Permite generar una fecha valida para los webservice aptartit de un String
	 * @param cadena de  Fecha valida 
	 * @return
	 */
	public static String GetFormatoFechaControl(String Fecha)
	{
		String currentDateandTime="";
		SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
		Date fecha = null;
		try {

			fecha = formatoDelTexto.parse(Fecha);
			
		    currentDateandTime = formatoDelTexto.format(fecha);
		   
		} catch (ParseException ex) {

			ex.printStackTrace();

		}
		return currentDateandTime;
	}

	/**
	 * a partir de una cadena genera una instancia de Bundle
	 * @param message cadena con informaci�n
	 * @return una instancia de Bundle
	 */
	public static Bundle getStringAsABundle(String message)
	{
		Bundle b = new Bundle();
		b.putString("msg", message);
		return b;
	}

	/**
	 * recupera una cadena asociada a la clave "msg" desde una instancia de Bundle
	 * @param b instancia de Bundle a tratar
	 * @return cadena asociada a la llave "msg"
	 */
	public static String getStringFromABundle(Bundle b)
	{
		return b.getString("msg");
	}
	/**
	 * retorna una fecha en formato epoch con marca de zona horaria
	 * @param yr a�o
	 * @param mes mes
	 * @param dia d�a
	 * @return
	 */
	public static String FechaJson(int yr, int mes, int dia) {
		return FechaJson(yr, mes, dia, 0, 0, 0);
	}

	/**
	 * retorna una fecha-hora en formato epoch con marca de zona horaria
	 * @param yr a�o
	 * @param mes mes
	 * @param dia d�a
	 * @param hr hora
	 * @param min minuto
	 * @param seg segundo
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static String FechaJson(int yr, int mes, int dia, int hr, int min, int seg) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(yr, mes, dia, hr, min, seg);

		long ms = cal.getTimeInMillis();
		long tz = cal.getTimeZone().getOffset(cal.getTimeInMillis())/(36000);
		return String.format("\\/Date(%s%+05d%n)\\/", ms, tz).replace("\r", "").replace("\n", "");
	}

	/**
	 * retorna una fecha-hora en formato para api Taxifiable yyyy-MM-dd
	 * @param yr a�o
	 * @param mes mes
	 * @param dia d�a
	 * @return
	 */ 
	public static String FechaJsonTF(int yr, int mes, int dia) {
		return FechaJsonTF(yr, mes, dia, 0, 0, 0);
	}

	/**
	 * retorna una fecha-hora en formato api Taxifiable yyyy-MM-dd
	 * @param yr a�o
	 * @param mes mes
	 * @param dia d�a
	 * @param hr hora
	 * @param min minuto
	 * @param seg segundo
	 * @return
	 */ 

	@SuppressLint("SimpleDateFormat")
	public static String FechaJsonTF(int yr, int mes, int dia, int hr, int min, int seg) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(yr, mes, dia, hr, min, seg);

		SimpleDateFormat sdtf = new SimpleDateFormat("yyyy-MM-dd");
		return sdtf.format(cal.getTime());
	}
	/**
	 * retorna una hora en formato para api Taxifiable HH:mm
	 * @param hr hora
	 * @param min minuto
	 * @return
	 */ 
	public static String HoraJsonTF(int hr, int min) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hr);
		cal.set(Calendar.MINUTE, min);

		SimpleDateFormat sdtf = new SimpleDateFormat("HH:mm");
		return sdtf.format(cal.getTime());
	}

	/**
	 * Obtiene y retorna como una cadena con formato "yyyy-MM-dd" la parte
	 * de fecha de una cadena de fecha-hora taxifiable con formato "yyyy-MM-dd hh:mm a"
	 * @param fechaHoraTF cadena a tratar
	 * @return
	 */
	public static String ParteFechaFormatoTF(String fechaHoraTF) {
		SimpleDateFormat sdtf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
		try {
			Date dt = sdtf.parse(fechaHoraTF);
			sdtf = new SimpleDateFormat("yyyy-MM-dd");
			return sdtf.format(dt);
		} catch (ParseException e) {
			return "";
		}
	}

	/**
	 * Obtiene y retorna como una cadena con formato "HH:mm" -hora militar- la parte de hora
	 * de una cadena de fecha-hora taxifiable con formato "yyyy-MM-dd hh:mm a" 
	 * @param fechaHoraTF
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String ParteHoraFormatoTF(String fechaHoraTF) {
		SimpleDateFormat sdtf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
		try {
			Date dt = sdtf.parse(fechaHoraTF);
			sdtf = new SimpleDateFormat("HH:mm");
			return sdtf.format(dt);
		} catch (ParseException e) {
			return "";
		}
	}

	/**
	 * Concatena las partes de fecha y hora con formatos "yyyy-MM-dd" y "HH:mm"
	 * y obtiene una representaci�n fecha hora en formato "yyyy-MM-dd hh:mm a"
	 * @param parteFecha cadena de fecha
	 * @param parteHora cadena de hora
	 * @return
	 */
	public static String CadenaFechaHoraTF(String parteFecha, String parteHora) {
		SimpleDateFormat sdtf = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
		String fechaHora = parteFecha + " " + parteHora;
		try {
			Date dt = sdtf.parse(fechaHora);
			sdtf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
			return sdtf.format(dt);
		} catch (ParseException e) {
			return "";
		}
	}

	/**
	 * Evalua un valor fecha-hora verificando que no sea anterior
	 * a la fecha-hora actual, ni sea anterior a la fecha-hora
	 * actual mas dos horas, evalua precisi�n al minuto
	 * @param fecha cadena de fecha
	 * @param hora cadena de hora
	 * @return
	 */
	public static boolean EsFechaProgramacionValida(String fecha, String hora) {
		SimpleDateFormat sdtf = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
		try {
			Date dt = sdtf.parse(fecha + " " + hora);
			// configurar la fecha-hora inicial de comparaci�n ajustando la precisi�n al minuto
			Calendar calIni = Calendar.getInstance();
			calIni.set(Calendar.MILLISECOND, 0);
			calIni.set(Calendar.SECOND, 0);

			// establecer la fecha-hora final de comparaci�n copiando el valor 
			// de la fecha-hora inicial y agregando dos horas
			Calendar calFin = Calendar.getInstance();
			calFin.setTimeInMillis(calIni.getTimeInMillis());
			calFin.add(Calendar.HOUR_OF_DAY, 2);
			return !(dt.before(calIni.getTime()) || dt.before(calFin.getTime()));
		} catch (ParseException e) {
			return false;
		}
	}


	/*
	 * retorna la parte com�n de un elemento secundario de direcci�n
	 * ej: 10012-100124 retornar� 100
	 */
	public static String RetornarParteComunDireccion(String direccion) throws Exception {
		try {
			// obtener las cadenas que componen el elemento, se asume "-" como separador
			direccion = direccion.replace(" ", "");
			String[] arr = direccion.split("-");

			// obtener la cantidad m�xima de caracteres a comparar
			int maxLen = arr[0].length();
			maxLen = arr[1].length() < maxLen ? arr[1].length() : maxLen;

			// encontrar la primera divergencia y retornar la subcadena
			// anterior a dicho punto
			for(int i = 0; i < maxLen; i++) {
				if (arr[0].charAt(i) != arr[1].charAt(i))
					return arr[0].substring(0, i);
			}

			// si no se han hallado divergencias se retorna
			// cualquiera de las cadenas hasta maxLen - 1
			return arr[0].substring(0, maxLen - 1);
		} catch(Exception ex) {
			throw ex;
		}
	}
}
