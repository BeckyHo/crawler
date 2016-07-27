package com.engine.db;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectionManager {

	private static ConnectionManager instance;
	private ComboPooledDataSource cpds;

	private ConnectionManager() {
		cpds = new ComboPooledDataSource();
		try {
			// 设置数据库驱动
			cpds.setDriverClass("com.mysql.jdbc.Driver");
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}

		// 设置数据库url
		cpds.setJdbcUrl("jdbc:mysql://localhost:3306/crawler");
		// 用户名和密码
		cpds.setUser("root");
		cpds.setPassword("root");
		// 设置初始化链接数
		cpds.setInitialPoolSize(1);
		// 设置最大连接数
		cpds.setMaxPoolSize(2);
		// 链接耗尽时c3p0一次同时获取的连接数
		cpds.setAcquireIncrement(1);
		// 检查空闲连接间隔, 单位秒
		cpds.setIdleConnectionTestPeriod(60);
		// 链接最大空闲时间, 单位秒
		cpds.setMaxIdleTime(60);
		// 链接关闭时是否将所有未提交的操作回滚
		cpds.setAutoCommitOnClose(true);
		// 定义所有链接测试执行的sql语句
		cpds.setPreferredTestQuery("SELECT id FROM User");
		// 链接提交时校验其有效性?
		cpds.setTestConnectionOnCheckout(true);
		// 获取链接的同时校验其有效性?
		cpds.setTestConnectionOnCheckin(true);
		// 获取链接失败后尝试获取的次数
		cpds.setAcquireRetryAttempts(30);
	}

	public static synchronized ConnectionManager getInstance() {
		if (instance == null) {
			instance = new ConnectionManager();
		}

		return instance;
	}

	public Connection getConnection() {
		try {
			return cpds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void close() {
		cpds.close();
	}
}
