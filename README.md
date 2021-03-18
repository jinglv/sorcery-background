# 测试管理平台
SpringBoot后台

## 功能介绍

![image-20210318182935192](../../Library/Application Support/typora-user-images/image-20210318182935192.png)

![image-20210318183034598](https://gitee.com/JeanLv/study_image2/raw/master///image-20210318183034598.png)



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

### 1. 创建一个Jenkins Job

新建一个自由风格的Jenkins Job，新增一些参数

![image-20210127135250684](https://gitee.com/JeanLv/study_image/raw/master///image-20210127135250684.png)

### 2. 获取Jenkins Job的配置数据

- 到Jenkins服务器上，进入到jobs中查看config.xml

  ![image-20210127135519329](https://gitee.com/JeanLv/study_image/raw/master///image-20210127135519329.png)

- 或者请求地址：http://ip:port/job/test/config.xml

  ![image-20210127135212352](https://gitee.com/JeanLv/study_image/raw/master///image-20210127135212352.png)

### 3. 编写代码，操作Jenkins

- 创建新的Job，以上面的获取到的配置文件为基础

  ```java
      public static void main(String[] args) throws URISyntaxException, IOException {
          String jenkinsUrl = "http://60.205.228.49:8086/";
          String username = "admin";
          String password = "admin";
          String jobName = "test02";
          // 创建Jenkins初始化
          JenkinsHttpClient jenkinsHttpClient = new JenkinsHttpClient(new URI(jenkinsUrl), username, password);
          // 创建jenkins服务
          JenkinsServer jenkinsServer = new JenkinsServer(jenkinsHttpClient);
          // 读取Jenkins配置文件
          FileReader fileReader = new FileReader(new File("src/main/resources/jenkins/test_01.xml"));
          String jobXml = fileReader.readString();
          // 创建Job，参数：job的名称与配置文件，参数3表示是需要登录权限校验，否则会抛出异常：status code: 403, reason phrase: Forbidden
          jenkinsServer.createJob(jobName, jobXml, true);
      }
  ```

  执行结果：

  ![image-20210127173008543](https://gitee.com/JeanLv/study_image/raw/master///image-20210127173008543.png)

  再刷新Jenkins，发现会有一个新的名为test02的Jobs，并查看配置与模板是一样的

- 已创建Job，接下来，我们需要执行Job

  ```java
      public static void main(String[] args) throws URISyntaxException, IOException {
          String jenkinsUrl = "http://60.205.228.49:8086/";
          String username = "admin";
          String password = "admin";
          // 创建Jenkins初始化
          JenkinsHttpClient jenkinsHttpClient = new JenkinsHttpClient(new URI(jenkinsUrl), username, password);
          // 创建jenkins服务
          JenkinsServer jenkinsServer = new JenkinsServer(jenkinsHttpClient);
          // 获取当前所有的Job
          Map<String, Job> jobMap = jenkinsServer.getJobs();
          // 获取名为test02的Job
          Job test02 = jobMap.get("test02");
          // 执行Job, 异常：status code: 400, reason phrase: Bad Request，这是因为Job在配置了两个参数，但是在构建的时候，并没有传入参数
          // test02.build(true);
  
          // 设置Job的参数
          Map<String, String> params = new HashMap<>();
          params.put("userId", "15");
          params.put("remark", "Jenkins演示调用");
          // 执行Job
          test02.build(params, true);
      }
  ```

  执行结果：

  ![image-20210127173953206](https://gitee.com/JeanLv/study_image/raw/master///image-20210127173953206.png)

  回到Jenkins进入到test02 Job里：

  ![image-20210127174047950](https://gitee.com/JeanLv/study_image/raw/master///image-20210127174047950.png)

  可进入到#1里面查看构建的参数

  ![image-20210127174151545](https://gitee.com/JeanLv/study_image/raw/master///image-20210127174151545.png)

### 4. 命令执行操作

1. Jenkins配置文件新增参数， 并配置执行命令

   ```xml
   <project>
       <description>Java操作Jenkins演示</description>
       <keepDependencies>false</keepDependencies>
       <properties>
           <hudson.model.ParametersDefinitionProperty>
               <parameterDefinitions>
                   <hudson.model.StringParameterDefinition>
                       <name>userId</name>
                       <description>用户id</description>
                       <defaultValue>12</defaultValue>
                       <trim>true</trim>
                   </hudson.model.StringParameterDefinition>
                   <hudson.model.TextParameterDefinition>
                       <name>remark</name>
                       <description>备注信息</description>
                       <defaultValue>Jenkins调用</defaultValue>
                       <trim>false</trim>
                   </hudson.model.TextParameterDefinition>
                   <hudson.model.TextParameterDefinition>
                       <name>testCommand</name>
                       <description>测试命令</description>
                       <defaultValue>测试命令演示</defaultValue>
                       <trim>false</trim>
                   </hudson.model.TextParameterDefinition>
               </parameterDefinitions>
           </hudson.model.ParametersDefinitionProperty>
       </properties>
       <scm class="hudson.scm.NullSCM"/>
       <canRoam>true</canRoam>
       <disabled>false</disabled>
       <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>
       <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>
       <triggers/>
       <concurrentBuild>false</concurrentBuild>
       <builders>
           <hudson.tasks.Shell>
               <command>
                   eval "${testCommand}"
               </command>
           </hudson.tasks.Shell>
       </builders>
       <publishers/>
       <buildWrappers/>
   </project>
   ```

   

2. 编写代码，更新已存在Job，传入命令执行

   ```java
       public static void main(String[] args) throws URISyntaxException, IOException {
           String jenkinsUrl = "http://60.205.228.49:8086/";
           String username = "admin";
           String password = "admin";
           String jobName = "test02";
           // 创建Jenkins初始化
           JenkinsHttpClient jenkinsHttpClient = new JenkinsHttpClient(new URI(jenkinsUrl), username, password);
           // 创建jenkins服务
           JenkinsServer jenkinsServer = new JenkinsServer(jenkinsHttpClient);
           // 读取Jenkins配置文件
           FileReader fileReader = new FileReader(new File("src/main/resources/jenkins/test_01.xml"));
           String jobXml = fileReader.readString();
           // 更新已存在的Job
           jenkinsServer.updateJob(jobName, jobXml, true);
           // 获取当前所有的Job
           Map<String, Job> jobMap = jenkinsServer.getJobs();
           // 获取名为test02的Job
           Job test02 = jobMap.get("test02");
           // 执行Job, 异常：status code: 400, reason phrase: Bad Request，这是因为Job在配置了两个参数，但是在构建的时候，并没有传入参数
           // test02.build(true);
   
           // 设置Job的参数
           Map<String, String> params = new HashMap<>();
           params.put("userId", "15");
           params.put("remark", "Jenkins演示调用");
           params.put("testCommand", "pwd");
           // 执行Job
           test02.build(params, true);
   ```

3. 执行成功，回到Jenkins查看构建结果

   ![image-20210127175511772](https://gitee.com/JeanLv/study_image/raw/master///image-20210127175511772.png)

