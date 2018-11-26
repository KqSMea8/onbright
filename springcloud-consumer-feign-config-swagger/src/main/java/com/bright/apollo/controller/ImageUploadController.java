package com.bright.apollo.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.tool.MD5;
import com.bright.apollo.vo.PicPathVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年11月26日  
 *@Version:1.1.0  
 */
@Api("image Controller")
@RestController
@RequestMapping("image")
public class ImageUploadController {
	private static final Logger logger = LoggerFactory.getLogger(ImageUploadController.class);
	@Autowired
	private PicPathVo picPathVo;
	
	@ApiOperation(value = "uploadPic", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "Success", response = ResponseObject.class)
	@RequestMapping(value = "/uploadPic", method = RequestMethod.POST)
	public ResponseObject<Map<String, Object>> uploadPic(
			@RequestParam(value="file",required=true) MultipartFile file//,
			/*@RequestParam(required=true,value="building") String building,
			@RequestParam(required=true,value="room") String room,
			@RequestParam(required=true,value="location") Integer location,
			@RequestParam(required=false,value="action",defaultValue="00") String action*/
			) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		try {
			if(file!=null){
				String filename = file.getName();
				filename = filename
						.substring(filename.lastIndexOf("\\") + 1);
				InputStream in = file.getInputStream();
				// 得到文件保存的名称
				String saveFilename = makeFileName(filename);
				// 得到文件的保存目录
				String realSavePath = makePath(saveFilename, picPathVo.getRealPath());
				// 创建一个文件输出流 在windows中使用\\ mac中用/

				FileOutputStream out = new FileOutputStream(realSavePath
						+ "/" + saveFilename);
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
				// 删除处理文件上传时生成的临时文件
			//	item.delete();
			}
		} catch (Exception e) {
			logger.error("===error msg:"+e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
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
		return RandomUtils.nextLong() + "_"
				+ MD5.getMD5Str(filename);
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
	private static String makePath(String filename, String savePath) {
		// 得到文件名的hashCode的值，得到的就是filename这个字符串对象在内存中的地址
		int hashcode = filename.hashCode();
		int dir1 = hashcode & 0xf; // 0--15
		int dir2 = (hashcode & 0xf0) >> 4; // 0-15
		// 构造新的保存目录 在windows中使用\\ mac中用/
		String dir = savePath + "/" + dir1 + "/" + dir2; // upload\2\3
															// upload\3\5
		// File既可以代表文件也可以代表目录
		File file = new File(dir);
		// 如果目录不存在
		if (!file.exists()) {
			// 创建目录
			file.mkdirs();
		}
		return dir;
	}
}
