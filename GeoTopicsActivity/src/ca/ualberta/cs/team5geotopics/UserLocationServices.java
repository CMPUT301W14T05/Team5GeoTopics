package ca.ualberta.cs.team5geotopics;

import android.location.LocationManager;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.location.Criteria;

public class UserLocationServices {
	private transient GeoTopicsApplication application;
	private transient LocationManager lm;
	private transient String provider;
	private Double myLat;
	private Double myLong;

	public UserLocationServices() {
		this.application = GeoTopicsApplication.getInstance();
	}

	public Context getContext() {
		return application.getContext();
	}

	public boolean isNetworkAvailable() {
		return application.isNetworkAvailable();
	}

	/**
	 * Used to initialize the LocationManager that will help provide the
	 * application with the users current location. Establishes the best
	 * provider for such a task.
	 * 
	 * @param void
	 * @return void
	 */
	public void setUpLocationServices() {
		Context context = application.getContext();
		lm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
	}

	/**
	 * Used to get the usersCurrentLocation, available at any time. The users
	 * Location is optionally used (by default) when a comment is
	 * created/edited. Users location is also used any time the application
	 * calls a user-location based sorting function If no provider is enabled it
	 * returns the users last stored location.
	 * 
	 * @return User's Current/Last Known Location
	 */
	public Location getCurrentLocation() {
		Location temp = new Location("userLocation");
		setUpLocationServices();
		if (isProviderAvailable()) {
			Log.w("COMMENT_LOC", "IS AVAILABLE");
			this.setMyLastKnownLocation(lm.getLastKnownLocation(provider));
		}
		temp.setLatitude(this.myLat);
		temp.setLongitude(this.myLong);
		return temp;
	}

	/**
	 * Checks if there are any location service providers enabled, ex.
	 * gps/network/MockProvider These providers are used to get the users
	 * current location. If there is no provider currently enabled it returns
	 * false and a default location is used. If a provider is enabled it returns
	 * True.
	 * 
	 * @return True if a provider is enabled, False if no provider is enabled.
	 */
	public boolean isProviderAvailable() {
		Criteria crit = new Criteria();
		crit.setAccuracy(Criteria.ACCURACY_COARSE);
		provider = lm.getBestProvider(crit, true);
		if (provider == null
				|| provider.equals(LocationManager.PASSIVE_PROVIDER)) {
			if (lm.isProviderEnabled("mockLocationProvider")) {
				Log.d("MOCK_PROVIDER_CHECK", "provider is working");
				setMyLastKnownLocation(lm
						.getLastKnownLocation("mockLocationProvider"));
			} else {
				Log.d("PROVIDER_CHECK", "NO PROVIDER AVAILABLE");
			}
			return false;
		}
		Log.d("PROVIDER_CHECK", "PROVIDER " + provider);
		return true;
	}

	public void setInitialLocation() {
		this.myLat = (double) 0;
		this.myLong = (double) 0;
	}

	/**
	 * Sets the users last known position to a specific location
	 * 
	 * @param location
	 *            The location to set the users last known locaiton to.
	 */
	public void setMyLastKnownLocation(Location location) {
		Log.d("NULL_LOC", "Caught the fake provided gps");
		if (location != null) {
			this.myLat = location.getLatitude();
			this.myLong = location.getLongitude();
		}
	}
}