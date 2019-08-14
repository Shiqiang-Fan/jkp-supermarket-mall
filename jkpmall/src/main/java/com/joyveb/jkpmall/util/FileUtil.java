package com.joyveb.jkpmall.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

    /**
     * 在指定目录下添加指定文件
     * @param file 二进制文件
     * @param filePath 文件路径
     * @param fileName 文件名
     * @throws IOException
     */
    public static void fileUpload(byte[] file, String filePath, String fileName) throws IOException{

        //目标目录
        File targetFile = new File(filePath);
        if (targetFile.exists()){
            targetFile.mkdirs();
        }

        //二进制流写入
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        out.flush();
        out.close();
    }
}
