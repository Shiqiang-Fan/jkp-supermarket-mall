package com.joyveb.redeem.controller;

import com.joyveb.redeem.utils.MixBarcodeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("CashManage")
public class MixBarcodeCtrl {

    @PostMapping("/barcode.action")
    @ResponseBody
    public String getMixBarcode(@RequestParam("barcode") String barcode){

        String mixBarcode =null;
        String result = null;
        try {
            mixBarcode = MixBarcodeUtil.encodeBarcode(barcode);
            result = MixBarcodeUtil.desEncrypt(mixBarcode, "0123456789666666");
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return result;
    }
}
