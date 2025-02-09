package com.x.teamwork.assemble.control;

import com.x.base.core.project.Context;
import com.x.teamwork.assemble.control.queue.QueueBatchOperation;
import com.x.teamwork.assemble.control.service.SystemConfigPersistService;
import com.x.teamwork.assemble.control.timertask.Timertask_BatchOperationTask;
import com.x.teamwork.assemble.control.timertask.Timertask_RefreshAllTaskReview;

public class ThisApplication {
	
	protected static Context context;
	public static QueueBatchOperation queueBatchOperation;
	
	public static Context context() {
		return context;
	}

	public static void init() {
		try {
			//执行数据库中的批处理操作
			queueBatchOperation = new QueueBatchOperation();
			context().startQueue( queueBatchOperation );
			new SystemConfigPersistService().initSystemConfig();
			
			//每隔5分钟检查是否有未完成的批处理工作需要完成以及是否有未review的task需要核对权限信息
			context.schedule( Timertask_BatchOperationTask.class, "0 */5 * * * ?" );
			//每天凌晨把所有项目的所有工作任务的权限和review信息核对一次
			context.schedule( Timertask_RefreshAllTaskReview.class, "0 0 2 * * ?" );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void destroy() {
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
