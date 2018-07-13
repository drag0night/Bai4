import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Service;

public class Main {

	/* Chinh sua tren server may client */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final LoadingCache<Integer, String> caches = CacheBuilder.newBuilder().maximumSize(100)
				.expireAfterAccess(10, TimeUnit.SECONDS).expireAfterWrite(20, TimeUnit.SECONDS)
				.build(new CacheLoader<Integer, String>() {

					@Override
					public String load(Integer key) throws Exception {
						// TODO Auto-generated method stub
						return listToStringJson(getNguyenTo(key));
					}

				});
		Service services = Service.ignite();
		services.port(8080);
		services.get("/prime", new Route() {
			
			public Object handle(Request request, Response response) throws Exception {
				int n = Integer.parseInt(request.queryParams("n"));
				return caches.get(n);
			}
		});
	}

	public static String listToStringJson(List<Integer> list) {
		return new Gson().toJson(list);
	}

	public static List<Integer> getNguyenTo(int n) {
		int i = 2;
		List<Integer> kq = new ArrayList<Integer>();
		while (i <= n) {
			if (checkNgTo(i)) {
				kq.add(i);
			}
			i++;
		}
		return kq;
	}

	public static boolean checkNgTo(int n) {
		if (n < 2) {
			return false;
		} else {
			if (n == 2) {
				return true;
			}
		}
		for (int i = 2; i <= Math.sqrt(n); i++) {
			if (n % i == 0) {
				return false;
			}
		}
		return true;
	}
}
