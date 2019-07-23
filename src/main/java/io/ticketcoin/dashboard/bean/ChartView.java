package io.ticketcoin.dashboard.bean;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

import io.ticketcoin.dashboard.dto.StatsDTO;
import io.ticketcoin.dashboard.persistence.service.EventService;
import io.ticketcoin.dashboard.persistence.service.StatsService;
 
@ManagedBean
public class ChartView implements Serializable {
 

    
    private LineChartModel ticketsSold;
    
    private StatsDTO kpis;
    
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    
    
	@ManagedProperty(value="#{userBean}")
	private UserBean userBean;
    
    
    @PostConstruct
    public void init() {
        createLineModels();
        initKpis();
    }
 

    private void createLineModels() {

        ticketsSold = initTicketsModel();
        ticketsSold.setTitle("Tickets sold on past 30 days");
        ticketsSold.setLegendPosition("e");
        ticketsSold.setShowPointLabels(true);
        ticketsSold.getAxes().put(AxisType.X, new CategoryAxis("Date"));
        Axis yAxis = ticketsSold.getAxis(AxisType.Y);
        yAxis.setLabel("Tickets");
        yAxis.setMin(0);
        yAxis.setMax(200);
    }
    
    
   
     
    private LineChartModel initTicketsModel() {
        LineChartModel model = new LineChartModel();
 
        ChartSeries tickets = new ChartSeries();
        tickets.setLabel("Tickets");
        
        Map<Date, Long> timeSeries = new StatsService().getTicketTimeSeries(userBean.getLoggedUser().isAdmin() ? null : userBean.getLoggedUser().getOrganization()!=null? userBean.getLoggedUser().getOrganization().getId():-1l);
        
        for (Date date : timeSeries.keySet())
        {
        	if(date!=null)
        		tickets.set(sdf.format(date), timeSeries.get(date));
        }

        
        model.addSeries(tickets);

        return model;
    }
    
    
    private void initKpis()
    {
    	this.kpis=new StatsService().getGenericStats(userBean.getLoggedUser().isAdmin() ? null : userBean.getLoggedUser().getOrganization()!=null? userBean.getLoggedUser().getOrganization().getId():-1l);
    }
    
    
    
	public StatsDTO getKpis() {
		return kpis;
	}

	public void setKpis(StatsDTO kpis) {
		this.kpis = kpis;
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}


	public LineChartModel getTicketsSold() {
		return ticketsSold;
	}


	public void setTicketsSold(LineChartModel ticketsSold) {
		this.ticketsSold = ticketsSold;
	}
 
}