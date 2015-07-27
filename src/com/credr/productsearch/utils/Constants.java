package com.credr.productsearch.utils;

public class Constants {
	
	public static final String URL = "http://flipkart.com/";
	public static final String URL_SEARCH = "search?q=";
	// expression to get data between <img> tag. 
	public static final String XPATH_STATS = "img[data-src~=(?i)\\.(png|jpe?g|gif)]";
	// expression to get href of the product
	public static final String DETAIL_XPATH_STATS = "a[class = pu-image fk-product-thumb]";

}
