package com.xui.action;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xui.db.MusicDB;
import com.xui.utils.ConfigUtil;
import com.xui.utils.LogUtils;
import com.xui.utils.Utils;

public class UploadZipAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private File uploadFile;
	private String uploadFileContentType;  
	private String uploadFileFileName; 
	private static Map<String,String> configMap = Utils.getConfig();
	@Override
	public String execute() throws Exception {
		if(!verifyFile())return ERROR;//检查是否满足上传条件
		 //设置服务器文件目录 
		String realpath = configMap.get(ConfigUtil.UPLOADPATH)+createRandomDate();
		File saveFile = new File(realpath,uploadFileFileName);
		if(!saveFile.getParentFile().exists()){
			saveFile.mkdir();
		}
		//上传文件
		FileUtils.copyFile(uploadFile, saveFile);
		//解压缩文件
		Utils.uncompressZip(saveFile);
		//检查专辑是否存在,存在就保留临时文件，不进行数据库操作与文件复制操作
		if(AlbumIsExist(uploadFileFileName.substring(0, uploadFileFileName.lastIndexOf(".")),realpath)){
			return ERROR;
		}
		//得到解压后的文件
		String saveFilePath = saveFile.getPath();
		String afterZipPath = saveFilePath.substring(0, saveFilePath.lastIndexOf("."));  
		File afterZipFile = new File(afterZipPath);LogUtils.log(afterZipPath);
		//拷贝文件到指定的文件夹中，并保存到数据库
		List<String> list = Utils.checkAndCopyFile(afterZipFile, configMap.get(ConfigUtil.MUSICPATH), configMap.get(ConfigUtil.LOGOPATH));
		LogUtils.log("设置上传文件目录 ！"+realpath);
		StringBuffer sb = new StringBuffer();
		for(String s : list){
			LogUtils.log("复制文件时出现的错误信息："+s);
			sb.append(s+"\n");
		}
		if(list.size()>0){
			this.addFieldError("errorMessage", sb.toString());
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * 校验文件格式
	 * @return
	 */
	private boolean verifyFile(){
		if(configMap == null){
			this.addFieldError("errorMessage", "读取配置文件有误！");
			return false;
		}
		if(uploadFile == null){
			this.addFieldError("errorMessage", "没有得到文件！");
			return false;
		}
		if(StringUtils.isEmpty(uploadFileFileName)){
			this.addFieldError("errorMessage", "没有读取到文件内容！");
			return false;
		}else{
			//检查后缀名是否为zip或rar
			String suffixName = uploadFileFileName.substring(uploadFileFileName.lastIndexOf(".")+1, uploadFileFileName.length());
			if(!suffixName.equals("zip") && !suffixName.equals("rar")){
				this.addFieldError("errorMessage", "上传的文件格式不正确，必须是zip的压缩文件");
				return false;
			}
		}
		return true;
	}
	
	/***
	 * 检查专辑是否存在
	 * @param uploadFile
	 */
	private boolean AlbumIsExist(String name,String path){
		int id = new MusicDB().getMusicAlbumCountByName(name);
		if(id > 0){
			this.addFieldError("errorMessage", "上传成功，但专辑已存在，没有进行数据库操作。保存在服务器的文件地址为（"+path+"）");
			return true;
		}
		return false;
	}
	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}
	public void setUploadFileContentType(String uploadFileContentType) {
		this.uploadFileContentType = uploadFileContentType;
	}
	public void setUploadFileFileName(String uploadFileFileName) {
		this.uploadFileFileName = uploadFileFileName;
	}
	private String createRandomDate(){
		StringBuffer sb = new StringBuffer();
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		sb.append(df.format(date)+"-");
		Random random = new Random();
		sb.append(random.nextInt(100));
		sb.append(random.nextInt(100));
		sb.append(random.nextInt(100));
		return sb.toString();
	}
}







