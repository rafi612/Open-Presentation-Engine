package com.tts;

public enum Language 
{
	ENGLISH("en-US"),
	POLISH("pl-PL"),
	GERMAN("de-DE"),
	FRENCH("fr-FR"),
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
