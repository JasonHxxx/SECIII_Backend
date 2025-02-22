package team.software.collect.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import lombok.Data;

@Data
public class OssUtil {
    private static String ENDPOINT="https://oss-cn-beijing.aliyuncs.com";//外网访问节点
    private static String AccessKeyID="LTAI5tA3y229tmeyuMazp5qS"; //accID
    private static String AccessKeySecret="dwjCrVrJBL0hN3lIj4XlnKBqL6dLXG";//ACCSecret
    private static String BUKETNAME="bt-vcmq";//仓库名称
    private static String SUFFER_URL ="http://"+BUKETNAME+"."+ENDPOINT; //上传成功返回的url，仓库名称+节点地址
    /**
     * 获取oss链接
     */
    public OSSClient getOSSClient(){
        OSSClient ossClient=new OSSClient(ENDPOINT,AccessKeyID,AccessKeySecret);
        //判断是否存在仓库
        if (ossClient.doesBucketExist(BUKETNAME)){
            System.out.println("存在仓库");
            ossClient.shutdown();
        }else{
            System.out.println("不存在仓库....创建一个仓库"+BUKETNAME);

            CreateBucketRequest bucketRequest=new CreateBucketRequest(null);
            bucketRequest.setBucketName(BUKETNAME);//设置仓库名称
            bucketRequest.setCannedACL(CannedAccessControlList.PublicRead);//设置仓库权限 公共读

            ossClient.createBucket(bucketRequest);
            ossClient.shutdown();
        }
        return ossClient;
    }
    public static void main(String[] args) {
        OssUtil util=new OssUtil();
        util.getOSSClient();
    }
}
