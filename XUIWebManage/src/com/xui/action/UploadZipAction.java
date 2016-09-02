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
		if(!verifyFile())return ERROR;//����Ƿ������ϴ�����
		 //���÷������ļ�Ŀ¼ 
		String realpath = configMap.get(ConfigUtil.UPLOADPATH)+createRandomDate();
		File saveFile = new File(realpath,uploadFileFileName);
		if(!saveFile.getParentFile().exists()){
			saveFile.mkdir();
		}
		//�ϴ��ļ�
		FileUtils.copyFile(uploadFile, saveFile);
		//��ѹ���ļ�
		Utils.uncompressZip(saveFile);
		//���ר���Ƿ����,���ھͱ�����ʱ�ļ������������ݿ�������ļ����Ʋ���
		if(AlbumIsExist(uploadFileFileName.substring(0, uploadFileFileName.lastIndexOf(".")),realpath)){
			return ERROR;
		}
		//�õ���ѹ����ļ�
		String saveFilePath = saveFile.getPath();
		String afterZipPath = saveFilePath.substring(0, saveFilePath.lastIndexOf("."));  
		File afterZipFile = new File(afterZipPath);LogUtils.log(afterZipPath);
		//�����ļ���ָ�����ļ����У������浽���ݿ�
		List<String> list = Utils.checkAndCopyFile(afterZipFile, configMap.get(ConfigUtil.MUSICPATH), configMap.get(ConfigUtil.LOGOPATH));
		LogUtils.log("�����ϴ��ļ�Ŀ¼ ��"+realpath);
		StringBuffer sb = new StringBuffer();
		for(String s : list){
			LogUtils.log("�����ļ�ʱ���ֵĴ�����Ϣ��"+s);
			sb.append(s+"\n");
		}
		if(list.size()>0){
			this.addFieldError("errorMessage", sb.toString());
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * У���ļ���ʽ
	 * @return
	 */
	private boolean verifyFile(){
		if(configMap == null){
			this.addFieldError("errorMessage", "��ȡ�����ļ�����");
			return false;
		}
		if(uploadFile == null){
			this.addFieldError("errorMessage", "û�еõ��ļ���");
			return false;
		}
		if(StringUtils.isEmpty(uploadFileFileName)){
			this.addFieldError("errorMessage", "û�ж�ȡ���ļ����ݣ�");
			return false;
		}else{
			//����׺���Ƿ�Ϊzip��rar
			String suffixName = uploadFileFileName.substring(uploadFileFileName.lastIndexOf(".")+1, uploadFileFileName.length());
			if(!suffixName.equals("zip") && !suffixName.equals("rar")){
				this.addFieldError("errorMessage", "�ϴ����ļ���ʽ����ȷ��������zip��ѹ���ļ�");
				return false;
			}
		}
		return true;
	}
	
	/***
	 * ���ר���Ƿ����
	 * @param uploadFile
	 */
	private boolean AlbumIsExist(String name,String path){
		int id = new MusicDB().getMusicAlbumCountByName(name);
		if(id > 0){
			this.addFieldError("errorMessage", "�ϴ��ɹ�����ר���Ѵ��ڣ�û�н������ݿ�����������ڷ��������ļ���ַΪ��"+path+"��");
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







