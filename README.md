# 1. 简介

json-sql  
将JsonArray转成数据列表，通过sql来查询数据  
还可以把查出的数据再传成Json

# 2.使用

## 1.位置

再顶部Tools里的JsonSql

## 2.操作步骤
<ul>
    <li>输入jsonArray
    <li>通过sql查询出数据
    <li>按需求导出查询数据
</ul>

# 3.说明

仅支持部分sql语句

## 1.支持的sql语句有：

<ul>
    <li>select *、name、name as alias、name.surname（多层查询）
    <li>where =、!=、in、not in、>、>=、<、<=、between、like、not like、is null、is not null
    <li>and、or、嵌套and和or
    <li>order by asc、desc、distinct
    <li>limit 1,2
</ul>

## 2.不支持的sql语句有：

<ul>
    <li>update、insert、delete
    <li>group by
    <li>函数
    <li>连表
    <li>union
    <li>特殊的关键词如：result、date（要带上``）
</ul>

## 3.一些例子：
<ul>
    <li>select level.type.name t from arr where level.type.name in ('hello') order by t
    <li>select distinct name from a where name = 'hello' and (age >= 11 or level like '%lin%')
</ul>


