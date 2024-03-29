package com.ll.exam.sen_books.app.rebate.controller;

import com.ll.exam.sen_books.app.rebate.entity.RebateOrderItem;
import com.ll.exam.sen_books.app.rebate.form.RebateDataForm;
import com.ll.exam.sen_books.app.rebate.service.RebateService;
import com.ll.exam.sen_books.util.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adm/rebate")
@Slf4j
public class AdmRebateController {
    private final RebateService rebateService;

    @GetMapping("/makeData")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showMakeData() {
        return "adm/rebate/makeData";
    }

    @PostMapping("/makeData")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String makeData(RebateDataForm rebateDataForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "/adm/rebate/makeData";
        }

        int year = rebateDataForm.getYear();
        int month = rebateDataForm.getMonth();
        rebateService.makeDate(year, month);

        return "redirect:/adm/rebate/rebateOrderItemList" + "?msg="+ Ut.url.encode("정산데이터가 성공적으로 생성되었습니다.");
    }

    @GetMapping("/rebateOrderItemList")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showRebateOrderItemList(@RequestParam(required = false) Integer year, @RequestParam(required = false) Integer month, Model model) {
        List<RebateOrderItem> items = null;

        if(year == null || month == null) {
            items = rebateService.findRebateOrderItems();
        } else {
            items = rebateService.findRebateOrderItemsByPayDateIn(year, month);
        }

        model.addAttribute("items", items);

        return "adm/rebate/rebateOrderItemList";
    }

    @PostMapping("/rebateOne/{orderItemId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseBody
    public String rebateOne(@PathVariable long orderItemId, HttpServletRequest req) {
        rebateService.rebate(orderItemId);

        String referer = req.getHeader("Referer");
        log.debug("referer : " + referer);

        RebateOrderItem orderItem = rebateService.findById(orderItemId);

        int calculateRebatePrice = orderItem.calculateRebatePrice();

        String yearMonth = Ut.url.getQueryParamValue(referer, "yearMonth", "");

        return "redirect:/adm/rebate/rebateOrderItemList?yearMonth="
                + yearMonth
                + "&msg=" + Ut.url.encode("주문품목번호 %d번에 대해서 판매자에게 %s원 정산을 완료하였습니다.".formatted(orderItemId, calculateRebatePrice));
    }

    @PostMapping("/rebate")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String rebate(String ids, HttpServletRequest req) {

        String[] idsArr = ids.split(",");

        Arrays.stream(idsArr)
                .mapToLong(Long::parseLong)
                .forEach(id -> {
                    rebateService.rebate(id);
                });

        String referer = req.getHeader("Referer");
        String yearMonth = Ut.url.getQueryParamValue(referer, "yearMonth", "");

        return "redirect:/adm/rebate/rebateOrderItemList?yearMonth="
                + yearMonth
                + "&msg=" + Ut.url.encode("%d건의 정산품목을 정산처리하였습니다.".formatted(idsArr.length));
    }
}
