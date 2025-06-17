package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.PurchaseDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.RoleModelInt;
import in.co.rays.project_3.model.PurchaseModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * purchase functionality controller.to perform add,delete and update operation
 * 
 * @author YOGESH SEN
 *
 */
@WebServlet(urlPatterns = { "/ctl/PurchaseCtl" })
public class PurchaseCtl extends BaseCtl {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	/** The log. */
	private static Logger log = Logger.getLogger(PurchaseCtl.class);

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

		
		if (DataValidator.isNull(request.getParameter("totalCost"))) {
			request.setAttribute("totalCost", PropertyReader.getValue("error.require", "totalCost"));
			pass = false;
		} else if (!DataValidator.isInteger(request.getParameter("totalCost"))) {
			request.setAttribute("error.integer", "Total Cost");
			pass = false;
		}
		
		if (DataValidator.isNull(request.getParameter("totalQuantity"))) {
			request.setAttribute("totalQuantity", PropertyReader.getValue("error.require", "TotalQuantity"));
			pass = false;
		} else if (!DataValidator.isInteger(request.getParameter("totalQuantity"))) {
			request.setAttribute("error.integer", "Total Quantity");
			pass = false;
		}
		
		if (DataValidator.isNull(request.getParameter("product"))) {
			request.setAttribute("product", PropertyReader.getValue("error.require", "Product"));
			pass = false;
		}
		
		if (DataValidator.isNull(request.getParameter("orderDate"))) {
			request.setAttribute("orderDate", PropertyReader.getValue("error.require", "OrderDate"));
			pass = false;
		} else if (!DataValidator.isDate(request.getParameter("orderDate"))) {
			request.setAttribute("orderDate", PropertyReader.getValue("error.date", "OrderDate"));
			pass = false;
		}

	
		return pass;

	}

	protected BaseDTO populateDTO(HttpServletRequest request) {
		PurchaseDTO dto = new PurchaseDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));

		dto.setTotalQuantity(DataUtility.getInt(request.getParameter("totalQuantity")));

		dto.setOrderDate(DataUtility.getDate(request.getParameter("orderDate")));
		
		dto.setTotalCost(DataUtility.getInt(request.getParameter("totalCost")));

		dto.setProduct(DataUtility.getInt(request.getParameter("product")));
		
		log.debug("PurchaseRegistrationCtl Method populatedto Ended");

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
		log.debug("PurchaseCtl Method doGet Started");
		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		PurchaseModelInt model = ModelFactory.getInstance().getPurchaseModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0 || op != null) {
			System.out.println("in id > 0  condition");
			PurchaseDTO dto;
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
		PurchaseModelInt model = ModelFactory.getInstance().getPurchaseModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			PurchaseDTO dto = (PurchaseDTO) populateDTO(request);
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

			PurchaseDTO dto = (PurchaseDTO) populateDTO(request);
			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.PURCHASE_LIST_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.PURCHASE_LIST_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.PURCHASE_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("PurchaseCtl Method doPostEnded");
	}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.PURCHASE_VIEW;
	}

}
