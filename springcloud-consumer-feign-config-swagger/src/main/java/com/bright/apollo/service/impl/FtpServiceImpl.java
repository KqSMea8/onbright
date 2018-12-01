package com.bright.apollo.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bright.apollo.service.FtpService;
import com.bright.apollo.tool.MD5;
import com.bright.apollo.vo.PicPathVo;

import io.swagger.models.auth.In;
import net.coobird.thumbnailator.Thumbnails;

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
	public String[] uploadFile(String originFileName, String oldPath, PicPathVo pathVo) {
		FTPClient ftp = null;
		String[] imagepaths = new String[2];
		try {
			ftp = loginFtp(pathVo);
			if (ftp == null)
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
			String zipfile = split2.length == 2 ? makeFileName + "_thum." + split2[1] : makeFileName + "_thum.jpg";
			File file =new File(oldPath);
			String picPath = uploadPic(ftp, makePath, savafile, file);
			String zipPath = compressPic(ftp, zipfile, file, pathVo);
			imagepaths[0] = picPath;
			imagepaths[1] = zipPath;
			file.delete();
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
		} finally {
			if (ftp != null && ftp.isConnected()) {
				try {
					ftp.logout();
					ftp.disconnect();
				} catch (IOException ioe) {
					logger.error("===error msg:" + ioe.getMessage());
				}
			}
		}
		return imagepaths;
	}

	/**
	 * @param savafile
	 * @param input
	 * @Description:
	 */
	private String uploadPic(FTPClient ftp, String makePath, String savafile, File file) {
		try {
			InputStream input = new FileInputStream(file);
			boolean storeFile = ftp.storeFile(savafile, input);
			input.close();
			if (storeFile)
				return "images/" + makePath + savafile;
		} catch (IOException e) {
			logger.error("===error msg:" + e.getMessage());
		}
		return null;
	}

 	private String compressPic(FTPClient ftp, String makeFileName,File file, PicPathVo pathVo) {
		File file2 = null;
 		try {
			Thumbnails.of(file).scale(1f).outputQuality(0.5f).toFile(pathVo.getTempPath() + makeFileName);
			// ftp upload
			file2 = new File(pathVo.getTempPath() + makeFileName);
 			
			return uploadPic(ftp, pathVo.getRealPath(), makeFileName, file2);
		} catch (IOException e) {
			logger.error("===error msg:" + e.getMessage());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
		} finally {
 			 if(file2!=null)
 				file2.delete();
		}
		return null;
	}

	private FTPClient loginFtp(PicPathVo pathVo) throws NumberFormatException, SocketException, IOException {
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

	public static void main(String[] args)
			throws IOException {/*
								 * String url =
								 * "images/15/3/4376164045528554996_9ffe82f5c5f31c8d0ab89aa38978d92e.jpg";
								 * String[] split = url.split("/"); for (int i =
								 * 0; i < split.length; i++) {
								 * System.out.println(split[i]); }
								 */
		Thumbnails
				.of("C:" + File.separator + "Users" + File.separator + "lenovo" + File.separator + "Desktop"
						+ File.separator + "1fcc5f2ddd3953681ed0e0b8731ec6fc.jpg")
				.scale(1f).outputQuality(0.5f).toFile("C:" + File.separator + "Users" + File.separator + "lenovo"
						+ File.separator + "Desktop" + File.separator + "111.jpg");
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.FtpService#saveToTemp(java.lang.String, java.io.InputStream, com.bright.apollo.vo.PicPathVo)  
	 */
	@Override
	public String saveToTemp(String originFileName, InputStream in, PicPathVo pathVo) {
 		try {
			FileOutputStream out = new FileOutputStream(pathVo.getTempPath()
					+ "/" + originFileName);
			// 创建一个缓冲区
			byte buffer[] = new byte[1024];
			// 判断输入流中的数据是否已经读完的标识
			int len = 0;
			// 循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
			while ((len = in.read(buffer)) > 0) {
				// 使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\"
				// + filename)当中
				out.write(buffer, 0, len);
			}
			// 关闭输入流
			in.close();
			// 关闭输出流
			out.close();
			return pathVo.getTempPath()
					+ "/" + originFileName;
		} catch (FileNotFoundException e) {
			logger.error("===error msg:" + e.getMessage());
		} catch (IOException e) {
			logger.error("===error msg:" + e.getMessage());
		}
		return null;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.FtpService#deleteFtpFile(java.lang.String, com.bright.apollo.vo.PicPathVo)  
	 */
	@Override
	public boolean deleteFtpFile(String path, PicPathVo pathVo) {
		FTPClient ftp=null;
		try {
			ftp = loginFtp(pathVo);
			if(ftp==null)
				return false;
			//遍历
			String[] split = path.split("/");
			if(split.length>0){
				for (int i = 0; i < split.length; i++) {
					if(!split[i].contains("."))
						ftp.changeWorkingDirectory(split[i]);
					else
						return ftp.deleteFile(split[i]);
				}
			}
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
		}  
		return false;
	}

}
