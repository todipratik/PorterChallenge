package com.domain.porter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.domain.porter.model.Parcel;
import com.domain.porter.provider.JSONParser;
import com.domain.porter.provider.ParcelLoader;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<Parcel>>, AdapterView.OnItemClickListener, View.OnClickListener {

    private static final String URL_API_HITS = "https://porter.0x10.info/api/parcel?type=json&query=api_hits";

    private ParcelAdapter mParcelAdapter;
    private ArrayList<Parcel> mParcels;
    private ImageLoader mImageLoader;
    private ListView mListView;

    private TextView mSortPrice;
    private TextView mSortWeight;
    private TextView mSortName;

    private TextView mTotalHits;
    private TextView mTotalParcels;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (ListView) view.findViewById(android.R.id.list);
        mTotalHits = (TextView) view.findViewById(R.id.api_hits);
        mTotalParcels = (TextView) view.findViewById(R.id.total_parcels);
        mSortPrice = (TextView) view.findViewById(R.id.sort_price);
        mSortWeight = (TextView) view.findViewById(R.id.sort_weight);
        mSortName = (TextView) view.findViewById(R.id.sort_name);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListView.setOnItemClickListener(this);
        mParcels = new ArrayList<>(); // empty for now, will be populated when the loader has finished fetching the parcels
        mImageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getActivity()).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .defaultDisplayImageOptions(options)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        mImageLoader.init(config);
        mParcelAdapter = new ParcelAdapter(getActivity(), mParcels, mImageLoader);
        setListAdapter(mParcelAdapter);

        mSortPrice.setOnClickListener(this);
        mSortWeight.setOnClickListener(this);
        mSortName.setOnClickListener(this);

        new ApiHit().execute(URL_API_HITS);
        getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @Override
    public Loader<List<Parcel>> onCreateLoader(int i, Bundle bundle) {
        return new ParcelLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Parcel>> loader, List<Parcel> parcels) {
        mParcels.clear();
        for (Parcel parcel : parcels) {
            mParcels.add(parcel);
        }
        mTotalParcels.setText("" + parcels.size());
        mParcelAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Parcel>> loader) {
        mParcels.clear();
        mParcelAdapter.clear();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sort_price:
                Collections.sort(mParcels, new Comparator<Parcel>() {
                    @Override
                    public int compare(Parcel lhs, Parcel rhs) {
                        String left = lhs.getPrice();
                        left = left.replaceAll(",", "");
                        String right = rhs.getPrice();
                        right = right.replaceAll(",", "");
                        Long l = Long.parseLong(left);
                        Long r = Long.parseLong(right);
                        return l.compareTo(r);
                    }
                });
                mParcelAdapter.notifyDataSetChanged();
                break;
            case R.id.sort_weight:
                Collections.sort(mParcels, new Comparator<Parcel>() {
                    @Override
                    public int compare(Parcel lhs, Parcel rhs) {
                        String left = lhs.getWeight();
                        String right = rhs.getWeight();
                        left = left.replace("kg", "");
                        right = right.replace("kg", "");
                        Double l = Double.parseDouble(left);
                        Double r = Double.parseDouble(right);
                        return l.compareTo(r);
                    }
                });
                mParcelAdapter.notifyDataSetChanged();
                break;
            case R.id.sort_name:
                Collections.sort(mParcels, new Comparator<Parcel>() {
                    @Override
                    public int compare(Parcel lhs, Parcel rhs) {
                        return lhs.getName().compareTo(rhs.getName());
                    }
                });
                mParcelAdapter.notifyDataSetChanged();
                break;
        }
    }

    class ApiHit extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            String url = params[0];
            JSONParser parser = new JSONParser();
            return parser.getApiHits(url);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            mTotalHits.setText(integer.toString());
        }
    }
}
