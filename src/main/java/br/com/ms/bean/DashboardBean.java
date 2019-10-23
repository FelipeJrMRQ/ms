package br.com.ms.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import br.com.ms.model.Registro;

@ManagedBean
@ViewScoped
public class DashboardBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4892185373539113028L;

	private Registro registro;
	private LineChartModel lineModel1;

	public DashboardBean() {
		registro = new Registro();
		lineModel1  = new LineChartModel();
	}
	
	@PostConstruct
	private void iniciar() {
		criarLinhaModelo();
	}

	private LineChartModel iniciaModeloGrafico() {
		
		LineChartModel model = new LineChartModel();
		ChartSeries entradas = new ChartSeries();
		entradas.setLabel("Entradas");
		entradas.set("JAN", 50);
		entradas.set("FEV", 250);
		entradas.set("MAR", 20);
		entradas.set("ABR", 40);
		entradas.set("MAI", 10);

		ChartSeries saidas = new ChartSeries();
		saidas.setLabel("Saidas");
		saidas.set("JAN", 60);
		saidas.set("FEV", 200);
		saidas.set("MAR", 40);
		saidas.set("ABR", 70);
		saidas.set("MAI", 120);
		
		ChartSeries atendimentos = new ChartSeries();
		atendimentos.setLabel("Atendimentos");
		atendimentos.set("JAN", 12);
		atendimentos.set("FEV", 180);
		atendimentos.set("MAR", 5);
		atendimentos.set("ABR", 26);
		atendimentos.set("MAI", 2);
		
		model.addSeries(entradas);
		model.addSeries(saidas);
		model.addSeries(atendimentos);
		return model;
	}
	
	private void criarLinhaModelo() {
		lineModel1 = iniciaModeloGrafico();
		lineModel1.setTitle("Registros");
		lineModel1.setLegendPosition("e");
		lineModel1.setShowPointLabels(false);
		lineModel1.setSeriesColors("03a59d, b52805,d8c40a");
		lineModel1.getAxes().put(AxisType.X, new CategoryAxis("Meses"));
		Axis yAxis = lineModel1.getAxis(AxisType.Y);
		//yAxis.setLabel("Registro");
		yAxis.setMin(0);
		yAxis.setMax(350);
	}

	public Registro getRegistro() {
		return registro;
	}

	public void setRegistro(Registro registro) {
		this.registro = registro;
	}

	public void consultaQtdEntradas() {

	}

	public LineChartModel getLineModel1() {
		return lineModel1;
	}

	public void setLineModel1(LineChartModel lineModel1) {
		this.lineModel1 = lineModel1;
	}

}
