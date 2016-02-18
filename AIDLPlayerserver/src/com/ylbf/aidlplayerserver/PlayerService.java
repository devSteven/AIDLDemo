package com.ylbf.aidlplayerserver;

import java.io.FileDescriptor;
import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

/**
 * @category 播放音乐的服务
 * @author ylbf
 * @version 2016-02-18 14:35:48
 */
public class PlayerService extends Service {
	public static final String TAG = "PlayerService";
	private MediaPlayer mPlayer;
	private IBinder mBinder = new IRemoteServiice.Stub() {

		@Override
		public void stop() throws RemoteException {
			try {
				if (mPlayer.isPlaying()) {
					mPlayer.stop();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void play() throws RemoteException {
			try {
				if (mPlayer.isPlaying()) {
					return;
				}
				// start之前需要prepare。
				// 如果前面实例化mplayer时使用方法一，则第一次play的时候直接start，不用prepare。
				// 但是stop一次之后,再次play就需要在start之前prepare了。
				// 前面使用方法二 这里就简便了， 不用判断各种状况
				mPlayer.prepare();
				mPlayer.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	public IBinder onBind(Intent arg0) {
		Log.i(TAG, "service onbind");
		if (mPlayer == null) {
			// 方法一说明
			// 此方法实例化播放器的同时指定音乐数据源 ,若用此方法在，mPlayer.start()
			// 之前不需再调用mplayer.prepare()
			// 官方文档有说明 ：On success, prepare() will already have been called and
			// must not be called again.
			// 译文：一旦create成功，prepare已被调用，勿再调用 。查看源代码可知create方法内部已经调用prepare方法。
			// 方法一开始
			// mPlayer = MediaPlayer.create(this, R.raw.monody);
			// 方法一结束

			// 方法二说明
			// 若用此方法，在mplayer.start() 之前需要调用mplayer.prepare()
			// 方法二开始
			mPlayer = new MediaPlayer();
			try {
				FileDescriptor fd = getResources().openRawResourceFd(R.raw.monody).getFileDescriptor(); // 获取音乐数据源
				mPlayer.setDataSource(fd); // 设置数据源
				mPlayer.setLooping(true); // 设为循环播放
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 方法二结束
			Log.i(TAG, "player created");
		}
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		if (mPlayer != null) {
			mPlayer.release();
		}
		Log.i(TAG, "service onUnbind");
		return super.onUnbind(intent);
	}

}
