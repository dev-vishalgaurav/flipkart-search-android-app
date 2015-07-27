package com.credr.productsearch.model;

public class Product {
	public static final String DEFAULT_URI = "http://flipkart.com";
	private String name;
	private String iconUrl;
	private String detailsUri ;
			
	public String getDetailsUri() {
		return detailsUri;
	}

	public void setDetailsUri(String detailsUri) {
		this.detailsUri = detailsUri;
	}

	public Product(){
		detailsUri = DEFAULT_URI ;
	}

	public String getName() {
		String [] splitedName =  name.split("\\s");
		return splitedName[0]+" "+splitedName[1];
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	
}
