package br.com.ms.bean;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import br.com.ms.agendador.Agendador;

@ManagedBean
@ApplicationScoped
public class AgendadorDeTarefas implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2153789129877554744L;
	private boolean statusAtualizaNFE;
	SchedulerFactory schedulerFactory;
	Scheduler scheduler;
	Trigger trigger;

	public AgendadorDeTarefas() throws SchedulerException {
		try {
			schedulerFactory = new StdSchedulerFactory();
			scheduler = schedulerFactory.getScheduler();
			JobDetail job = JobBuilder.newJob(Agendador.class).withIdentity("atualizaNFE", "g1").build();
			trigger = TriggerBuilder.newTrigger().withIdentity("ValidadorTrigger", "g1").withSchedule(CronScheduleBuilder.cronSchedule("0 0/30 * * * ?")).build();
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			throw e;
		}
	}

	public void inicarAtualizacaoNFE() throws Exception {
		try {
			if (statusAtualizaNFE == true) {
				scheduler.start();
			} else {
				scheduler.standby();
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public boolean isStatusAtualizaNFE() {
		return statusAtualizaNFE;
	}

	public void setStatusAtualizaNFE(boolean statusAtualizaNFE) {
		this.statusAtualizaNFE = statusAtualizaNFE;
	}
}
