package com.engine.writer;

import java.io.File;

public interface WriteChainAnalyzer {

	public void analysisFile(File destFile, String url);

	public void initialize(ExtendsConfigration configration);
}
