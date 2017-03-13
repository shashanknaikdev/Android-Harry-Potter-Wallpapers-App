package com.ssn.harry.potter.wallpapers.app;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	// PERSISTENT VARAIBLES
	private static Bitmap the_one_bitmap;

	private static int res_id;
	private static int posi;
	private static final int SWIPE_MIN_DISTANCE = 80;
	private static final int SWIPE_THRESHOLD_VELOCITY = 160;

	// RECREATED ON ACTIVITY START,CHANGE IN ORIENTATION

	private Gallery gallery; // the gallery widget on top in portrait mode
	private ImageView imageView; // the same imageview is used across the
									// portrait and the landscape modes
	private ImageAdapter ia;
	final static BitmapFactory.Options options = new BitmapFactory.Options();

	int[] IMAGE_IDS_FULL = {

			R.drawable.fhp_1, R.drawable.fhp_5, R.drawable.fhp_3,
			R.drawable.fhp_4, R.drawable.fhp_2, R.drawable.fhp_6,
			R.drawable.fhp_7, R.drawable.fhp_8, R.drawable.fhp_9,
			R.drawable.fhp_10, R.drawable.fhp_11, R.drawable.fhp_12,
			R.drawable.fhp_13, R.drawable.fhp_14, R.drawable.fhp_15,
			R.drawable.fhp_16, R.drawable.fhp_17, R.drawable.fhp_18,
			R.drawable.fhp_19, R.drawable.fhp_20, R.drawable.fhp_21,
			R.drawable.fhp_22, R.drawable.fhp_23, R.drawable.fhp_24,
			R.drawable.fhp_25, R.drawable.fhp_26, R.drawable.fhp_27,
			R.drawable.fhp_28, R.drawable.fhp_29, R.drawable.fhp_30,
			

	};

	// //////////////////////////--------ONCREATE--------////////////////////////////////////////
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		options.inSampleSize = (2);

		try { // /////START TRY
			imageView = (ImageView) findViewById(R.id.ImageView01);
			gallery = (Gallery) findViewById(R.id.gallery);
			ia = new ImageAdapter(this);
			gallery.setAdapter(ia); // THE GALLERY WILL BE SET UP NOW

			if (res_id != 0) // res_id will be zero only when app is first
								// loaded.
			{
				gallery.setSelection(posi);
				the_one_bitmap = BitmapFactory.decodeResource(getResources(),
						res_id, options);
				imageView.setImageBitmap(the_one_bitmap);
			} else {
				the_one_bitmap = BitmapFactory.decodeResource(getResources(),
						IMAGE_IDS_FULL[0], options);
				imageView.setImageBitmap(the_one_bitmap);
			}

			gallery.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {

					if (the_one_bitmap != null) {
						the_one_bitmap.recycle();
					}

					posi = arg2;
					res_id = IMAGE_IDS_FULL[arg2];
					the_one_bitmap = BitmapFactory.decodeResource(
							getResources(), res_id, options);
					imageView.setImageBitmap(the_one_bitmap);
				}
			});

		}// //////END TRY
		catch (Exception e)// //////START CATCH MEANS YOU ARE IN LANDSCAPE MODE
		{
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN); // Remove the
																	// status
																	// bar

			Toast.makeText(this, "Swipe Right/Left", Toast.LENGTH_SHORT).show();

			final GestureDetector gdt = new GestureDetector(
					new GestureListener());

			if (res_id == 0) { // This means that the app is loaded in landscape
								// mode itself
				res_id = IMAGE_IDS_FULL[0];
			}
			the_one_bitmap = BitmapFactory.decodeResource(getResources(),
					res_id, options);
			imageView = (ImageView) findViewById(R.id.ImageView01);
			imageView.setImageBitmap(the_one_bitmap);
			// imageView.setImageResource(IMAGE_IDS_FULL[posi]);

			imageView.setOnTouchListener(new OnTouchListener() {// NEW BLOCK
						public boolean onTouch(final View view,
								final MotionEvent event) {
							gdt.onTouchEvent(event);
							return true;
						}
					});

		}// //////END CATCH

	} // //////-----------END ONCREATE--------//////////////////////

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.layout.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_save:
			if (the_one_bitmap != null) {
				the_one_bitmap.recycle();
			}
			the_one_bitmap = BitmapFactory.decodeResource(getResources(),
					IMAGE_IDS_FULL[posi]);
			String filename = "hp_" + res_id + ".jpg";
			File sd = Environment.getExternalStorageDirectory();
			String full_path = sd.getAbsolutePath() + "/HPWallpapers/";
			File temp = new File(full_path);
			temp.mkdirs();
			File dest = new File(temp, filename);
			try {
				FileOutputStream out = new FileOutputStream(dest);
				the_one_bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
				Toast.makeText(this, "Saved in Folder " + full_path,
						Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
			}
			return true;

		case R.id.menu_set_wallpapaer:
			final WallpaperManager wallpaperManager = WallpaperManager
					.getInstance(MainActivity.this);
			// mBitmap = BitmapFactory.decodeResource(getResources(),res_id);
			try {
				wallpaperManager.setResource(IMAGE_IDS_FULL[posi]);
				Toast.makeText(this, "Wallpaper Set", Toast.LENGTH_SHORT)
						.show();
			} catch (Exception e) {
				Toast.makeText(this, "error setting wallpaper",
						Toast.LENGTH_SHORT).show();

			}
			return true;

		case R.id.menu_help:
			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.dialogs);

			TextView text = (TextView) dialog.findViewById(R.id.TextView01);

			dialog.setCancelable(true);
			dialog.setTitle("About/Donate");
			text.setText(R.string.dialog_text);
			dialog.show();
			Button cancel = (Button) dialog.findViewById(R.id.button_cancel);
			Button donate = (Button) dialog.findViewById(R.id.button_donate);
			cancel.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

					dialog.dismiss();

				}

			});
			donate.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					dialog.dismiss();
					Intent internetIntent = new Intent(Intent.ACTION_VIEW, Uri
							.parse("http://goo.gl/okxtm"));

					// internetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(internetIntent);

				}

			});

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// NESTED CLASS GESTURELISTENER BEGINS FROM HERE
	private class GestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

				if (posi == 29) {
					posi = 0;
				} else {
					posi++;
				}
				if (the_one_bitmap != null) {
					the_one_bitmap.recycle();
				}
				res_id = IMAGE_IDS_FULL[posi];
				the_one_bitmap = BitmapFactory.decodeResource(getResources(),
						res_id, options);
				imageView.setImageBitmap(the_one_bitmap);

				return false; // Right to left
			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

				if (posi == 0) {
					posi = 29;
				} else {
					posi--;
				}
				if (the_one_bitmap != null) {
					the_one_bitmap.recycle();
				}
				res_id = IMAGE_IDS_FULL[posi];
				the_one_bitmap = BitmapFactory.decodeResource(getResources(),
						res_id, options);
				imageView.setImageBitmap(the_one_bitmap);

				return false; // Left to right
			}

			return false;
		}
	}

	@Override
	protected void onDestroy() {

		if (the_one_bitmap != null) {
			the_one_bitmap.recycle();

		}
		super.onDestroy();
	}

	// NESTED CLASS IMAGEADAPTER BEGINS FROM HERE
	private class ImageAdapter extends BaseAdapter {
		private Context context;
		int res;
		//Bitmap gall_bmp;
		

		private ImageAdapter(Context context) {
			this.context = context;
		}

		private int[] IMAGE_IDS = {


				R.drawable.hp_1, R.drawable.hp_5, R.drawable.hp_3,
				R.drawable.hp_4, R.drawable.hp_2, R.drawable.hp_6,
				R.drawable.hp_7, R.drawable.hp_8, R.drawable.hp_9,
				R.drawable.hp_10, R.drawable.hp_11, R.drawable.hp_12,
				R.drawable.hp_13, R.drawable.hp_14, R.drawable.hp_15,
				R.drawable.hp_16, R.drawable.hp_17, R.drawable.hp_18,
				R.drawable.hp_19, R.drawable.hp_20, R.drawable.hp_21,
				R.drawable.hp_22, R.drawable.hp_23, R.drawable.hp_24,
				R.drawable.hp_25, R.drawable.hp_26, R.drawable.hp_27,
				R.drawable.hp_28, R.drawable.hp_29, R.drawable.hp_30,
				

		};

		public int getCount() {
			return IMAGE_IDS.length;
		}

		public Object getItem(int position) {
			return IMAGE_IDS[position];
		}

		public long getItemId(int position) {
			return IMAGE_IDS[position];
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			ImageView imageView;
			if (convertView == null) {
				imageView = new ImageView(context);

				imageView.setLayoutParams(new Gallery.LayoutParams(120, 120));
				imageView.setAdjustViewBounds(true);

				// imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

			} else {
				imageView = (ImageView) convertView;
			}
			res = IMAGE_IDS[position];

			//gall_bmp = BitmapFactory.decodeResource(
				//	this.context.getResources(), res);

			imageView.setImageDrawable(context.getResources().getDrawable(res));//(gall_bmp);

			return imageView;

		}
	}

}
