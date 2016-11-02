package midian.baselib.bean;

import midian.baselib.app.AppException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 结果
 * 
 * @author XuYang
 * 
 */
public class NetResult extends NetBase {
	public String ret = "";
	public String ret_code = "";
    public String ret_info = "";

	public static Gson gson = new Gson();

	public boolean isOK() {
		return "success".equals(ret);
	}

	public static NetResult parse(String json) throws AppException {
		NetResult res = new NetResult();
		try {
			res = gson.fromJson(json, NetResult.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getRet_code() {
        return ret_code;
    }

    public void setRet_code(String ret_code) {
        this.ret_code = ret_code;
    }

    public String getRet_info() {
        return ret_info;
    }

    public void setRet_info(String ret_info) {
        this.ret_info = ret_info;
    }
}
