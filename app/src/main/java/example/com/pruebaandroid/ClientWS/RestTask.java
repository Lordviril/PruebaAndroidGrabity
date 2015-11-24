package example.com.pruebaandroid.ClientWS;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;


public class RestTask extends AsyncTask<HttpUriRequest, Void, String> {

		public static final String HTTP_RESPONSE = "httpResponse";
		private Context mContext;
		private HttpClient mClient;
		private String mAction;
		
		public RestTask(Context context, String action) {
			mContext = context;
			mAction = action;
			mClient = new DefaultHttpClient();
		}
		
		public RestTask(Context context, String action, HttpClient client) {
			mContext = context;
			mAction = action;
			mClient = client;
		}
		
		@Override
		protected String doInBackground(HttpUriRequest... params) {
			try{
				HttpUriRequest request = params[0];
				HttpResponse serverResponse = mClient.execute(request);
				// validar el c�digo de retorno del servicio rest
				int statusCode = serverResponse.getStatusLine().getStatusCode();
				if ( statusCode < 200 || statusCode >= 300 )
					return String.valueOf(statusCode);
				else {
					BasicResponseHandler handler = new BasicResponseHandler();
					String response = handler.handleResponse(serverResponse);
					return response;				
				}
			} catch (Exception e) {
				return e.getMessage();
			}
		}

		@Override
		protected void onPostExecute(String result) {
			Intent intent  = new Intent(mAction);
			intent.putExtra(HTTP_RESPONSE, result);
			mContext.sendBroadcast(intent);
		}
	}



