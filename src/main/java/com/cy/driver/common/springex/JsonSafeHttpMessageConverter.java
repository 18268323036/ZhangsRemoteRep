//package com.cy.driver.common.springex;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.core.JsonEncoding;
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import org.springframework.http.HttpOutputMessage;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.HttpMessageNotWritableException;
//
//import java.io.IOException;
//import java.nio.charset.Charset;
//
///**
// * 重写json
// * Created by wyh on 2015/5/12.
// */
//public class JsonSafeHttpMessageConverter extends MappingJacksonHttpMessageConverter {
//    private String jsonPrefix;
//    private ObjectMapper jsonObjMapper = new ObjectMapper();
//
//
//    @Override
//    public void setJsonPrefix(String jsonPrefix) {
//        this.jsonPrefix = jsonPrefix;
//        super.setJsonPrefix(jsonPrefix);
//    }
//
//    @Override
//    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
//        //数字也加引号
//        jsonObjMapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
//        //null  not output
//        jsonObjMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//
//        JsonEncoding encoding = getXmlJsonEncoding(outputMessage.getHeaders().getContentType());
//        JsonGenerator jsonGenerator = jsonObjMapper.getFactory().createJsonGenerator(outputMessage.getBody(), encoding);
//        if (jsonObjMapper.getSerializationConfig().isEnabled(SerializationFeature.INDENT_OUTPUT)) {
//            jsonGenerator.useDefaultPrettyPrinter();
//        }
//        try {
//            if (this.jsonPrefix != null) {
//                jsonGenerator.writeRaw(this.jsonPrefix);
//            }
//            jsonObjMapper.writeValue(jsonGenerator, object);
//        }
//        catch (JsonProcessingException ex) {
//            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
//        }
//    }
//
//    private JsonEncoding getXmlJsonEncoding(MediaType contentType) {
//        if(contentType != null && contentType.getCharSet() != null) {
//            Charset charset = contentType.getCharSet();
//            JsonEncoding[] arr$ = JsonEncoding.values();
//            int len$ = arr$.length;
//
//            for(int i$ = 0; i$ < len$; ++i$) {
//                JsonEncoding encoding = arr$[i$];
//                if(charset.name().equals(encoding.getJavaName())) {
//                    return encoding;
//                }
//            }
//        }
//
//        return JsonEncoding.UTF8;
//    }
//}
