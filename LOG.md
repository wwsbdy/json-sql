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

### 11.无法下载插件 Could not find org.jetbrains.intellij.plugins:structure-base:3.139.

structure-base-3.139依赖找不到了，提高版本
```
plugins {
    id 'java'
    id 'org.jetbrains.intellij' version '1.0'
}
```
提高gradle版本 修改gradle/wrapper/gradle-wrapper.properties
```properties
distributionUrl=file\:///D:/BaiduNetdiskDownload/gradle-6.5-bin.zip
```
用=代替空格
```yaml
intellij {
    plugins = ['com.intellij.java']
    version = '2020.1.2'
}
patchPluginXml {
    //最低支持的版本
    //版本参考：https://plugins.jetbrains.com/docs/intellij/build-number-ranges.html?from=jetbrains.org#intellij-platform-based-products-of-recent-ide-versions
    sinceBuild = '201'
    //最高支持的版本，不能不设置，不设置是默认为 project.version
    untilBuild = ''
    changeNotes = """
      2.0版本.<br>
      针对2020版本的更新，主要修复了2020版本及更高版本报explicitly marked as incompatible的问题.<br>
      使用了新的gradle来创建插件.<br>
      """
}
```

