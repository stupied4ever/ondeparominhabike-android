package br.com.ondeparominhabike;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import br.com.ondeparominhabike.json.Lugar;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.gson.Gson;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends MapActivity {
	
	String url = "http://10.10.1.100:3000/lugares.json";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        MapView mapView = (MapView) findViewById(R.id.mapview);
        MapController mapController = mapView.getController();
        
        mapView.setBuiltInZoomControls(true);
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.bicicle);
        
        BicicleItemizedOverlay itemizedoverlay = new BicicleItemizedOverlay(drawable, MainActivity.this);
        
        
        Lugar[] lugares = parseJson();
        
        for(Lugar lugar: lugares){
        	
        	OverlayItem overlayitem = new OverlayItem(lugar.getGeoPoint(), lugar.tipo, lugar.nome);
            itemizedoverlay.addOverlay(overlayitem);
        }
        
        mapOverlays.add(itemizedoverlay);
        
        if(lugares.length > 0){
        	mapController.setZoom(14);
            mapController.setCenter(lugares[0].getGeoPoint());	
        }
        
        
        
        
    }
    
    
    private Lugar[] parseJson(){
    	Gson gson = new Gson();
        
    	InputStream source = retrieveStream(url);
    	
        Reader reader = new InputStreamReader(source);
        
        Lugar[] lugares = gson.fromJson(reader, Lugar[].class);
        
        return lugares;
        
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private InputStream retrieveStream(String url) {
        
        DefaultHttpClient client = new DefaultHttpClient(); 
        
        HttpGet getRequest = new HttpGet(url);
          
        try {
           
           HttpResponse getResponse = client.execute(getRequest);
           final int statusCode = getResponse.getStatusLine().getStatusCode();
           
           if (statusCode != HttpStatus.SC_OK) { 
              return null;
           }

           HttpEntity getResponseEntity = getResponse.getEntity();
           return getResponseEntity.getContent();
           
        } 
        catch (IOException e) {
           getRequest.abort();
        }
        
        return null;
        
     }
}