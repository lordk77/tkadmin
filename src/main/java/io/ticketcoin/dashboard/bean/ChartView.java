package io.ticketcoin.dashboard.bean;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;
 
@ManagedBean
public class ChartView implements Serializable {
 
    private BarChartModel barModel;
    private HorizontalBarChartModel horizontalBarModel;
    private PieChartModel pieModel1;
    private PieChartModel pieModel2;
    private PieChartModel pieModel3;
    
    private LineChartModel lineModel1;
    private LineChartModel lineModel2;
    
    
    @PostConstruct
    public void init() {
        createBarModels();
        createPieModels();
        createLineModels();
    }
 
    public BarChartModel getBarModel() {
        return barModel;
    }
     
    public HorizontalBarChartModel getHorizontalBarModel() {
        return horizontalBarModel;
    }
 
    private BarChartModel initBarModel() {
        BarChartModel model = new BarChartModel();
 
        ChartSeries boys = new ChartSeries();
        boys.setLabel("Male");
        boys.set("2004", 120);
        boys.set("2005", 100);
        boys.set("2006", 44);
        boys.set("2007", 150);
        boys.set("2008", 25);
 
        ChartSeries girls = new ChartSeries();
        girls.setLabel("Female");
        girls.set("2004", 52);
        girls.set("2005", 60);
        girls.set("2006", 110);
        girls.set("2007", 135);
        girls.set("2008", 120);
 
        model.addSeries(boys);
        model.addSeries(girls);
         
        return model;
    }
     
    private void createBarModels() {
        createBarModel();
        createHorizontalBarModel();
    }
     
    private void createBarModel() {
        barModel = initBarModel();
         
        barModel.setTitle("Access by gender");
        barModel.setLegendPosition("ne");
         
        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Gender");
         
        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Accesses");
        yAxis.setMin(0);
        yAxis.setMax(200);
    }
     
    private void createHorizontalBarModel() {
        horizontalBarModel = new HorizontalBarChartModel();
 
        ChartSeries boys = new ChartSeries();
        boys.setLabel("Boys");
        boys.set("2014", 50);
        boys.set("2015", 96);
        boys.set("2016", 44);
        boys.set("2017", 55);
        boys.set("2018", 25);
 
        ChartSeries girls = new ChartSeries();
        girls.setLabel("Girls");
        girls.set("2014", 52);
        girls.set("2015", 60);
        girls.set("2016", 82);
        girls.set("2017", 35);
        girls.set("2018", 120);
 
        horizontalBarModel.addSeries(boys);
        horizontalBarModel.addSeries(girls);
         
        horizontalBarModel.setTitle("Stacked accesses");
        horizontalBarModel.setLegendPosition("e");
        horizontalBarModel.setStacked(true);
         
        Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
        xAxis.setLabel("Access");
        xAxis.setMin(0);
        xAxis.setMax(200);
         
        Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
        yAxis.setLabel("Gender");        
    }

    
    
    private void createPieModels() {
        createPieModel1();
        createPieModel2();
        createPieModel3();
    }
 
    private void createPieModel1() {
        pieModel1 = new PieChartModel();
         
        pieModel1.set("America", 540);
        pieModel1.set("Asia", 325);
        pieModel1.set("Europe", 702);
        pieModel1.set("China", 421);
         
        pieModel1.setTitle("Customers by area");
        pieModel1.setLegendPosition("w");
    }
     
    private void createPieModel2() {
        pieModel2 = new PieChartModel();
         
        pieModel2.set("<18", 120);
        pieModel2.set("18-25", 325);
        pieModel2.set("25-50", 460);
        pieModel2.set("50+", 360);
         
        pieModel2.setTitle("Customers by age");
        pieModel2.setLegendPosition("w");
    }

    private void createPieModel3() {
    	pieModel3 = new PieChartModel();
         
        pieModel3.set("Male", 540);
        pieModel3.set("Female", 421);
         
        pieModel3.setTitle("Customers gender");
        pieModel3.setLegendPosition("w");
    }
    
    
    
    private void createLineModels() {
        lineModel1 = initLinearModel();
        lineModel1.setTitle("Linear Chart");
        lineModel1.setLegendPosition("e");
        Axis yAxis = lineModel1.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(10);
         
        lineModel2 = initCategoryModel();
        lineModel2.setTitle("Category Chart");
        lineModel2.setLegendPosition("e");
        lineModel2.setShowPointLabels(true);
        lineModel2.getAxes().put(AxisType.X, new CategoryAxis("Years"));
        yAxis = lineModel2.getAxis(AxisType.Y);
        yAxis.setLabel("Births");
        yAxis.setMin(0);
        yAxis.setMax(200);
    }
    
    
    private LineChartModel initLinearModel() {
        LineChartModel model = new LineChartModel();
 
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Series 1");
 
        series1.set(1, 2);
        series1.set(2, 1);
        series1.set(3, 3);
        series1.set(4, 6);
        series1.set(5, 8);
 
        LineChartSeries series2 = new LineChartSeries();
        series2.setLabel("Series 2");
 
        series2.set(1, 6);
        series2.set(2, 3);
        series2.set(3, 2);
        series2.set(4, 7);
        series2.set(5, 9);
 
        model.addSeries(series1);
        model.addSeries(series2);
         
        return model;
    }
     
    private LineChartModel initCategoryModel() {
        LineChartModel model = new LineChartModel();
 
        ChartSeries boys = new ChartSeries();
        boys.setLabel("Boys");
        boys.set("2014", 120);
        boys.set("2015", 100);
        boys.set("2016", 44);
        boys.set("2017", 150);
        boys.set("2018", 25);
 
        ChartSeries girls = new ChartSeries();
        girls.setLabel("Girls");
        girls.set("2014", 52);
        girls.set("2015", 60);
        girls.set("2016", 110);
        girls.set("2017", 90);
        girls.set("2018", 120);
 
        model.addSeries(boys);
        model.addSeries(girls);
         
        return model;
    }
    
	public PieChartModel getPieModel1() {
		return pieModel1;
	}

	public void setPieModel1(PieChartModel pieModel1) {
		this.pieModel1 = pieModel1;
	}

	public PieChartModel getPieModel2() {
		return pieModel2;
	}

	public void setPieModel2(PieChartModel pieModel2) {
		this.pieModel2 = pieModel2;
	}

	public LineChartModel getLineModel1() {
		return lineModel1;
	}

	public void setLineModel1(LineChartModel lineModel1) {
		this.lineModel1 = lineModel1;
	}

	public LineChartModel getLineModel2() {
		return lineModel2;
	}

	public void setLineModel2(LineChartModel lineModel2) {
		this.lineModel2 = lineModel2;
	}

	public PieChartModel getPieModel3() {
		return pieModel3;
	}

	public void setPieModel3(PieChartModel pieModel3) {
		this.pieModel3 = pieModel3;
	}
 
}