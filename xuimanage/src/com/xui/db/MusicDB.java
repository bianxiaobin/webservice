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
	 * ���musicName���ȴ���100�ͻع���������true������false��
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
		LogUtils.log(fileName+"����С-��"+musicFile.length());
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
	 * ���album_name���ȴ���50�ͻع���������true������false��
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
	 * ���������ݺ󣬼�ʱ�����������ID
	 * ����С��0˵��û�в鵽���
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


