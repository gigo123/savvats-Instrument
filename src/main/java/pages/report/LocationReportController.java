package pages.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import dao.LocationDAO;
import models.Location;
import savvats.BoxReport;
import savvats.LocationReport;
import savvats.ReportSettings;
import savvats.utils.ControllerReportsWorker;

@Controller
@RequestMapping("/locationReport")
public class LocationReportController {

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getLocReportForm() {
		ModelAndView model = new ModelAndView("reports/LocationReport");
		model.addObject("reportSettings", new ReportSettings());
		model.addObject("page", "locReport");

		return model;
	}

	@SuppressWarnings("resource")
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView generateReport(@ModelAttribute("ReportSettings") ReportSettings settings ) {
		ModelAndView model = new ModelAndView("reports/LocationReportResault");
		LocationReport locReport = null;
		if (settings.isBox() == true) {
			locReport = new LocationReport();
			ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
			LocationDAO locDAO = (LocationDAO) context.getBean("LocationDAO");
			locReport.setName(locDAO.getLocById(settings.getLocationId()).getName());
			BoxReport boxReport = ControllerReportsWorker.getInstInBox(settings.getBoxId());
			List<BoxReport> boxList = new ArrayList<BoxReport>();
			boxList.add(boxReport);
			locReport.setReportBox(boxList);
		} else {
			locReport = ControllerReportsWorker.getInstInLocation(settings.getLocationId());
		}
		model.addObject("reportSettings", settings);
		model.addObject("locReport", locReport);
		model.addObject("page", "exDocList");
		return model;
	}

	@SuppressWarnings("resource")
	@ModelAttribute("locationList")
	private Map<Long, String> getLocationList() {
		Map<Long, String> lociationMap = new HashMap<Long, String>();
		ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
		LocationDAO locDAO = (LocationDAO) context.getBean("LocationDAO");
		List<Location> locList = locDAO.getAllLocatin();
		for (Location location : locList) {
			lociationMap.put(location.getId(), location.getName());
		}
		return lociationMap;
	}

}
