//package shop.libertyform.cdc.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import shop.libertyform.cdc.service.KafkaProducer;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping(value = "/kafka")
//public class KafkaController {
//    private final KafkaProducer producer;
//
//    @PostMapping
//    public String sendMessage(@RequestParam("message")String message){
//        producer.sendMessage(message);
//
//        return "success";
//    }
//}
