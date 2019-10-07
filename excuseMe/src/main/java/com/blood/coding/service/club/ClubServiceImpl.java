package com.blood.coding.service.club;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.blood.coding.controller.common.Criteria;
import com.blood.coding.controller.common.PageMaker;
import com.blood.coding.dao.attach.AttachDAO;
import com.blood.coding.dao.category.CategoryDAO;
import com.blood.coding.dao.club.ClubDAO;
import com.blood.coding.dao.joinClub.JoinClubDAO;
import com.blood.coding.dao.local.LocalDAO;
import com.blood.coding.dao.member.MemberDAO;
import com.blood.coding.dao.up.UpDAO;
import com.blood.coding.dto.attach.AttachVO;
import com.blood.coding.dto.category.CategoryVO;
import com.blood.coding.dto.club.ClubVO;
import com.blood.coding.dto.local.LocalVO;
import com.blood.coding.dto.member.MemberVO;

public class ClubServiceImpl implements ClubService {

	private ClubDAO clubDAO;
	public void setClubDAO(ClubDAO clubDAO) {
		this.clubDAO = clubDAO;
	}
	
	private AttachDAO attachDAO;
	public void setAttachDAO(AttachDAO attachDAO) {
		this.attachDAO = attachDAO;
	}

	/*private ReplyDAO replyDAO;
	public void setRelyDAO(ReplyDAO replyDAO) {
		this.replyDAO = replyDAO;
	}
 */
	private UpDAO upDAO;
	public void setUpDAO(UpDAO upDAO) {
		this.upDAO = upDAO;
	}

	private DownDAO downDAO;
	public void setDownDAO(DownDAO downDAO) {
		this.downDAO = downDAO;
	}
	
	private MemberDAO memberDAO;
	public void setMemberDAO(MemberDAO memberDAO) {
		this.memberDAO=memberDAO;
	}
	
	private CategoryDAO categoryDAO;
	public void setCategoryDAO(CategoryDAO categoryDAO) {
		this.categoryDAO=categoryDAO;
	}
	
	private LocalDAO localDAO;
	public void setLocalDAO(LocalDAO localDAO) {
		this.localDAO=localDAO;
	}
	
	/*private JoinClubDAO joinClubDAO;
	public void setJoinClubDAO(JoinClubDAO joinClubDAO) {
		this.joinClubDAO=joinClubDAO;
	}*/

	

	
	@Override
	public Map<String, Object> getClubList(Criteria cri,MemberVO memberVO) throws SQLException { //(λ§€κ°λ³??? memberVOμΆκ?)
		Map<String, Object> dataMap = new HashMap<String, Object>();

		cri.setPerPageNum(3);
		//cri.setLocal(memberVO.getMem_local());
		cri.setLocal("??? ");
		cri.setAlignment(2);
		//κ²??μ°? ?λ¦΄λ μΆμ²??Έ?? κ²????Όλ―Έν°λ₯? ?¬?¨?΄? κ°?? Έ?€?κΉ? κ°μ  fix?΄μ£ΌκΈ°
		cri.setLocal("");
		cri.setCategory("");
		cri.setKeyword("");

		//μΆμ²λ¦¬μ€?Έ
		List<ClubVO> recommendList = clubDAO.selectSearchClubList(cri);
		
		//μΉ΄νκ³ λ¦¬
		List<CategoryVO> categoryList = categoryDAO.selectCategoryList();

		//μ§??­
		List<LocalVO> localList = localDAO.selectLocalList();

		cri.setPerPageNum(10);
		cri.setAlignment(0);
		
		List<ClubVO> clubList = clubDAO.selectSearchClubList(cri);
		//List<CategoryVO> cateList = categoryDAO.selectCategoryList();
		//List<LocalVO> localList = localDAO.selectLocalList();
		
		int totalCount = clubDAO.selectSearchClubCount(cri);

		// pagination
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(totalCount);
		
		// dataMap? ?£κΈ?
		dataMap.put("clubList", clubList);
		dataMap.put("pageMaker", pageMaker);
		dataMap.put("recommendList", recommendList);
		dataMap.put("member", memberVO);
		dataMap.put("cateList", categoryList);
		dataMap.put("localList", localList);

		return dataMap;
 	}

	//??Έ? μ‘°ν[???Ό]:replycnt ??κ±?
	@Override
	public Map<String, Object> readClub(String club_no) throws SQLException {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		//??Έ? ?κ°? 
		ClubVO club = clubDAO.selectClub(club_no);
		
		//??€?
		String memberId = club.getMem_id();
		MemberVO member = memberDAO.selectMember(memberId);
		String memberNick = member.getMem_nick();

		//μ²¨λ???Ό
		AttachVO attachVO = new AttachVO();
		String attach_board = attachVO.getAttach_board();
		attach_board=club_no;
		List<AttachVO> attachList = attachDAO.selectAttachesByAttachBoard(attach_board);
		
		
		//int replycnt = replyDAO.selectReplyListCount(club_no);
		//club.setReplycnt(replycnt);
		
		dataMap.put("club", club);
		dataMap.put("member", member);
		dataMap.put("memberNick", memberNick);
		dataMap.put("attachList", attachList);
		
		return dataMap;
	}

	// κ·Έλ₯ ?? κ°?? Έ?€?κ±?-?? ?κΈ? ?  ?¨κ³?(?? λ²νΌ? ?΄λ¦????)
	@Override
	public ClubVO getClub(String club_no) throws SQLException {
		ClubVO club = clubDAO.selectClub(club_no);
		return club;
	}

	@Override
	public void regist(ClubVO club) throws SQLException {
		int cno = clubDAO.selectClubSeq();
		String club_no = "c_" + cno;
		//System.out.println(club_no);
		club.setClub_no(club_no);
		clubDAO.insertClub(club);
	}

	//?? ?κ³? submitλ²νΌ? ?΄λ¦????
	@Override
	public void modify(ClubVO club) throws SQLException {
		clubDAO.updateClub(club);
		

	}

	@Override
	public void remove(String club_no) throws SQLException {
		clubDAO.deleteClub(club_no);
	}

	@Override
	public Map<String, Object> getNewClubList(Criteria cri) throws SQLException {
		Map<String, Object> dataMap = new HashMap<String, Object>();

		List<ClubVO> newclubList = clubDAO.selectNewClubList(cri);
		
		int totalCount = clubDAO.selectNewClubListCount(cri);
		
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(totalCount);
		
		
		dataMap.put("newclubList", newclubList);
		dataMap.put("pageMaker", pageMaker);

		return dataMap;
	}


	@Override
	public Map<String, List<ClubVO>> getClubListMain(String mem_local) throws SQLException {
		Map<String, List<ClubVO>> map = new HashMap();
		List<ClubVO> list = clubDAO.recommendClubMain(mem_local);
		map.put("recommendClubList", list);
		return map;
	}




	@Override
	public Map<String, List<ClubVO>> getClubListMainRecent() throws SQLException {
		Map<String, List<ClubVO>> map = new HashMap();
		List<ClubVO> list = clubDAO.recentClubMain();
		map.put("recentClubList", list);
		return map;
	}

}