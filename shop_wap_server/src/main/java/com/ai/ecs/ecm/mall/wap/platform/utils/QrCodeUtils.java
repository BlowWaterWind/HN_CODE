package com.ai.ecs.ecm.mall.wap.platform.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hewei on 2017/11/27/027.
 */
public class QrCodeUtils {
    public static final int width = 300;

    public static final int height = 300;

    /**
     * qr code generate
     *
     * @param url     url
     * @param logoImg byte array of logo image
     * @return byte array of qr code image
     */
    public static byte[] fetchQrCode(String url, byte[] logoImg, int onColor, int offColor, int margin) {
        try {
            BufferedImage image = generate(url, logoImg, onColor, offColor, margin);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "png", out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error: generate qr code.", e);
        }
    }

    private static BufferedImage generate(String url, byte[] logImg, int onColor, int offColor, int margin)
            throws WriterException, IOException {
        if (logImg != null && logImg.length > 0) {
            ByteArrayInputStream in = new ByteArrayInputStream(logImg);
            BufferedImage logo = ImageIO.read(in);
            return generateImg(url, logo, onColor, offColor, margin);
        } else {
            return generateImg(url, null, onColor, offColor, margin);
        }
    }

    private static BufferedImage generateImg(String url, BufferedImage logImg, int onColor, int offColor, int margin)
            throws WriterException {
        BitMatrix bitMatrix;
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.MARGIN, margin);//set the write margin
        MatrixToImageConfig config = new MatrixToImageConfig(onColor, offColor);
        if (logImg != null) {
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height, hints);

            int logoSize = (int) (width / (4.0 + 20 / (url.length() + 10))); // into 4~6
            int start = (width - logoSize) / 2;
            bitMatrix.setRegion(start, start, logoSize, logoSize);// logo region
            BufferedImage qrCode = MatrixToImageWriter.toBufferedImage(bitMatrix);

            //combine qr code and logo
            BufferedImage combine = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = combine.getGraphics();
            g.drawImage(qrCode, 0, 0, null);
            g.drawImage(resizeImg(logImg, logoSize, logoSize), start, start, null);
            return combine;
        } else {
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height, hints);
            return MatrixToImageWriter.toBufferedImage(bitMatrix, config);
        }
    }

    /**
     * resize image
     *
     * @param logoImg image BufferedImage
     * @param width   resize width
     * @param height  resize height
     * @return resized image
     */
    private static BufferedImage resizeImg(BufferedImage logoImg, int width, int height) {
        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        tag.getGraphics().drawImage(logoImg.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING), 0, 0, null);
        return tag;

    }
}
