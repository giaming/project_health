import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2021/12/16 14:32
 */
public class QiNiuYunTest {
    // 上传文件
    @Test
    public void test01(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "uAex2Ws5x-8UWxRhLaH5eN9_CGDFPBPMzRnJfPIP";
        String secretKey = "V-YsNQ4RQ2aOUkQIxFxru_V3Zt0rsa4kUBlBBZdZ";
        String bucket = "giaming-health";//创建的存储空间的名称
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "C:\\Users\\giaming\\Pictures\\macOS Big Sur 壁纸\\macOS Big Sur 壁纸 (3).jpg";
        //上传文件的名称，默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "壁纸.jpg";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    // 删除文件
    @Test
    public void test02() {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释
        //...生成上传凭证，然后准备上传
        String accessKey = "uAex2Ws5x-8UWxRhLaH5eN9_CGDFPBPMzRnJfPIP";
        String secretKey = "V-YsNQ4RQ2aOUkQIxFxru_V3Zt0rsa4kUBlBBZdZ";
        //创建的存储空间的名称
        String bucket = "giaming-health";//创建的存储空间的名称
        String key = "壁纸.jpg";
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }
}
