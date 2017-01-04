package com.my.app.task;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.my.util.TimeUtil;
public class WxUpdateTask extends Thread{
	@Override
	public void run() {
		super.run();
		try{
		sleep(20000);
		while(true)
		{
			
			long thetime=System.currentTimeMillis()-60000L;			
			Db.update("update  follow_task set finish_quantity=real_quantity where (real_quantity>(finish_quantity-20)) or (finish_quantity>=total_quantity and lasttime<"+thetime+")");	
			//System.out.println("f1:"+f1);
			sleep(10000);
			Db.update("update  follow_task set status=1,finish_time='"+TimeUtil.GetSqlDate(System.currentTimeMillis())+"' where real_quantity>=total_quantity and status=0 ");	
			//System.out.println("f2:"+f2);
			sleep(10000);
			Db.update("update  read_task set finish_quantity=real_quantity,finish_praise=real_praise where   (real_quantity>(finish_quantity-15)) or (finish_quantity>=total_quantity and lasttime<"+thetime+")");
			//System.out.println("f3:"+f3);
			sleep(10000);
			Db.update("update  read_task set status=1,finish_time='"+TimeUtil.GetSqlDate(System.currentTimeMillis())+"'  where total_quantity<=real_quantity and push_quantity>=total_quantity and real_quantity>=total_quantity and status=0 ");	
			//System.out.println("f4:"+f4);
			sleep(10000);
			List<Record> list=Db.find("select * from read_task where settle=1");
			for(int i=0;i<list.size();i++)
			{
				Record record=list.get(i);
				Db.save("read_task_settlement", record);
				Db.delete("read_task", record);
			}
			sleep(10000);
		}
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}		
	}

}
