# 1. 简介

json-sql插件

# 项目遇到的问题

## 1.初始化项目时

### 1.gradle-6.1.1-all.zip重复下载
    
问题：初始化gradle时，gradle-6.1.1-all.zip重复下载  
解决：下载好gradle-6.1.1-all.zip，在gradle/wrapper/gradle-wrapper.properties里修改distributionUrl路径  
```properties
distributionUrl=file\:///D:/gradle/gradle-6.1.1-all.zip
```

### 2.下载超时 

解决：配置maven镜像  
build.gradle里
```
repositories {
    maven { url 'http://maven.aliyun.com/nexus/content/groups/public/'}
    maven { url'https://maven.aliyun.com/repository/public/' }
    maven { url'https://maven.aliyun.com/repository/google/' }
    maven { url'https://maven.aliyun.com/repository/jcenter/' }
    maven { url'https://maven.aliyun.com/repository/central/' }
    google()
    jcenter()
}
```

### 3.gradle插件版本过高无法启动
解决：build.gradle里降低版本
```
plugins {
    id 'java'
    id 'org.jetbrains.intellij' version '0.6.3'
}
```

### 4.初始化慢

IdeaIc-2020.1....zip下载慢问题，这个zip有500mb，需要等待较久时间

### 5.lombok不识别

修改前：

```
dependencies {
    compileOnly group: 'org.projectlombok', name: 'lombok', version: '1.18.18'
}
```

修改后：

```
dependencies {
    compileOnly group: 'org.projectlombok', name: 'lombok', version: '1.18.18'
    annotationProcessor group: 'org.projectlombok', name: 'lombok', version: '1.18.18'
}
```

### 6.项目启动，gbk乱码报错

setting->build->gradle->build and run using和run tests using修改为idea?  
在help->edit custom vm options 添加 -Dfile.encoding=UTF-8


### 7.ui界面乱码

build.gradle里配置

```
tasks.withType(JavaCompile) {
    options.encoding = "UTF-8"
}
```

### 8.依赖冲突

org.apache.calcite:calcite-core:1.35.0 和 com.jetbrains:ideaIC:2020.1.2 的 slf4j依赖冲突

排除calcite的slf4j
```
dependencies {
    implementation("org.apache.calcite:calcite-core:1.35.0") {
        exclude group: 'org.slf4j', module: 'slf4j-api'
    }
}
```

### 9.IDEA2021.3不支持安装该插件

在build.gradle里配置最高版本和最低版本
```
patchPluginXml {
    sinceBuild = "201"
    untilBuild = '233.*'
}
```

### 10. 无法区分json里的. 
如 
```json
{
  "a.b":555,
  "a": {
    "b": "99u9w"
  }
}
```
sql查询时无法区分 a.b是查的哪一个  
把key里含有.的全部换成_NaN_，导出时会还原成.
```json
{
  "a_NaN_b":555,
  "a": {
    "b": "99u9w"
  }
}
```

