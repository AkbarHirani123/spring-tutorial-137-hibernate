package com.caveofprogramming.spring.web.dao;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@Component("offersDao")
public class OffersDao {

	private NamedParameterJdbcTemplate jdbc;

	@Autowired
	private SessionFactory sessionFactory;

	public Session session() {
		return sessionFactory.getCurrentSession();
	}

	@Autowired
	public void setDataSource(DataSource jdbc) {
		this.jdbc = new NamedParameterJdbcTemplate(jdbc);
	}

	public List<Offer> getOffers() {
		Criteria crit = session().createCriteria(Offer.class);
		
		crit.createAlias("user", "u").add(Restrictions.eq("u.enabled", true));
		
		return crit.list();
	}

	public List<Offer> getOffers(String username) {

		return jdbc
				.query("select * from offer, users where offer.username=users.username and users.enabled=true and offer.username=:username",
						new MapSqlParameterSource("username", username),
						new OfferRowMapper());
	}

	public boolean update(Offer offer) {
		BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(
				offer);

		return jdbc.update("update offer set text=:text where id=:id", params) == 1;
	}

	public void create(Offer offer) {

		session().save(offer);
	}

	@Transactional
	public int[] create(List<Offer> offers) {

		SqlParameterSource[] params = SqlParameterSourceUtils
				.createBatch(offers.toArray());

		return jdbc.batchUpdate(
				"insert into offer (text, username) values (:text, :username)",
				params);
	}

	public boolean delete(int id) {
		MapSqlParameterSource params = new MapSqlParameterSource("id", id);

		return jdbc.update("delete from offer where id=:id", params) == 1;
	}

	public Offer getOffer(int id) {

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		return jdbc
				.queryForObject(
						"select * from offer, users where offer.id=:id and offer.username=users.username and users.enabled=true",
						params, new OfferRowMapper());
	}

}
