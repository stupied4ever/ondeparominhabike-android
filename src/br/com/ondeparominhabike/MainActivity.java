package br.com.ondeparominhabike;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import br.com.ondeparominhabike.json.Lugar;
import br.com.ondeparominhabike.model.Lugares;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MainActivity extends MapActivity implements LocationListener {

	List<Lugar> lugares;
	//	private LocationManager locationManager;
	private MapController mapController;
	private MyLocationOverlay me = null;
	private MapView mapView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mapView = (MapView) findViewById(R.id.mapview);
		mapController = mapView.getController();

		mapView.setBuiltInZoomControls(true);

		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.pin);

		BicicleItemizedOverlay itemizedoverlay = new BicicleItemizedOverlay(drawable, MainActivity.this);


		lugares = Lugares.todos(this);

		for(Lugar lugar: lugares){
			OverlayItem overlayitem = new OverlayItem(lugar.getGeoPoint(), lugar.tipo, lugar.nome);
			itemizedoverlay.addOverlay(overlayitem);
		}

		mapOverlays.add(itemizedoverlay);

		mapController.setZoom(17);

		initMyLocation();
		//        	// Get the location manager
		//    		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		//
		//    		// List all providers:
			//    		
			//    		Location location = locationManager.getLastKnownLocation("gps");
		//    		if (location != null){
		//    			mapController.animateTo(new GeoPoint((int)(location.getLatitude()* 1E6), (int)(location.getLongitude()* 1E6)));	
		//    		}else{
		//    			mapController.animateTo(lugares.get(0).getGeoPoint());
		//    		}
	}

	private void initMyLocation(){
		me = new MyLocationOverlay(MainActivity.this, mapView);
		me.enableCompass();
		me.enableMyLocation();
		me.runOnFirstFix(new Runnable() {
			public void run() {
				mapController.animateTo(me.getMyLocation());
				mapController.setZoom(17);
			}
		});

		mapView.getOverlays().add(me);
	}


	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}


	/** Register for the updates when Activity is in foreground */
	@Override
	protected void onResume() {
		super.onResume();
		//		locationManager.requestLocationUpdates("gps", 20000, 1, this);
	}

	/** Stop the updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		//		locationManager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		mapController.animateTo(new GeoPoint((int)(location.getLatitude()* 1E6), (int)(location.getLongitude()* 1E6)));

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}