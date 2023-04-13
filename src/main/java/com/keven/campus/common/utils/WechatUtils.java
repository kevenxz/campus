package com.keven.campus.common.utils;


import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
// vo实体类参数
//import com.entity.TestEntityVO;
//import com.utils.TestUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Keven
 * @version 1.0
 */
@Slf4j(topic = "WechatUtils")
@Component
public class WechatUtils {

    @Value("${mini.appId}")
    private String appId;
    @Value("${mini.secret}")
    private String secret;


    /**
     * 获取小程序code换取openid、session_key
     *
     * @param code
     * @return
     */
    public JSONObject getOpenId(String code) {

        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId
                + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        // 发送请求
        String res = HttpUtil.get(url);
        log.info("请求响应结果:-->{}", res);
        JSONObject entries = JSONUtil.parseObj(res);
        String openid = entries.getStr("openid");
        log.info("微信小程序唯一标识:-->{}", openid);
        return entries;

//        PrintWriter out = null;
//        BufferedReader in = null;
//        String line;
//        StringBuffer stringBuffer = new StringBuffer();
//        try {
//            URL realUrl = new URL(url);
//            // 打开和URL之间的连接
//            URLConnection conn = realUrl.openConnection();
//
//            // 设置通用的请求属性 设置请求格式
//            //设置返回类型
//            conn.setRequestProperty("contentType", "text/plain");
//            //设置请求类型
//            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
//            //设置超时时间
//            conn.setConnectTimeout(1000);
//            conn.setReadTimeout(1000);
//            conn.setDoOutput(true);
//            conn.connect();
//            // 获取URLConnection对象对应的输出流
//            out = new PrintWriter(conn.getOutputStream());
//            // flush输出流的缓冲
//            out.flush();
//            // 定义BufferedReader输入流来读取URL的响应    设置接收格式
//            in = new BufferedReader(
//                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
//            while ((line = in.readLine()) != null) {
//                stringBuffer.append(line);
//            }
//
//            JSONObject jsonObject = JSONUtil.parseObj(stringBuffer.toString());
//            return jsonObject;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //使用finally块来关闭输出流、输入流
//        finally {
//            try {
//                if (out != null) {
//                    out.close();
//                }
//                if (in != null) {
//                    in.close();
//                }
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//        return null;
//    }
    }

//    public static Map<String, Object> getPhoneNumber(TestEntityVO vo) {
//        Map<String,Object> map=new HashMap<>();
//        String openid= vo.getWechatOpenId();
//        String session_key = vo.getSessionKey();
//        if (!EmptyUtils.isEmpty(openid)) {
//
//            if(EmptyUtils.isEmpty(session_key)){
//                return null;
//            }
//            map.put("openid",openid);
//            // 被加密的数据
//            byte[] dataByte = Base64.decode(vo.getEncryptedData());
//            // 加密秘钥
//            byte[] keyByte = Base64.decode(session_key);
//            // 偏移量
//            byte[] ivByte = Base64.decode(vo.getIv());
//            try {
//                // 如果密钥不足16位，那么就补足. 这个if 中的内容很重要
//                int base = 16;
//                String result = null;
//                if (keyByte.length % base != 0) {
//                    int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
//                    byte[] temp = new byte[groups * base];
//                    Arrays.fill(temp, (byte) 0);
//                    System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
//                    keyByte = temp;
//                }
//                // 初始化
//                Security.addProvider(new BouncyCastleProvider());
//                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
//                SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
//                AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
//                parameters.init(new IvParameterSpec(ivByte));
//                // 初始化
//                cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
//                byte[] resultByte = cipher.doFinal(dataByte);
//                if (null != resultByte && resultByte.length > 0) {
//                    result = new String(resultByte, "UTF-8");
//                    JSONObject jsonObject = JSONObject.parseObject(result);
//                    map.put("param",jsonObject);
//                    return map;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
}
