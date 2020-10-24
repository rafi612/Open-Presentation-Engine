package com.tts;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Speak
{

	//"https://translate.google.com/translate_tts?ie=UTF-8&tl=pl-PL&client=tw-ob&q=hello+world"
	public Speak()
	{
		
	}
	
	public static void download(String url,String savepath)
	{
		URLConnection openConnection;
		try 
		{
			openConnection = new URL(url).openConnection();
			openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			InputStream in = openConnection.getInputStream();
			
			try
			{
				@SuppressWarnings("resource")
				FileOutputStream fileOutputStream = new FileOutputStream(savepath);
				byte dataBuffer[] = new byte[1024];
				int bytesRead;
				while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) 
				{
					fileOutputStream.write(dataBuffer, 0, bytesRead);
				}
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		} 
		catch (MalformedURLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
