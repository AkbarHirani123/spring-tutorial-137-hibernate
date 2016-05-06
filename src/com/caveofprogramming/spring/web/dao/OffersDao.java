package com.caveofprogramming.spring.web.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("offersDao")
public class OffersDao {

	private NamedParameterJdbcTemplate jdbc;

	@Autowired
	public void setDataSource(DataSource jdbc) {
		this.jdbc = new NamedParameterJdbcTemplate(jdbc);
	}

	public List<Offer> getOffers() {

		return jdbc
				.query("select * from offer, users where offer.username=users.username and users.enabled=true",
						new OfferRowMapper());
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

	public boolean create(Offer offer) {

		BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(
				offer);

		return jdbc.update(
				"insert into offer (text, username) values (:text, :username)",
				params) == 1;
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
