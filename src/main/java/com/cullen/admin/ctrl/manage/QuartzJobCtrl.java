package com.cullen.admin.ctrl.manage;

import com.cullen.admin.constant.Constant;
import com.cullen.admin.exception.TatuException;
import com.cullen.admin.utils.ResultUtil;
import com.cullen.admin.vo.Result;
import com.cullen.admin.base.BaseCtrl;
import com.cullen.admin.server.system.entity.QuartzJob;
import com.cullen.admin.server.system.service.QuartzJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@Slf4j
@RestController
@Api("定时任务管理接口")
@RequestMapping("/api/quartzJob")
@Transactional
public class QuartzJobCtrl extends BaseCtrl {

    @Autowired
    private QuartzJobService quartzJobService;

    @Autowired
    private Scheduler scheduler;

    public static Job getClass(String classname) throws Exception {
        Class<?> class1 = Class.forName(classname);
        return (Job) class1.newInstance();
    }

    @GetMapping(value = "/getAllByPage")
    @ApiOperation(value = "获取所有定时任务")
    public Result<Object> getAll() {

        startPage();
        List<QuartzJob> list = quartzJobService.findByCondition();
        return new ResultUtil<>().setData(getDataTable(list));
    }

    @PostMapping(value = "/add")
    @ApiOperation(value = "添加定时任务")
    public Result<Object> addJob(@ModelAttribute QuartzJob job) {

        List<QuartzJob> list = quartzJobService.findByJobClassName(job.getJobClassName());
        if (list != null && list.size() > 0) {
            return new ResultUtil<Object>().setErrorMsg("该定时任务类名已存在");
        }
        add(job.getJobClassName(), job.getCronExpression(), job.getParameter());
        quartzJobService.save(job);
        return new ResultUtil<Object>().setSuccessMsg("创建定时任务成功");
    }

    @PostMapping(value = "/edit")
    @ApiOperation(value = "更新定时任务")
    public Result<Object> editJob(@ModelAttribute QuartzJob job) {

        delete(job.getJobClassName());
        add(job.getJobClassName(), job.getCronExpression(), job.getParameter());
        job.setStatus(Constant.STATUS_NORMAL);
        quartzJobService.update(job);
        return new ResultUtil<Object>().setSuccessMsg("更新定时任务成功");
    }

    @PostMapping(value = "/pause")
    @ApiOperation(value = "暂停定时任务")
    public Result<Object> pauseJob(@ModelAttribute QuartzJob job) {

        try {
            scheduler.pauseJob(JobKey.jobKey(job.getJobClassName()));
        } catch (SchedulerException e) {
            throw new TatuException("暂停定时任务失败");
        }
        job.setStatus(Constant.STATUS_DISABLE);
        quartzJobService.update(job);
        return new ResultUtil<Object>().setSuccessMsg("暂停定时任务成功");
    }

    @PostMapping(value = "/resume")
    @ApiOperation(value = "恢复定时任务")
    public Result<Object> resumeJob(@ModelAttribute QuartzJob job) {

        try {
            scheduler.resumeJob(JobKey.jobKey(job.getJobClassName()));
        } catch (SchedulerException e) {
            throw new TatuException("恢复定时任务失败");
        }
        job.setStatus(Constant.STATUS_NORMAL);
        quartzJobService.update(job);
        return new ResultUtil<Object>().setSuccessMsg("恢复定时任务成功");
    }

    @DeleteMapping(value = "/delByIds/{ids}")
    @ApiOperation(value = "删除定时任务")
    public Result<Object> deleteJob(@PathVariable String[] ids) {

        for (String id : ids) {
            QuartzJob job = quartzJobService.get(id);
            delete(job.getJobClassName());
            quartzJobService.delete(job.getId());
        }
        return new ResultUtil<Object>().setSuccessMsg("删除定时任务成功");
    }

    /**
     * 添加定时任务
     *
     * @param jobClassName
     * @param cronExpression
     * @param parameter
     */
    public void add(String jobClassName, String cronExpression, String parameter) {

        try {
            // 启动调度器
            scheduler.start();

            //构建job信息
            JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass())
                    .withIdentity(jobClassName)
                    .usingJobData("parameter", parameter)
                    .build();

            //表达式调度构建器(即任务执行的时间) 使用withMisfireHandlingInstructionDoNothing() 忽略掉调度暂停过程中没有执行的调度
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing();

            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName)
                    .withSchedule(scheduleBuilder).build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error(e.toString());
            throw new TatuException("创建定时任务失败");
        } catch (Exception e) {
            throw new TatuException("后台找不到该类名任务");
        }
    }

    /**
     * 删除定时任务
     *
     * @param jobClassName
     */
    public void delete(String jobClassName) {

        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName));
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName));
            scheduler.deleteJob(JobKey.jobKey(jobClassName));
        } catch (Exception e) {
            throw new TatuException("删除定时任务失败");
        }
    }

}
