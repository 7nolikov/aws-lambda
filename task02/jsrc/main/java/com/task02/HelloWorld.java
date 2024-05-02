package com.task02;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import com.syndicate.deployment.model.RetentionSetting;
import com.syndicate.deployment.model.lambda.url.AuthType;
import com.syndicate.deployment.model.lambda.url.InvokeMode;

@LambdaHandler(
    lambdaName = "hello_world",
    roleName = "hello_world-role",
    isPublishVersion = true,
    logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED)
@LambdaUrlConfig(authType = AuthType.NONE, invokeMode = InvokeMode.BUFFERED)
public class HelloWorld implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

  public APIGatewayV2HTTPResponse  handleRequest(APIGatewayV2HTTPEvent request, Context context) {
    context.getLogger().log("request:" + request.toString());
    String httpMethod = request.getRequestContext().getHttp().getMethod();
    String rawPath = request.getRawPath();
    context.getLogger().log("path:" + rawPath);

    if (rawPath.equals("/hello")) {
      return APIGatewayV2HTTPResponse .builder()
          .withBody("{'statusCode': 200, 'message': 'Hello from Lambda'}")
          .withStatusCode(200)
          .build();
    } else {
      return APIGatewayV2HTTPResponse.builder()
          .withBody(
              String.format(
                  "{'statusCode': 400, 'message': 'Bad request syntax or unsupported method. Request path: %s. HTTP method: %s'}",
                  rawPath, httpMethod))
          .withStatusCode(400)
          .build();
    }
  }
}
