package com.engine.fileprocessor.impl;

import java.io.File;

import com.engine.bean.Resource;
import com.engine.fileprocessor.FileProcessor;

public class HBUTxyzpFileProcessor implements FileProcessor {

	public static final String POST_TIME = "����ʱ��";
	public static final String COMPANY_NAME = "��˾����";
	public static final String HOLD_TIME = "��Ƹʱ��";
	public static final String HOLD_PLACE = "��Ƹ�ص�";
	public static final String END_INDEX = "רҵ��¼ѡ��";
	public static final String CHARSET = "UTF-8";

	@Override
	public void process(File destFile, Resource resource, String url) {

	}
}
