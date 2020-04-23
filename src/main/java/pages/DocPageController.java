package pages;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/doc")
public class DocPageController {
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView returnString() {
		ModelAndView model = new ModelAndView("DocView");
		model.addObject("page", "doc");
		return model;
	}

}
