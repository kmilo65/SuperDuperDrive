package com.udacity.jwdnd.course1.cloudstorage.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);


        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == 404) {
                model.addAttribute("errorMessage", "Resource not found.");
                model.addAttribute("statusCode",statusCode);

            } else if (statusCode == 500) {
                model.addAttribute("errorMessage", "Internal server error. It seems the file you try to upload exceeds the max size of 15M");
                model.addAttribute("statusCode",statusCode);
            } else {
                model.addAttribute("errorMessage", "An unexpected error occurred.");
                model.addAttribute("statusCode",statusCode);
            }
        }
        System.out.println(status);
        return "result";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
