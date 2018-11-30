import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

public class demo {

    @Test
    public void test(){
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
        String p=bCryptPasswordEncoder.encode("123456");
        System.out.println(p);
    }
    /** 上传文件 */
    @Test
    public void upload_file() throws Exception{
        /** 加载配置文件，产生绝对路径 */
        String conf_filename = this.getClass()
                .getResource("/fastdfs_client.conf").getPath();
        /** 初始化客户端全局的对象 */
        ClientGlobal.init(conf_filename);

        /** 创建存储客户端对象 */
        StorageClient storageClient = new StorageClient();
        /** 上传文件 */
        String[] arr = storageClient.upload_file("C:\\Users\\Administrator\\Desktop\\1.jpg", "jpg", null);

        /**
         * 访问路径：http://192.168.12.131/group1/M00/00/00/wKgMg1o53fOAL1CRAABonuLw4M4127.jpg
         * [group1, M00/00/00/wKgMgFlIkk2AHfnLAABonuLw4M4075.jpg]
         * 数组中的第一个元素：组的名称
         * 数组中的第二个元素：文件存储的路径
         */
        System.out.println(Arrays.toString(arr));
    }

    /** 文件下载 */
    @Test
    public void download_file() throws Exception{
        /** 加载配置文件，产生绝对路径 */
        String conf_filename = this.getClass()
                .getResource("/fastdfs_client.conf").getPath();
        /** 初始化客户端全局的对象 */
        ClientGlobal.init(conf_filename);
        /** 创建存储客户端对象 */
        StorageClient storageClient = new StorageClient();
        /** 下载文件 */
        byte[] data = storageClient.download_file("group1", "M00/00/00/wKgMgFlIkk2AHfnLAABonuLw4M4075.jpg");
        System.out.println(data.length);
        FileOutputStream fos = new FileOutputStream(new File("F:/workspace/java/fastdfs-test/src/test/resources/1.jpg"));
        fos.write(data);
        fos.flush();
        fos.close();
    }

    /** 删除文件 */
    @Test
    public void delete_file() throws Exception{
        /** 加载配置文件，产生绝对路径 */
        String conf_filename = this.getClass()
                .getResource("/fastdfs_client.conf").getPath();
        /** 初始化客户端全局的对象 */
        ClientGlobal.init(conf_filename);
        /** 创建存储客户端对象 */
        StorageClient storageClient = new StorageClient();
        /** 删除文件 */
        int res = storageClient.delete_file("group1", "M00/00/00/wKgMgFlIkk2AHfnLAABonuLw4M4075.jpg");
        System.out.println(res);
    }
}
