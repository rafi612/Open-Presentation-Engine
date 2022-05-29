package com.ope.translate;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

public class Translate
{
	static ResourceBundle i18n;
	
	public static void setTranslator(Locale loc)
	{
		URL[] url = {Translate.class.getResource("/lang/")};
		ClassLoader loader = new URLClassLoader(url);
		i18n = ResourceBundle.getBundle("lang", loc,loader);
	}
	
	public static String tr(String s)
	{
		return i18n.getString(s);
	}
	
	public static String tr(String s,Object... ob)
	{
		return i18n.getString(s);
	}

}

