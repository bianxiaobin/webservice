package com.xui.bean;

import java.util.Date;

public class MusicAlbum {
	private int music_album_id;
	private String music_album_name;
	private String music_album_lable;
	private String music_album_desc;
	private Date db_createtime;
	private Date db_updatetime;
	private int tinyint;
	public int getMusic_album_id() {
		return music_album_id;
	}
	public void setMusic_album_id(int music_album_id) {
		this.music_album_id = music_album_id;
	}
	public String getMusic_album_name() {
		return music_album_name;
	}
	public void setMusic_album_name(String music_album_name) {
		this.music_album_name = music_album_name;
	}
	public String getMusic_album_lable() {
		return music_album_lable;
	}
	public void setMusic_album_lable(String music_album_lable) {
		this.music_album_lable = music_album_lable;
	}
	public String getMusic_album_desc() {
		return music_album_desc;
	}
	public void setMusic_album_desc(String music_album_desc) {
		this.music_album_desc = music_album_desc;
	}
	public Date getDb_createtime() {
		return db_createtime;
	}
	public void setDb_createtime(Date db_createtime) {
		this.db_createtime = db_createtime;
	}
	public Date getDb_updatetime() {
		return db_updatetime;
	}
	public void setDb_updatetime(Date db_updatetime) {
		this.db_updatetime = db_updatetime;
	}
	public int getTinyint() {
		return tinyint;
	}
	public void setTinyint(int tinyint) {
		this.tinyint = tinyint;
	}
	
	
}
