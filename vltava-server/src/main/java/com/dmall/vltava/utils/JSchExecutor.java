//package com.dmall.vltava.utils;
//
//import com.dmall.vltava.domain.base.CommonException;
//import com.jcraft.jsch.*;
//import org.apache.commons.io.IOUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.*;
//import java.util.Properties;
//
///**
// * @author Rob
// * @date Create in 1:13 PM 2020/2/17
// */
//public class JSchExecutor {
//    private static final Logger logger = LoggerFactory.getLogger(JSchExecutor.class);
//
//    private final static String CHARSET = "UTF-8";
//    private final static String USER_NAME = "dmadmin";
//    private final static String PWD = "db#dm.admin";
//    private final static Integer PORT = 22;
//    private final static String BASE_DIR = "~/web/Vltava/sandbox/bin/";
//    private final static String CMD_TRACE = "cd %s && nohup sh sandbox.sh -p $(ps -ef|grep %s|grep java|awk '{print $2}') %s > %s.log 2>&1 &";
////    private final static String CMD_TRACE = "cd %s && sh sandbox.sh -p $(ps -ef|grep %s|grep java|awk '{print $2}') %s";
//    private String host;
//    private JSch jsch;
//    private Session session;
//    private ChannelExec exec;
//
//    public JSchExecutor(String host) {
//        this.host = host;
//    }
//
//    public void coverageExecute(String appName, String sandboxCmd) {
////        execute(String.format(CMD_TRACE, BASE_DIR, appName, sandboxCmd));
//        execute(String.format(CMD_TRACE, BASE_DIR, appName, sandboxCmd, BASE_DIR + appName));
//    }
//
//    private void execute(String cmd) {
//        connect(host);
//        logger.info("准备执行命令：" + cmd);
//        executeCommandShell(cmd);
//        disconnect();
//    }
//
//
//    private void connect(String host) {
//        jsch = new JSch();
//        try {
//            logger.info("准备连接到主机: " + host);
//            session = jsch.getSession(USER_NAME, host, PORT);
//            session.setPassword(PWD);
//            Properties config = new Properties();
//            config.put("StrictHostKeyChecking", "no");
//            session.setConfig(config);
//            session.connect();
//            if (session.isConnected()) {
//                logger.info("连接主机成功: " + host);
//            } else {
//                throw new CommonException("连接主机失败: " + host);
//            }
//        } catch (JSchException e) {
//            logger.error("连接主机失败: " + host);
//            e.printStackTrace();
//            throw new CommonException(e.getMessage(), e.getStackTrace());
//        }
//    }
//
//    private void disconnect() {
//        if (exec != null && exec.isConnected()) {
//            exec.disconnect();
//        }
//        if (session != null && session.isConnected()) {
//            session.disconnect();
//        }
//        logger.info("关闭连接成功：" + host);
//    }
//
//    private String executeCommandShell(String cmd) {
//        try {
//            ChannelShell shell = (ChannelShell) session.openChannel("shell");
//            InputStream in = shell.getInputStream();
//            shell.setPty(true);
//            shell.connect();
//            OutputStream out = shell.getOutputStream();
//            out.write((cmd + "\r\n").getBytes());
//            out.flush();
//            Thread.sleep(1000);
////            byte[] tmp = new byte[1024];
////            while (true) {
////                while (in.available() > 0) {
////                    int i = in.read(tmp, 0, 1024);
////                    if (i < 0) {
////                        break;
////                    }
////                    logger.info(new String(tmp, 0, i));
////                }
////                if (shell.isClosed()){
////                    if (in.available()>0) {
////                        continue;
////                    }
////                    logger.info("exit:" + shell.getExitStatus());
////                    break;
////                }
////                Thread.sleep(1000);
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
////    private String executeCommand(String cmd) {
////        Channel channel = null;
////        InputStream in = null;
////        try {
////            channel = session.openChannel("exec");
////            exec = (ChannelExec) channel;
////            exec.setCommand(cmd);
////            exec.setErrStream(System.err);
////            in = exec.getInputStream();
////            exec.connect();
////            byte[] tmp = new byte[1024];
////            while (true) {
////                while (in.available() > 0) {
////                    int i = in.read(tmp, 0, 1024);
////                    if (i < 0) {
////                        break;
////                    }
////                    logger.info(new String(tmp, 0, i));
////                }
////                if (channel.isClosed()) {
////                    if (in.available() > 0) {
////                        continue;
////                    }
////                    logger.info("exit-status: " + channel.getExitStatus());
////                    break;
////                }
////                try {
////                    Thread.sleep(1000);
////                } catch (Exception ee) {
////                }
////            }
////            try {
////                Thread.sleep(30000);
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
////            return IOUtils.toString(in, CHARSET);
////        } catch (Exception e) {
////            e.printStackTrace();
////            throw new CommonException(e.getMessage(), e.getStackTrace());
////        } finally {
////            if (in != null) {
////                try {
////                    in.close();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////            }
////        }
////    }
//}
