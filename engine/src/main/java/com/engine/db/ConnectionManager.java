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
			// �������ݿ�����
			cpds.setDriverClass("com.mysql.jdbc.Driver");
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}

		// �������ݿ�url
		cpds.setJdbcUrl("jdbc:mysql://localhost:3306/crawler");
		// �û���������
		cpds.setUser("root");
		cpds.setPassword("root");
		// ���ó�ʼ��������
		cpds.setInitialPoolSize(1);
		// �������������
		cpds.setMaxPoolSize(2);
		// ���Ӻľ�ʱc3p0һ��ͬʱ��ȡ��������
		cpds.setAcquireIncrement(1);
		// ���������Ӽ��, ��λ��
		cpds.setIdleConnectionTestPeriod(60);
		// ����������ʱ��, ��λ��
		cpds.setMaxIdleTime(60);
		// ���ӹر�ʱ�Ƿ�����δ�ύ�Ĳ����ع�
		cpds.setAutoCommitOnClose(true);
		// �����������Ӳ���ִ�е�sql���
		cpds.setPreferredTestQuery("SELECT id FROM User");
		// �����ύʱУ������Ч��?
		cpds.setTestConnectionOnCheckout(true);
		// ��ȡ���ӵ�ͬʱУ������Ч��?
		cpds.setTestConnectionOnCheckin(true);
		// ��ȡ����ʧ�ܺ��Ի�ȡ�Ĵ���
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
