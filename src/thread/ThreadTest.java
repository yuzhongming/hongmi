package thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTest {

	/**
	 * @param args
	 */
	//3300
	//
	public static void main(String[] args) {
		int count = 2;
		// 初始化线程池
		ExecutorService executorService = Executors.newFixedThreadPool(count);
				
		for (int i = 0; i < count; i++) {
			ThreadDemoCollector demoCollector = new ThreadDemoCollector();
			// 设置线程名称
			demoCollector.setThreadName("示例线程ThreadDemoCollector"+i);
			
			executorService.submit(demoCollector);
		}

	}

}
