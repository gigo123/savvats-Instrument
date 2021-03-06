package savvats.ajax;

import com.fasterxml.jackson.annotation.JsonView;

import savvats.Views;

public class AjaxResponse {
	@JsonView(Views.Public.class)
    String msg;

    @JsonView(Views.Public.class)
    String code;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
    
    
}
