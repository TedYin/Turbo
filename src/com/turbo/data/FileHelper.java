
package com.turbo.data;

import android.content.Context;

import com.turbo.app.TurboBaseApp;
import com.turbo.common.EnvHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.text.DecimalFormat;

/**
 * 文件助手类，处理文件的读写操作
 * 
 * @author Ted
 * @date 2013-03-10
 */
public class FileHelper {

    private FileHelper() {
        throw new RuntimeException("不可实例化！");
    }

    /**
     * 根据路径获取文件目标文件名 如果传入的为URI则进行网络访问并返回文件名称
     * 
     * @param path
     * @return
     */
    public static String getFileName(String path) {
        if (path != null && path.startsWith("http")) {
            // TODO:进行网络访问获取文件信息
            return "fileName";
        }
        return new File(path).getName();
    }

    /**
     * 根据路径获取文件大小 如果传入的为URI则进行网络访问并返回文件大小 TODO:需测试才可使用
     * 
     * @param path
     * @return
     */
    public static long getFileSize(String path) {
        long totalSize = 0L;
        if (path != null && path.startsWith("http")) {
            // TODO:进行网络访问获取文件信息
            return totalSize;
        }
        File srcFile = new File(path);
        if (srcFile.isDirectory()) {
            for (File file : srcFile.listFiles()) {
                totalSize += getFileSize(file.getPath());
            }
        } else
            return srcFile.length();
        return totalSize;
    }

    /**
     * init file if the file is not exists
     * 
     * @param file
     * @return if the file is null || createFile failed return false,else return
     *         true
     */
    public static boolean initFile(File file) {
        if (file == null) {
            return false;
        }
        if (!file.exists()) {
            File dir = file.getParentFile();
            if (!dir.exists()) {
                if (dir.mkdirs()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                } else
                    return false;
            }
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * delete the file or files or directory(with the files under it)
     * 
     * @param File ...
     * @return success return true otherwise false
     */
    public static boolean delFiles(File... files) {
        boolean isDeleted = false;
        for (File file : files) {
            if (file != null && file.exists()) {
                if (file.isDirectory() && file.listFiles().length > 0)
                    delFiles(file.listFiles());
                else {
                    file.delete();
                    isDeleted = true;
                }
            } else
                isDeleted = true;
        }
        return isDeleted;
    }

    /**
     * seperate the file content by special divider
     * 
     * @param file
     * @param divider
     * @return the sections of the file
     */
    public static String[] seperate(File file, String divider) {
        String data = readFile(file);
        return data.split(divider);
    }

    /**
     * 从文件中获取字符串
     * 
     * @param file
     * @return the file content
     */
    public static String readFile(File file) {
        if (!initFile(file))
            return "";
        InputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }
        return inputStream2String(fis);
    }

    /**
     * convert input stream to string by UTF-8
     * 
     * @param InputStream
     * @return String
     */
    public static String inputStream2String(InputStream fis) {
        return inputStream2String(fis, null);
    }

    /**
     * convert input stream to string by the special encode.
     * 
     * @param InputStream
     * @param charset
     * @return String
     */
    public static String inputStream2String(InputStream is, String charset) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = -1;
        byte[] data = new byte[1024];
        String result = null;
        try {
            while ((len = is.read(data)) != -1) {
                baos.write(data, 0, len);
            }
            if (null == charset)
                result = new String(baos.toByteArray());
            else
                result = new String(baos.toByteArray(), charset);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * write the msg to the file
     * 
     * @param file
     * @param msg
     */
    public static void writeFile(File file, String msg) {
        if (!initFile(file))
            return;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * read the object from file
     * 
     * @param objectFile
     * @return
     */
    public static Object readObject(File objectFile) {
        if (objectFile.exists()) {
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            try {
                fis = new FileInputStream(objectFile);
                ois = new ObjectInputStream(fis);
                return ois.readObject();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (ois != null) {
                    try {
                        ois.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    /**
     * save the object to file
     * 
     * @param object
     * @param outFile
     */
    public static void writeObject(Object object, File outFile) {
        File dir = outFile.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(outFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 转换文件大小
     * 
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 获取指定名称的文件对象引用
     * 
     * @param fileName
     * @return 如果SD卡没有挂载则返回null，否则返回指定名称的File对象
     */
    public static File createExternalCacheFile(String fileName) {
        File file = null;
        if (!EnvHelper.hasSdcard()) {
            return file;
        }
        Context context = TurboBaseApp.getAppContext();
        File dir = context.getExternalCacheDir();
        file = new File(dir, fileName);
        if(initFile(file))
            return file;
        else
            return null;
    }
    
    /**
     * 获取指定名称的文件对象引用
     * 
     * @param fileName
     * @return 文件创建失败，返回null
     */
    public static File createCacheFile(String fileName) {
        Context context = TurboBaseApp.getAppContext();
        File dir = context.getCacheDir();
        File file = new File(dir, fileName);
        if(initFile(file))
            return file;
        else 
            return null;
            
    }
}
