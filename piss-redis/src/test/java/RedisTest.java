import static org.junit.Assert.*;

import org.junit.Test;

import com.ai.piss.redis.SerializeUtil;


public class RedisTest {

	@Test
	public void test() {
		String user="wutianbiao";
		byte[] res=SerializeUtil.serialize(user);
		
		Object obj=SerializeUtil.deserialize(res);
		System.out.println(obj);
	}

}
