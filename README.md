# 1. 简介

json-sql  
将JsonArray转成数据列表，通过sql来查询数据  
还可以把查出的数据再传成Json
配置环境：
jdk： 17
gradle：8.1
org.jetbrains.intellij：1.14.0
idea： 2024.3
# 2.使用

## 1.位置

再顶部Tools里的JsonSql  
插件压缩包在build分支的buildzip里

## 2.操作步骤
<ul>
    <li>输入jsonArray</li>
    <li>通过sql查询出数据</li>
    <li>按需求导出查询数据</li>
</ul>

# 3.说明

仅支持部分sql语句

## 1.支持的sql语句有：

<ul>
    <li>select *、name、name as alias、name.surname（多层查询）</li>
    <li>where =、!=、in、not in、&gt;、&gt;=、&lt;、&lt;=、between</li>
    <li>like（_和%转义用\\_和\\%）、not like、is null、is not null</li>
    <li>and、or、嵌套and和or</li>
    <li>order by asc、desc、distinct</li>
    <li>limit 1,2</li>
</ul>

## 2.不支持的sql语句有：

<ul>
    <li>update、insert、delete</li>
    <li>group by</li>
    <li>函数</li>
    <li>连表</li>
    <li>union</li>
    <li>特殊的关键词如：result、date（要带上``）</li>
</ul>

## 3.一些例子：
<ul>
    <li>select level.type.name t from arr where level.type.name in ('hello') order by t</li>
    <li>select distinct name from a where name = 'hello' and (age >= 11 or level like '%\\%lin%')</li>
</ul>


