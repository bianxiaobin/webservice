package com.xui.db;

import java.io.File;

import org.apache.ibatis.session.SqlSession;

import com.xui.bean.Music;
import com.xui.bean.MusicAlbum;
import com.xui.utils.DBHelp;
import com.xui.utils.LogUtils;

public class MusicDB {
	int maxId = 0;
	SqlSession session = DBHelp.getSqlSession();
	/**
	 * 如果musicName长度大于100就回滚，并返回true，否则false；
	 * @param music
	 * @return
	 */
	public boolean insertMusic(File musicFile){
		String fileName = musicFile.getName();
		fileName = fileName.substring(0, fileName.lastIndexOf("."));
		Music music = new Music();
		music.setMusic_album_id(maxId);
		music.setMusic_name(fileName);
		music.setMusic_size(musicFile.length());
		LogUtils.log(fileName+"：大小-》"+musicFile.length());
		String mapper = "com.xui.bean.mapper.musicMapper.insertMusic";
		session.insert(mapper, music);
		if(music.getMusic_name().length() > 100){
			session.rollback(true);
			return true;
		}
		session.commit();
		return false;
	}
	/**
	 * 如果album_name长度大于50就回滚，并返回true，否则false；
	 * @param music
	 * @return
	 */
	public boolean insertMusicAlbum(File albumFile){
		MusicAlbum album = new MusicAlbum();
		String fileName = albumFile.getName();
		album.setMusic_album_desc(fileName);
		album.setMusic_album_name(fileName);
		album.setMusic_album_lable(fileName);
		
		String mapper = "com.xui.bean.mapper.musicAlbumMapper.insertMusicAlbum";
		session.insert(mapper, album);
		if(album.getMusic_album_name().length() > 50){
			session.rollback(true);
			return true;
		}
		session.commit();
		maxId = getMusicAlbumMaxId();
		return false;
	}
	public int getMaxId() {
		return maxId;
	}
	/**
	 * 当插入数据后，及时获得最大的主键ID
	 * 返回小于0说明没有查到结果
	 * @return
	 */
	public int getMusicAlbumMaxId(){
		int id = -1;
		String mapper = "com.xui.bean.mapper.musicAlbumMapper.getMusicAlbumId";
		id = session.selectOne(mapper);
		return id;
	}
	public int getMusicAlbumCountByName(String name){
		int id = -1;
		String mapper = "com.xui.bean.mapper.musicAlbumMapper.getMusicAlbumCountByName";
		id = session.selectOne(mapper,name);
		return id;
	}
}


