package app.mediczy_com.webservice.request;

/**
 * Created by Prithivi on 19-11-2016.
 */
public interface ResponseListener {
	
	public void onResponseReceived(Object responseObj, int requestType);
 
}