package team.software.collect.serviceimpl.report;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
//import org.xm.team.software.collect.similarity.text.CosineSimilarity;
//import org.xm.team.software.collect.similarity.text.EditDistanceSimilarity;
//import org.xm.team.software.collect.similarity.text.TextSimilarity;
import team.software.collect.enums.Device;
import team.software.collect.enums.TestType;
import team.software.collect.mapperservice.filepicture.FilePictureMapper;
import team.software.collect.mapperservice.report.ReportMapper;
import team.software.collect.mapperservice.task.TaskMapper;
import team.software.collect.mapperservice.task.TaskOrderMapper;
import team.software.collect.mapperservice.user.PortraitMapper;
import team.software.collect.po.filepicture.FilePicture;
import team.software.collect.po.report.Report;
import team.software.collect.po.task.Task;
import team.software.collect.po.task.TaskOrder;
import team.software.collect.po.user.Portrait;
import team.software.collect.service.report.ReportService;
import team.software.collect.util.Constant;
import team.software.collect.similarity.imgSimilarity.ImageHistogram;
import team.software.collect.similarity.imgSimilarity.ImagePHash;
import team.software.collect.util.PageInfoUtil;
import team.software.collect.similarity.textSimilarity.similarity.text.CosineSimilarity;
import team.software.collect.similarity.textSimilarity.similarity.text.EditDistanceSimilarity;
import team.software.collect.similarity.textSimilarity.similarity.text.TextSimilarity;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.report.ReportVO;
import team.software.collect.vo.task.TaskVO;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

@Service
public class ReportServiceImpl implements ReportService {
    @Resource
    private ReportMapper reportMapper;
    @Resource
    private TaskMapper taskMapper;
    @Resource
    private TaskOrderMapper taskOrderMapper;
    @Resource
    private FilePictureMapper filePictureMapper;
    @Resource
    private PortraitMapper portraitMapper;

    //报告要检查是否抄袭
    @Override
    public ResultVO<ReportVO> postReport(ReportVO reportVO) {
        List<TaskOrder> taskOrderList=taskOrderMapper.selectByUid(reportVO.getUid());
        TaskOrder newTaskOrder = null;
        for(TaskOrder taskOrder : taskOrderList){
            if(taskOrder.getTid().equals(reportVO.getTid()) && taskOrder.getFinished().equals(1))
                return new ResultVO<>(Constant.REQUEST_FAIL, "已经上传过测试报告！");
            else if(taskOrder.getTid().equals(reportVO.getTid()))
                newTaskOrder=taskOrder;
        }

        //...增加上传时间字段
        Report report=new Report(reportVO);
        report.setCreatTime(new Date());
        report.setCommentNum(1);
        report.setScore(BigDecimal.valueOf(2.50));
        report.setParentId(-1);
        //todo 下一阶段优化，若不存在抄袭行为，则将相似报告存入数据库（同时需要更新别的报告的相似报告字段，这样相似报告推荐的时候就不用再浪费时间计算相似度）
        int insertRe=reportMapper.insert(report);
        if(getSimilarReports(1,3,-1,report.getRid())==null){
            reportMapper.deleteByPrimaryKey(report.getRid());
            return new ResultVO<>(Constant.REQUEST_FAIL,"检测到您存在抄袭行为，请重新提交报告！");
        };
        if(insertRe!=1){
            return new ResultVO<>(Constant.REQUEST_FAIL, "数据库插入错误！");
        }
        newTaskOrder.setFinished(1);
        taskOrderMapper.updateByPrimaryKey(newTaskOrder);
        //更新用户画像 能力值、活跃度、最近一个月——添加一个定时任务、每天凌晨更新preference和device
        //活跃度暂时每次接受任务加0.3，每次提交加0.4，每次协作加0.2，定时：每天凌晨减去0.1
        //能力值暂时设置为0，度量点：1.用户评分2.没有评分的时候？添加算法？

        //更新用户偏爱的测试设备
        updateDevice(reportVO.getUid(),report.getRid());
        //更新活跃度
        Portrait portrait=portraitMapper.selectByUid(report.getUid());
        BigDecimal preNum=portrait.getActivity();
        BigDecimal postNum=preNum.add(new BigDecimal(0.4));
        portrait.setActivity(postNum);
        portraitMapper.updateByPrimaryKey(portrait);
        return new ResultVO<>(Constant.REQUEST_SUCCESS, "报告上传成功！", new ReportVO(report));
    }

    @Override
    public List<ReportVO> getReportsByTid(Integer userId, Integer taskId) {
        //todo... 迭代三？ 任务大厅展示测试报告时，根据用户的id判断有没有查看权限   众包工人：有没有参与该任务、发包方：可以添加付费查看功能
        List<Report> reportList=reportMapper.selectByTid(taskId);
        List<ReportVO> reportVOS=new ArrayList<>();
        for(int i=0;i<reportList.size();i++)
            reportVOS.add(new ReportVO(reportList.get(i)));
        return reportVOS;
    }

    @Override
    public List<ReportVO> getReportsByTidAndUid(Integer uid, Integer tid){
        List<Report> reportList=reportMapper.selectByTid(tid);
        List<ReportVO> reportVOS=new ArrayList<>();
        for(int i=0;i<reportList.size();i++){
            if(reportList.get(i).getUid().equals(uid))
                reportVOS.add(new ReportVO(reportList.get(i)));
        }
        return reportVOS;
    }

    @Override
    public List<ReportVO> getParentReports(Integer rid){
        List<ReportVO> reportList=new ArrayList<>();
        Report report=reportMapper.selectByPrimaryKey(rid);
        reportList.add(new ReportVO(report));
        while(!report.getParentId().equals(Integer.valueOf(-1))){
            report=reportMapper.selectByPrimaryKey(report.getParentId());
            reportList.add(new ReportVO(report));
        }
        return reportList;
    }

    //检查是否抄袭
    @Override
    public ResultVO<ReportVO> postCollaborativeReport(Integer parentId, ReportVO reportVO) {
        List<Report> reportList=reportMapper.selectByUid(reportVO.getUid());
        for(Report report:reportList){
            if(report.getRid().equals(reportVO.getParentId()))
                return new ResultVO<>(Constant.REQUEST_FAIL, "不能与自己发布的报告协作！");
            else if(report.getParentId().equals(reportVO.getParentId()))
                return new ResultVO<>(Constant.REQUEST_FAIL, "已经协作过该报告！");
        }

        Report report=new Report(reportVO);
        report.setCreatTime(new Date());
        report.setCommentNum(1);
        report.setScore(BigDecimal.valueOf(2.50));
        int insertRe=reportMapper.insert(report);
        if(getSimilarReports(1,3,-1,report.getRid())==null){
            reportMapper.deleteByPrimaryKey(report.getRid());
            return new ResultVO<>(Constant.REQUEST_FAIL,"检测到您的协作报告存在抄袭行为，请重新提交报告，提示：请不要打断摘抄被协作报告的内容，提出问题即可");
        };
        if(insertRe>0) {
            //更新用户偏爱的测试设备和测试类型
            updateDevice(reportVO.getUid(),report.getRid());
//            updatePrefer(reportVO.getUid(),reportVO.getTid());
            //更新活跃度，每次协作添加0.2
            Portrait portrait=portraitMapper.selectByUid(report.getUid());
            BigDecimal preNum=portrait.getActivity();
            BigDecimal postNum=preNum.add(new BigDecimal(0.2));
            portrait.setActivity(postNum);
            portraitMapper.updateByPrimaryKey(portrait);
            return new ResultVO<>(Constant.REQUEST_SUCCESS, "报告协作成功！", new ReportVO(report));//直接返回reportVO不含有rid，这样处理后含有rid，更好一点
        }
        return new ResultVO<>(Constant.REQUEST_FAIL, "数据库插入错误！");
    }

    @Override
    public ReportVO getReportDetails(Integer rid, Integer uid) {
        Report reportFromDB = reportMapper.selectByPrimaryKey(rid);
        //一般查询结果不会是null
        if(reportFromDB == null){
            return new ReportVO();
        }else{
            return new ReportVO(reportFromDB);
        }
    }

    @Override
    public PageInfo<ReportVO> getLowQualityReports(Integer crrPage,Integer pageSize,Integer tid){
        if (crrPage == null || crrPage < 1) crrPage = 1;
        List<Report> reportList=reportMapper.selectByTid(tid);
        Collections.sort(reportList, new Comparator<Report>() {
            @Override
            public int compare(Report o1, Report o2) {
                if(o1.getScore().compareTo(o2.getScore())>0)
                    return 1;
                else if(o1.getScore().compareTo(o2.getScore())==0)
                    return 0;
                else
                    return -1;
            }
        });
        PageHelper.startPage(crrPage, pageSize);
        List<Report> re=new ArrayList<>();
        for (int i = (crrPage - 1) * pageSize; i < crrPage*pageSize && i < reportList.size(); i++) {
            re.add(reportList.get(i));
        }
        PageInfo<Report> po = new PageInfo<>(re);
        PageInfo<ReportVO> result = PageInfoUtil.convert(po, ReportVO.class);
        return result;
    }

    //上传报告后请求这个接口，如果在这里判定为抄袭则删除该报告，返回null，前端正常是跳转到相似报告页面，抄袭的情况下直接要求用户重新上传报告
    @Override
    public PageInfo<ReportVO> getSimilarReports(Integer currPage, Integer pageSize, Integer uid, Integer rid) {
        // 2.调用相似度算法计算，返回一个pageInfo
        //得到一个recommendedReportList
        int recommendNum=3;
        if(currPage==null || currPage<1) currPage=1;
        PageHelper.startPage(currPage, pageSize);
        List<Report> recommendedReportList=new ArrayList<>();
        Report userReport = reportMapper.selectByPrimaryKey(rid);
        List<Report> reportList=reportMapper.selectByTid(userReport.getTid());
//        if(reportList.size()<=recommendNum+1){
//            for(Report r:reportList){
//                if(!r.getRid().equals(rid)){
//                    recommendedReportList.add(r);
//                }
//            }
//        }else{
            Map<Report, Double> map=new HashMap<>();
            for(Report r:reportList){
                if(!r.getRid().equals(rid)){
                    map.put(r,getSimilarityScore(userReport,r));
                }
            }
            Map<Report, Double> mapSorted = sortDescend(map);
            if(mapSorted.size()!=0){
                for (Map.Entry<Report, Double> entry : mapSorted.entrySet()) {
                    if(entry.getValue()>50){
//                        reportMapper.deleteByPrimaryKey(rid);
                        return null;
                    }
                    break;
                }
            }
            List<Report> listSorted = new ArrayList<>(mapSorted.keySet());
            int minNum=Math.min(recommendNum,listSorted.size());
            for(int i=0;i<minNum;i++){
                if(!listSorted.get(i).getUid().equals(uid))//检测是否抄袭的时候要与自己在这个任务下的别的报告检测（和自己的另外一份报告太相似也认为没有太大意义），推荐相似报告进行协作的时候把自己的别的报告去掉
                    recommendedReportList.add(listSorted.get(i));
            }


        for(Report r:recommendedReportList){
            System.out.println(r);
        }
        PageInfo<Report> po = new PageInfo<>(recommendedReportList);
        return PageInfoUtil.convert(po, ReportVO.class);

    }

    @Override
    public List<List<ReportVO>> getClusters(Integer tid){
        List<List<Report>> reportClusters = getReportClusters(tid);
        List<List<ReportVO>> clusters = new ArrayList<>();
        for(List<Report> li:reportClusters){
            List<ReportVO> li2 = new ArrayList<>();
            for(Report r:li){
                li2.add(new ReportVO(r));
            }
            clusters.add(li2);
        }
        for(List<ReportVO> li:clusters){
            System.out.println(li);
        }
        return clusters;

    }

    private List<List<Report>> getReportClusters(Integer tid){
        double boundary = 30.0;
        List<List<Report>> reportClusters = new ArrayList<>();
        List<Report> reportList = reportMapper.selectByTid(tid);
        if(reportList.size()==1){
            reportClusters.add(new ArrayList<>(Arrays.asList(reportList.get(0))));
        }else if (reportList.size() > 1){
            List<Report> centroids = getCentroids(reportList);
            for(Report centroid:centroids){
                List<Report> li = new ArrayList<>();
                li.add(centroid);
                reportClusters.add(li);
            }
            for(Report r:reportList){
                if(!centroids.contains(r)){
                    int centroidNum = 0;
                    for(int i=0;i<centroids.size();i++){
                        if(getSimilarityScore_1(r,centroids.get(i))>=boundary){
                            centroidNum = i;
                            break;
                        }
                    }
                    reportClusters.get(centroidNum).add(r);
                }
            }
        }

        return reportClusters;
    }

    private List<Report> getCentroids(List<Report> reportList){
        double boundary = 30.0;
        List<Report> centroids = new ArrayList<>();
        centroids.add(reportList.get(0));
        for(Report r:reportList){
            boolean b=true;
            for(Report centroid:centroids){
                if(getSimilarityScore_1(r,centroid)>=boundary){
                    b=false;
                }
            }
            if(b){
                centroids.add(r);
            }
        }

        return centroids;
    }

//    @Override
//    public List<List<ReportVO>> getClusters(Integer tid){
//        List<List<Report>> reportClusters = getReportClusters(tid);
//        List<List<ReportVO>> clusters = new ArrayList<>();
//        for(List<Report> li:reportClusters){
//            List<ReportVO> li2 = new ArrayList<>();
//            for(Report r:li){
//                li2.add(new ReportVO(r));
//            }
//            clusters.add(li2);
//        }
//        for(List<ReportVO> li:clusters){
//            System.out.println(li);
//        }
//        return clusters;
//    }
//
//    private List<List<Report>> getReportClusters(Integer tid){
//        List<List<Report>> reportClusters = new ArrayList<>();
//        List<Report> reportList = reportMapper.selectByTid(tid);
//        for(Report r:reportList){
//            reportClusters.add(new ArrayList<>(Arrays.asList(r)));
//        }
//        OUT1:
//        while(true){
//            if(reportClusters.size()<=1) {
//                break;
//            }else{
//                OUT2:
//                for(int i=0;i<reportClusters.size()-1;i++){
//                    for(int j=i+1;j<reportClusters.size();j++){
//                        if(compareClusters(reportClusters.get(i),reportClusters.get(j))){
//                            reportClusters.get(i).addAll(reportClusters.get(j));
//                            reportClusters.remove(j);
//                            break OUT2;
//                        }else{
//                            if((i==reportClusters.size()-2)&&(j==reportClusters.size()-1)){
//                                break OUT1;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        return reportClusters;
//    }
//
//    private boolean compareClusters(List<Report> l1,List<Report> l2){
//        double boundary = 40.0;
//        boolean result = true;
//        for(Report r1:l1){
//            for(Report r2:l2){
//                if(getSimilarityScore_1(r1,r2)<boundary){
//                    result = false;
//                }
//            }
//        }
//        return result;
//    }

    private double getSimilarityScore(Report r1,Report r2){
        Integer rid1=r1.getRid();
        Integer rid2=r2.getRid();
        List<FilePicture> pics1=filePictureMapper.selectByRid(rid1);
        List<FilePicture> pics2=filePictureMapper.selectByRid(rid2);
        String intro1=r1.getIntro();
        String intro2=r2.getIntro();
        String tips1=r1.getRecovertips();
        String tips2=r2.getRecovertips();
        TextSimilarity cosSimilarity = new CosineSimilarity();
        TextSimilarity editSimilarity = new EditDistanceSimilarity();
        double score1 = (cosSimilarity.getSimilarity(intro1, intro2)+cosSimilarity.getSimilarity(tips1,tips2))/2;
        double score2 = (editSimilarity.getSimilarity(intro1, intro2)+editSimilarity.getSimilarity(tips1,tips2))/2;
        double textSimilarityScore=(score1+score2)/2*100;
        double sum=0.0;
        int pairNum= pics1.size()*pics2.size();
        for(FilePicture f1:pics1){
            for(FilePicture f2:pics2){
                sum+=getPictureSimilarityScore(f1,f2);
            }
        }
        double pictureSimilarityScore=0;
        if(pairNum!=0)
            pictureSimilarityScore=sum/pairNum;
        return textSimilarityScore*0.7+pictureSimilarityScore*0.3;
    }

    private double getSimilarityScore_1(Report r1,Report r2){
        String intro1=r1.getIntro();
        String intro2=r2.getIntro();
        String tips1=r1.getRecovertips();
        String tips2=r2.getRecovertips();
        TextSimilarity cosSimilarity = new CosineSimilarity();
        TextSimilarity editSimilarity = new EditDistanceSimilarity();
        double score1 = (cosSimilarity.getSimilarity(intro1, intro2)+cosSimilarity.getSimilarity(tips1,tips2))/2;
        double score2 = (editSimilarity.getSimilarity(intro1, intro2)+editSimilarity.getSimilarity(tips1,tips2))/2;
        return (score1+score2)/2*100;
    }

    private double getPictureSimilarityScore(FilePicture f1,FilePicture f2) {
        double pictureSimilarityScore=0.0;
        try{
            ImagePHash p=new ImagePHash();
            ImageHistogram histogram=new ImageHistogram();
            String url1="https://bt-vcmq.oss-cn-beijing.aliyuncs.com/img/"+f1.getFileName();
            String url2="https://bt-vcmq.oss-cn-beijing.aliyuncs.com/img/"+f2.getFileName();
            int dis=p.distance(new URL(url1),new URL(url2));
            double pScore=(64- (double) dis)/64*100;
            double histogramScore=histogram.match(new URL(url1),new URL(url2))*100;
            pictureSimilarityScore=(pScore+histogramScore)/2;
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            return pictureSimilarityScore;
        }
    }

    private static <K, V extends Comparable<? super V>> Map<K, V> sortDescend(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>(){
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                int compare = (o1.getValue()).compareTo(o2.getValue());
                return -compare;
            }
        });

        Map<K, V> returnMap = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            returnMap.put(entry.getKey(), entry.getValue());
        }
        return returnMap;
    }


    //数据分析时调用，一个格式化的方法
    private HashMap<TestType, Integer> formatHandlePrefer(Integer uid){
        Portrait portrait=portraitMapper.selectByUid(uid);
        String[] preferStrValues=portrait.getPreference().split(",");//字符串形式的数字
        HashMap<TestType,Integer> re=new HashMap<>();
        HashMap<Integer,TestType> typeTable=new HashMap<>();
        typeTable.put(0,TestType.FunctionTest);
        typeTable.put(1,TestType.PerformanceTest);
        typeTable.put(2,TestType.StabilityTest);
        for(int i=0;i<preferStrValues.length;i++){
            re.put(typeTable.get(i),Integer.valueOf(preferStrValues[i]));
        }
        return re;
    }

    private HashMap<TestType, Integer> updatePrefer(Integer uid,Integer tid){
        HashMap<TestType, Integer> map=formatHandlePrefer(uid);
        Task task=taskMapper.selectByPrimaryKey(tid);
        for (Map.Entry<TestType, Integer> entry : map.entrySet()) {
            if(entry.getKey().toString().equals(task.getType()))
                map.put(entry.getKey(),entry.getValue()+1);
        }
        String newStr="";
        HashMap<Integer,TestType> typeTable=new HashMap<>();
        typeTable.put(0,TestType.FunctionTest);
        typeTable.put(1,TestType.PerformanceTest);
        typeTable.put(2,TestType.StabilityTest);
        for(int i=0;i<map.size();i++){
            newStr+=map.get(typeTable.get(i))+",";
        }
        newStr=newStr.substring(0,newStr.length()-1);
        Portrait portrait=portraitMapper.selectByUid(uid);
        portrait.setPreference(newStr);
        portraitMapper.updateByPrimaryKey(portrait);
        return map;
    }

    private HashMap<Device, Integer> formatHandleDevice(Integer uid){
        Portrait portrait=portraitMapper.selectByUid(uid);
        String[] preferStrValues=portrait.getDevice().split(",");
        HashMap<Device,Integer> re=new HashMap<>();
        HashMap<Integer,Device> typeTable=new HashMap<>();
        typeTable.put(0, Device.Android);
        typeTable.put(1,Device.Harmony);
        typeTable.put(2,Device.IOS);
        typeTable.put(3,Device.Linux);
        typeTable.put(4,Device.Windows);
        typeTable.put(5,Device.MacOs);
        for(int i=0;i<preferStrValues.length;i++){
            re.put(typeTable.get(i),Integer.valueOf(preferStrValues[i]));
        }
        return re;
    }

    private HashMap<Device, Integer> updateDevice(Integer uid,Integer rid){
        HashMap<Device, Integer> map=formatHandleDevice(uid);
        Report report=reportMapper.selectByPrimaryKey(rid);
        for (Map.Entry<Device, Integer> entry : map.entrySet()) {
            if(entry.getKey().toString().equals(report.getDevice()))
                map.put(entry.getKey(),entry.getValue()+1);
        }
        String newStr="";
        HashMap<Integer,Device> typeTable=new HashMap<>();
        typeTable.put(0, Device.Android);
        typeTable.put(1,Device.Harmony);
        typeTable.put(2,Device.IOS);
        typeTable.put(3,Device.Linux);
        typeTable.put(4,Device.Windows);
        typeTable.put(5,Device.MacOs);
        for(int i=0;i<map.size();i++){
            newStr+=map.get(typeTable.get(i))+",";
        }
        newStr=newStr.substring(0,newStr.length()-1);
        Portrait portrait=portraitMapper.selectByUid(uid);
        portrait.setDevice(newStr);
        portraitMapper.updateByPrimaryKey(portrait);
        return map;
    }
}
