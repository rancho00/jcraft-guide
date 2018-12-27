package com.amt.gd.common.listener;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

/**
 * ssh监听器，需要xml配置监听
 */
public class SSHListener implements ServletContextListener {

    //represents each ssh session
    private Session sesion;

    /**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
        System.out.println("Context initialized ... !");
        try {
            SSHConnection();
        } catch (Throwable e) {
            e.printStackTrace(); // error connecting SSH server
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Context destroyed ... !");
        closeSSH(); // disconnect
    }



    public void closeSSH(){
        sesion.disconnect();
    }

    /**
     * 连接ssh
     * @throws Throwable
     */
    public  void SSHConnection () throws Throwable{
        //服务1
        Map<String,String> param1=new HashMap<String,String>();
		//跳转机1
        param1.put("ssh_remote_server","xxx");
        param1.put("ssh_user","xxx");
        param1.put("ssh_password","xxx");
        param1.put("ssh_remote_port","xxx");
        param1.put("destination_remote_server","xxx");
        param1.put("destination_remote_port","xxx");
        param1.put("local_port","xxx");
        param1.put("next","true");

        //服务2
        Map<String,String> param2=new HashMap<String,String>();
		//跳转机1
        param2.put("ssh_remote_server","xxx");
        param2.put("ssh_user","xxx");
        param2.put("ssh_password","xxx");
        param2.put("ssh_remote_port","xxx");
		//跳转机2
        param2.put("ssh1_user","xxx");
        param2.put("ssh1_remote_server","xxx");
        param2.put("ssh1_password","xxx");
        param2.put("ssh1_remote_port","xxx");
		//目的地ip和port
        param2.put("destination_remote_server","xxx");
        param2.put("destination_remote_port","xxx");
		//本地端口
        param2.put("local_port","xxx");

        List<Map<String,String>> params=new ArrayList<>();
        params.add(param1);
        params.add(param2);

        for(Map<String,String> param:params){
            //连接跳转机1
            JSch jsch = new JSch();
            //jsch.setKnownHosts(S_PATH_FILE_KNOWN_HOSTS);
            //jsch.addIdentity(S_PATH_FILE_PRIVATE_KEY);
            sesion = jsch.getSession(param.get("ssh_user"), param.get("ssh_remote_server"),Integer.parseInt(param.get("ssh_remote_port")));
            sesion.setPassword(param.get("ssh_password"));
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            sesion.setConfig(config);
            sesion.connect(); //ssh connection established!
            //连接跳转机2
            if(StringUtils.isNotEmpty(param.get("next"))){
                try{
                    String host = param.get("ssh1_remote_server");
                    String user = param.get("ssh1_user");
                    int assinged_port = sesion.setPortForwardingL(0, host, Integer.parseInt(param.get("ssh1_remote_port")));
                    sesion =jsch.getSession(user, "127.0.0.1", assinged_port);
                    sesion.setPassword(param.get("ssh1_password"));
                    sesion.setConfig(config);
                    sesion.setHostKeyAlias(host);
                    sesion.connect();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            //端口转发
            sesion.setPortForwardingL(Integer.parseInt(param.get("local_port")), param.get("destination_remote_server"), Integer.parseInt(param.get("destination_remote_port")));
        }

    }
}
