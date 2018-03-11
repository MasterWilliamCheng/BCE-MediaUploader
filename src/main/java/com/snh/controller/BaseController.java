package com.snh.controller;

import com.google.gson.*;
import com.snh.util.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2016/11/14.
 */
public class BaseController {

	protected final Logger logger = LoggerFactory.getLogger( getClass() );

	public static final Gson gson = new Gson();

	public static final JsonParser parser=new JsonParser();


	protected JsonObject toSuccessJson( JsonElement content ) {
		JsonObject object = new JsonObject();
		object.addProperty( "status", 200 );
		object.addProperty( "message", "请求成功" );
		object.add( "content", content );
		return object;
	}

	protected JsonObject toErrorJson( int status, String message ) {
		JsonObject object = new JsonObject();
		object.addProperty( "status", status );
		object.addProperty( "message", message );
		return object;
	}

	protected JsonObject toSuccessObject( Object content ) {
		JsonObject object = new JsonObject();
		object.addProperty( "status", 200 );
		object.addProperty( "message", "请求成功" );
		object.add( "content", GsonUtil.toJsonElement( content ) );
		return object;
	}

	protected JsonObject toSuccessObject( Object content, String message ) {
		JsonObject object = new JsonObject();
		object.addProperty( "status", 200 );
		object.addProperty( "message", message );
		object.add( "content", GsonUtil.toJsonElement( content ) );
		return object;
	}

	protected JsonObject toSuccessJson() {
		JsonObject result = new JsonObject();
		result.addProperty( "status", 200 );
		result.addProperty( "message", "success" );
		result.add( "content", new JsonObject() );
		return result;
	}

	/**
	 * 接口返回信息
	 *
	 * @param content 内容
	 * @return 返回信息
	 */
	public JsonObject toJson( JsonElement content ) {
		if (content == null ||
				content instanceof JsonNull) {

			content = new JsonObject();
		}
		JsonObject object = new JsonObject();
		object.addProperty( "status", 0 );
		object.addProperty( "message", "请求成功" );
		object.add( "content", content );
		return object;

	}
}
