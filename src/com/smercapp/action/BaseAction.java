package com.smercapp.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.smercapp.helper.Scraper;

public class BaseAction {
	
	public String firstLink;
	
	public String summary;
	
	public String jsonString;
	
	private InputStream inputStream;
	
	private String query;
	
	public InputStream getInputStream() {
	    return inputStream;
	}
	
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getFirstLink() {
		return firstLink;
	}

	public void setFirstLink(String firstLink) {
		this.firstLink = firstLink;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String execute()
	{
		System.out.println("Hi from execute1");
		
		return "success";
	}

/*
 * 
 	public String findAnswer()
	{
		System.out.println("Hello from findAnswer");
		
		System.out.println("First link is: *"+this.getFirstLink()+"*");
		
		Scraper scr = new Scraper();
		
		if(getFirstLink().contains("stackoverflow"))
		{
			
			try
			{
				this.setSummary(scr.getAnswerStack(this.getFirstLink()));
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		else if(getFirstLink().contains("oracle"))
		{
			try
			{
				this.setSummary(scr.getAnswerOracle(this.getFirstLink()));
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		
		System.out.println(this.getSummary());
		
		inputStream = new ByteArrayInputStream(
				(this.getSummary()).getBytes(StandardCharsets.UTF_8));		
		
		return "success";
	}
 */
	
	public String findAnswer()
	{
		System.out.println("Hello from findAnswer");
		System.out.println("JSON string: "+this.getJsonString());

		String[] linkArr = this.getJsonString().split("\\^");
		
		
		System.out.println("First link is: *"+linkArr[0]+"*");
		
		
		setFirstLink(linkArr[0]);
		Scraper scr = new Scraper();
		
		String mrsOracle = null;
		String mrsStack = null;
		float mrOracle = 0f;
		float mrStack = 0f;
		SimpleEntry<String,Float> pairTemp;
		for(String s : linkArr)
		{
			if(s.contains("stackoverflow"))
			{
				
				try
				{
					pairTemp = scr.getAnswerStack(s,query);
					System.out.println("Stack Pair: "+pairTemp.getValue());
					
					if(mrsStack == null)
					{
						mrsStack = pairTemp.getKey();
						mrStack = pairTemp.getValue();
					}
					
					if(pairTemp.getValue() > mrOracle)
					{
						mrsStack = pairTemp.getKey();
						mrStack = pairTemp.getValue();
					}
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			else if(s.contains("oracle"))
			{
				try
				{
					pairTemp = scr.getAnswerOracle(s,query);
					System.out.println("Oracle Pair: "+pairTemp.getValue());
					
					if(mrsOracle == null)
					{
						mrsOracle = pairTemp.getKey();
						mrOracle = pairTemp.getValue();
					}
					
					if(pairTemp.getValue() > mrOracle)
					{
						mrsOracle = pairTemp.getKey();
						mrOracle = pairTemp.getValue();
					}
					
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}		
			
			
			
			System.out.println("*******************************************");
			
		}
		
		System.out.println("mrOracle "+mrOracle);
		System.out.println("mrStack "+mrStack);
		System.out.println("mrsStack "+mrsStack);
		System.out.println("mrsOracle "+mrsOracle);
		if(mrOracle >= mrStack && mrsOracle != null)
		{
			inputStream = new ByteArrayInputStream(
					(mrsOracle).getBytes(StandardCharsets.UTF_8));
		}
		else if(mrStack > mrOracle && mrsStack != null)
		{
			inputStream = new ByteArrayInputStream(
					(mrsStack).getBytes(StandardCharsets.UTF_8));
		}
		else
		{
			inputStream = new ByteArrayInputStream(
					("").getBytes(StandardCharsets.UTF_8));
		}
		
	
		
		
		return "success";
	}

}
