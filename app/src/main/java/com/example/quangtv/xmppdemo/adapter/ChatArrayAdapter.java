package com.example.quangtv.xmppdemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.quangtv.xmppdemo.entity.MessageInfo;
import com.example.quangtv.xmppdemo.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChatArrayAdapter extends BaseAdapter {

	private TextView chatText;
	private List<MessageInfo> chatMessageList = new ArrayList<MessageInfo>();

	private List<MessageInfo> mEntries = new ArrayList<MessageInfo>();
	private LayoutInflater mLayoutInflater;
	private Context mContext;


	public ChatArrayAdapter(Context context, List<MessageInfo> entries){
		this.mEntries = entries;
		this.mContext = context;
		this.mLayoutInflater = LayoutInflater.from(context);
	}



	@Override
	public int getCount() {
		return this.mEntries.size();
	}

	@Override
	public MessageInfo getItem(int position) {
		return this.mEntries.get(position);
	}

	@Override
	public long getItemId(int position) {
		return (long) position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mViewHolder;

		if(convertView == null){
			mViewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.activity_chat_singlemessage,parent,false);
			mViewHolder.singleMessageContainer = (LinearLayout) convertView.findViewById(R.id.singleMessageContainer);
			mViewHolder.txtMessage = (TextView) convertView.findViewById(R.id.txtMessage);
			mViewHolder.imgMessage = (ImageView) convertView.findViewById(R.id.imgMessage);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder = (ViewHolder) convertView.getTag();
		}

		MessageInfo items = getItem(position);
		if (items != null){

			if (items.getType().equalsIgnoreCase("image")) {
				mViewHolder.imgMessage.setTag(items.getBody());
				new LoadImage(mViewHolder.imgMessage).execute();
				mViewHolder.imgMessage.setVisibility(View.VISIBLE);
				mViewHolder.txtMessage.setVisibility(View.GONE);
			} else if (items.getType().equalsIgnoreCase("text")){
				mViewHolder.txtMessage.setText(items.getBody());
				mViewHolder.imgMessage.setVisibility(View.GONE);
				mViewHolder.txtMessage.setVisibility(View.VISIBLE);

			}

			if (items.isUser()) {

				mViewHolder.txtMessage.setBackgroundResource(R.mipmap.bubble_a);
				mViewHolder.imgMessage.setBackgroundResource(R.mipmap.bubble_a);
				mViewHolder.singleMessageContainer.setGravity(Gravity.RIGHT);
			} else if (!items.isUser()){
				mViewHolder.txtMessage.setBackgroundResource(R.mipmap.bubble_b);
				mViewHolder.imgMessage.setBackgroundResource(R.mipmap.bubble_b);
				mViewHolder.singleMessageContainer.setGravity(Gravity.LEFT);
			}



		}

		return convertView;
	}



	private static class ViewHolder {
		private TextView txtMessage;
		private ImageView imgMessage;
		private LinearLayout singleMessageContainer;
	}

	public static Bitmap decodeScaledBitmapFromSdCard(String filePath,
													  int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	public static int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}



	class LoadImage extends AsyncTask<Object, Void, Bitmap> {

		private ImageView imv;
		private String path;

		public LoadImage(ImageView imv) {
			this.imv = imv;
			this.path = imv.getTag().toString();
		}

		@Override
		protected Bitmap doInBackground(Object... params) {

			Bitmap bitmap = decodeScaledBitmapFromSdCard(path, 300, 300);

			return bitmap;
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			if (!imv.getTag().toString().equals(path)) {
               /* The path is not same. This means that this
                  image view is handled by some other async task.
                  We don't do anything and return. */
				return;
			}

			if(result != null && imv != null){
				imv.setVisibility(View.VISIBLE);
				imv.setImageBitmap(result);
			}else{
				imv.setVisibility(View.GONE);
			}
		}

	}

}