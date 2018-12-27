# jcraft-guide
jcraft是SSH2的一个纯Java实现。它允许你连接到一个sshd 服务器，使用端口转发，X11转发，文件传输等等。
这里只介绍端口转发，因为在我们开发环境中，很多服务器是不对外暴露的，都要通过跳转机跳转，这样使我们开发人员开发很不方便，为了在本地开发，这里我们通过jcraft的转口转发功能来实现我们在本地机器上连接内网服务器进行开发


# 使用
## 导入maven包 
![Image text](https://raw.githubusercontent.com/rancho00/jcraft-guide/master/images/maven-link.png)


## 加入一个监听器，并实现监听（/resources/SSHListener.java）
![Image text](https://raw.githubusercontent.com/rancho00/jcraft-guide/master/images/web-conf.png)
![Image text](https://raw.githubusercontent.com/rancho00/jcraft-guide/master/images/java-core.png)


## 最后配置（以配置数据库为例）
用远程数据库3306端口为例，
代码中配置了
destination_remote_port=3306
local_port=3307
上诉代码中的destination_remote_port的3306端口会被转发到本机的3307端口，所以在配置jdbcUrl的时候就要用localhost:3307/xxxx
![Image text](https://raw.githubusercontent.com/rancho00/jcraft-guide/master/images/mysql-config.png)
