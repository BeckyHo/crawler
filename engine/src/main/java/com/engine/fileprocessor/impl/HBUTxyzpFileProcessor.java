package com.engine.fileprocessor.impl;

import java.io.File;

import com.engine.bean.Resource;
import com.engine.fileprocessor.FileProcessor;

public class HBUTxyzpFileProcessor implements FileProcessor {

	public static final String POST_TIME = "发布时间";
	public static final String COMPANY_NAME = "公司名称";
	public static final String HOLD_TIME = "招聘时间";
	public static final String HOLD_PLACE = "招聘地点";
	public static final String END_INDEX = "专业招录选项";
	public static final String CHARSET = "UTF-8";

	@Override
	public void process(File destFile, Resource resource, String url) {

	}
}
