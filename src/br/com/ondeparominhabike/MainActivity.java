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

import br.com.ondeparominhabike.database.DatabaseHelper;
import br.com.ondeparominhabike.json.Lugar;
import br.com.ondeparominhabike.model.Lugares;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.gson.Gson;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends MapActivity {
	
	List<Lugar> lugares;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Lugares.sincronizaLugares(this);
        
        MapView mapView = (MapView) findViewById(R.id.mapview);
        MapController mapController = mapView.getController();
        
        mapView.setBuiltInZoomControls(true);
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.bicicle);
        
        BicicleItemizedOverlay itemizedoverlay = new BicicleItemizedOverlay(drawable, MainActivity.this);
        
        
        lugares = Lugares.todos(this);
        
        for(Lugar lugar: lugares){
        	
        	OverlayItem overlayitem = new OverlayItem(lugar.getGeoPoint(), lugar.tipo, lugar.nome);
            itemizedoverlay.addOverlay(overlayitem);
        }
        
        mapOverlays.add(itemizedoverlay);
        
        if(lugares.size() > 0){
        	mapController.setZoom(14);
            mapController.setCenter(lugares.get(0).getGeoPoint());	
        }                
    }
    
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}