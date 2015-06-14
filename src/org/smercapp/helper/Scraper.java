package org.smercapp.helper;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {
	
	String kw[] = {"=", "+", "-", "/", "*", "%", "++", "--", "!", "==", 
			"<", ">", "<=", ">=", "&&", "||", "?:", "~", "<<", ">>", ">>>",
			"&", "^", "|", "{","}","(",")","[","]","//","/*","*/",",",":",
			";","abstract",	"continue",	"for",	"new",	"switch",
			"assert",	"default",	"goto",	"package",	"synchronized",
			"boolean",	"do",	"if",	"private",	"this",
			"break",	"double",	"implements",	"protected",	"throw",
			"byte",	"else",	"import",	"public",	"throws",
			"case",	"enum",	"instanceof",	"return",	"transient",
			"catch",	"extends",	"int",	"short",	"try",
			"char",	"final",	"interface",	"static",	"void",
			"class",	"finally",	"long",	"strictfp",	"volatile",
			"const",	"float",	"native",	"super", "while", "do while"};
	
	HashMap<String,Boolean> kwMap;
	
	public Scraper()
	{
		kwMap = new HashMap<String,Boolean>();
		for(String s: kw)
		{
			kwMap.put(s, true);
		}
	}
	
	public SimpleEntry<String,Float> getAnswerStack(String link, String query) throws IOException
	{
		//Document doc = Jsoup.connect("http://stackoverflow.com/questions/513832/how-do-i-compare-strings-in-java").get();
		Document doc = Jsoup.connect(link.trim()).get();
		System.out.println("Title: "+doc.title());
		
		Elements accAns = doc.getElementsByAttributeValue("itemprop", "acceptedAnswer");
		String answer = null;
		String mrSumm = null;
		float mr = 0f;
		
		if(accAns.size() == 1)
		{
			Integer votes = Integer.parseInt(accAns.get(0).getElementsByAttributeValue("itemprop","upvoteCount").text());
			System.out.println("Votes: "+votes);
			
			if(accAns.get(0).getElementsByClass("post-text").get(0).getElementsByTag("code")!=null &&
					accAns.get(0).getElementsByClass("post-text").get(0).getElementsByTag("code").size() > 0){
				
				mrSumm = "<pre>"+accAns.get(0).getElementsByClass("post-text").get(0).getElementsByTag("code").get(0).html()+"</pre>";
				mr = relCompute(mrSumm,query);
				//System.out.println("Answer: "+ answer);
				if(mr == 1)
					return new SimpleEntry<String,Float>(mrSumm, mr);
			}
		}
		
			
		Elements ans = doc.getElementsByClass("answer");
		
		String tempSumm;
		float tr = 0f;
		for(int i=0 ; i<ans.size() ;i++)
		{
			if(ans.get(i).getElementsByClass("post-text").get(0).getElementsByTag("code")!=null && 
					ans.get(i).getElementsByClass("post-text").get(0).getElementsByTag("code").size() > 0 )
			{
				tempSumm = "<pre>"+ans.get(i).getElementsByClass("post-text").get(0).getElementsByTag("code").get(0).html()+"</pre>";
				tr= relCompute(tempSumm,query);
				
				if(tr == 1)
					return new SimpleEntry<String,Float>(tempSumm,tr);
				
				if(mrSumm == null)
				{
					mr = tr;
					mrSumm = tempSumm;
				}
				else
				{
					if(tr > mr)
					{
						mr = tr;
						mrSumm = tempSumm;
					}
				}
			}
			
		}
		
		return new SimpleEntry<String,Float>(mrSumm, mr);
		
		
		
	}
	
	public SimpleEntry<String,Float> getAnswerOracle(String link, String query) throws IOException
	{
		Document doc = Jsoup.connect(link).get();
		System.out.println("Title: "+doc.title());

		Elements codeBlocks = doc.getElementsByClass("codeBlock");
		//System.out.println("Number of code block elems: "+codeBlocks.size());
		
		
		Element elem;
		String mrSumm = null;
		String tempSumm;
		float mr = 0f;
		float tr = 0f;
		for(int i = 0 ; i < codeBlocks.size() ; i++)
		{
			elem = codeBlocks.get(i);
			tempSumm = elem.html();
			tr= relCompute(tempSumm,query);
			
			if(tr == 1)
				return new SimpleEntry<String,Float>(tempSumm,tr);
			
			if(i == 0)
			{
				mr = tr;
				mrSumm = tempSumm;
			}
			else
			{
				if(tr > mr)
				{
					mr = tr;
					mrSumm = tempSumm;
				}
			}
			
		}
		
		return new SimpleEntry<String,Float>(mrSumm,mr);
	}
	
	private float relCompute(String summary,String query)
	{
		String[] qArr = query.split(" ");
		
		List<String> checkFor = new ArrayList<String>();
		
		for(String s: qArr)
		{
			if(kwMap.get(s) != null)
			{
				checkFor.add(s);
			}
		}
		
		if(checkFor.size() == 0)
		{
			return 0f;
		}
		
		float count = 0;
		for(String s: checkFor)
		{
			if(summary.contains(s))
			{
				count++;
			}
		}
		
		return (count/checkFor.size());
	}

}
