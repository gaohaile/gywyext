package com.mvc.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mvc.dao.AlarmLogDao;
import com.mvc.dao.EquipMainDao;
import com.mvc.entityReport.EquipMain;
import com.mvc.entityReport.Equipment;

@Repository("alarmLogDaoImpl")
public class AlarmLogDaoImpl implements AlarmLogDao {

	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;

	@Override
	public Integer countEquipFailNumById(Integer equipId) {
		// TODO Auto-generated method stub
		EntityManager em = emf.createEntityManager();
		String selectSql = "select count(*) from alarm_log where equipment=:equip_id";
		Query query = em.createNativeQuery(selectSql);
		query.setParameter("equip_id", equipId);
		List<Object> totalRow = query.getResultList();
		em.close();
		return Integer.parseInt(totalRow.get(0).toString());
	}

	@Override
	public Integer countEquipFailNumByIdAndDate(Integer equipId, Date date) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		EntityManager em = emf.createEntityManager();
		String selectSql = "";
		Query query;
		if (date == null) {
			selectSql = "select count(*) from alarm_log where equipment=:equip_id ";
			query = em.createNativeQuery(selectSql);
			query.setParameter("equip_id", equipId);
		} else {
			selectSql = "select count(*) from alarm_log where equipment=:equip_id and  alarm_log_date>:date";
			query = em.createNativeQuery(selectSql);
			query.setParameter("equip_id", equipId);
			query.setParameter("date", date);
		}

		List<Object> totalRow = query.getResultList();
		em.close();
		return Integer.parseInt(totalRow.get(0).toString());
	}

	@Override
	public List<Object> getEquipFailCountById(Integer equipId, String year) {
		// TODO Auto-generated method stub
		EntityManager em = emf.createEntityManager();
		String selectSql = "select DATE_FORMAT(a.alarm_log_date,'%m') months,count(*) num from alarm_log a where equipment=:equipId and YEAR(alarm_log_date)=:year group by months;";
		Query query = em.createNativeQuery(selectSql);
		query.setParameter("equipId", equipId);
		query.setParameter("year", year);
		List<Object> list = query.getResultList();
		em.close();
		return list;
	}

	@Override
	public Integer getEquipAlarmNumByProId(Integer proId) {
		// TODO Auto-generated method stub

		EntityManager em = emf.createEntityManager();
		String selectSql = "";
		Query query;
		if (proId == null) {
			return null;
		}
		selectSql = "select count(*) from alarm_log where equipment in (select equip_id from equipment where equip_isdeleted='0' and equip_room in (select equip_room_id from equip_room where proj_id =:proj_id)) ";
		query = em.createNativeQuery(selectSql);
		query.setParameter("proj_id", proId);
		List<Object> totalRow = query.getResultList();
		em.close();
		return Integer.parseInt(totalRow.get(0).toString());

	}

}
