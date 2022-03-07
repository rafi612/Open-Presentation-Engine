package com.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class JarLoader
{
	public static void main(String[] args) throws IOException 
	{
		Enumeration<URL> resEnum = Thread.currentThread().getContextClassLoader().getResources("META-INF/MANIFEST.MF");
		String rsrcMainClass = null;
		String[] classpath = null;
		
		String libdir = "/lib";
		
		File tempdir = Files.createTempDirectory("ope_jarloader").toFile();
		tempdir.deleteOnExit();
		
		while (resEnum.hasMoreElements())
		{
			try
			{
				URL url = (URL) resEnum.nextElement();
				InputStream is = url.openStream();
				
				Manifest manifest = new Manifest(is);
				Attributes mainAttribs = manifest.getMainAttributes();
				String main = mainAttribs.getValue("Main-Class");
				
				if (main.equals("com.main.JarLoader"))
				{
					rsrcMainClass = mainAttribs.getValue("Rsrc-Main-Class");
					classpath = mainAttribs.getValue("Class-Path").split(" ");
					break;
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		
		try 
		{
			for (String path : classpath)
				copyFile(libdir + "/" + path, tempdir + File.separator + path);
			
			URL[] urls = new URL[classpath.length+1];
			for (int i = 0;i < classpath.length;i++)
			{
				urls[i] = new URL("file:" + tempdir.getPath() + "/" + classpath[i]);
			}
			urls[urls.length-1] = JarLoader.class.getProtectionDomain().getCodeSource().getLocation();
			
			URLClassLoader loader = new URLClassLoader(urls,null);
			
			Class<?> classToLoad = Class.forName(rsrcMainClass, true, loader);
			Method main = classToLoad.getMethod("main", String[].class);
			main.invoke((Object) null, new Object[] { args });
			
		} catch (ClassNotFoundException | SecurityException  | IllegalArgumentException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void copyFile(String input,String output)
	{
        //File source = new File(input);
        InputStream is = JarLoader.class.getResourceAsStream(input);
        File dest = new File(output);
        dest.deleteOnExit();

        try (//FileInputStream fis = new FileInputStream(source);
        	FileOutputStream fos = new FileOutputStream(dest)) {

            byte[] buffer = new byte[1024];
            int length;

            while ((length = is.read(buffer)) > 0)
            {

                fos.write(buffer, 0, length);
            }
            is.close();
        } 
        catch (FileNotFoundException e) 
        {
			e.printStackTrace();
		} 
        catch (IOException e)
        {
			e.printStackTrace();
		}
        
	}

}