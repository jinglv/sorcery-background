# 测试管理平台
SpringBoot后台

## Jenkins调用
### Jenkins调用Maven依赖
```xml
    <!--Jenkins调用-->
    <dependency>
        <groupId>com.offbytwo.jenkins</groupId>
        <artifactId>jenkins-client</artifactId>
        <version>0.3.8</version>
    </dependency>
```

### 常用类-JenkinsHttpClient
- 封装了调用JenkinsApi的底层方法
  - JenkinsHttpClient(URI uri, String username, String password)
  - get(String path)
  - getFile(URI path)
  - post(String path, boolean crumbFlag)
  - post(String path, D data, Class<R> cls)
  - post_xml(String path, String xml_data, boolean crumbFlag)
  - ......

![image-20210125115749829](https://gitee.com/JeanLv/study_image/raw/master///image-20210125115749829.png)

### 常用类-JenkinsServer

- 封装了调用JenkinsAPI的语义级别的方法

  - JenkinsServer(JenkinsHttpConnection client)
  - getJob(String jobName)
  - createJob(String jobName, String jobXml, Boolean crumbFlag)
  - updateJob(String jobName, String jobXml, Boolean crumbFlag)
  - getJobXml(String jobName)
  - deleteJob(FolderJob folder, String jobName, Boolean crumbFlag)
  - ......

  ![image-20210125135759255](https://gitee.com/JeanLv/study_image/raw/master///image-20210125135759255.png)

### 常用类-Job

- Jenkins中job对应实体类，有很多实用的语义级别的方法
  - Job(String name, String url)
  - build(Job job)
  - build(Job job, Map<String, String> params)
  - getFileFromWorkspace(String fileName)
  - setClient(JenkinsHttpConnection client)
  - ......

![image-20210125140642175](https://gitee.com/JeanLv/study_image/raw/master///image-20210125140642175.png)

## 查看Jenkins Api

![image-20210125135530692](https://gitee.com/JeanLv/study_image/raw/master///image-20210125135530692.png)

Jenkins接口地址：http://ip:port/api/



## 示例

### 获取JenkinsJob的配置数据

- 创建Job
- 进入Job
- 到Jenkins服务器上，进入到jobs中查看config.xml

![image-20210125141837226](https://gitee.com/JeanLv/study_image/raw/master///image-20210125141837226.png)

或者请求地址：http://ip:port/job/test/config.xml

![image-20210125141749415](https://gitee.com/JeanLv/study_image/raw/master///image-20210125141749415.png)

