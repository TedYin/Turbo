package com.turbo.data;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.turbo.interfaces.IPullParserCallBack;
import com.turbo.interfaces.ISAXParseHandler;

/**
 * XML解析助手
 * @author Ted
 *
 */
public class XMLHelper {
	
	/**
	 * 使用SAX解析XML
	 * @param is
	 * @param handler
	 */
	public static void startSAXParser(InputStream is , ISAXParseHandler handler){
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			parser.parse(is, handler);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
	
	/**
	 * 使用Pull解析XML
	 * @param callBack
	 * @param is
	 */
	public static void startPullParse(IPullParserCallBack callBack,InputStream is){
		startPullParse(callBack, is, "UTF-8");
	}
	
	/**
	 * 使用Pull解析XML
	 * @param callBack
	 * @param is
	 * @param charset
	 */
	public static void startPullParse(IPullParserCallBack callBack,InputStream is,String charset){
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(is, charset);
			int event = parser.getEventType();
			while(event != XmlPullParser.END_DOCUMENT){
				switch(event){
				case XmlPullParser.START_DOCUMENT:
					callBack.startDocCallBack();
					break;
				case XmlPullParser.START_TAG:
					callBack.startTagCallBack();
					break;
				case XmlPullParser.END_TAG:
					callBack.endTagCallBack();
					break;
				}
				if(!callBack.isCanInterrupt())
					event = parser.getEventType();
				else
					break;
			}
			callBack.endDocCallBack();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}
}
