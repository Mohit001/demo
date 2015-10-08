package com.example.demo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

public class ListImage extends Activity
{
	ListView listView;
	ArrayList<UserImage> arrayList;
	ListImageAdapter adapter;
	Database database;
	Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listimage);
		
		getActionBar().hide();
		
		mContext = this;
		database = new Database(mContext);
		
		listView = (ListView) findViewById(R.id.listview);
		
		arrayList = database.getImageFromDatabase();
		adapter = new ListImageAdapter(arrayList, mContext);
		listView.setAdapter(adapter);
		
	}

}
