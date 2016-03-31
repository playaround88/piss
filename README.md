# piss
Platform independent session share(平台无关的会话共享系统)  

## 说明
web项目中，通常认为负载均衡与集群的区别是，一个只做请求转发，而另一个在能够在多个节点中维护一直的session。  
比如常见的tomcat的一种集群方式，就是借助memcached的session-manager插件完成。  
这个项目就是期望实现平台无关的（tomcat、jetty等中间件）会话共享能力。  
目前，仅实现基于redis的版本，memcached应该很简单扩展

## 使用与测试
项目实现mvn管理  
git下载源码：git clone
可以导入eclipse，import -> import existing maven project
testweb是一个测试项目，不被piss-parent管理，需要单独导入。该工程仅仅引入了piss-redis，可以通过浏览器调试查看token的生成与管理。

## 原理说明
piss通过前置filter替换HttpServletRequest的实现，request中把具体的创建和存取session的部分替换为redis-session的实现。这样就完成了在把session替换到redis中的一个hash。其他集成系统，可以直接通过传递的token获取hash，生成新的session，存取已存在的session属性。
