var  MobilePhoneApp = {

	//调用手机通讯录
	getContacts:function($ele){
		var param_msg = {"CODE":29, "BUSINESSNAME":"getContacts"};
		var returnF = function(obj) {
		    if (obj.RESULT=="ok"){
		    	$ele.val(obj.USERPHONENUM);
		    }
		};
		//第三方统一调用接口
		fashion.invokeMobile(param_msg, returnF);
	},

	//判断用户是否登录
	isLogin:function(fc){
		var param_msg = {"CODE":23, "BUSINESSNAME": "isLogin"};

		var returnF = function(obj) {
			//调接口成功
		    if (obj.RESULT == "ok"){
		    	fc(obj.ISLOGIN);
		    }
		};
		//第三方统一调用接口
		fashion.invokeMobile(param_msg, returnF); 
	},

	//调用登录
	showLogin:function(){
		var param_msg = {"CODE":22, "BUSINESSNAME": "showlogin"};

		//第三方统一调用接口
		fashion.invokeMobile(param_msg); 
	},

	//获取用户基础信息
	getUserInfo:function(fc){
		var param_msg = {"CODE":100, "BUSINESSNAME": "getUserInfo"};
		var returnF = function(obj) {
			//调接口成功
		    if (obj.RESULT == "ok"){
		    	fc(obj.USERPHONENUM);
		    }
		}
		//第三方统一调用接口
		fashion.invokeMobile(param_msg, returnF); 
		
	},
	
	/**
	 * 加密功能
	 */
	encryptString:function(str,fc){
  		var msg={"CODE": 63,
  			"BUSINESSNAME": "encryptString",
  			"str": str
  		};
  		var returnF = function(obj) {
			//调接口成功
		    if (obj.RESULT == "ok"){
		    	fc(obj.encryptString);
		    }
		};
		fashion.invokeMobile(msg, returnF);
  	},
	

}
	