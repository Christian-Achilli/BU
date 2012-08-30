/**
 * 
 */
package com.kp.marsh.ebt.shared.dto;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * @author christianachilli
 *
 * POJO that serves line of business and related products
 *
 */
public class LineOfBusiness extends ProductDTO implements IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<ProductInfoDTO> products; // products related to this line of business
	
	
	public LineOfBusiness() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param dto the dto that ensembles the lob
	 */
	public LineOfBusiness(ProductInfoDTO dto) {
		super();
		setId(dto.getId());
		setItemName(dto.getItemName());
		setItemType(dto.getItemType());
	}

	public List<ProductInfoDTO> getProducts() {
		return products;
	}

	public void setProducts(List<ProductInfoDTO> products) {
		this.products = products;
	}
	
	/**
	 * @param id
	 * @return true if the product with id <code>id</code> belongs this LOB
	 */
	public boolean hasInTheList(int id) {
		for (ProductInfoDTO product : products) {
			if(product.getId() == id) 
				return true;
		}
		return false;
	}
	
}
