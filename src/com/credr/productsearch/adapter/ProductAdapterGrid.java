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

public class ProductAdapterGrid extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<Product> productData;
	private ImageLoader imageLoader;

	public ProductAdapterGrid(Context activity, List<Product> productData) {
		this.context = activity;
		this.productData = productData;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageLoader = CredrTestApplication.getInstance().getImageLoader();
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

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.grid_row, null);
			viewHolder = new ViewHolder();
			viewHolder.listText = (TextView) convertView.findViewById(R.id.title);
			viewHolder.image = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (imageLoader == null)
			imageLoader = CredrTestApplication.getInstance().getImageLoader();

		// getting place data for the row
		Product product = (Product) getItem(position);
		// icon thumbnail image
		viewHolder.listText.setText(product.getName());
		viewHolder.image.setImageUrl(product.getIconUrl(), imageLoader);
		return convertView;
	}

}