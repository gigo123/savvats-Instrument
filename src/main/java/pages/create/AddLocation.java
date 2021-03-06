package pages.create;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import models.Location;
import savvats.utils.ControllersCheckWrite;

@Controller
@RequestMapping("/addlocation")
public class AddLocation {

	@ModelAttribute("location")
	public Location createLocationModel() {
		return new Location();
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getLocationCF() {
		ModelAndView model = new ModelAndView("AddLocation", "command", new Location());
		model.addObject("page", "location");
		return model;
	}
	@RequestMapping(method = RequestMethod.POST)
	public String postLocationCF(@ModelAttribute("location") @Validated Location location, 
			BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			return "AddLocation";
		}
		model.addAttribute("errorText",ControllersCheckWrite.addLocationWork(location));
		return "OperationInfo";
	}

}
