package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.CustomerDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.RoleModelInt;
import in.co.rays.project_3.model.CustomerModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * customer functionality controller.to perform add,delete and update operation
 * 
 * @author YOGESH SEN
 *
 */
@WebServlet(urlPatterns = { "/ctl/CustomerCtl" })
public class CustomerCtl extends BaseCtl {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	/** The log. */
	private static Logger log = Logger.getLogger(CustomerCtl.class);

	protected void preload(HttpServletRequest request) {
		RoleModelInt model = ModelFactory.getInstance().getRoleModel();
		try {
			List list = model.list();
			request.setAttribute("roleList", list);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("clientName"))) {
			request.setAttribute("clientName", PropertyReader.getValue("error.require", "ClientName"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("clientName"))) {
			request.setAttribute("clientName", "ClientName  must contains alphabets only");
			pass = false;

		}
		if (DataValidator.isNull(request.getParameter("location"))) {
			request.setAttribute("location", PropertyReader.getValue("error.require", "Location"));
			System.out.println(pass);
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("location"))) {
			request.setAttribute("location", "Location must contains alphabets only");
			pass = false;

		}
		if (DataValidator.isNull(request.getParameter("contactNumber"))) {
			request.setAttribute("contactNumber", PropertyReader.getValue("error.require", "ContactNumber"));
			pass = false;
		} else if (!DataValidator.isPhoneNo(request.getParameter("contactNumber"))) {
			request.setAttribute("contactNumber", "Please Enter Valid Contact Number");
			pass = false;
		}
		
		if (DataValidator.isNull(request.getParameter("importance"))) {
			request.setAttribute("importance", PropertyReader.getValue("error.require", "Importance"));
			pass = false;
		}

	
		return pass;

	}

	protected BaseDTO populateDTO(HttpServletRequest request) {
		CustomerDTO dto = new CustomerDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));

		dto.setClientName(DataUtility.getString(request.getParameter("clientName")));

		dto.setLocation(DataUtility.getString(request.getParameter("location")));
		
		dto.setContactNumber(DataUtility.getLong(request.getParameter("contactNumber")));

		dto.setImportance(DataUtility.getInt(request.getParameter("importance")));
		
		log.debug("CustomerRegistrationCtl Method populatedto Ended");

		return dto;

	}

	/**
	 * Contain Display Logics.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		log.debug("CustomerCtl Method doGet Started");
		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		CustomerModelInt model = ModelFactory.getInstance().getCustomerModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0 || op != null) {
			System.out.println("in id > 0  condition");
			CustomerDTO dto;
			try {
				dto = model.findByPK(id);
				ServletUtility.setDto(dto, request);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}
		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Contain Submit Logics.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String op = DataUtility.getString(request.getParameter("operation"));
		System.out
				.println("-------------------------------------------------------------------------dopost run-------");
		// get model
		CustomerModelInt model = ModelFactory.getInstance().getCustomerModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			CustomerDTO dto = (CustomerDTO) populateDTO(request);
			System.out.println(" in do post method jkjjkjk++++++++" + dto.getId());
			try {
				if (id > 0) {
					model.update(dto);
					ServletUtility.setDto(dto, request);
					ServletUtility.setSuccessMessage("Data is successfully Update", request);
				} else {

					try {
						long pk = model.add(dto); 
						
						dto = model.findByPK(pk);
						ServletUtility.setDto(dto, request);
						ServletUtility.setSuccessMessage("Data is successfully saved", request);
					} catch (ApplicationException e) {
						log.error(e);
						ServletUtility.handleException(e, request, response);
						return;
					} catch (DuplicateRecordException e) {
						ServletUtility.setDto(dto, request);
						ServletUtility.setErrorMessage("Login id already exists", request);
					}

				}

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("Login id already exists", request);
			}
		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			CustomerDTO dto = (CustomerDTO) populateDTO(request);
			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.CUSTOMER_LIST_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.CUSTOMER_LIST_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.CUSTOMER_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("CustomerCtl Method doPostEnded");
	}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.CUSTOMER_VIEW;
	}

}
