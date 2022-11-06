package team.software.collect.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AliyunOSSConfig {
    private String endPoint="https://oss-cn-beijing.aliyuncs.com";// 地域节点
    private String accessKeyId="LTAI5tA3y229tmeyuMazp5qS";
    private String accessKeySecret="dwjCrVrJBL0hN3lIj4XlnKBqL6dLXG";
    private String bucketName="bt-vcmq";// OSS的Bucket名称
    private String urlPrefix="bt-vcmq.oss-cn-beijing.aliyuncs.com";// Bucket 域名
    private String fileHost="file";// 文件的目标文件夹
    private String imgHost="img";// 图片的目标文件夹
    @Bean
    public OSS OSSClient(){
        return new OSSClient(endPoint,accessKeyId,accessKeySecret);
    }
}

