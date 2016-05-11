package com.caveofprogramming.spring.web.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@Component("usersDao")
public class UsersDao {

//	private NamedParameterJdbcTemplate jdbc;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private SessionFactory sessionFactory;
	
	public Session session() {
		return sessionFactory.getCurrentSession();
	}

//	@Autowired
//	public void setDataSource(DataSource jdbc) {
//		this.jdbc = new NamedParameterJdbcTemplate(jdbc);
//	}

	@Transactional
	public void create(User user) {

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		session().save(user);
	}

	public boolean exists(String username) {
		// return
		// jdbc.queryForObject("select count(*) from users where username =:username",
		// new MapSqlParameterSource("username", username), Integer.class) > 0;
		Criteria crit = session().createCriteria(User.class);
		crit.add(Restrictions.idEq(username));
		User user = (User)crit.uniqueResult();
		
		return user != null;
	}

	@SuppressWarnings("unchecked")
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		// return
		// jdbc.query("select * from users",BeanPropertyRowMapper.newInstance(User.class));
		return session().createQuery("from User").list();
	}
}
