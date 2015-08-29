package com.domain.porter;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.domain.porter.model.Parcel;
import com.domain.porter.provider.ParcelLoader;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<Parcel>>, AdapterView.OnItemClickListener {

    private ParcelAdapter mParcelAdapter;
    private ArrayList<Parcel> mParcels;
    private ImageLoader mImageLoader;
    private ListView mListView;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (ListView) view.findViewById(android.R.id.list);
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
//        setListShown(false);

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
}
