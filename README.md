# 1. 简介

插件demo

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

