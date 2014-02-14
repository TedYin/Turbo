package com.turbo.common;

import java.util.HashMap;
import java.util.Map;

import com.turbo.data.FileHelper;

/** 
 * 媒体类型工具类
 * @author  Ted
 * @mail water-cs@qq.com
 * @version 1.0.0
 */
public class MediaUtils
{
	private static Map<String, String> MEDIA_TYPE_MAP = new HashMap<String, String>();
	
	static
	{
		//音频
		MEDIA_TYPE_MAP.put( "mp3", "audio" );
		MEDIA_TYPE_MAP.put( "mid", "audio" );
		MEDIA_TYPE_MAP.put( "midi", "audio" );
		MEDIA_TYPE_MAP.put( "asf", "audio" );
		MEDIA_TYPE_MAP.put( "wm", "audio" );
		MEDIA_TYPE_MAP.put( "wma", "audio" );
		MEDIA_TYPE_MAP.put( "wmd", "audio" );
		MEDIA_TYPE_MAP.put( "amr", "audio" );
		MEDIA_TYPE_MAP.put( "wav", "audio" );
		MEDIA_TYPE_MAP.put( "3gpp", "audio" );
		MEDIA_TYPE_MAP.put( "mod", "audio" );
		MEDIA_TYPE_MAP.put( "mpc", "audio" );
		
		//视频
		MEDIA_TYPE_MAP.put( "fla", "video" );
		MEDIA_TYPE_MAP.put( "flv", "video" );
		MEDIA_TYPE_MAP.put( "wav", "video" );
		MEDIA_TYPE_MAP.put( "wmv", "video" );
		MEDIA_TYPE_MAP.put( "avi", "video" );
		MEDIA_TYPE_MAP.put( "rm", "video" );
		MEDIA_TYPE_MAP.put( "rmvb", "video" );
		MEDIA_TYPE_MAP.put( "3gp", "video" );
		MEDIA_TYPE_MAP.put( "mp4", "video" );
		MEDIA_TYPE_MAP.put( "mov", "video" );
		
		//flash
		MEDIA_TYPE_MAP.put( "swf", "video" );
		
		MEDIA_TYPE_MAP.put( "null", "video" );
		
		//图片
		MEDIA_TYPE_MAP.put( "jpg", "photo" );
		MEDIA_TYPE_MAP.put( "jpeg", "photo" );
		MEDIA_TYPE_MAP.put( "png", "photo" );
		MEDIA_TYPE_MAP.put( "bmp", "photo" );
		MEDIA_TYPE_MAP.put( "gif", "photo" );
	}
	
	/**
	 * 根据根据扩展名获取类型
	 * @param attFormat
	 * @return
	 */
	public static String getContentType( String attFormat )
	{
		String contentType = MEDIA_TYPE_MAP.get("null");
		
		if ( attFormat != null ) 
		{
			contentType = (String)MEDIA_TYPE_MAP.get( attFormat.toLowerCase() );
		}
		return contentType;
	}
	
	/**
	 * 判断文件MimeType的method
	 * @param f
	 * @return
	 */
    public static String getMIMEType(String filePath)
    {
        String type = "";
        String fName = FileHelper.getFileName(filePath);
        /* 取得扩展名 */
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
        
        /* 按扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg")
            || end.equals("wav"))
        {
            type = "audio";
        }
        else if (end.equals("3gp") || end.equals("mp4"))
        {
            type = "video";
        }
        else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp"))
        {
            type = "image";
        }
        else if(end.equals("doc") || end.equals("docx"))
        {
            type = "application/msword";
        }
        else if(end.equals("xls"))
        {
            type = "application/vnd.ms-excel";
        }
        else if(end.equals("ppt") || end.equals("pptx") || end.equals("pps") || end.equals("dps"))
        {
            type = "application/vnd.ms-powerpoint";
        }
        else
        {
            type = "*";
        }
        /* 如果无法直接打开，就弹出软件列表给用户选择 */
        type += "/*";
        return type;
    }

}