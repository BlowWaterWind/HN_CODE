package com.ai.ecs.ecm.mall.wap.modules.goods.utils;



//import com.sun.media.imageio.plugins.tiff.TIFFImageWriteParam;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import java.io.FileOutputStream;
import java.io.OutputStream;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * Created by pimengjue on 2018/8/17.
 */
public class ImageIOHelper {
//    /**
//     * 图片文件转换为tif格式
//     * @param imageFile 文件路径
//     * @param imageFormat 文件扩展名
//     * @return
//     */
//    public static File createImage(File imageFile, String imageFormat) {
//        File tempFile = null;
//        try {
//            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(imageFormat);
//            ImageReader reader = readers.next();
//
//            ImageInputStream iis = ImageIO.createImageInputStream(imageFile);
//            reader.setInput(iis);
//            //Read the stream metadata
//            IIOMetadata streamMetadata = reader.getStreamMetadata();
//
//            //Set up the writeParam
//            TIFFImageWriteParam tiffWriteParam = new TIFFImageWriteParam(Locale.CHINESE);
//            tiffWriteParam.setCompressionMode(ImageWriteParam.MODE_DISABLED);
//
//            //Get tif writer and set output to file
//            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("tiff");
//            ImageWriter writer = writers.next();
//
//            BufferedImage bi = reader.read(0);
//            IIOImage image = new IIOImage(bi,null,reader.getImageMetadata(0));
//            tempFile = tempImageFile(imageFile);
//            ImageOutputStream ios = ImageIO.createImageOutputStream(tempFile);
//            writer.setOutput(ios);
//            writer.write(streamMetadata, image, tiffWriteParam);
//            ios.close();
//
//            writer.dispose();
//            reader.dispose();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return tempFile;
//    }

    private static File tempImageFile(File imageFile) {
        String path = imageFile.getPath();
        StringBuffer strB = new StringBuffer(path);
        strB.insert(path.lastIndexOf('.'),0);
        return new File(strB.toString().replaceFirst("(?<=//.)(//w+)$", "tif"));
    }

    /**
     * 上传图片base64字符串转化成图片
     *
     * @param directionry
     *            保存的文件夹名称
     * @param basePath
     *            保存路径
     * @param imgStr
     *            图片码流文件
     * @return
     */
    public static String GenerateImage(String directionry, String basePath,String imgStr) { // 对字节数组字符串进行Base64解码并生成图片
        String imgFilePath = "", imgPath = "";
        Random random = new Random();
        String ranNum="";
        for (int i=0;i<6;i++){ranNum+=random.nextInt(10);}

        String fileName = System.currentTimeMillis() + ranNum + ".jpg";
        if (imgStr == null) // 图像数据为空
            return null;
        try {
            if (imgStr.indexOf(",") != -1) {
                imgStr = imgStr.substring(imgStr.indexOf(",") + 1);
            }
            byte dataByte[] = Base64.decode(imgStr);

            File file = new File(directionry + basePath);
            if (!file.exists()) {
                file.mkdirs();// 创建父目录地址
            }
            // 生成jpeg图片
            imgFilePath = directionry + "/" + basePath + "/" + fileName;
            imgPath = directionry + "/"  + basePath + "/" + fileName;// 创建图片地址
            OutputStream out = new FileOutputStream(imgFilePath);

            out.write(dataByte);
            out.flush();
            out.close();
        } catch (Exception e) {

        }
        return imgPath;
    }


    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static  boolean deleteFile(String sPath) {
        boolean flag = false;
        File  file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
}
