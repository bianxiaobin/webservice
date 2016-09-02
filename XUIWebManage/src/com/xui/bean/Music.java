package com.xui.bean;

import java.util.Date;

public class Music {
	private long music_id;
	private String music_name;
	private int singer_id;
	private long music_size;
	private int music_time;
	private int music_album_id;
	private String music_path;
	private Date db_createtime;
	public long getMusic_id() {
		return music_id;
	}
	public void setMusic_id(long music_id) {
		this.music_id = music_id;
	}
	public String getMusic_name() {
		return music_name;
	}
	public void setMusic_name(String music_name) {
		this.music_name = music_name;
	}
	public int getSinger_id() {
		return singer_id;
	}
	public void setSinger_id(int singer_id) {
		this.singer_id = singer_id;
	}
	public long getMusic_size() {
		return music_size;
	}
	public void setMusic_size(long music_size) {
		this.music_size = music_size;
	}
	public int getMusic_time() {
		return music_time;
	}
	public void setMusic_time(int music_time) {
		this.music_time = music_time;
	}
	public int getMusic_album_id() {
		return music_album_id;
	}
	public void setMusic_album_id(int music_album_id) {
		this.music_album_id = music_album_id;
	}
	public String getMusic_path() {
		return music_path;
	}
	public void setMusic_path(String music_path) {
		this.music_path = music_path;
	}
	public Date getDb_createtime() {
		return db_createtime;
	}
	public void setDb_createtime(Date db_createtime) {
		this.db_createtime = db_createtime;
	}
	
}
