package com.credr.productsearch.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.credr.productsearch.R;
import com.credr.productsearch.adapter.ProductAdapter;
import com.credr.productsearch.model.Product;
import com.credr.productsearch.query.SearchQueryResultsFetcher;
import com.credr.productsearch.query.SearchQueryResultsFetcher.SearchQueryListener;
import com.credr.productsearch.utils.Utils;

public class ProductSearchActivity extends Activity {

	private static final int MODE_LIST = 1;
	private static final int MODE_GRID = 2;
	private static final int MODE_DEFAULT = MODE_LIST;

	private ListView lvSearchResults;
	private GridView gvSearchResults;
	private EditText edtSearch;
	private Button btnSearchResults ;
	private ProductAdapter productAdapter;
	private ProgressDialog progressDialog;
	private List<Product> productData = new ArrayList<Product>();
	private SearchQueryResultsFetcher resultsFetcher;
	private int currentMode = MODE_DEFAULT;
	
	// implement interfaces
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnSearch:
				checkAndStartQuery();
				break;
			default:
				break;
			}	
		}
	};
	
	private SearchQueryListener onSearchQueryListener = new SearchQueryListener() {
		
		@Override
		public void onQueryProgress(int progressStatus) {
			switch (progressStatus) {
			case SearchQueryResultsFetcher.STATUS_QUERY_STARTED: {
				showProgressDialog(getString(R.string.query_fetching_results));
			}
				break;
			case SearchQueryResultsFetcher.STATUS_RESULTS_SUCCESS: {
				showProgressDialog(getString(R.string.query_parsing_results));
			}
				break;
			case SearchQueryResultsFetcher.STATUS_PARSE_SUCCESS: {
				showProgressDialog(getString(R.string.query_success));
			}
				break;
			default:
				break;
			}
		}
		@Override
		public void onQueryError(String errorMessage) {
			Toast.makeText(ProductSearchActivity.this,errorMessage, Toast.LENGTH_LONG).show();
		}

		@Override
		public void onQuerySuccess(List<Product> results) {
			dismissProgressDialog();
			updateResults(results);
			
		}
	};
	
	private void hideKeyBoardIfApplicable(){
		// Check if no view has focus:
		    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		    imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);
	}
	
	private OnEditorActionListener onEditorActionListener = new OnEditorActionListener() {
		
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			switch (v.getId()) {
			case R.id.edtSearch: {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					checkAndStartQuery();
				}
			}
				break;
			default:
				break;
			}
			return false;
		}
	};

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adpaterView, View arg1, int position, long id) {

			Product clickedProduct = (Product) productAdapter.getItem(position);
			if (clickedProduct != null) {
				onClickProduct(clickedProduct);
			}

		}
	};
	
	// implement interfaces ends
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_search);
		intiViews();
		setUpViews();
	}
	
	private void resetViews(){
		getCurrentAdaperView().setAdapter(null);
	}
	private void setUpViews() {
		lvSearchResults.setVisibility(currentMode == MODE_LIST ? View.VISIBLE : View.GONE);
		gvSearchResults.setVisibility(currentMode == MODE_GRID ? View.VISIBLE : View.GONE);
		getCurrentAdaperView().setAdapter(getAdapter());
	}
	private AbsListView getCurrentAdaperView() {
		return currentMode == MODE_LIST ? lvSearchResults : gvSearchResults ;
	}

	private int getCurrentViewType(){
		return currentMode == MODE_LIST ? ProductAdapter.TYPE_LIST : ProductAdapter.TYPE_GRID ;
	}
	private ProductAdapter getAdapter(){
		if(productAdapter == null){
			productAdapter = new ProductAdapter(this, productData,ProductAdapter.TYPE_LIST);
		}
		productAdapter.setViewType(getCurrentViewType());
		return productAdapter ; 
	}
	private void intiViews() {
		lvSearchResults = (ListView) findViewById(R.id.lvSearchResults);
		gvSearchResults = (GridView) findViewById(R.id.gvSearchResults);
		edtSearch = (EditText) findViewById(R.id.edtSearch);
		edtSearch.requestFocus();
		btnSearchResults = (Button) findViewById(R.id.btnSearch);
		btnSearchResults.setOnClickListener(onClickListener);
		lvSearchResults.setEmptyView(findViewById(R.id.empty_view));
		gvSearchResults.setEmptyView(findViewById(R.id.empty_view));
		lvSearchResults.setOnItemClickListener(onItemClickListener);
		gvSearchResults.setOnItemClickListener(onItemClickListener);
		edtSearch.setOnEditorActionListener(onEditorActionListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.product_search_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
			case R.id.menu_list: {
				changeViewTo(MODE_LIST);
			}
				return true;
			case R.id.menu_grid: {
				changeViewTo(MODE_GRID);
			}
				return true;

			// no need of default statement
		}
		return super.onOptionsItemSelected(item);
	}
	private void changeViewTo(int type){
		resetViews();
		currentMode = type;
		setUpViews();
	}
	private void showProgressDialog(String message) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
		}
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(message);
		progressDialog.show();

	}

	private void dismissProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		progressDialog = null;
	}

	private void updateResults(List<Product> results) {
		if (productData != null) {
			productData.clear();
			productData.addAll(results);
			productAdapter.notifyDataSetChanged();
		}
		setUpViews();
	}

	private void checkAndStartQuery() {
		String searchQuery = edtSearch.getText().toString().trim();
		if (Utils.getConnectivityStatus(ProductSearchActivity.this)) {
			if (Utils.isValidQuery(searchQuery)) {
				startQuery(searchQuery);
			} else {
				edtSearch.setError(getString(R.string.no_data));
				Toast.makeText(ProductSearchActivity.this, R.string.no_data,Toast.LENGTH_SHORT).show();
			}
		} else {
			// set empty view
			Toast.makeText(ProductSearchActivity.this, R.string.error_msg,Toast.LENGTH_LONG).show();
		}

	}

	private void startQuery(String searchQuery) {
		cancelCurrentIfRunning();
		resultsFetcher = new SearchQueryResultsFetcher(onSearchQueryListener);
		resultsFetcher.execute(Utils.createProductURl(searchQuery));
		hideKeyBoardIfApplicable();
	}

	private void cancelCurrentIfRunning() {
		if (resultsFetcher != null) {
			resultsFetcher.cancel(true);
		}
		resultsFetcher = null;

	}

	@Override
	protected void onDestroy() {
		cancelCurrentIfRunning();
		dismissProgressDialog();
		super.onDestroy();
	}

	private void onClickProduct(Product product) {
		Intent detailIntent = new Intent(Intent.ACTION_VIEW);
		detailIntent.setData(Uri.parse(
				product.getDetailsUri()));
		startActivity(detailIntent);
	}

}
