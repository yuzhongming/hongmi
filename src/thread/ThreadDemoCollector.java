package thread;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Response;


public class ThreadDemoCollector implements Callable {
	
	/**
	 * 日志
	 */
	private final static Logger log = Logger.getLogger(ThreadDemoCollector.class);
	
	/**
	 * 线程名
	 */
	private String threadName;
	
	public String call() throws Exception {
		
		startup();
		
		return "1";
	}

	public String getThreadName() {
		return this.threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}
	
	private String runFlag = "1";
	public String getRunFlag() {
		return runFlag;
	}

	public void setRunFlag(String runFlag) {
		this.runFlag = runFlag;
	}
	
	private String  execute(String url) {
		DefaultHttpClient httpClient = null;
		try {
			
			httpClient = new DefaultHttpClient();
			
			// 设置连接超时
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1 * 1000);
			// 设置读取超时
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1 * 1000);
			
			HttpPost post = new HttpPost(url);
			HttpResponse response = httpClient.execute(post);
			
			return EntityUtils.toString(response.getEntity(),"utf-8");
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(null != httpClient) {
				httpClient.getConnectionManager().shutdown();
			}
		}
	}
	
	private String asynExecute(String url) {
		
		AsyncHttpClient asyncHttpClient = null;
		
		try {
			AsyncHttpClientConfig.Builder builder = new AsyncHttpClientConfig.Builder();
			builder.setMaximumConnectionsTotal(1 * 1000);
			
			asyncHttpClient = new AsyncHttpClient(builder.build());
			
			ListenableFuture<Response> future = asyncHttpClient.prepareGet(url).execute();
			Response response = future.get();
			
			return response.getResponseBody("utf-8");
		
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} catch (ExecutionException e) {
			
			e.printStackTrace();
		} finally {
			if(null != asyncHttpClient) {
				asyncHttpClient.close();
			}
		}
		return null;
	}
	String templete = "<span class=\"word\" id=\"stock\">";
	public void startup() {
		
		// 运行中
		while(getRunFlag().equals("1") && !Thread.currentThread().isInterrupted()) {
			try {
				
				String context = asynExecute("http://www.gz.10086.cn/shop/portal/ecps/portal/item/getAjaxStockBySkuId.do?skuId=3300");
				//int start = context.indexOf(templete);// + templete.length();
				//String result = context.substring(start, start + 50);
				//System.out.println(start + "/" +result);
				if(!context.equals("0")) {
					System.out.println("--------------------------");
					System.out.println(context);
					System.out.println("--------------------------");
				}
				
				Thread.sleep(1 * 1000);
				Thread.yield();
			
			} catch (Exception e) {
				log.error(e);
				
				e.printStackTrace();
			}
		}
	}

	public int stop() {
		
		return 0;
	}

	public int callback(Object object) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
