package com.blood.coding.service.wish;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.blood.coding.controller.common.Criteria;
import com.blood.coding.controller.common.PageMaker;
import com.blood.coding.dao.wish.WishDAO;
import com.blood.coding.dto.wish.WishVO;

public class WishServiceImpl implements WishService {

	private WishDAO wishDAO;
	public void setWishDAO(WishDAO wishDAO) {
		this.wishDAO = wishDAO;
	}
	
	@Override
	public boolean registWish(WishVO wishVO) throws SQLException {
		
		WishVO wish = wishDAO.checkWish(wishVO);
		
		//널이아니면 값이있는거다
		if(wish != null) {
			return false;
		}
		else {
			wishDAO.insertWish(wishVO);
			return true;
		}
		
	}

	@Override
	public boolean removeWish(WishVO wishVO) throws SQLException {
		
		WishVO wish = wishDAO.checkWish(wishVO);
		
		if(wish == null) {
			return false;
		}
		else {
			wishDAO.deleteWish(wishVO);
			return true;
		}
		
	}

	@Override
	public Map<String, Object> selectWishList(Criteria cri , WishVO wishVO) throws SQLException {
		List<WishVO> wishList = wishDAO.selectWishList(cri, wishVO.getMem_id());

		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(wishDAO.selectWishListCount(wishVO));
		
		dataMap.put("wishList", wishList);
		dataMap.put("pageMaker", pageMaker);

		return dataMap;
	}

	
}
