package trainedge;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import trainedge.android_project.R;

public class FetchAddressIntentService extends IntentService {

    private static final String TAG = "FetchAddress";
    private ResultReceiver mReceiver;


    public FetchAddressIntentService() {
        super("AddressFetcher");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String errorMessage = "";

        mReceiver = intent.getParcelableExtra(Constants.RECEIVER);
        // Check if receiver was properly registered.
        if (mReceiver == null) {
            Log.wtf(TAG, "No receiver received. There is nowhere to send the results.");
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses = null;

        // Get the location passed to this service through an extra.
        try {
            Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
            try {


                addresses = geocoder.getFromLocation(
                        location.getLatitude(),
                        location.getLongitude(),
                        // In this sample, get just a single address.
                        1);

            } catch (IOException e) {
                e.printStackTrace();
                errorMessage = getString(R.string.service_not_available);
                Log.e(TAG, errorMessage, e);
            } catch (IllegalArgumentException e) {
                // Catch invalid latitude or longitude values.
                errorMessage = getString(R.string.invalid_lat_long_used);
                Log.e(TAG, errorMessage + ". " + "Latitude = " + location.getLatitude() +
                        ", Longitude = " + location.getLongitude(), e);

            }
        } catch (Exception e) {
            Double lat = intent.getDoubleExtra(Constants.LOCATION_LATITUDE_EXTRA, 0);
            Double lng = intent.getDoubleExtra(Constants.LOCATION_LONGITUDE_EXTRA, 0);

            try {
                addresses = geocoder.getFromLocation(lat, lng, 1);
            } catch (IOException e1) {
                e1.printStackTrace();
                errorMessage = getString(R.string.service_not_available);
            }
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            Log.i(TAG, getString(R.string.address_found));
            deliverResultToReceiver(Constants.SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"),
                            addressFragments));
        }


    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }

    public final class Constants {
        public static final int SUCCESS_RESULT = 0;
        public static final int FAILURE_RESULT = 1;
        public static final String PACKAGE_NAME =
                "trainedge";
        public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
        public static final String RESULT_DATA_KEY = PACKAGE_NAME +
                ".RESULT_DATA_KEY";
        public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
                ".LOCATION_DATA_EXTRA";
        public static final String LOCATION_LATITUDE_EXTRA = PACKAGE_NAME + ".latitude";
        public static final String LOCATION_LONGITUDE_EXTRA = PACKAGE_NAME + ".longitude";
    }
}