package io.ticketcoin.dashboard.persistence.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.query.Query;

import io.ticketcoin.dashboard.dto.StatsDTO;
import io.ticketcoin.dashboard.utils.HibernateUtils;

public class StatsDAO {

	public StatsDTO getGenericStats(Long organizationId) {

		StatsDTO statsDTO = new StatsDTO();
		
		Query q = HibernateUtils.getSessionFactory().getCurrentSession()
				.createSQLQuery(
						
						"SELECT  " + 
				
						// customers
						"count(distinct tk.OWNED_BY) as unique_customers,  " + 
						"count(distinct tk.OWNED_BY) - count(distinct case when tk.ENROLLED_ON < NOW() - INTERVAL 30 DAY then tk.OWNED_BY else null end) as unique_customers_last_month, " + 
						 
						//tickets 
						"sum(ALLOWED_ENTRANCES) as sold_tickets,  " + 
						"sum(case when tk.ENROLLED_ON BETWEEN NOW() - INTERVAL 30 DAY AND NOW() then ALLOWED_ENTRANCES else 0 end) as sold_tickets_last_month, " + 
						 
						// incomes 
						"sum(tc.streetPrice) as total_incomes,  " + 
						"sum(case when tk.ENROLLED_ON BETWEEN NOW() - INTERVAL 30 DAY AND NOW() then tc.streetPrice else 0 end) as total_incomes_last_month " + 
						 
						"FROM TICKET tk " + 
						"	join TICKET_CATEGORY tc on tc.id=tk.CATEGORY_ID " + 
						"	join TDEF_EVENT ev on ev.id=tc.EVENT_ID " + 
						"	join ORGANIZATION org on ev.ORGANIZATION_ID = org.id " + 
						"	join TDEF_USER u on u.id=tk.OWNED_BY " + 
						"where " + 
						"	tk.TICKET_STATE <> 2 " +
						(organizationId !=null ? " and org.id = " + organizationId : "")
						);
				
				

		List<Object[]> ids = q.getResultList();
		
		
		for(Object[] n: ids)
		{
				statsDTO.setAllCustomers(((Number)n[0]).longValue());
				statsDTO.setNewCustomers(((Number)n[1]).longValue());
				
				
				statsDTO.setAllTickets(((Number)n[2]).longValue());
				statsDTO.setNewTickets(((Number)n[3]).longValue());	
				
				statsDTO.setAllIncomes(new BigDecimal(((Number)n[4]).doubleValue()).setScale(2));
				statsDTO.setNewIncomes(new BigDecimal(((Number)n[5]).doubleValue()).setScale(2));					
			
		}
		
		return statsDTO;
	}

	
	
	public Map<Date, Long> getTicketTimeSeries(Long organizationId)
	{
		Map<Date, Long> timeSeries  = new HashMap<Date, Long>();
		
		Query q = HibernateUtils.getSessionFactory().getCurrentSession()
				.createSQLQuery(
					"SELECT  " + 
					"	cast(tk.ENROLLED_ON as date), " + 
					"    sum(tk.ALLOWED_ENTRANCES) " + 
					"FROM TICKET tk  " + 
					"	join TICKET_CATEGORY tc on tc.id=tk.CATEGORY_ID  " + 
					"	join TDEF_EVENT ev on ev.id=tc.EVENT_ID " + 
					"	join ORGANIZATION org on ev.ORGANIZATION_ID = org.id " + 
					"where  " + 
					"	tk.TICKET_STATE <> 2 " +
					(organizationId !=null ? " and org.id = " + organizationId + " " : "") +
					"    and tk.ENROLLED_ON BETWEEN NOW() - INTERVAL 30 DAY AND NOW() " + 
					"group by " + 
					"	cast(tk.ENROLLED_ON as date) " +
					" order by 1 asc");
		
		
		List<Object[]> ids = q.getResultList();
		
		
		for(Object[] n: ids)
		{
			timeSeries.put(((Date)n[0]), ((Number)n[1]).longValue());
		}
		
		return timeSeries; 
	}



}
