package com.engine.fileprocessor;

import java.io.File;

import com.engine.bean.Resource;

@FunctionalInterface
public interface FileProcessor {

	public void process(File destFile, Resource resource, String url);
}
