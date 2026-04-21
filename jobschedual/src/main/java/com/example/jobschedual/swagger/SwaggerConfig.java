package com.example.jobschedual.swagger;

import com.google.common.base.Predicate;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.collect.Lists.newArrayList;

/**
 * SwaggerConfig
 * @author zhx
 * swagger地址 :采用默认地址 http://192.168.1.109:8080/swagger-ui.html
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	 @Bean
	   public Docket userApi() {

		/* ParameterBuilder ticketPar = new ParameterBuilder();
		 List<Parameter> pars = new ArrayList<Parameter>();
		 ticketPar.name("Authorization").description("请求头")
				 .modelRef(new ModelRef("string")).parameterType("header")
				 .required(false).build(); //header中的Authorization参数非必填，传空也可以
		 pars.add(ticketPar.build());    //根据每个方法名也知道当前方法在设置什么参数*/


	       Predicate<RequestHandler> swaggerSelector = RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class);
	       return new Docket(DocumentationType.SWAGGER_2)
	               .securitySchemes(newArrayList(new ApiKey[]{this.apiKey()}))
//	               .securitySchemes(newArrayList(new BasicAuth("school"))) //账号密码登录
//	               .enable(false)   //禁止使用  
	               .apiInfo(apiInfo())  
	               .select()  
//	               .apis(RequestHandlerSelectors.basePackage("com.boot"))  
	               .apis(swaggerSelector)  
	               .paths(PathSelectors.any())
	               .build();
				   //.globalOperationParameters(pars) ;
	   }  
	  
	    private ApiInfo apiInfo(){
	        return new ApiInfoBuilder()
	        		.title("Swagger")
	              /*  .description("构建restful api,learn more:springfox.io")  //副标题
	                .license("Licens")  
	                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")  
	                .contact(new Contact("boot","官网地址url","email地址"))  
	                .version("0.0.1")  */
	                .build();  
	    }  

	    ApiKey apiKey() {
	        return new ApiKey("sessionId", "sessionId", "header");
	    }  
}
