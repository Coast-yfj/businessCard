package com.ifast.oss.sdk;

import com.ifast.common.exception.IFastException;
import com.ifast.common.type.EnumErrorCode;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * <pre>
 * 七牛对象存储服务
 * </pre>
 * 
 * <small> 2018年4月6日 | Aron</small>
 */
public class QiNiuOSSService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private UploadManager uploadManager;
    private Configuration cfg;
    private OSSConfig config;


    public QiNiuOSSService(OSSConfig config, Zone zone) {
        cfg = new Configuration(zone);
        uploadManager = new UploadManager(cfg);
        this.config = config;
    }

    // method

    public String upload(byte[] uploadBytes, String fileName) {
        String token = Auth.create(this.config.getQiNiuAccessKey(), this.config.getQiNiuSecretKey())
                .uploadToken(this.config.getQiNiuBucket());
        try {
            uploadManager.put(uploadBytes, fileName, token);
            String fileURL = this.config.getQiNiuAccessURL() + fileName;
            log.info("上传成功，url:{}", fileURL);
            return fileURL;
        } catch (QiniuException ex) {
            ex.printStackTrace();
            throw new IFastException(EnumErrorCode.FileUploadError.getCodeStr());
        }
    }

    /**上传文件
     * @param file 			//文件对象
     * @param filePath		//上传路径
     * @param fileName		//文件名
     * @return  文件名
     */
    public  String fileUp(MultipartFile file, String filePath, String fileName){
        // 扩展名格式：
        String extName = "";
        try {
            if (file.getOriginalFilename().lastIndexOf(".") >= 0){
                extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            }
            copyFile(file.getInputStream(), filePath, fileName+extName).replaceAll("-", "");
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return fileName+extName;
    }
    /**
     * 写文件到当前目录的upload目录中
     * @param in
     * @param dir
     * @throws IOException
     */
    private static String copyFile(InputStream in, String dir, String realName)
            throws IOException {
        File file = mkdirsmy(dir,realName);
        FileUtils.copyInputStreamToFile(in, file);
        return realName;
    }


    /**判断路径是否存在，否：创建此路径
     * @param dir  文件路径
     * @param realName  文件名
     * @throws IOException
     */
    public static File mkdirsmy(String dir, String realName) throws IOException{
        File file = new File(dir, realName);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        return file;
    }


}
