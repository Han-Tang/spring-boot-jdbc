package com.lou.springboot.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;

@EnableScheduling
@Slf4j
@RestController
public class JdbcController {

    //自动配置，因此可以直接通过 @Autowired 注入进来
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Value("${post.url}")
    String posturl;

    // 按日期批量调用采集接口
    @GetMapping("/batchRequest")
    // 请求示例：http://localhost:8080/batchRequest?biz_date=2020-08-21
    public HashMap batchRequest(String biz_date) {
        HashMap map = new HashMap();

        if (StringUtils.isEmpty(biz_date)) {
            System.out.println("接口调用失败：缺失合法入参。");
            map.put("异常：", "接口调用失败：缺失合法入参.");
            return map;
        }

        List<Map<String, Object>> list = jdbcTemplate.queryForList("select tablename,date_field from t_tableinfo where status = '1'");
        Iterator<Map<String, Object>> iter = list.iterator();
        while (iter.hasNext()) {
            Map<String, Object> t = iter.next();
            String tablename = t.get("tablename").toString();
            String date_field = t.get("date_field").toString();
            String sql = "select count(1) cnt from " + tablename + " where date_format(" + date_field + ", '%Y-%m-%d') ='" + biz_date + "'";
            List<Map<String, Object>> count = jdbcTemplate.queryForList(sql);
            log.info(">>>>>>>>tablename:"+tablename+"----count:"+count.size());
            if (count.size() == 0) return null;
            Iterator<Map<String, Object>> iter2 = count.iterator();
            Map<String, Object> t2 = iter2.next();
            String cnt = t2.get("cnt").toString();
            String postparam = " { " +
                    "	\"requestBody\": { " +
                    "		\"requestData\": { " +
                    "			\"taskType\": \"1\", " +
                    "            \"taskStep\": \"1\", " +
                    "			\"params\": { " +
                    "				\"database\": \"crtfcent_db\", " +
                    "				\"table_name\": \"" + tablename + "\", " +
                    "                \"sgbcode\": \"all\", " +
                    "                \"bizdate\": \"" + biz_date + "\", " +
                    "                \"declare_num\": " + cnt + ", " +
                    "                \"is_his_data\": \"0\", " +
                    "                \"subsys_code\": \"cep\" " +
                    "			} " +
                    "		} " +
                    "	}, " +
                    "	\"requestHead\": { " +
                    "		\"no\": \"" + this.dateToString(new Date()) + "\", " +
                    "		\"source\": \"dragoon\", " +
                    "		\"sourceId\": \"datasrv\", " +
                    "		\"version\": \"1.0\", " +
                    "		\"auth\": { " +
                    "			\"name\": \"Kpk23BvqjijFEi1P5j5p2TQHSrhYNHNDgFOw\" ," +
                    "			\"password\": \"yFGH3Ih43YPkrTu0dl6fodKeUx1RZ5iW\" " +
                    "		} " +
                    "	} " +
                    "} ";
            log.info("请求参数>>>>>>>>>>>>"+postparam.replaceAll("\\s*",""));
            //发送 POST 请求
            String sr = JdbcController.sendPost(posturl, postparam);
            if (sr == "") {
                map.put(tablename, "网络异常，发送post采集任务请求失败.数量："+cnt);
                String RtnMsg = "网络异常，批量发送post采集任务请求失败.数量："+cnt;
                String RtnCode = "-2";
                jdbcTemplate.execute("insert into t_datacollection_log(`biz_date`,`tablename`,`rtnCode`,`rtnMsg`) value (\"" + biz_date + "\",\"" + tablename + "\",\"" + RtnCode + "\",\"" + RtnMsg + "\")");
                continue;
            }
            Response res = JSON.parseObject(sr, Response.class);
            map.put(tablename, res.getResponseBody().getRtn().getRtnMsg()+".数量："+cnt);
            jdbcTemplate.execute("insert into t_datacollection_log(`biz_date`,`tablename`,`rtnCode`,`rtnMsg`) value (\"" + biz_date + "\",\"" + tablename + "\",\"" + res.getResponseBody().getRtn().getRtnCode() + "\",\"" + res.getResponseBody().getRtn().getRtnMsg() +".数量："+cnt + "\")");
            log.info("返回结果>>>>>>>>>>>>"+sr);
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    // 按日期、业务表调用采集接口
    // 请求示例：http://localhost:8080/singleRequest?biz_date=2020-08-21&table_name=psn_order_info
    @GetMapping("/singleRequest")
    public HashMap singleRequest(String biz_date, String table_name) {
        HashMap map = new HashMap();

        if (StringUtils.isEmpty(biz_date) || StringUtils.isEmpty(table_name)) {
            System.out.println("接口调用失败：缺失合法入参。");
            map.put("异常：", "接口调用失败：缺失合法入参.");
            return map;
        }
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select tablename,date_field from t_tableinfo where tablename='" + table_name + "' and status = '1'");
        Iterator<Map<String, Object>> iter = list.iterator();
        while (iter.hasNext()) {
            Map<String, Object> t = iter.next();
            String tablename = t.get("tablename").toString();
            String date_field = t.get("date_field").toString();
            String sql = "select count(1) cnt from " + tablename + " where date_format(" + date_field + ", '%Y-%m-%d') ='" + biz_date + "'";
            List<Map<String, Object>> count = jdbcTemplate.queryForList(sql);
            if (count.size() == 0) return null;
            Iterator<Map<String, Object>> iter2 = count.iterator();
            Map<String, Object> t2 = iter2.next();
            String cnt = t2.get("cnt").toString();
            String postparam = " { " +
                    "	\"requestBody\": { " +
                    "		\"requestData\": { " +
                    "			\"taskType\": \"1\", " +
                    "            \"taskStep\": \"1\", " +
                    "			\"params\": { " +
                    "				\"database\": \"crtfcent_db\", " +
                    "				\"table_name\": \"" + tablename + "\", " +
                    "                \"sgbcode\": \"all\", " +
                    "                \"bizdate\": \"" + biz_date + "\", " +
                    "                \"declare_num\": " + cnt + ", " +
                    "                \"is_his_data\": \"0\", " +
                    "                \"subsys_code\": \"cep\" " +
                    "			} " +
                    "		} " +
                    "	}, " +
                    "	\"requestHead\": { " +
                    "		\"no\": \"" + this.dateToString(new Date()) + "\", " +
                    "		\"source\": \"dragoon\", " +
                    "		\"sourceId\": \"datasrv\", " +
                    "		\"version\": \"1.0\", " +
                    "		\"auth\": { " +
                    "			\"name\": \"Kpk23BvqjijFEi1P5j5p2TQHSrhYNHNDgFOw\" ," +
                    "			\"password\": \"yFGH3Ih43YPkrTu0dl6fodKeUx1RZ5iW\" " +
                    "		} " +
                    "	} " +
                    "} ";
            log.info("请求参数>>>>>>>>>>>>"+postparam.replaceAll("\\s*",""));
            //发送 POST 请求
            String sr = JdbcController.sendPost(posturl, postparam);
            if (sr == "") {
                map.put(tablename, "网络异常，发送post采集任务请求失败.数量："+cnt);
                String RtnMsg = "网络异常，发送post采集任务请求失败.数量："+cnt ;
                String RtnCode = "-2";
                jdbcTemplate.execute("insert into t_datacollection_log(`biz_date`,`tablename`,`rtnCode`,`rtnMsg`) value (\"" + biz_date + "\",\"" + tablename + "\",\"" + RtnCode + "\",\"" + RtnMsg + "\")");
                continue;
            }
            Response res = JSON.parseObject(sr, Response.class);
            map.put(tablename, res.getResponseBody().getRtn().getRtnMsg()+".数量："+cnt);
            jdbcTemplate.execute("insert into t_datacollection_log(`biz_date`,`tablename`,`rtnCode`,`rtnMsg`) value (\"" + biz_date + "\",\"" + tablename + "\",\"" + res.getResponseBody().getRtn().getRtnCode() + "\",\"" + res.getResponseBody().getRtn().getRtnMsg() +".数量："+cnt  + "\")");
            log.info("返回结果>>>>>>>>>>>>"+sr);
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8"));
            //in = new BufferedReader(
            //new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            log.info("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }


    public String dateToString(Date date) {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(date);
    }

    /*每天2点15分到30分启动定时任务*/
    @Scheduled(cron = "0 15 2 * * ?")
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void testTask() {
        log.info(String.format("每日数据中台定时任务开始于: %s", new Date()));
        try {
            Date date=new Date();
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
            this.batchRequest(format.format(calendar.getTime()));
        } catch (Exception e){
            e.printStackTrace();
        }
        log.info(String.format("每日数据中台定时任务结束于: %s", new Date()));
    }

}
