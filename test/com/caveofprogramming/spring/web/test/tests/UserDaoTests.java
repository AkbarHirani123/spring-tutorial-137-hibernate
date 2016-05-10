package com.caveofprogramming.spring.web.test.tests;

import static org.junit.Assert.*;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.caveofprogramming.spring.web.dao.User;
import com.caveofprogramming.spring.web.dao.UsersDao;

@ActiveProfiles("dev")
@ContextConfiguration(locations = {
		"classpath:com/caveofprogramming/spring/web/config/dao-context.xml",
		"classpath:com/caveofprogramming/spring/web/config/security-context.xml",
		"classpath:com/caveofprogramming/spring/web/test/config/datasource.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class UserDaoTests {

	@Autowired
	private UsersDao userDao;

	@Autowired
	private DataSource dataSource;

	User user1 = new User("akbarhirani", "akbar hirani", "123456",
			"akbar@hirani.com", true, "ROLE_USER");
	User user2 = new User("deanwinchester", "dean winchester", "123456",
			"akbar@hirani.com", true, "ROLE_ADMIN");
	User user3 = new User("barryallen", "barry allen", "123456",
			"akbar@hirani.com", true, "ROLE_USER");
	User user4 = new User("oliverqueen", "oliver queen", "123456",
			"akbar@hirani.com", true, "user");

	@Before
	public void init() {
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);

		jdbc.execute("delete from offer");
		jdbc.execute("delete from users");
	}

	@Test // create user
	public void testCreate() {
		userDao.create(user1);
		
		List<User> users1 = userDao.getAllUsers();

		assertEquals("Number of users should be 1", 1, users1.size());
		
		assertEquals("Inserted user should match retreuved",user1, users1.get(0));

		userDao.create(user2);
		userDao.create(user3);
		userDao.create(user4);
		
		List<User> users2 = userDao.getAllUsers();

		assertEquals("Number of users should be 1",4, users2.size());
	
	}
	
	@Test
	public void testExists(){
		userDao.create(user1);
		
		assertTrue("User should exist.", userDao.exists(user1.getUsername()));
		assertFalse("User should not exist.", userDao.exists("123"));
		
	}
	@Test // Reimplement this
	public void testCreateUser() {
		User user = new User("akbar123", "Akbar Hirani", "123456",
				"akbar@123.com", true, "ROLE_USER");

		userDao.create(user);

		List<User> users = userDao.getAllUsers();

		assertEquals("Number of users should be 1", 1, users.size());

		assertTrue("User should exist.", userDao.exists(user.getUsername()));

		assertEquals("Created user should be identical to retreived user",
				user, users.get(0));

	}

}
