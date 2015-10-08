package com.example.demo;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListImageAdapter extends BaseAdapter
{

	ArrayList<UserImage> arrayList;
	Context context;
	LayoutInflater inflater;
	
	public ListImageAdapter(ArrayList<UserImage> arrayList, Context context)
	{
		super();
		this.arrayList = arrayList;
		this.context = context;
		
		inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return arrayList.size();
	}

	@Override
	public UserImage getItem(int position)
	{
		// TODO Auto-generated method stub
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		ViewHolder holder;
		UserImage userImage = getItem(position);
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.list_child, parent, false);
			holder.imageView = (ImageView) convertView.findViewById(R.id.listchild_image);
			holder.imageNameTextView = (TextView) convertView.findViewById(R.id.listchild_name);
			
			convertView.setTag(holder);
			
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		holder.imageNameTextView.setText(userImage.getName());
		Bitmap bitmap = BitmapFactory.decodeByteArray(userImage.getImage(), 0, userImage.getImage().length);
		holder.imageView.setImageBitmap(bitmap);
		
		return convertView;
	}

	
	private class ViewHolder
	{
//		Bitmap bitmap;
		ImageView imageView;
		TextView imageNameTextView;
	}
}
