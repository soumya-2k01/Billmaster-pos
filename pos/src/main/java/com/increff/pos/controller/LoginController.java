package com.increff.pos.controller;

import com.increff.pos.model.InfoData;
import com.increff.pos.model.LoginForm;
import com.increff.pos.model.SignupForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.AdminService;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.Helper;
import com.increff.pos.util.Normalize;
import com.increff.pos.util.SecurityUtil;
import com.increff.pos.util.Validate;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

@Controller
@RequestMapping("/session")
public class LoginController {

    @Autowired
    private AdminService service;
    @Autowired
    private InfoData info;

    @ApiOperation(value = "Logs in a user")
    @RequestMapping(path = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView login(HttpServletRequest req, LoginForm loginForm) {
        info.setMessage(Validate.validateLoginForm(loginForm));
        if (!Objects.equals(info.getMessage(), "")) {
            info.setHasMsg(true);
            return new ModelAndView("redirect:/site/login");
        }
        UserPojo userPojo = service.get(loginForm.getEmail());
        if (userPojo == null) {
            info.setMessage("Unregistered Email!");
            info.setHasMsg(true);
            return new ModelAndView("redirect:/site/login");
        }
        if (!Objects.equals(userPojo.getPassword(), loginForm.getPassword())) {
            info.setMessage("Wrong password!");
            info.setHasMsg(true);
            return new ModelAndView("redirect:/site/login");
        }

        // Create authentication object
        Authentication authentication = Helper.convertUserPojoToAuthObject(userPojo);
        // Create new session
        HttpSession session = req.getSession(true);
        // Attach Spring SecurityContext to this new session
        SecurityUtil.createContext(session);
        // Attach Authentication object to the Security Context
        SecurityUtil.setAuthentication(authentication);
        info.setMessage("");
        info.setHasMsg(false);
        return new ModelAndView("redirect:/ui/home");
    }

    @ApiOperation(value = "SignUp a user")
    @RequestMapping(path = "/signup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView signup(SignupForm signupForm) throws ApiException {
        info.setMessage(Validate.validateSignupForm(signupForm));
        if (Objects.equals(info.getMessage(), "")) {
            info.setHasMsg(false);
        } else {
            info.setHasMsg(true);
            return new ModelAndView("redirect:/site/signup");
        }
        UserPojo userPojo = Helper.convertSignupFormToPojo(signupForm);
        //if exists in properties file give supervisor role else operator.
        Properties property = new Properties();
        try {
            property.load(Files.newInputStream(Paths.get("pos.properties")));
        } catch (IOException e) {
            System.out.println("Failed to load properties file.");
            e.printStackTrace();
        }
        if (property.containsKey(userPojo.getEmail())) {
            userPojo.setRole("supervisor");
        } else userPojo.setRole("operator");

        Normalize.normalizeEmail(userPojo);
        UserPojo existing = service.get(userPojo.getEmail());
        if (existing != null) {
            info.setMessage("User with given email already exists!");
            info.setHasMsg(true);
            return new ModelAndView("redirect:/site/signup");
        }
        service.add(userPojo);
        info.setMessage("");
        info.setHasMsg(false);
        return new ModelAndView("redirect:/site/login");
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return new ModelAndView("redirect:/site/login");
    }

}
