package com.bright.apollo.service.impl;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bright.apollo.service.FtpService;
import com.bright.apollo.tool.MD5;
import com.bright.apollo.vo.PicPathVo;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年11月27日
 * @Version:1.1.0
 */
@Service
public class FtpServiceImpl implements FtpService {
	private static final Logger logger = LoggerFactory.getLogger(FtpServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bright.apollo.service.FtpService#uploadFile(java.lang.String,
	 * java.io.InputStream, com.bright.apollo.vo.PicPathVo)
	 */
	@Override
	public String uploadFile(String originFileName, InputStream input, PicPathVo pathVo) {
		FTPClient ftp =null;
		try {
			ftp = loginFtp(pathVo);
			if(ftp==null)
				return null;
			String makeFileName = makeFileName(originFileName);
			String makePath = makePath(makeFileName);
			logger.info("===makeFileName:" + makeFileName + "===makePath:" + makePath);
			String[] split = makePath.split("/");
			for (int i = 0; i < split.length; i++) {
				ftp.makeDirectory(split[i]);
				ftp.changeWorkingDirectory(split[i]);
			}
			String[] split2 = originFileName.split(".");
			String savafile = split2.length == 2 ? makeFileName + "." + split2[1] : makeFileName + ".jpg";
			boolean storeFile = ftp.storeFile(savafile, input);
			input.close();
			ftp.logout();
			if (storeFile)
				return "images/" + makePath + "savafile";
		} catch (IOException e) {
			logger.error("===error msg:" + e.getMessage());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
		} finally {
			if (ftp!=null&&ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					logger.error("===error msg:" + ioe.getMessage());
				}
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bright.apollo.service.FtpService#uploadZipFile(java.lang.String,
	 * java.io.InputStream, com.bright.apollo.vo.PicPathVo)
	 */
	@Override
	public String uploadZipFile(String originFileName, InputStream InputStream, PicPathVo pathVo) {
		FTPClient ftp=null;
		try {		
			ftp= loginFtp(pathVo);
			if(ftp==null)
				return null;
			// 0, 448, 0.9f
			Image srcFile = ImageIO.read(InputStream);
			int w = srcFile.getWidth(null);
			int h = srcFile.getHeight(null);
			double bili;
			bili = 448 / (double) h;
			int width = (int) (w * bili);
			BufferedImage tag = new BufferedImage(width, 448, BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(srcFile, 0, 0, width, 448, null);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(tag, "gif", os);
			InputStream is = (InputStream)new ByteArrayInputStream(os.toByteArray());
			String[] split = originFileName.split(".");
            String newFileName=split.length==2?split[0]+"_thum."+split[1]:makeFileName(originFileName)+"_thum.jpg";
            String[] split2 = originFileName.split("/");
            for (int i = 0; i < split2.length-1; i++) {
				//ftp.makeDirectory(split[i]);
				ftp.changeWorkingDirectory(split[i]);
			}
            String[] split3 = newFileName.split("/");
            boolean storeFile = ftp.storeFile(split3.length>0?split3[split3.length-1]:newFileName, is);
            is.close();
            os.close();
			ftp.logout();
			if (storeFile)
				return split3.length>0?newFileName:"images/" + newFileName + "savafile";
		} catch (IOException e) {
			logger.error("===error msg:" + e.getMessage());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
		}finally {
			if (ftp!=null&&ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					logger.error("===error msg:" + ioe.getMessage());
				}
			}
		}
		return null;
	}
	private FTPClient loginFtp(PicPathVo pathVo) throws NumberFormatException, SocketException, IOException{
		FTPClient ftp = new FTPClient();
		ftp.setControlEncoding("utf-8");
		int reply;
		ftp.connect(pathVo.getFtpAddress(), Integer.parseInt(pathVo.getFtpPort()));// 连接FTP服务器
		ftp.login(pathVo.getFtpName(), pathVo.getFtpPwd());// 登录
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			logger.warn("===ftp disconnect===");
			ftp.disconnect();
			return null;
		}
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		return ftp;
	}
	/**
	 * @Method: makeFileName
	 * @Description: 生成上传文件的文件名，文件名以：uuid+"_"+文件的原始名称
	 * @param filename
	 *            文件的原始名称
	 * @return uuid+"_"+文件的原始名称
	 * @throws Exception
	 */
	private static String makeFileName(String filename) throws Exception { // 2.jpg
		// 为防止文件覆盖的现象发生，要为上传文件产生一个唯一的文件名
		return RandomUtils.nextLong() + "_" + MD5.getMD5Str(filename);
		// return RandomUtils.genUUID().toString() + "_" + filename;
	}

	/**
	 * 为防止一个目录下面出现太多文件，要使用hash算法打散存储
	 * 
	 * @Method: makePath
	 * @Description:
	 *
	 * @param filename
	 *            文件名，要根据文件名生成存储目录
	 * @param savePath
	 *            文件存储路径
	 * @return 新的存储目录
	 */
	private static String makePath(String filename) {
		// 得到文件名的hashCode的值，得到的就是filename这个字符串对象在内存中的地址
		int hashcode = filename.hashCode();
		int dir1 = hashcode & 0xf; // 0--15
		int dir2 = (hashcode & 0xf0) >> 4; // 0-15
		// 构造新的保存目录 在windows中使用\\ mac中用/
		String dir = dir1 + "/" + dir2; // upload\2\3
										// upload\3\5
		logger.info("===save pic url===");
		return dir;
	}

	public static void main(String[] args) throws IOException { 
		String url="images/15/3/4376164045528554996_9ffe82f5c5f31c8d0ab89aa38978d92e.jpg";
		String[] split = url.split("/");
		for (int i = 0; i < split.length; i++) {
			System.out.println(split[i]);
		}
	}

}
