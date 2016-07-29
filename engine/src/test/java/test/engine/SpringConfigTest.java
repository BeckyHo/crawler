package test.engine;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringConfigTest {

	@Test
	public void testSpringConfig() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"applicationContext.xml");

		DataSource dataSource = (DataSource) ctx.getBean("dataSource");
		try {
			System.out.println(dataSource.getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
