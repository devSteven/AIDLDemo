package com.ylbf.aidlplayerclient;

import com.ylbf.aidlplayerserver.IRemoteServiice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * 客户端控制界面
 * 
 * @author ylbf
 * @version 2016-02-18 15:44:45
 */
public class MainActivity extends Activity implements OnClickListener {
	public static final String TAG = "MainActivity";

	/**
	 * 服务端 AndroidManifest.xml中的intent-filter action声明的字符串
	 */
	public static final String ACTION = "com.ylbf.aidlplayerserver.PlayerService";
	private IRemoteServiice mService;
	private boolean isBinded = false;
	private Button playbtn, stopbtn;

	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			isBinded = false;
			mService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = IRemoteServiice.Stub.asInterface(service);
			isBinded = true;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		doBind();
		initViews();
	}

	private void initViews() {
		playbtn = (Button) findViewById(R.id.button1);
		stopbtn = (Button) findViewById(R.id.button2);
		playbtn.setOnClickListener(this);
		stopbtn.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		doUnbind();
		super.onDestroy();
	}

	/**
	 * 绑定服务
	 */
	public void doBind() {
		Intent intent = new Intent(ACTION);
		bindService(intent, conn, Context.BIND_AUTO_CREATE);
	}

	/**
	 * 解绑服务
	 */
	public void doUnbind() {
		if (isBinded) {
			unbindService(conn);
			mService = null;
			isBinded = false;
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == playbtn.getId()) {
			// play
			Log.i(TAG, "play button clicked");
			try {
				if (mService != null) {
					mService.play();
				} else {
					Toast.makeText(this, "服务连接失败", Toast.LENGTH_SHORT).show();
					Log.e(TAG, "请安装服务");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// stop
			Log.i(TAG, "stop button clicked");
			try {
				if (mService != null) {
					mService.stop();
				} else {
					Toast.makeText(this, "服务连接失败", Toast.LENGTH_SHORT).show();
					Log.e(TAG, "请安装服务");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
