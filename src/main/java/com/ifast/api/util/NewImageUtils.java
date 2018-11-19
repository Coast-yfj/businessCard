package com.ifast.api.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
  public class NewImageUtils {
     /**
        *
       * @Title: 构造图片
        * @Description: 生成水印并返回java.awt.image.BufferedImage
        * @param file
        *            源文件(图片)
        * @param waterFile
        *            水印文件(图片)
        * @param x
        *            距离右下角的X偏移量
        * @param y
        *            距离右下角的Y偏移量
        * @param alpha
        *            透明度, 选择值从0.0~1.0: 完全透明~完全不透明
        * @return BufferedImage
        * @throws IOException
        */
              public static BufferedImage watermark(File file, File waterFile, int x, int y, float alpha) throws IOException {
                 // 获取底图
                 BufferedImage buffImg = ImageIO.read(file);
                 // 获取层图
                 BufferedImage waterImg = ImageIO.read(waterFile);
                 // 创建Graphics2D对象，用在底图对象上绘图
                 Graphics2D g2d = buffImg.createGraphics();
                 int waterImgWidth = waterImg.getWidth();// 获取层图的宽度
                 int waterImgHeight = waterImg.getHeight();// 获取层图的高度
                 // 在图形和图像中实现混合和透明效果
                 g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
                 // 绘制
                g2d.drawImage(waterImg, x, y, waterImgWidth, waterImgHeight, null);
                 g2d.dispose();// 释放图形上下文使用的系统资源
                 return buffImg;
             }

             /**
       * 输出水印图片
       *
       * @param buffImg
       *            图像加水印之后的BufferedImage对象
       * @param savePath
       *            图像加水印之后的保存路径
       */
             public void generateWaterFile(BufferedImage buffImg, String savePath,String name) {
                 int temp = savePath.lastIndexOf(".") + 1;
                 try {
                         ImageIO.write(buffImg, savePath.substring(temp), new File(savePath,name));
                     } catch (IOException e1) {
                         e1.printStackTrace();
                     }
             }

             /**
      *
       * @param args
       * @throws IOException
       *             IO异常直接抛出了
       * @author bls
       */
             public static void main(String[] args) throws IOException {
                 try {
//                     zoomImage("D://imgs//2.jpg","D://imgs//4.jpg",142,147);
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
                 String sourceFilePath = "D://imgs//3.png";
                 String waterFilePath = "D://imgs//4.jpg";
                 String saveFilePath = "D://imgs//5.png";
                 NewImageUtils newImageUtils = new NewImageUtils();
                 // 构建叠加层
//                 BufferedImage buffImg = NewImageUtils.watermark(new File(sourceFilePath), new File(waterFilePath), 125, 170, 1.0f);
                 BufferedImage buffImg = NewImageUtils.watermark(new File(sourceFilePath), new File(waterFilePath), 145, 142, 1.0f);
                 // 输出水印图片
//                 newImageUtils.generateWaterFile(buffImg, saveFilePath);
             }


      /*
       * 图片缩放,w，h为缩放的目标宽度和高度
       * src为源文件目录，dest为缩放后保存目录
       */
      public static void zoomImage(String src,String srcName,String dest, String destName,int w,int h) throws Exception {

          double wr=0,hr=0;
          File srcFile = new File(src,srcName);
          File destFile = new File(dest,destName);

          BufferedImage bufImg = ImageIO.read(srcFile); //读取图片
          Image Itemp = bufImg.getScaledInstance(w, h, bufImg.SCALE_SMOOTH);//设置缩放目标图片模板

          wr=w*1.0/bufImg.getWidth();     //获取缩放比例
          hr=h*1.0 / bufImg.getHeight();

          AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
          Itemp = ato.filter(bufImg, null);
          try {
              ImageIO.write((BufferedImage) Itemp,dest.substring(dest.lastIndexOf(".")+1), destFile); //写入缩减后的图片
          } catch (Exception ex) {
              ex.printStackTrace();
          }
      }

 }
