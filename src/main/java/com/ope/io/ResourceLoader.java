package com.ope.io;

import java.io.InputStream;

public class ResourceLoader 
{
	public static InputStream load(String path)
	{
		return ResourceLoader.class.getResourceAsStream(path);
	}

}
