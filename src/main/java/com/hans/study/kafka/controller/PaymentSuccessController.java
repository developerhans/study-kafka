package com.hans.study.kafka.controller;

import com.hans.study.kafka.dto.OrderReq;
import com.hans.study.kafka.service.PaymentSuccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentSuccessController {

    private final PaymentSuccessService paymentSuccessService;

    /**
     * 1. 결제가 완료라는 가정에서 호출함
     * 2. 결제 완료 메시지를 작성
     * 3. 결제 이후 처리해야할 아이템들을 각 서비스에 전달
     * 4. 각 서비스는 그에 해당하는 토픽을 작성
     *
     */
    @PostMapping("/success")
    public String success(@RequestBody final OrderReq orderReq) throws Exception {
        paymentSuccessService.success(orderReq);
        return "ok";
    }

}
