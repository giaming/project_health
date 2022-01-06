# SSM-----springMVC No mapping found for HTTP request with URI的问题

No mapping found for HTTP request with URI
出现这个问题的原因是在web.xml中配置错了，如：

```
 <servlet>
 <servlet-name>springMVCDispatcherServlet</servlet-name>
 <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
 <init-param>
 <param-name>contextConfigLocation</param-name>
 <param-value>/WEB-INF/springMVC-servlet.xml</param-value>
 </init-param>
  <load-on-startup>1</load-on-startup>
 </servlet>

 <servlet-mapping>
 <servlet-name>springMVCDispatcherServlet</servlet-name>
 <url-pattern>/*</url-pattern>
 </servlet-mapping>
123456789101112131415
```

当你在control中返回一个路径的时候，它又把路径（/view/index.jsp）当作一个请求被dispatcherServlet所拦截。所以会抛出异常。

### 解决的办法

#### 第一个方法

```
即使让dispatcherServlet的拦截加上后缀如：*.do;
这样以jsp后缀的就不会别拦截了。
12
```

#### 第二个方法

```
在spring-servlet.xml中加入：
1
<mvc:default-servlet-handler/>
```