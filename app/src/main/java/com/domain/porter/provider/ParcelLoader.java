package com.domain.porter.provider;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.domain.porter.model.Parcel;

import java.util.List;

/**
 * Created by pratik on 29/8/15.
 */
public class ParcelLoader extends AsyncTaskLoader<List<Parcel>> {

    private static final String URL = "https://porter.0x10.info/api/parcel?type=json&query=list_parcel";

    public ParcelLoader(Context context) {
        super(context);
    }

    // deliver the data fetched from API to the client (deliverResult())
    @Override
    public List<Parcel> loadInBackground() {
        JSONParser jsonParser = new JSONParser();
        return jsonParser.getJSONFromUrl(URL);
    }
}
