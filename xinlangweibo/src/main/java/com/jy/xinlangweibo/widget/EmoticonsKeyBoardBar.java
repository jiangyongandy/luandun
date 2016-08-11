package com.jy.xinlangweibo.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jy.xinlangweibo.bean.EmoticonBean;
import com.jy.xinlangweibo.utils.EmoticonsKeyboardBuilder;
import com.jy.xinlangweibo.utils.Utils;
import com.jy.xinlangweibo.widget.EmoticonsEditText;
import com.jy.xinlangweibo.widget.EmoticonsIndicatorView;
import com.jy.xinlangweibo.widget.EmoticonsPageView;
import com.jy.xinlangweibo.widget.EmoticonsToolBarView;
import com.jy.xinlangweibo.widget.I.IEmoticonsKeyboard;
import com.jy.xinlangweibo.widget.I.IView;
import com.jy.xinlangweibo.R;

public class EmoticonsKeyBoardBar extends LinearLayout implements
		IEmoticonsKeyboard, EmoticonsToolBarView.OnToolBarItemClickListener {

	public static int FUNC_CHILLDVIEW_EMOTICON = 0;
	public static int FUNC_CHILLDVIEW_APPS = 1;
	public int mChildViewPosition = -1;

	private EmoticonsPageView mEmoticonsPageView;
	private EmoticonsIndicatorView mEmoticonsIndicatorView;
	private EmoticonsToolBarView mEmoticonsToolBarView;

	private LinearLayout ly_foot_func;
	private boolean mIsMultimediaVisibility = true;
	private EmoticonsEditText et_chat;

	public EmoticonsKeyBoardBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_keyboardbar, this);
		initView();
	}
	public void setEt_chat(EmoticonsEditText et_chat) {
		this.et_chat = et_chat;
	}
	private void initView() {
		mEmoticonsPageView = (EmoticonsPageView) findViewById(R.id.view_epv);
		mEmoticonsIndicatorView = (EmoticonsIndicatorView) findViewById(R.id.view_eiv);
		mEmoticonsToolBarView = (EmoticonsToolBarView) findViewById(R.id.view_etv);

		ly_foot_func = (LinearLayout) findViewById(R.id.ly_foot_func);

		mEmoticonsPageView
				.setOnIndicatorListener(new EmoticonsPageView.OnEmoticonsPageViewListener() {
//Viewpage显示第一个表情集时EmoticonsIndicatorView被调用的初始化
					@Override
					public void emoticonsPageViewInitFinish(int count) {
						mEmoticonsIndicatorView.init(count);
					}
//Viewpage的页码改变时被调用
					@Override
					public void emoticonsPageViewCountChanged(int count) {
						mEmoticonsIndicatorView.setIndicatorCount(count);
					}
//Viewpage在切换到其它表情集时EmoticonsIndicatorView被调用的动画？
					@Override
					public void playTo(int position) {
						mEmoticonsIndicatorView.playTo(position);
					}
//Viewpage在本集合中切换时EmoticonsIndicatorView被调用的动画？
					@Override
					public void playBy(int oldPosition, int newPosition) {
						mEmoticonsIndicatorView
								.playBy(oldPosition, newPosition);
					}
				});

		mEmoticonsPageView.setIViewListener(new IView() {
			//适配器的内容被点击时调用
			@Override
			public void onItemClick(EmoticonBean bean) {
				if (et_chat != null) {
					et_chat.setFocusable(true);
					et_chat.setFocusableInTouchMode(true);
					et_chat.requestFocus();

					// 删除
					if (bean.getEventType() == EmoticonBean.FACE_TYPE_DEL) {
						int action = KeyEvent.ACTION_DOWN;
						int code = KeyEvent.KEYCODE_DEL;
						KeyEvent event = new KeyEvent(action, code);
						et_chat.onKeyDown(KeyEvent.KEYCODE_DEL, event);
						return;
					}
					// 用户自定义
					else if (bean.getEventType() == EmoticonBean.FACE_TYPE_USERDEF) {
						return;
					}

					int index = et_chat.getSelectionStart();
					Editable editable = et_chat.getEditableText();
					if (index < 0) {
						editable.append(bean.getContent());
					} else {
						editable.insert(index, bean.getContent());
					}
				}
			}
			//适配器的的单个内容显示时调用
			@Override
			public void onItemDisplay(EmoticonBean bean) {
			}
			//Viewpage在切换到其它表情集时被调用
			@Override
			public void onPageChangeTo(int position) {
				mEmoticonsToolBarView.setToolBtnSelect(position);
			}
		});

		mEmoticonsToolBarView
				.setOnToolBarItemClickListener(new EmoticonsToolBarView.OnToolBarItemClickListener() {
					@Override
					public void onToolBarItemClick(int position) {
						mEmoticonsPageView.setPageSelect(position);
					}
				});
//
//		et_chat.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if (!et_chat.isFocused()) {
//					et_chat.setFocusable(true);
//					et_chat.setFocusableInTouchMode(true);
//				}
//				return false;
//			}
//		});
//		et_chat.setOnFocusChangeListener(new OnFocusChangeListener() {
//			@Override
//			public void onFocusChange(View view, boolean b) {
//				if (b) {
//					setEditableState(true);
//				} else {
//					setEditableState(false);
//				}
//			}
//		});
	}

//	private void setEditableState(boolean b) {
//		if (b) {
//			et_chat.setFocusable(true);
//			et_chat.setFocusableInTouchMode(true);
//			et_chat.requestFocus();
//		} else {
//			et_chat.setFocusable(false);
//			et_chat.setFocusableInTouchMode(false);
//		}
//	}

	public EmoticonsToolBarView getEmoticonsToolBarView() {
		return mEmoticonsToolBarView;
	}

	public EmoticonsPageView getEmoticonsPageView() {
		return mEmoticonsPageView;
	}

	public EmoticonsEditText getEt_chat() {
		return et_chat;
	}

	public void addToolView(int icon) {
		if (mEmoticonsToolBarView != null && icon > 0) {
			mEmoticonsToolBarView.addData(icon);
		}
	}

	public void addFixedView(View view, boolean isRight) {
		if (mEmoticonsToolBarView != null) {
			mEmoticonsToolBarView.addFixedView(view, isRight);
		}
	}

	public void clearEditText() {
		if (et_chat != null) {
			et_chat.setText("");
		}
	}

	public void del() {
		if (et_chat != null) {
			int action = KeyEvent.ACTION_DOWN;
			int code = KeyEvent.KEYCODE_DEL;
			KeyEvent event = new KeyEvent(action, code);
			et_chat.onKeyDown(KeyEvent.KEYCODE_DEL, event);
		}
	}

	@Override
	public void setBuilder(EmoticonsKeyboardBuilder builder) {
//		把表情集合设置给EmoticonsPageView，EmoticonsToolBarView
		mEmoticonsPageView.setBuilder(builder);
		mEmoticonsToolBarView.setBuilder(builder);
	}



	public void add(View view) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		ly_foot_func.addView(view, params);
	}

	KeyBoardBarViewListener mKeyBoardBarViewListener;

	public void setOnKeyBoardBarViewListener(KeyBoardBarViewListener l) {
		this.mKeyBoardBarViewListener = l;
	}

	@Override
	public void onToolBarItemClick(int position) {

	}

	public interface KeyBoardBarViewListener {
		public void OnKeyBoardStateChange(int state, int height);

		public void OnSendBtnClick(String msg);

		public void OnVideoBtnClick();

		public void OnMultimediaBtnClick();
	}
}
