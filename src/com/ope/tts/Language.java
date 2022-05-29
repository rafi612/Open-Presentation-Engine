/* Copyright 2019-2020 by rafi612 */
package com.ope.tts;

public enum Language 
{
	ENGLISH("en-US"),
	POLISH("pl-PL"),
	GERMAN("de-DE"),
	FRENCH("fr-FR"),
	SPANISH("es-ES"),
	RUSSIAN("ru-RU"),
	CHINESE("zh-CN");
	
	String lang;
	
	private Language(String lang)
	{
		this.lang = lang;
	}
	
	public String getLang()
	{
		return lang;
	}

}
