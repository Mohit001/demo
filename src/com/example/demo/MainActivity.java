package com.example.demo;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener
{
	public static String TAG = "MainActivity";
	private Context mContext;

	Button browseButton, displayButton, saveButton;
	ImageView imageView;
	EditText imageNameEditText;

	private int pic_image = 100;
	private int capture_image = 101;

	Bitmap bitmap;
	String filePathString;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;

		getActionBar().hide();

		browseButton = (Button) findViewById(R.id.browse_image);
		displayButton = (Button) findViewById(R.id.display_image);
		saveButton = (Button) findViewById(R.id.save_image);
		imageView = (ImageView) findViewById(R.id.capture_image);
		imageNameEditText = (EditText) findViewById(R.id.image_name_txt);

		browseButton.setOnClickListener(this);
		displayButton.setOnClickListener(this);
		saveButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.browse_image:
			final Dialog dialog = new Dialog(mContext);
			dialog.setCanceledOnTouchOutside(true);
			dialog.setContentView(R.layout.dialog);

			Button cameraButton = (Button) dialog.findViewById(R.id.camera_btn);
			Button galleryButton = (Button) dialog.findViewById(R.id.gallery_btn);

			cameraButton.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(cameraIntent, capture_image);

					dialog.dismiss();
				}
			});

			// if button is clicked, close the custom dialog
			galleryButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{

					Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
					photoPickerIntent.setType("image/*");
					startActivityForResult(photoPickerIntent, pic_image);

					dialog.dismiss();
				}
			});

			dialog.show();

			break;

		case R.id.display_image:
			startActivity(new Intent(mContext, ListImage.class));
			break;

		case R.id.save_image:
			String imageName = imageNameEditText.getText().toString().trim();
			if (TextUtils.isEmpty(imageName))
			{
				Toast.makeText(mContext, "Please enter image name", Toast.LENGTH_SHORT).show();
				return;
			}

			FileInputStream instream;
			try
			{
				instream = new FileInputStream(filePathString);
				BufferedInputStream bif = new BufferedInputStream(instream);
				byte[] byteImage1 = new byte[bif.available()];
				bif.read(byteImage1);

				Database database = new Database(mContext);
				Boolean result = database.insertIamgeToDatabse(imageName, byteImage1);
				if (result)
				{
					Toast.makeText(mContext, "Image saved successfully", Toast.LENGTH_SHORT).show();
				} else
				{
					Toast.makeText(mContext, "Porblme occure during saveing image to database", Toast.LENGTH_SHORT).show();
				}

			} catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(mContext, "Image not found or move", Toast.LENGTH_SHORT).show();
				return;
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(mContext, "Can not read image", Toast.LENGTH_SHORT).show();
				return;
			}

			imageView.setVisibility(View.GONE);
			imageNameEditText.setVisibility(View.GONE);
			saveButton.setVisibility(View.GONE);

			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		imageView.setVisibility(View.VISIBLE);
		imageNameEditText.setVisibility(View.VISIBLE);
		saveButton.setVisibility(View.VISIBLE);

		if (resultCode == RESULT_OK && requestCode == pic_image)
		{
			Uri selectedMedia = data.getData();

			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedMedia, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex;
			columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			filePathString = cursor.getString(columnIndex);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			if (bitmap != null && !bitmap.isRecycled())
			{
				bitmap = null;
			}

			bitmap = BitmapFactory.decodeFile(filePathString);
			bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, false);

			Log.d(TAG, filePathString);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			imageView.setImageBitmap(bitmap);
			cursor.close();

		} else
		{
			bitmap = (Bitmap) data.getExtras().get("data");
			Uri uri = getImageUri(mContext, bitmap);
			filePathString = getRealPathFromURI(uri);

			imageView.setImageBitmap(bitmap);

		}
	}

	// As I noticed in some device after captured image
	// the data in onActivityResult() is null,
	// so i take different approch

	public Uri getImageUri(Context inContext, Bitmap inImage)
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
		return Uri.parse(path);
	}

	public String getRealPathFromURI(Uri uri)
	{
		Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		cursor.moveToFirst();
		int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
		return cursor.getString(idx);
	}
}
