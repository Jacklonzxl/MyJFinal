package com.my.util;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity; 
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair; 
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse; 
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient; 
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.IOException; 
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HTTP ���󹤾���
 *
 * @author : liii
 * @version : 1.0.0
 * @date : 2015/7/21
 * @see : TODO
 */
@SuppressWarnings("deprecation")
public class HttpUtil {
    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 7000;

    static {
        // �������ӳ�
        connMgr = new PoolingHttpClientConnectionManager();
        // �������ӳش�С
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // �������ӳ�ʱ
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // ���ö�ȡ��ʱ
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // ���ô����ӳػ�ȡ����ʵ���ĳ�ʱ
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        // ���ύ����֮ǰ ���������Ƿ����
        //configBuilder.setStaleConnectionCheckEnabled(true);
        requestConfig = configBuilder.build();
    }

 

    /**
     * ���� POST ����HTTP����������������
     * @param apiUrl
     * @return
     */
    public static String doPost(String apiUrl) {
        return doPost(apiUrl, new HashMap<String, Object>());
    }

    /**
     * ���� POST ����HTTP����K-V��ʽ
     * @param apiUrl API�ӿ�URL
     * @param params ����map
     * @return
     */
    public static String doPost(String apiUrl, Map<String, Object> params) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;

        try {
            httpPost.setConfig(requestConfig);
            List<NameValuePair> pairList = new ArrayList<>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry
                        .getValue().toString());
                pairList.add(pair);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
            response = httpClient.execute(httpPost);
            //System.out.println(response.toString());
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    /**
     * ���� POST ����HTTP����JSON��ʽ
     * @param apiUrl
     * @param json json����
     * @return
     */
    public static String doPost(String apiUrl, Object json) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;

        try {
            httpPost.setConfig(requestConfig);
            StringEntity stringEntity = new StringEntity(json.toString(),"UTF-8");//���������������
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            System.out.println(response.getStatusLine().getStatusCode());
            httpStr = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    /**
     * ���� SSL POST ����HTTPS����K-V��ʽ
     * @param apiUrl API�ӿ�URL
     * @param params ����map
     * @return
     */
    public static String doPostSSL(String apiUrl, Map<String, Object> params) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        String httpStr = null;

        try {
            httpPost.setConfig(requestConfig);
            List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry
                        .getValue().toString());
                pairList.add(pair);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("utf-8")));
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, "utf-8");
        } catch (Exception e) {
			e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    /**
     * ���� SSL POST ����HTTPS����JSON��ʽ
     * @param apiUrl API�ӿ�URL
     * @param json JSON����
     * @return
     */
    public static String doPostSSL(String apiUrl, Object json) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        String httpStr = null;

        try {
            httpPost.setConfig(requestConfig);
            StringEntity stringEntity = new StringEntity(json.toString(),"UTF-8");//���������������
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    /**
     * ����SSL��ȫ����
     *
     * @return
     */

	private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                @Override
                public void verify(String host, SSLSocket ssl) throws IOException {
                }

                @Override
                public void verify(String host, X509Certificate cert) throws SSLException {
                }

                @Override
                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }
            });
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return sslsf;
    }

    /**
    httpClient��get����ʽ2
    * @return
    * @throws Exception
    */
   public static String doGet(String url, String charset,String ips[])
       throws Exception {
     /*
      * ʹ�� GetMethod ������һ�� URL ��Ӧ����ҳ,ʵ�ֲ���: 1:����һ�� HttpClinet ����������Ӧ�Ĳ�����
      * 2:����һ�� GetMethod ����������Ӧ�Ĳ����� 3:�� HttpClinet ���ɵĶ�����ִ�� GetMethod ���ɵ�Get
      * ������ 4:������Ӧ״̬�롣 5:����Ӧ���������� HTTP ��Ӧ���ݡ� 6:�ͷ����ӡ�
      */
     /* 1 ���� HttpClinet �������ò��� */
     HttpClient httpClient = new HttpClient();
     // ���� Http ���ӳ�ʱΪ5��
     httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
     if(ips!=null)
     {
     httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(500);	 
	 httpClient.getHostConfiguration().setProxy(ips[0], Integer.parseInt(ips[1]));
		//ʹ��������֤
	 httpClient.getParams().setAuthenticationPreemptive(true);
     }
     /* 2 ���� GetMethod �������ò��� */
     GetMethod getMethod = new GetMethod(url);
     // ���� get ����ʱΪ 5 ��
     getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 2000);
     // �����������Դ����õ���Ĭ�ϵ����Դ�����������
     getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,	new DefaultHttpMethodRetryHandler());
     String response = "";
     /* 3 ִ�� HTTP GET ���� */
     try {
       int statusCode = httpClient.executeMethod(getMethod);
       /* 4 �жϷ��ʵ�״̬�� */
       if (statusCode != HttpStatus.SC_OK) {
         System.err.println("�������: "+ getMethod.getStatusLine());
       }
       /* 5 ���� HTTP ��Ӧ���� */
       // HTTP��Ӧͷ����Ϣ������򵥴�ӡ
       //Header[] headers = getMethod.getResponseHeaders();
       //for (Header h : headers)
         //System.out.println(h.getName() + "------------ " + h.getValue());
       // ��ȡ HTTP ��Ӧ���ݣ�����򵥴�ӡ��ҳ����
       byte[] responseBody = getMethod.getResponseBody();// ��ȡΪ�ֽ�����
       response = new String(responseBody, charset);
       //System.out.println("----------response:" + response);
       // ��ȡΪ InputStream������ҳ������������ʱ���Ƽ�ʹ��
       // InputStream response = getMethod.getResponseBodyAsStream();
     } catch (HttpException e) {
       // �����������쳣��������Э�鲻�Ի��߷��ص�����������
       System.out.println("���������URL!");
       e.printStackTrace();
     } catch (IOException e) {
       // ���������쳣
       System.out.println("���������쳣!");
       e.printStackTrace();
     } finally {
       /* 6 .�ͷ����� */
       getMethod.releaseConnection();
     }
     return response;
   }
    /**
     * ���Է���
     * @param args
     */
    public static void main(String[] args) throws Exception {

    }
} 