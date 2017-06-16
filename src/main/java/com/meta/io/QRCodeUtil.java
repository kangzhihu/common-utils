package com.meta.io;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Hashtable;

/**
 * Description: 一维码、二维码工具类<br/>
 * Created by zhihu.kang<br/>
 * Time: 2017/6/16 23:48<br/>
 * Email:kangzhihu@163.com<br/>
 */
public final class QRCodeUtil {

    private static final String CHARSET = "utf-8";
    private static final String FORMAT_NAME = "JPG";
    /** default qrcode width and height */
    private static final int DEFAULT_QRCODE_SIZE = 200;
    /** LOGO宽度 */
    private static final int WIDTH = 60;
    /** LOGO高度 */
    private static final int HEIGHT = 60;

    /**
     * 生成二维码
     * @param content 内容
     * @param destPath 存储地址
     * @throws Exception
     */
    public static void encode(String content, String destPath) throws Exception {
        QRCodeUtil.encode(content, null, destPath, false);
    }

    /**
     * 生成二维码
     * @param content 内容
     * @param destPath 存储地址
     * @param width 宽度
     * @param height 高度
     * @param offset 偏移量
     * @throws Exception
     */
    public static void encode(String content, String destPath, int width, int height, int offset) throws Exception {
        QRCodeUtil.encode(content, null, destPath, false);
    }

    /**
     * 生成二维码(内嵌LOGO图片)
     * @param content  内容
     * @param imgPath LOGO地址
     * @param destPath 存储地址
     * @throws Exception
     */
    public static void encode(String content, String imgPath, String destPath) throws Exception {
        QRCodeUtil.encode(content, imgPath, destPath, false);
    }

    /**
     * 生成二维码
     * @param content 内容
     * @param destPath 存储地址
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */
    public static void encode(String content, String destPath, boolean needCompress) throws Exception {
        QRCodeUtil.encode(content, null, destPath, needCompress);
    }

    /**
     * 生成二维码
     * @param content 内容
     * @param output 输出流
     * @throws Exception
     */
    public static void encode(String content, OutputStream output) throws Exception {
        QRCodeUtil.encode(content, null, output, false);
    }

    /**
     * 生成二维码(内嵌LOGO)
     * 
     * @param content 内容
     * @param imgPath LOGO地址
     * @param destPath 存储地址
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */
    public static void encode(String content, String imgPath, String destPath, boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress);
        ImageIO.write(image, FORMAT_NAME, new File(destPath));
    }

    public static void encode(String content, String imgPath, String destPath, boolean needCompress, int width,
            int height, int offset) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress, width, height, offset);
        ImageIO.write(image, FORMAT_NAME, new File(destPath));
    }

    /**
     * 生成二维码(内嵌LOGO)
     * 
     * @param content 内容
     * @param imgPath LOGO地址
     * @param output 输出流
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */
    public static void encode(String content, String imgPath, OutputStream output, boolean needCompress)
            throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    public static void encode(String content, String imgPath, OutputStream output, boolean needCompress, int width,
            int height, int offset) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress, width, height, offset);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    public static BufferedImage createImage(String content, String imgPath, boolean needCompress) throws Exception {
        BufferedImage image = null;
        if (imgPath == null || "".equals(imgPath)) {
            return toBufferedImage(content, DEFAULT_QRCODE_SIZE, DEFAULT_QRCODE_SIZE, 0);
        }
        // 插入图片
        QRCodeUtil.insertImage(image, imgPath, needCompress);
        return image;
    }

    public static BufferedImage createImage(String content, String imgPath, boolean needCompress, int width, int height,
            int offset) throws Exception {
        BufferedImage image = null;
        if (imgPath == null || "".equals(imgPath)) {
            return toBufferedImage(content, width, height, offset);
        }
        // 插入图片
        QRCodeUtil.insertImage(image, imgPath, needCompress);
        return image;
    }

    private static BufferedImage toBufferedImage(String content, int width, int height, int offset)
            throws WriterException {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width - offset, height, hints);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

     /**
     * 生成条形码<br>
     * <b>注意</b>条形码的宽度不能等于图片的宽度，否则解析不出来,如果解析不出来，请加大offset的值
     * @param contents 内容
     * @param dest 条形码图片地址
     * @param width 宽度
     * @param height 高度
     * @param offset 偏移量
    */
    public static void encodeBarCode128(String contents, String dest, int width, int height, int offset)
            throws WriterException, FileNotFoundException, IOException {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix matrix = new MultiFormatWriter().encode(contents, BarcodeFormat.CODE_128, width - offset, height, hints);
        try(OutputStream stream = new FileOutputStream(new File(dest))){
            MatrixToImageWriter.writeToStream(matrix, FORMAT_NAME, stream);
        }
    }

    /**
     * 插入LOGO
     * 
     * @param source 二维码图片
     * @param imgPath LOGO图片地址
     * @param needCompress 是否压缩
     * @throws Exception
     */
    private static void insertImage(BufferedImage source, String imgPath, boolean needCompress) throws Exception {
        File file = new File(imgPath);
        if (!file.exists()) {
            return;
        }
        Image src = ImageIO.read(new File(imgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > WIDTH) {
                width = WIDTH;
            }
            if (height > HEIGHT) {
                height = HEIGHT;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (DEFAULT_QRCODE_SIZE - width) / 2;
        int y = (DEFAULT_QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 解析二维码
     * 
     * @param file 二维码图片
     * @return
     * @throws Exception
     */
    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

    /**
     * 解析二维码
     * 
     * @param path 二维码图片地址
     * @return
     * @throws Exception
     */
    public static String decode(String path) throws Exception {
        return QRCodeUtil.decode(new File(path));
    }

}