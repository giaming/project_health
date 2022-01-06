# spring security报Bad credentials错误

### **错误提示：**

org.[springframework](https://so.csdn.net/so/search?q=springframework).security.authentication.BadCredentialsException: **Bad credentials。**



### 解决办法

**默认情况下**：**用户名或者密码错误都会报Bad credentials错误**，**如果发生这个错误，先检查用户名和密码是否输入正确；
或者对比下存入用户到数据库时使用的加密算法，和spring security中配置的加密算法是否一致。**

### 附spring security明文和密文配置片段：

**密文配置片段（以bcrypt加密算法为例，具体根据自己存储数据库时使用的加密算法为准）：**

```xml
<!-注入到需要使用加密的bean中-->
<bean class="com.itheima.security.UserService" id="userService">
        <property name="passwordEncoder" ref="passwordEncoder"/>
</bean>

<!--配置密码加密对象（加密类型，可以不使用BCrypt，换做md5等加密算法也可
以，具体要看自己存入数据库密码时使用的什么加密算法，那么我们校验时要使用相
同的加密算法）-->
<bean id="passwordEncoder" 
      class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder" />

<!--认证管理器，用于处理认证操作-->
<security:authentication-manager>
  <!--认证提供者，执行具体的认证逻辑（此处配置自己的bean）-->
  <security:authentication-provider user-service-ref="userService">
    <!--指定密码加密策略-->
    <security:password-encoder ref="passwordEncoder" />
  </security:authentication-provider>
</security:authentication-manager>
```

**明文配置片段（项目中不会使用）：**

```xml
 <!--
        authentication-manager：认证管理器，用于处理认证操作
    -->
    <security:authentication-manager>
        <!--
            authentication-provider：认证提供者，执行具体的认证逻辑
        -->
        <security:authentication-provider>
            <!--
                user-service：用于获取用户信息，提供给authentication-provider进行认证
            -->
            <security:user-service>
                <!--
                    user：定义用户信息，可以指定用户名、密码、角色，真实情况下我们需要从数据库查询用户信息
				  {noop}：表示当前使用的密码为明文
                -->
                <security:user name="admin" password="{noop}admin" authorities="ROLE_ADMIN">
              	</security:user>
            </security:user-service>
        </security:authentication-provider>
    </security:authentication-manager>

```

