package com.domain.porter.provider;

import android.util.Log;

import com.domain.porter.model.Parcel;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by pratik on 29/8/15.
 */
public class JSONParser {

    public static final String KEY_PARCELS = "parcels";
    public static final String KEY_NAME = "name";
    public static final String KEY_IMAGE_URL = "image";
    public static final String KEY_DATE = "date";
    public static final String KEY_TYPE = "type";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_PRICE = "price";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_COLOR = "color";
    public static final String KEY_LINK = "link";
    public static final String KEY_LOCATION = "live_location";
    public static final String KEY_LONGITUDE = "latitude";
    public static final String KEY_LATITUDE = "longitude";
    public static final String KEY_API_HITS = "api_hits";


    static InputStream inputStream = null;
    static JSONObject jsonObject = null;
    static String json = "";


    protected JSONObject getJSONFromUrl(String source_url) {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            URL url = new URL(source_url);
            URLConnection con = url.openConnection();
            Reader reader = new InputStreamReader(con.getInputStream());
            StringBuilder builder = new StringBuilder();
            while (true) {
                int ch = reader.read();
                if (ch == -1) {
                    break;
                }
                builder.append((char) ch);
            }
            json = builder.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject object = null;
        // try to parse the string to a JSON object
        try {
            object = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return object;
    }

    public Integer getApiHits(String source_url) {
        JSONObject object = getJSONFromUrl(source_url);
        Integer hits = 0;
        try {
            hits = object.getInt(KEY_API_HITS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hits;
    }

    public List<Parcel> getParcels(String source_url) {
        jsonObject = getJSONFromUrl(source_url);
        JSONArray jsonArray = null;
        List<Parcel> parcelList = new ArrayList<>();
        if (jsonObject != null) {
            try {
                jsonArray = jsonObject.getJSONArray(KEY_PARCELS);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    Parcel parcel = new Parcel();
                    parcel.setName(object.getString(KEY_NAME));
                    parcel.setImageUrl(object.getString(KEY_IMAGE_URL));
                    parcel.setDate(object.getString(KEY_DATE));
                    parcel.setType(object.getString(KEY_TYPE));
                    parcel.setWeight(object.getString(KEY_WEIGHT));
                    parcel.setPhone(object.getString(KEY_PHONE));
                    parcel.setPrice(object.getString(KEY_PRICE));
                    parcel.setQuantity(object.getString(KEY_QUANTITY));
                    parcel.setColor(object.getString(KEY_COLOR));
                    parcel.setLink(object.getString(KEY_LINK));
                    parcel.setLatitude(object.getJSONObject(KEY_LOCATION).getDouble(KEY_LATITUDE));
                    parcel.setLongitude(object.getJSONObject(KEY_LOCATION).getDouble(KEY_LONGITUDE));
                    parcelList.add(parcel);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // return parcel list
        return parcelList;
    }

}
