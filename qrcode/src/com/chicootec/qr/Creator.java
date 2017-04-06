package com.chicootec.qr;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public final class Creator {
  private Creator(){
    throw new AssertionError();
  }

  private static final int BLACK = 0xFF000000;
  private static final int WHITE = 0xFFFFFFFF;

  public static BufferedImage toBufferedImage(BitMatrix matrix) {
    int width = matrix.getWidth();
    int height = matrix.getHeight();
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
      }
    }
    return image;
  }

  public static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
    BufferedImage image = toBufferedImage(matrix);
    if (!ImageIO.write(image, format, file)) {
      throw new IOException("Could not write an image of format " + format + " to " + file);
    }
  }

  public static void writeToStream(BitMatrix matrix, String format, OutputStream stream)
      throws IOException {
    BufferedImage image = toBufferedImage(matrix);
    if (!ImageIO.write(image, format, stream)) {
      throw new IOException("Could not write an image of format " + format);
    }
  }

  public static void writeToStream(String content, int width, int height, OutputStream os) {
    BitMatrix matrix = createBitMatrix(content, width, height);
    try {
      writeToStream(matrix, "jpg", os);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public static BitMatrix createBitMatrix(String content, int width, int height) {
    if (width < 1)
      width = 800;
    if (height < 1)
      height = 800;

    if (null == content || "".equals(content = content.trim()))
      return null;

    Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
    hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
    QRCodeWriter writer = new QRCodeWriter();
    BitMatrix bitMatrix = null;
    try {
      bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
//      bitMatrix = writer.encode(content, BarcodeFormat.CODABAR, width, height, hints);
    } catch (Exception e) {

    }
    return bitMatrix;
  }

  public static Image create(String content, int width, int height) {
    return create(content,  width, height);
  }

  public static Image create(String content, String path, int width, int height) {
    if (null != path) {
      File file = new File(path);
      return create(content,file,width,height);
    }
    return null;
  }
  
  public static Image create(String content, File file, int width,int height){
    BitMatrix bitMatrix = createBitMatrix(content, width, height);
    try {
      Image img = toBufferedImage(bitMatrix);
      if (null != file) {
        writeToFile(bitMatrix, "jpg", file);
      }
      return img;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
