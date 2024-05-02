package com.task02;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import com.syndicate.deployment.model.RetentionSetting;
import com.syndicate.deployment.model.lambda.url.AuthType;
import com.syndicate.deployment.model.lambda.url.InvokeMode;
import java.util.Map;

@LambdaHandler(
    lambdaName = "hello_world",
    roleName = "hello_world-role",
    isPublishVersion = true,
    logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED)
@LambdaUrlConfig(authType = AuthType.NONE, invokeMode = InvokeMode.BUFFERED)
public class HelloWorld implements RequestHandler<APIGatewayV2HTTPEvent, Map<String, Object>> {

  public Map<String, Object> handleRequest(APIGatewayV2HTTPEvent request, Context context) {
    context.getLogger().log("request:" + request.toString());
    String httpMethod = request.getRequestContext().getHttp().getMethod();
    String rawPath = request.getRawPath();
    context.getLogger().log("path:" + rawPath);

    if (rawPath.equals("/hello")) {
      return Map.of("message", "Hello from Lambda", "statusCode", 200);
    } else {
      return Map.of(
          "message",
          String.format(
              "Bad request syntax or unsupported method. Request path: %s. HTTP method: %s",
              rawPath, httpMethod),
          "statusCode",
          400);
    }
  }
}
