package com.credr.productsearch.query;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.os.AsyncTask;
import android.util.Log;

import com.credr.productsearch.R;
import com.credr.productsearch.controller.CredrTestApplication;
import com.credr.productsearch.model.Product;
import com.credr.productsearch.utils.Constants;

public class SearchQueryResultsFetcher extends AsyncTask<String, Integer, List<Product>> {
	
	public static final int STATUS_RESULTS_SUCCESS = 1;
	public static final int STATUS_PARSE_SUCCESS = 2;
	public static final int STATUS_QUERY_STARTED = 3;

	public static interface SearchQueryListener {
		public void onQueryProgress(int progressStatus);

		public void onQueryError(String errorMessage);

		public void onQuerySuccess(List<Product> results);
	}

	private SearchQueryListener queryListener;

	public SearchQueryResultsFetcher(SearchQueryListener queryListener) {
		super();
		this.queryListener = queryListener;
	}

	@Override
	protected void onPreExecute() {
		if (queryListener != null) {
			queryListener.onQueryProgress(STATUS_QUERY_STARTED);
		}
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {

		if (queryListener != null) {
			queryListener.onQueryProgress(values[0]);
		}
		super.onProgressUpdate(values);
	}

	@Override
	protected List<Product> doInBackground(String... params) {
		Document resultDocument = getQueryResult(params[0]);
		List<Product> productList = null;
		if (resultDocument != null) {
			productList = getProductListFromDom(resultDocument);
		}
		return productList;
	}

	private List<Product> getProductListFromDom(Document resultDocument) {
		List<Product> productList = null;
		List<String> detailUrl = null;
		int i = 0;
		if (resultDocument != null) {
			productList = new ArrayList<Product>();
			detailUrl = new ArrayList<String>();
			for (Element detailData : resultDocument.select(Constants.DETAIL_XPATH_STATS)) {
				Log.i(CredrTestApplication.TAG, detailData.attr("href"));
				detailUrl.add(detailData.attr("href"));
			}

			for (Element data : resultDocument.select(Constants.XPATH_STATS)) {
				Log.i(CredrTestApplication.TAG, data.attr("alt") + " " + data.attr("data-src"));
				Product mProduct = new Product();
				mProduct.setName(data.attr("alt"));
				mProduct.setIconUrl(data.attr("data-src"));
				if (i < detailUrl.size() && detailUrl.get(i) != null)
					mProduct.setDetailsUri(Constants.URL + detailUrl.get(i++));
				productList.add(mProduct);
			}
		}
		return productList;
	}

	private Document getQueryResult(String urlString) {
		Document resultDocument = null;
		try {
			Connection mConnection = Jsoup.connect(urlString).timeout(30000).userAgent("mozilla").followRedirects(true);
			Connection.Response response = mConnection.execute();
			Log.i(CredrTestApplication.TAG, "statusCode" + response.statusCode());
			if (response.statusCode() == 200) {
				resultDocument = mConnection.get();
				Log.i(CredrTestApplication.TAG, "result " + resultDocument.toString());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultDocument;
	}

	@Override
	protected void onPostExecute(List<Product> result) {

		if (queryListener != null && result != null) {
			queryListener.onQuerySuccess(result);
		}else{
			queryListener.onQueryError( CredrTestApplication.getInstance().getString(R.string.error_msg));
		}
		super.onPostExecute(result);
	}

}
