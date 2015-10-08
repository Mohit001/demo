package com.example.demo;

import java.util.Arrays;

public class UserImage
{
	String name;
	byte[] image;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public byte[] getImage()
	{
		return image;
	}

	public void setImage(byte[] image)
	{
		this.image = image;
	}

	@Override
	public String toString()
	{
		return "UserImage [name=" + name + ", image=" + Arrays.toString(image) + "]";
	}

}
