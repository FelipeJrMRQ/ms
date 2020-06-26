package br.com.ms.util;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;

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


@ApplicationScoped
public class ScheduleUtil implements Serializable{

	private static final long serialVersionUID = -6711809756096409570L;
	private static  SchedulerFactory schedulerFactory;
	private static Scheduler scheduler;
	private static Trigger trigger;
	private static boolean status = true;

	
	private static void createSchedule() {
		try {
			schedulerFactory = new StdSchedulerFactory();
			scheduler = schedulerFactory.getScheduler();
			JobDetail job = JobBuilder.newJob(Agendador.class).withIdentity("atualizaNFE", "g1").build();
			trigger = TriggerBuilder.newTrigger().withIdentity("ValidadorTrigger", "g1").withSchedule(CronScheduleBuilder.cronSchedule("0 0/30 * * * ?")).build();
			scheduler.scheduleJob(job, trigger);
			status = false;
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}


	public static void start() throws Exception {
		try {
			if(status) {
				createSchedule();
				scheduler.start();
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public static void pause() throws Exception {
		try {
			scheduler.standby();
		} catch (Exception e) {
			throw e;
		}
	}
}
