package com.credr.productsearch.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.credr.productsearch.R;
import com.credr.productsearch.controller.CredrTestApplication;
import com.credr.productsearch.model.Product;

public class ProductAdapter extends BaseAdapter {
	
	public static final int TYPE_GRID = 1 ; 
	public static final int TYPE_LIST = 0 ; 
	public static final int VIEW_TYPE_COUNT = 2 ; 

	
	private Context context;
	private LayoutInflater inflater;
	private List<Product> productData;
	private ImageLoader imageLoader;
	private int viewType ; 

	public ProductAdapter(Context activity, List<Product> productData,int viewType) {
		this.context = activity;
		this.productData = productData;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageLoader = CredrTestApplication.getInstance().getImageLoader();
		this.viewType = viewType ;
	}

	public int getViewType() {
		return viewType;
	}

	public void setViewType(int viewType) {
		this.viewType = viewType;
	}

	@Override
	public int getCount() {
		return productData.size();
	}

	@Override
	public Object getItem(int location) {
		return productData.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		TextView listText;
		NetworkImageView image;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder viewHolder;
		// convertView null check is done inside "getViewAccordingToType" method
		convertView = getViewAccordingToType(position,convertView);
		viewHolder = (ViewHolder) convertView.getTag();
		if (imageLoader == null)
			imageLoader = CredrTestApplication.getInstance().getImageLoader();
		// getting Product data for the row
		Product product = (Product) getItem(position);
		// icon thumbnail image
		viewHolder.listText.setText(product.getName());
		viewHolder.image.setImageUrl(product.getIconUrl(), imageLoader);
		return convertView;
	}

	private View getViewAccordingToType(int position, View convertView) {
		if (convertView == null) {
			convertView = inflater.inflate(getLayoutId(position), null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.listText = (TextView) convertView.findViewById(R.id.title);
			viewHolder.image = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
			convertView.setTag(viewHolder);
		}
		return convertView;
	}

	private int getLayoutId(int position) {
		return getItemViewType(position)== TYPE_LIST ? R.layout.list_row : R.layout.grid_row ; 
	}

	@Override
	public int getItemViewType(int position) {	
		return viewType;
	}

	@Override
	public int getViewTypeCount() {
		return VIEW_TYPE_COUNT;
	}

	
}