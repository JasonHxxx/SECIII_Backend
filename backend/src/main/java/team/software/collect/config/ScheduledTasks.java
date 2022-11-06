package team.software.collect.config;

import io.swagger.models.auth.In;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team.software.collect.enums.Device;
import team.software.collect.mapperservice.report.ReportMapper;
import team.software.collect.mapperservice.user.PortraitMapper;
import team.software.collect.po.report.Report;
import team.software.collect.po.user.Portrait;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
@Configuration     //主要用于标记配置类，兼备Component的效果
@EnableScheduling
public class ScheduledTasks {
    @Resource
    private PortraitMapper portraitMapper;
    @Resource
    private ReportMapper reportMapper;

    //添加定时任务,每天凌晨0点更新一次
    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(fixedRate=5000)  //测试：每隔5s执行一次
    private void configureTasks1(){
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
        List<Portrait> portraits=portraitMapper.selectAll();
        for(Portrait portrait:portraits){
            BigDecimal preNum=portrait.getActivity();
            BigDecimal postNum=preNum.subtract(new BigDecimal(0.1));
            portrait.setActivity(postNum);
            portraitMapper.updateByPrimaryKey(portrait);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(fixedRate=5000)
    private void configureTasks2(){
        Date time=new Date();
        List<Report> reports=reportMapper.selectAll();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(Report report:reports){
            Date reportDate=report.getCreatTime();
            try {
                Date d1 = time;
                Date d2 = reportDate;

                //毫秒ms
                long diff = d1.getTime() - d2.getTime();

                long diffSeconds = diff / 1000 % 60;
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                long diffDays = diff / (24 * 60 * 60 * 1000);

                System.out.print("两个时间相差：");
                System.out.print(diffDays + " 天, ");
                System.out.print(diffHours + " 小时, ");
                System.out.print(diffMinutes + " 分钟, ");
                System.out.print(diffSeconds + " 秒.");
                System.out.println();
                if(diffDays==30){//如果设置为>会连续减去很多次，出错
                    Portrait portrait=portraitMapper.selectByUid(report.getUid());
                    //todo，将这个任务对应的设备从portrait里减去
                    String devices=portrait.getDevice();
                    String[] strs=devices.split(",");
                    HashMap<String, Integer> map=new HashMap<>();
                    map.put(Device.Android.toString(),0);
                    map.put(Device.Harmony.toString(),1);
                    map.put(Device.IOS.toString(),2);
                    map.put(Device.Linux.toString(),3);
                    map.put(Device.Windows.toString(),4);
                    map.put(Device.MacOs.toString(),5);
                    int pos=map.get(report.getDevice());
                    Integer temp=Integer.valueOf(strs[pos]);
                    temp-=1;
                    strs[pos]=String.valueOf(temp);
                    String newDevices="";
                    for(int i=0;i<strs.length;i++){
                        newDevices+=strs[i]+",";
                    }
                    portrait.setDevice(newDevices.substring(0,newDevices.length()-1));
                    portraitMapper.updateByPrimaryKey(portrait);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }
}
